package com.yanncer.fixconvnum.data

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.yanncer.fixconvnum.common.PrefSingleton
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.models.PhoneNumber
import com.yanncer.fixconvnum.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl(private val context: Context): ContactsRepository {


    override suspend fun fetchContacts(limit: Int, offset: Int, searchQuery: String):  List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()


        // Projection for different names
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
        )



        // Request for structured name
        val nameSelection = "${ContactsContract.Data.MIMETYPE} = ? AND (${ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME} LIKE ? OR ${ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME} LIKE ?)"
        val nameSelectionArgs = arrayOf(
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
            "%$searchQuery%",
            "%$searchQuery%"
        )

        val nameCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            nameSelection,
           nameSelectionArgs,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )


        // mapping of names per contract id
        val contactNames = mutableMapOf<Long, Pair<String, String>>()

        nameCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val firstNameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
            val lastNameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)

            while (cursor.moveToNext()) {
                val contactId = cursor.getLong(idColumn)
                val firstName = cursor.getString(firstNameColumn) ?: ""
                val lastName = cursor.getString(lastNameColumn) ?: ""

                contactNames[contactId] = Pair(firstName, lastName)
            }
        }

        // Principal request for contacts
        val selection = "${ContactsContract.Contacts.HAS_PHONE_NUMBER} = 1 AND (${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE '%$searchQuery%')"

        val contactCursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
            ),
            selection,
            null,
            "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC LIMIT $limit OFFSET $offset"
        )


        contactCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn) ?: "Unknown"

                // getting structured names
                val (firstName, lastName) = contactNames[id] ?: Pair("", "")

                val phoneNumbers = fetchPhoneNumbers(id)

                // filtering if necessary
                if (phoneNumbers.isNotEmpty() && !PrefSingleton.getBool(id.toString())) {
                    contacts.add(
                        Contact(
                            id = id,
                            firstName = firstName,
                            lastName = lastName,
                            displayName = displayName,
                            phoneNumbers = phoneNumbers
                        )
                    )
                }
            }
        }

        contacts
    }

    fun getRawContactId(contactId: Long, context: Context): Long? {
        val contentResolver = context.contentResolver
        var rawContactId: Long? = null

        val cursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(ContactsContract.RawContacts._ID),
            "${ContactsContract.RawContacts.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                rawContactId = it.getLong(it.getColumnIndexOrThrow(ContactsContract.RawContacts._ID))
            }
        }

        return rawContactId
    }

    override suspend fun updateContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            try {
                val contentResolver = context.contentResolver
                val operations = arrayListOf<ContentProviderOperation>()
                val rawContactId = getRawContactId(contact.id, context)
                if (rawContactId == null) {
                    Log.e("contact", "Raw contact ID not found for contact ID: ${contact.id}")
                    return@withContext
                }

                contact.phoneNumbers.forEach { phoneNumber ->
                    operations.add(

                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                          //  .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                          //  .withValue(ContactsContract.Data.CONTACT_ID, contact.id)
                            .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            //   .withValue(ContactsContract.CommonDataKinds.Phone.CONTACT_ID, contact.id)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber.number)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phoneNumber.type)
                          //  .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)

                        .build()
                    )
                }


                //Execute update of operations
                contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
                Log.e("contact","Contact is added successfully: $operations")

            }catch (e: Exception) {
                Log.e("contact","An exception occured: ${e.localizedMessage}")
            }
            //Get content resolver to update contacts

        }

    }

    override suspend fun removeContact(contactId: Long, contacts: List<Contact>): List<Contact>{
      return contacts.filter {
          it.id != contactId
      }
    }

    override suspend fun removeContacts(
        contactIds: List<Long>,
        contacts: List<Contact>
    ): List<Contact> {
        return contacts.filter { contact ->
            contact.id !in contactIds
        }
    }

    private fun fetchPhoneNumbers(contactId:Long): List<PhoneNumber> {
        val phoneNumbers = mutableListOf<PhoneNumber>()

        val phoneCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )

        phoneCursor?.use { cursor ->
            val numberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val typeColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
            val labelColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)

            while (cursor.moveToNext()) {
                val number = cursor.getString(numberColumn)
                val type = cursor.getInt(typeColumn)
                val label = when (type) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> "Domicile"
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> "Mobile"
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> "Travail"
                    ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> "Autre"
                    else -> cursor.getString(labelColumn) ?: "Non spécifié"
                }

                phoneNumbers.add(PhoneNumber(number, type, label))
            }
        }
        return phoneNumbers
    }


}














