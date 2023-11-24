package com.Apic.apic.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class Todo(
    val title:String,
    val date: Long =  Calendar.getInstance().timeInMillis
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
