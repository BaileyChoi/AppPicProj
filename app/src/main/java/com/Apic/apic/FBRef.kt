package com.Apic.apic

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object {
        private val database = Firebase.database("https://pictogether-e13c3-default-rtdb.asia-southeast1.firebasedatabase.app")
        val groupRef = database.getReference("groups")
    }
}