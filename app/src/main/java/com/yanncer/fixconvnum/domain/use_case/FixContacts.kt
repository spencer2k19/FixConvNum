package com.yanncer.fixconvnum.domain.use_case

import com.yanncer.fixconvnum.common.BeninPhoneValidator.hasPhoneNumberIssue
import com.yanncer.fixconvnum.common.BeninPhoneValidator.isExtendedNumber
import com.yanncer.fixconvnum.common.BeninPhoneValidator.isLocalNumber
import com.yanncer.fixconvnum.common.BeninPhoneValidator.lastEightDigits
import com.yanncer.fixconvnum.common.Resource
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.models.PhoneNumber
import com.yanncer.fixconvnum.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FixContacts @Inject constructor(
    private val repository: ContactsRepository
) {
     operator fun invoke(contacts: List<Contact>): Flow<Resource<List<Contact>>> {
        return  flow {
            try {
                emit(Resource.Loading())

                contacts.forEach {contact ->
                    val updatedPhoneNumbers = mutableListOf<PhoneNumber>()
                    contact.phoneNumbers.forEach {phoneNumber ->
                        val cleanNumber = phoneNumber.number.replace("\\s".toRegex(), "")
                        when {
                            cleanNumber.isLocalNumber() -> {
                                //Create extended version
                                val extendedNumber = "+22901" + cleanNumber.lastEightDigits()
                                // Verify if extended phone number doesn't exist
                                if (!contact.phoneNumbers.any { it.number.replace("\\s".toRegex(), "") == extendedNumber }) {
                                    updatedPhoneNumbers.add(
                                        PhoneNumber(
                                            number = extendedNumber,
                                            type = phoneNumber.type,
                                            label = phoneNumber.label
                                        )
                                    )
                                }
                            }

                            cleanNumber.isExtendedNumber() -> {
                                // Create local version
                                val localNumber = cleanNumber.lastEightDigits()

                                //Verify if local number doesn't exists
                                if (!contact.phoneNumbers.any { it.number.replace("\\s".toRegex(), "") == localNumber }) {
                                    updatedPhoneNumbers.add(
                                        PhoneNumber(
                                            number = localNumber,
                                            type = phoneNumber.type,
                                            label = phoneNumber.label
                                        )
                                    )
                                }
                            }
                        }

                        //add new phone numbers
                        if (updatedPhoneNumbers.isNotEmpty()) {
//                    val finalPhoneNumbers = contact.phoneNumbers.toMutableList()
//                    finalPhoneNumbers.addAll(updatedPhoneNumbers)

                            //update contact with new phone numbers
                            val updatedContact = contact.copy(phoneNumbers = updatedPhoneNumbers)
                            repository.updateContact(updatedContact)
                        }
                    }
                }
                val allUpdated = repository.fetchContacts()

                emit(Resource.Success(allUpdated))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: ""))
            }
        }
    }
}