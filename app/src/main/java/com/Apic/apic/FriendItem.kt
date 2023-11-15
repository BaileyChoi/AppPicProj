package com.Apic.apic


data class FriendItem(
    var RESOURCEID: Int? = null,
    var NAME: String? = null,
    var MESSAGE: String? = null
)

data class MyItems(val row:MutableList<FriendItem>)
