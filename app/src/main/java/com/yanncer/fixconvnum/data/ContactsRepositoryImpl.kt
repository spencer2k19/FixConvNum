package com.yanncer.fixconvnum.data

import android.content.Context
import android.provider.ContactsContract
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.models.PhoneNumber
import com.yanncer.fixconvnum.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl(private val context: Context): ContactsRepository {
    override suspend fun fetchContacts():  List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()


        // Projection for differents names
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
        )


        // Request for structured name
        val nameCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            "${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE),
            null
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
        val contactCursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            ),
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )


        contactCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn) ?: "Unknown"

                // Récupérer les noms structurés ou utiliser le nom complet
                val (firstName, lastName) = contactNames[id] ?: Pair("", "")

                val phoneNumbers = fetchPhoneNumbers(id)
                if (phoneNumbers.isNotEmpty()) {
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














