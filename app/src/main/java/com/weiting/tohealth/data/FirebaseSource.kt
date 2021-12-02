package com.weiting.tohealth.data

import androidx.lifecycle.MutableLiveData

interface FirebaseSource {

    /*
       Login
    */

    fun getLiveUser(userId: String): MutableLiveData<User>

    suspend fun getUser(userId: String): Result<User>

    fun signIn(user: User)

    /*
       Items operation
     */

    // Get Items list
    suspend fun getAllDrugs(userId: String): Result<List<Drug>>

    suspend fun getDrug(itemId: String): Result<Drug>

    suspend fun getAllMeasures(userId: String): Result<List<Measure>>

    suspend fun getAllEvents(userId: String): Result<List<Event>>

    suspend fun getAllCares(userId: String): Result<List<Care>>

    // Get Item
    suspend fun getMeasure(itemId: String): Result<Measure>

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
    suspend fun getDrugLogId(itemId: String): Result<String>

    fun postDrugLog(id: String, drugLog: DrugLog)

    fun deleteDrugLog(drugId: String, drugLogId: String)

    suspend fun getMeasureLogId(itemId: String): Result<String>

    fun postMeasureLog(id: String, measureLog: MeasureLog)

    fun postEventLog(id: String, eventLog: EventLog)

    fun postCareLog(id: String, careLog: CareLog)

    // Get Item Logs
    suspend fun getDrugLogs(itemId: String): Result<List<DrugLog>>

    suspend fun getMeasureLogs(itemId: String): Result<List<MeasureLog>>

    suspend fun getMeasureLog(itemId: String, itemsLogId: String): Result<MeasureLog>

    suspend fun getEventLogs(itemId: String): Result<List<EventLog>>

    suspend fun getCareLogs(itemId: String): Result<List<CareLog>>

    fun getLiveDrugLogs(itemId: String): MutableLiveData<List<DrugLog>>

    fun getLiveMeasureLogs(itemId: String): MutableLiveData<List<MeasureLog>>

    fun getLiveEventLogs(itemId: String): MutableLiveData<List<EventLog>>

    fun getLiveCareLogs(itemId: String): MutableLiveData<List<CareLog>>

    /*
        Group operation
     */

    // Post Group
    fun createGroup(group: Group)

    fun getNewGroupId(): Result<String>

    fun joinGroup(member: Member, groupId: String)

    suspend fun checkIsRelationExist(userId: String, groupId: String): Result<Boolean>

    suspend fun checkIsGroupExist(groupId: String): Result<Boolean>

    suspend fun getGroups(groupId: String): Result<List<Group>>

    suspend fun getMembers(groupId: String): Result<List<Member>>

    suspend fun getNotes(groupId: String): Result<List<Note>>

    suspend fun getReminders(groupId: String): Result<List<Reminder>>

    fun getLiveMembers(groupId: String): MutableLiveData<List<Member>>

    fun updateMemberInfo(groupId: String, member: Member)

    fun getLiveNotes(groupId: String): MutableLiveData<List<Note>>

    fun getLiveReminders(groupId: String): MutableLiveData<List<Reminder>>

    fun postNote(note: Note, groupId: String)

    fun postReminder(reminder: Reminder, groupId: String)

    fun deleteNote(note: Note, groupId: String)

    fun deleteReminder(reminder: Reminder, groupId: String)

    fun getLiveChatMessages(
        userId: String,
        groupId: String
    ): MutableLiveData<List<Chat>>

    fun postChatMessage(chat: Chat)

    fun editStock(itemId: String, num: Float)

    fun postAlertMessage(alertMessage: AlertMessage)

    fun getLiveAlertMessages(
        userIdList: List<String>
    ): MutableLiveData<List<AlertMessage>>

    fun postOnGetAlertMessagesForService(alertMessage: AlertMessage)

    fun getLiveChatMessagesForService(
        groupId: String
    ): MutableLiveData<List<Chat>>

    fun postOnGetChatForService(chat: Chat)
}
