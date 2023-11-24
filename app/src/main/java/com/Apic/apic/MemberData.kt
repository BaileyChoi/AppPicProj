package com.Apic.apic

class MemberData (
    val email : String,
    val name : String,
    //val password : String,
){
    @JvmName("callFromString")
    fun getName(): String {
    return name
    }
}