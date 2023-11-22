package com.Apic.apic

data class GroupListItem(
//    val group_photo : String = "",    // 그룹 사진
    val group_name: String = "",    // 그룹 이름
    val group_num: Int = 0,         // 그룹 멤버명수
    val group_liked : Boolean = false    // 그룹 즐겨찾기
)
{
    fun getGroupName(): String {
        return group_name
    }
    fun getGroupNum() : Int {
        return group_num
    }
    fun isGroupLiked() : Boolean {
        return group_liked
    }
 }