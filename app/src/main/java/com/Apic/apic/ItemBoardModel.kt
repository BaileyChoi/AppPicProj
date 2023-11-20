package com.Apic.apic

//kotlin-> data class

data class ItemBoardModel(
    var docId: String? = null, //각각의 데이터에 대한 id에 대한 정보(fireStore)
    var email: String? = null,
    var content: String? = null,
    var date: String? = null,
)
