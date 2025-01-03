package com.yanncer.fixconvnum.domain.use_case

import android.util.Log
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
        return flow {
            try {
                emit(Resource.Loading())

                contacts.forEach { contact ->
                    val updatedPhoneNumbers = mutableListOf<PhoneNumber>()
                    contact.phoneNumbers.forEach { phoneNumber ->
                        val cleanNumber = phoneNumber.number.replace("\\s".toRegex(), "")
                        when {
                            cleanNumber.isLocalNumber() -> {
                                //Create extended version
                                val extendedNumber = "+22901" + cleanNumber.lastEightDigits()
                                // Verify if extended phone number doesn't exist
                                if (
                                    !contact.phoneNumbers.any {
                                        it.number.replace("\\s".toRegex(), "") in listOf(
                                            extendedNumber,
                                            "0022901" + cleanNumber.lastEightDigits(),
                                            "01" + cleanNumber.lastEightDigits()
                                        )

                                    }) {
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
                                if (!contact.phoneNumbers.any {
                                        it.number.replace(
                                            "\\s".toRegex(),
                                            ""
                                        ) in listOf(
                                            localNumber,
                                            "+229$localNumber",
                                            "00229$localNumber"
                                        )
                                    }

                                ) {
                                    updatedPhoneNumbers.add(
                                        PhoneNumber(
                                            number = "+229$localNumber",
                                            type = phoneNumber.type,
                                            label = phoneNumber.label
                                        )
                                    )
                                }
                            }
                        }


                    }

                    //add new phone numbers
                    if (updatedPhoneNumbers.isNotEmpty()) {
                        //update contact with new phone numbers
                        val updatedContact = contact.copy(phoneNumbers = updatedPhoneNumbers)
                        repository.updateContact(updatedContact)
                    }
                }
                val allUpdated = repository.fetchContacts(searchQuery = "")

                emit(Resource.Success(allUpdated))
            } catch (e: Exception) {
                Log.e("contact","An exception while fixed contacts: ${e.localizedMessage}")
                emit(Resource.Error(e.message ?: ""))
            }
        }
    }
}