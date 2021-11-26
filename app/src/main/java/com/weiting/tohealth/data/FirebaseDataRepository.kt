package com.weiting.tohealth.data

import androidx.lifecycle.MutableLiveData

class FirebaseDataRepository(private val firebaseSource: FirebaseSource) : FirebaseRepository {

    override fun getLiveUser(userId: String): MutableLiveData<User> {
        return firebaseSource.getLiveUser(userId)
    }

    override suspend fun getUser(userId: String): User {
        return firebaseSource.getUser(userId)
    }

    override fun signIn(user: User) {
        return firebaseSource.signIn(user)
    }

    override suspend fun getAllDrugs(userId: String): List<Drug> {
        return firebaseSource.getAllDrugs(userId)
    }

    override suspend fun getDrug(itemId: String): Drug {
        return firebaseSource.getDrug(itemId)
    }

    override suspend fun getAllMeasures(userId: String): List<Measure> {
        return firebaseSource.getAllMeasures(userId)
    }

    override suspend fun getAllEvents(userId: String): List<Event> {
        return firebaseSource.getAllEvents(userId)
    }

    override suspend fun getAllCares(userId: String): List<Care> {
        return firebaseSource.getAllCares(userId)
    }

    override suspend fun getMeasure(itemId: String): Measure {
        return firebaseSource.getMeasure(itemId)
    }

    override fun getLiveDrugs(userId: String): MutableLiveData<List<Drug>> {
        return firebaseSource.getLiveDrugs(userId)
    }

    override fun getLiveMeasures(userId: String): MutableLiveData<List<Measure>> {
        return firebaseSource.getLiveMeasures(userId)
    }

    override fun getLiveEvents(userId: String): MutableLiveData<List<Event>> {
        return firebaseSource.getLiveEvents(userId)
    }

    override fun getLiveCares(userId: String): MutableLiveData<List<Care>> {
        return firebaseSource.getLiveCares(userId)
    }

    override fun postDrug(drug: Drug) {
        return firebaseSource.postDrug(drug)
    }

    override fun postMeasure(measure: Measure) {
        return firebaseSource.postMeasure(measure)
    }

    override fun postEvent(event: Event) {
        return firebaseSource.postEvent(event)
    }

    override fun postCare(care: Care) {
        return firebaseSource.postCare(care)
    }

    override fun updateDrug(drug: Drug) {
        return firebaseSource.updateDrug(drug)
    }

    override fun updateMeasure(measure: Measure) {
        return firebaseSource.updateMeasure(measure)
    }

    override fun updateEvent(event: Event) {
        return firebaseSource.updateEvent(event)
    }

    override fun updateCare(care: Care) {
        return firebaseSource.updateCare(care)
    }

    override fun postDrugLog(id: String, drugLog: DrugLog) {
        return firebaseSource.postDrugLog(id, drugLog)
    }

    override suspend fun getMeasureLogId(itemId: String): String {
        return firebaseSource.getMeasureLogId(itemId)
    }

    override fun postMeasureLog(id: String, measureLog: MeasureLog) {
        return firebaseSource.postMeasureLog(id, measureLog)
    }

    override fun postEventLog(id: String, eventLog: EventLog) {
        return firebaseSource.postEventLog(id, eventLog)
    }

    override fun postCareLog(id: String, careLog: CareLog) {
        return firebaseSource.postCareLog(id, careLog)
    }

    override suspend fun getDrugLogs(itemId: String): List<DrugLog> {
        return firebaseSource.getDrugLogs(itemId)
    }

    override suspend fun getMeasureLogs(itemId: String): List<MeasureLog> {
        return firebaseSource.getMeasureLogs(itemId)
    }

    override suspend fun getMeasureLog(itemId: String, itemsLogId: String): MeasureLog {
        return firebaseSource.getMeasureLog(itemId, itemsLogId)
    }

    override suspend fun getEventLogs(itemId: String): List<EventLog> {
        return firebaseSource.getEventLogs(itemId)
    }

    override suspend fun getCareLogs(itemId: String): List<CareLog> {
        return firebaseSource.getCareLogs(itemId)
    }

    override fun getLiveDrugLogs(itemId: String): MutableLiveData<List<DrugLog>> {
        return firebaseSource.getLiveDrugLogs(itemId)
    }

