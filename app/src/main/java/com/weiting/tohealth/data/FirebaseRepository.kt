package com.weiting.tohealth.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp

interface FirebaseRepository {

    /*
    Login with livedata
 */

    fun login(userName: String): MutableLiveData<User>

    fun signIn(userName: String)

    /*
       Items operation
     */

    //Get Items list
    suspend fun getAllDrugs(): List<Drug>

    suspend fun getAllMeasures(): List<Measure>

    suspend fun getAllActivities(): List<Activity>

    suspend fun getAllCares(): List<Care>

    fun getLiveDrugList(userId: String): MutableLiveData<List<Drug>>

    fun getLiveMeasureList(userId: String): MutableLiveData<List<Measure>>

    fun getLiveActivityList(userId: String): MutableLiveData<List<Activity>>

    fun getLiveCareList(userId: String): MutableLiveData<List<Care>>

    //Post Item
    fun postDrug(drug: Drug)

    fun postMeasure(measure: Measure)

    fun postActivity(activity: Activity)

    fun postCare(care: Care)

    //Post Item record
    fun postDrugRecord(id: String, drugLog: DrugLog)

    fun postMeasureRecord(id: String, measureLog: MeasureLog)

    fun postActivityRecord(id: String, activityLog: ActivityLog)

    fun postCareRecord(id: String, careLog: CareLog)

    //Get Item Log
    fun getLiveDrugRecord(itemId: String, createTime: Timestamp): MutableLiveData<List<DrugLog>>

    fun getLiveMeasureRecord(
        itemId: String,
        createTime: Timestamp
    ): MutableLiveData<List<MeasureLog>>

    fun getLiveActivityRecord(
        itemId: String,
        createTime: Timestamp
    ): MutableLiveData<List<ActivityLog>>

    fun getLiveCareRecord(itemId: String, createTime: Timestamp): MutableLiveData<List<CareLog>>

    /*
       Group operation
     */

    //Post Group
    fun createGroup(group: Group)

    fun getNewGroupId(): String

    // Get group Id
    suspend fun checkIsGroupExist(groupId: String): Boolean

    //Join Group
    fun joinGroup(member: Member, groupId: String)

    suspend fun checkIsRelationExist(userId: String, groupId: String): Boolean

    suspend fun getGroups(groupId: String): List<Group>

    suspend fun getMember(groupId: String): List<Member>

    suspend fun getNote(groupId: String): List<Note>

    suspend fun getCalenderItem(groupId: String): List<CalenderItem>

    fun getLiveMember(groupId: String): MutableLiveData<List<Member>>

    fun getLiveNote(groupId: String): MutableLiveData<List<Note>>

    fun getLiveCalenderItem(groupId: String): MutableLiveData<List<CalenderItem>>

    fun postNote(note: Note, groupId: String)

    fun postCalenderItem(calenderItem: CalenderItem, groupId: String)

    fun getLiveChatMessage(
        userId: String,
        groupId: String
    ): MutableLiveData<List<Chat>>

    fun postChatMessage(chat: Chat)

    suspend fun getUserInfo (userId: String):User

}