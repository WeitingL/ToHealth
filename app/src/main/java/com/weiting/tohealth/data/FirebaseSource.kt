package com.weiting.tohealth.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.io.LittleEndianDataInputStream
import com.google.firebase.Timestamp

interface FirebaseSource {

    /*
        Login with livedata
     */

    fun login(userId: String): MutableLiveData<User>

    suspend fun getUser(userId: String): User

    fun signIn(user: User)

    /*
       Items operation
     */

    //Get Items list
    suspend fun getAllDrugs(userId: String): List<Drug>

    suspend fun getAllMeasures(userId: String): List<Measure>

    suspend fun getAllActivities(userId: String): List<Activity>

    suspend fun getAllCares(userId: String): List<Care>


    fun getLiveDrugList(userId: String): MutableLiveData<List<Drug>>

    fun getLiveMeasureList(userId: String): MutableLiveData<List<Measure>>

    fun getLiveActivityList(userId: String): MutableLiveData<List<Activity>>

    fun getLiveCareList(userId: String): MutableLiveData<List<Care>>

    //Post Item
    fun postDrug(drug: Drug)

    fun postMeasure(measure: Measure)

    fun postActivity(activity: Activity)

    fun postCare(care: Care)

    //Update Item
    fun updateDrug(drug: Drug)

    fun updateMeasure(measure: Measure)

    fun updateActivity(activity: Activity)

    fun updateCare(care: Care)

    //Post Item record
    fun postDrugRecord(id: String, drugLog: DrugLog)

    fun postMeasureRecord(id: String, measureLog: MeasureLog)

    fun postActivityRecord(id: String, activityLog: ActivityLog)

    fun postCareRecord(id: String, careLog: CareLog)

    //Get Item Log
    suspend fun getDrugRecord(itemId: String, createTime: Timestamp): List<DrugLog>

    suspend fun getMeasureRecord(
        itemId: String,
        createTime: Timestamp
    ): List<MeasureLog>

    suspend fun getActivityRecord(
        itemId: String,
        createTime: Timestamp
    ): List<ActivityLog>

    suspend fun getCareRecord(itemId: String, createTime: Timestamp): List<CareLog>

    /*
        Group operation
     */

    //Post Group
    fun createGroup(group: Group)

    fun getNewGroupId(): String

    fun joinGroup(member: Member, groupId: String)


    suspend fun checkIsRelationExist(userId: String, groupId: String): Boolean

    suspend fun checkIsGroupExist(groupId: String): Boolean

    suspend fun getGroups(groupId: String): List<Group>

    suspend fun getMember(groupId: String): List<Member>

    suspend fun getNote(groupId: String): List<Note>

    suspend fun getCalenderItem(groupId: String): List<CalenderItem>


    fun getLiveMember(groupId: String): MutableLiveData<List<Member>>

    fun updateMemberInfo(groupId: String, member: Member)

    fun getLiveNote(groupId: String): MutableLiveData<List<Note>>

    fun getLiveCalenderItem(groupId: String): MutableLiveData<List<CalenderItem>>

    fun postNote(note: Note, groupId: String)

    fun postCalenderItem(calenderItem: CalenderItem, groupId: String)

    fun deleteNote(note: Note, groupId: String)

    fun deleteCalenderItem(calenderItem: CalenderItem, groupId: String)

    fun getLiveChatMessage(
        userId: String,
        groupId: String
    ): MutableLiveData<List<Chat>>

    fun postChatMessage(chat: Chat)

    suspend fun getUserInfo(userId: String): User

    fun editStock(itemId:String, num:Int)

}