    override fun getLiveMeasureLogs(itemId: String): MutableLiveData<List<MeasureLog>> {
        return firebaseSource.getLiveMeasureLogs(itemId)
    }

    override fun getLiveEventLogs(itemId: String): MutableLiveData<List<EventLog>> {
        return firebaseSource.getLiveEventLogs(itemId)
    }

    override fun getLiveCareLogs(itemId: String): MutableLiveData<List<CareLog>> {
        return firebaseSource.getLiveCareLogs(itemId)
    }

    override fun createGroup(group: Group) {
        return firebaseSource.createGroup(group)
    }

    override fun getNewGroupId(): String {
        return firebaseSource.getNewGroupId()
    }

    override suspend fun checkIsGroupExist(groupId: String): Boolean {
        return firebaseSource.checkIsGroupExist(groupId)
    }

    override fun joinGroup(member: Member, groupId: String) {
        return firebaseSource.joinGroup(member, groupId)
    }

    override suspend fun checkIsRelationExist(userId: String, groupId: String): Boolean {
        return firebaseSource.checkIsRelationExist(userId, groupId)
    }

    override suspend fun getGroups(groupId: String): List<Group> {
        return firebaseSource.getGroups(groupId)
    }

    override suspend fun getMembers(groupId: String): List<Member> {
        return firebaseSource.getMembers(groupId)
    }

    override suspend fun getNotes(groupId: String): List<Note> {
        return firebaseSource.getNotes(groupId)
    }

    override suspend fun getReminders(groupId: String): List<Reminder> {
        return firebaseSource.getReminders(groupId)
    }

    override fun getLiveMembers(groupId: String): MutableLiveData<List<Member>> {
        return firebaseSource.getLiveMembers(groupId)
    }

    override fun updateMemberInfo(groupId: String, member: Member) {
        return firebaseSource.updateMemberInfo(groupId, member)
    }

    override fun getLiveNotes(groupId: String): MutableLiveData<List<Note>> {
        return firebaseSource.getLiveNotes(groupId)
    }

    override fun getLiveReminders(groupId: String): MutableLiveData<List<Reminder>> {
        return firebaseSource.getLiveReminders(groupId)
    }

    override fun postNote(note: Note, groupId: String) {
        return firebaseSource.postNote(note, groupId)
    }

    override fun postReminder(reminder: Reminder, groupId: String) {
        return firebaseSource.postReminder(reminder, groupId)
    }

    override fun deleteNote(note: Note, groupId: String) {
        return firebaseSource.deleteNote(note, groupId)
    }

    override fun deleteReminder(reminder: Reminder, groupId: String) {
        return firebaseSource.deleteReminder(reminder, groupId)
    }

    override fun getLiveChatMessages(
        userId: String,
        groupId: String
    ): MutableLiveData<List<Chat>> {
        return firebaseSource.getLiveChatMessages(userId, groupId)
    }

    override fun postChatMessage(chat: Chat) {
        return firebaseSource.postChatMessage(chat)
    }

    // Reduce the stock when task finished.
    override fun editStock(itemId: String, num: Float) {
        return firebaseSource.editStock(itemId, num)
    }

    override fun postAlertMessage(alertMessage: AlertMessage) {
        return firebaseSource.postAlertMessage(alertMessage)
    }

    override fun getLiveAlertMessageForService(
        userId: String
    ): MutableLiveData<List<AlertMessage>> {
        return firebaseSource.getLiveAlertMessageForService(userId)
    }

    override fun getLiveAlertMessages(userIdList: List<String>):
            MutableLiveData<List<AlertMessage>> {
        return firebaseSource.getLiveAlertMessages(userIdList)
    }

    override fun postOnGetAlertMessagesForService(alertMessage: AlertMessage) {
        return firebaseSource.postOnGetAlertMessagesForService(alertMessage)
    }

    override fun getLiveChatMessagesForService(
        groupId: String
    ): MutableLiveData<List<Chat>> {
        return firebaseSource.getLiveChatMessagesForService(groupId)
    }

    override fun postOnGetChatForService(chat: Chat) {
        return firebaseSource.postOnGetChatForService(chat)
    }
}
