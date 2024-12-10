package com.yanncer.fixconvnum.domain.models

// Contact.kt
data class Contact(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val phoneNumbers: List<PhoneNumber>
)
