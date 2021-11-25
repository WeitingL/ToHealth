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

    override suspend fun getAllActivities(userId: String): List<Event> {
        return firebaseSource.getAllActivities(userId)
    }

    override suspend fun getAllCares(userId: String): List<Care> {
        return firebaseSource.getAllCares(userId)
    }

    override suspend fun getMeasure(itemId: String): Measure {
        return firebaseSource.getMeasure(itemId)
    }

    override fun getLiveDrugList(userId: String): MutableLiveData<List<Drug>> {
        return firebaseSource.getLiveDrugList(userId)
    }

    override fun getLiveMeasureList(userId: String): MutableLiveData<List<Measure>> {
        return firebaseSource.getLiveMeasureList(userId)
    }

    override fun getLiveActivityList(userId: String): MutableLiveData<List<Event>> {
        return firebaseSource.getLiveActivityList(userId)
    }

    override fun getLiveCareList(userId: String): MutableLiveData<List<Care>> {
        return firebaseSource.getLiveCareList(userId)
    }

    override fun postDrug(drug: Drug) {
        return firebaseSource.postDrug(drug)
    }

    override fun postMeasure(measure: Measure) {
        return firebaseSource.postMeasure(measure)
    }

    override fun postActivity(event: Event) {
        return firebaseSource.postActivity(event)
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

    override fun updateActivity(event: Event) {
        return firebaseSource.updateActivity(event)
    }

    override fun updateCare(care: Care) {
        return firebaseSource.updateCare(care)
    }

    override fun postDrugRecord(id: String, drugLog: DrugLog) {
        return firebaseSource.postDrugRecord(id, drugLog)
    }

    override suspend fun getMeasureRecordId(itemId: String): String {
        return firebaseSource.getMeasureRecordId(itemId)
    }

    override fun postMeasureRecord(id: String, measureLog: MeasureLog) {
        return firebaseSource.postMeasureRecord(id, measureLog)
    }

    override fun postActivityRecord(id: String, eventLog: EventLog) {
        return firebaseSource.postActivityRecord(id, eventLog)
    }

    override fun postCareRecord(id: String, careLog: CareLog) {
        return firebaseSource.postCareRecord(id, careLog)
    }

    override suspend fun getDrugRecord(itemId: String): List<DrugLog> {
        return firebaseSource.getDrugRecord(itemId)
    }

    override suspend fun getMeasureRecord(itemId: String): List<MeasureLog> {
        return firebaseSource.getMeasureRecord(itemId)
    }

    override suspend fun getMeasureLog(itemId: String, itemsLogId: String): MeasureLog {
        return firebaseSource.getMeasureLog(itemId, itemsLogId)
    }

    override suspend fun getActivityRecord(itemId: String): List<EventLog> {
        return firebaseSource.getActivityRecord(itemId)
    }

    override suspend fun getCareRecord(itemId: String): List<CareLog> {
        return firebaseSource.getCareRecord(itemId)
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

    override suspend fun getMember(groupId: String): List<Member> {
        return firebaseSource.getMember(groupId)
    }

    override suspend fun getNote(groupId: String): List<Note> {
        return firebaseSource.getNote(groupId)
    }

    override suspend fun getCalenderItem(groupId: String): List<CalenderItem> {
        return firebaseSource.getCalenderItem(groupId)
    }

    override fun getLiveMember(groupId: String): MutableLiveData<List<Member>> {
        return firebaseSource.getLiveMember(groupId)
    }

    override fun updateMemberInfo(groupId: String, member: Member) {
        return firebaseSource.updateMemberInfo(groupId, member)
    }

    override fun getLiveNote(groupId: String): MutableLiveData<List<Note>> {
        return firebaseSource.getLiveNote(groupId)
    }

    override fun getLiveCalenderItem(groupId: String): MutableLiveData<List<CalenderItem>> {
        return firebaseSource.getLiveCalenderItem(groupId)
    }

    override fun postNote(note: Note, groupId: String) {
        return firebaseSource.postNote(note, groupId)
    }

    override fun postCalenderItem(calenderItem: CalenderItem, groupId: String) {
        return firebaseSource.postCalenderItem(calenderItem, groupId)
    }

    override fun deleteNote(note: Note, groupId: String) {
        return firebaseSource.deleteNote(note, groupId)
    }

    override fun deleteCalenderItem(calenderItem: CalenderItem, groupId: String) {
        return firebaseSource.deleteCalenderItem(calenderItem, groupId)
    }

    override fun getLiveChatMessage(
        userId: String,
        groupId: String
    ): MutableLiveData<List<Chat>> {
        return firebaseSource.getLiveChatMessage(userId, groupId)
    }

    override fun postChatMessage(chat: Chat) {
        return firebaseSource.postChatMessage(chat)
    }

    // Reduce the stock when task finished.
    override fun editStock(itemId: String, num: Float) {
        return firebaseSource.editStock(itemId, num)
    }

    override fun postNotification(notification: Notification) {
        return firebaseSource.postNotification(notification)
    }

    override fun getLiveNotificationForService(
        userId: String
    ): MutableLiveData<List<Notification>> {
        return firebaseSource.getLiveNotificationForService(userId)
    }

    override fun getLiveNotification(userIdList: List<String>):
            MutableLiveData<List<Notification>> {
        return firebaseSource.getLiveNotification(userIdList)
    }

    override fun postOnGetNotificationForService(notification: Notification) {
        return firebaseSource.postOnGetNotificationForService(notification)
    }

    override fun getLiveChatMessageForService(
        groupId: String
    ): MutableLiveData<List<Chat>> {
        return firebaseSource.getLiveChatMessageForService(groupId)
    }

    override fun postOnGetChatForService(chat: Chat) {
        return firebaseSource.postOnGetChatForService(chat)
    }
}
