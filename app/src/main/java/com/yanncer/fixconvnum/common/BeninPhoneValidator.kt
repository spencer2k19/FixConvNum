package com.yanncer.fixconvnum.common

import com.yanncer.fixconvnum.domain.models.Contact

// PhoneValidation.kt
object BeninPhoneValidator {
    // Regex for local numbers (8 digits)
    private val localPhoneRegex = Regex(
        "^(\\+229|00229)?(40|41|42|43|44|45|46|47|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|90|91|92|93|94|95|96|97|98|99)\\d{6}$"
    )

    // Regex for extends numbers (10 digits)
    private val extendedPhoneRegex = Regex(
        "^(\\+229|00229)?(01){1}(40|41|42|43|44|45|46|47|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|90|91|92|93|94|95|96|97|98|99)\\d{6}$"
    )

    // Extension for clean numbers (delete spaces)
    private fun String.withoutSpaces(): String = replace("\\s".toRegex(), "")

    // Extension to get the 8 last digits
    private fun String.lastEightDigits(): String =
        withoutSpaces().takeLast(8)

    // Verify if a number is a local number
    private fun String.isLocalNumber(): Boolean =
        withoutSpaces().matches(localPhoneRegex)

    //  Verify if a number is an extended number
    private fun String.isExtendedNumber(): Boolean =
        withoutSpaces().matches(extendedPhoneRegex)

    // Principal method of validation for a contact
    fun Contact.hasPhoneNumberIssue(): Boolean {
        var isValid = true

        for (phoneNumber in phoneNumbers) {
            val cleanNumber = phoneNumber.number.withoutSpaces()
            val lastEight = cleanNumber.lastEightDigits()

            when {
                cleanNumber.isLocalNumber() -> {
                    //for a local number, search for extended versions
                    isValid = isValid && phoneNumbers.any { otherNumber ->
                        val otherClean = otherNumber.number.withoutSpaces()
                        otherClean == "+22901$lastEight" ||
                                otherClean == "0022901$lastEight" ||
                                otherClean == "01$lastEight"
                    }
                }
                cleanNumber.isExtendedNumber() -> {
                    //for an extended number, search for local versions
                    isValid = isValid && phoneNumbers.any { otherNumber ->
                        val otherClean = otherNumber.number.withoutSpaces()
                        otherClean == lastEight ||
                                otherClean == "+229$lastEight" ||
                                otherClean == "00229$lastEight"
                    }
                }
            }
        }

        return !isValid
    }
}