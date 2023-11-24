package com.Apic.apic

class FriendList (
        val email : String,
        val name : String
){
        @JvmName("callFromString")
        fun getName(): String {
                return name
        }
}