package com.madteam.sunset.data.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

data class Report(
    val id: String,
    val date: String,
    val description: String,
    val docReference: DocumentReference,
    val issue: String,
    val reporter: String,
    val type: String,
    val assignedBy: String
) {
    constructor() : this(
        id = "",
        date = "",
        description = "",
        docReference = FirebaseFirestore.getInstance().document(""),
        issue = "",
        reporter = "",
        type = "",
        assignedBy = ""
    )
}
