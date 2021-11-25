package com.weiting.tohealth.data

import androidx.lifecycle.MutableLiveData

interface FirebaseRepository {

    /*
    Login with livedata
 */

    fun getLiveUser(userId: String): MutableLiveData<User>

    suspend fun getUser(userId: String): User

    fun signIn(user: User)

    /*
       Items operation
     */

    // Get Items list
    suspend fun getAllDrugs(userId: String): List<Drug>

    suspend fun getDrug(itemId: String): Drug

    suspend fun getAllMeasures(userId: String): List<Measure>

    suspend fun getAllEvents(userId: String): List<Event>

    suspend fun getAllCares(userId: String): List<Care>

    suspend fun getMeasure(itemId: String): Measure

    fun getLiveDrugs(userId: String): MutableLiveData<List<Drug>>

    fun getLiveMeasures(userId: String): MutableLiveData<List<Measure>>

    fun getLiveEvents(userId: String): MutableLiveData<List<Event>>

    fun getLiveCares(userId: String): MutableLiveData<List<Care>>

    // Post Item
    fun postDrug(drug: Drug)

    fun postMeasure(measure: Measure)

    fun postEvent(event: Event)

    fun postCare(care: Care)

    // Update Item
    fun updateDrug(drug: Drug)

    fun updateMeasure(measure: Measure)

    fun updateEvent(event: Event)

    fun updateCare(care: Care)

    // Post Item record
    fun postDrugLog(id: String, drugLog: DrugLog)

    suspend fun getMeasureLogId(itemId: String): String

    fun postMeasureLog(id: String, measureLog: MeasureLog)

    fun postEventLog(id: String, eventLog: EventLog)

    fun postCareLog(id: String, careLog: CareLog)

    // Get Item Log
    suspend fun getDrugLogs(itemId: String): List<DrugLog>

    suspend fun getMeasureLogs(itemId: String): List<MeasureLog>

    suspend fun getMeasureLog(itemId: String, itemsLogId: String): MeasureLog

    suspend fun getEventLogs(itemId: String): List<EventLog>

    suspend fun getCareLogs(itemId: String): List<CareLog>
    /*
       Group operation
     */

    // Post Group
    fun createGroup(group: Group)

    fun getNewGroupId(): String

    // Get group Id
    suspend fun checkIsGroupExist(groupId: String): Boolean

    // Join Group
    fun joinGroup(member: Member, groupId: String)

    suspend fun checkIsRelationExist(userId: String, groupId: String): Boolean

    suspend fun getGroups(groupId: String): List<Group>

    suspend fun getMembers(groupId: String): List<Member>

    suspend fun getNotes(groupId: String): List<Note>

    suspend fun getReminders(groupId: String): List<Reminder>

    fun getLiveMembers(groupId: String): MutableLiveData<List<Member>>

    fun updateMemberInfo(groupId: String, member: Member)

    fun getLiveNotes(groupId: String): MutableLiveData<List<Note>>

    fun getLiveReminders(groupId: String): MutableLiveData<List<Reminder>>

    fun postNote(note: Note, groupId: String)

    fun postCalenderItem(reminder: Reminder, groupId: String)

    fun deleteNote(note: Note, groupId: String)

    fun deleteReminder(reminder: Reminder, groupId: String)

    fun getLiveChatMessages(
        userId: String,
        groupId: String
    ): MutableLiveData<List<Chat>>

    fun postChatMessage(chat: Chat)

    fun editStock(itemId: String, num: Float)

    fun postNotification(alertMessage: AlertMessage)

    fun getLiveNotificationsForService(
        userId: String
    ): MutableLiveData<List<AlertMessage>>

    fun getLiveNotifications(
        userIdList: List<String>
    ): MutableLiveData<List<AlertMessage>>

    fun postOnGetNotificationForService(alertMessage: AlertMessage)

    fun getLiveChatMessagesForService(
        groupId: String
    ): MutableLiveData<List<Chat>>

    fun postOnGetChatForService(chat: Chat)
}
