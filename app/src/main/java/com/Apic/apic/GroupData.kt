package com.Apic.apic

data class GroupData (
    val g_name : String = "",
    val g_participants : String
    )
{
    @JvmName("callFromString")
    fun getName():String {
        return g_name
    }
}



