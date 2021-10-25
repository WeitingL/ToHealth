package com.weiting.tohealth.data

import androidx.lifecycle.MutableLiveData

class FirebaseDataRepository(private val firebaseSource: FirebaseSource) : FirebaseRepository {

    override fun login(userName: String): MutableLiveData<User> {
        return firebaseSource.login(userName)
    }

    override fun signIn(userName: String) {
        return firebaseSource.signIn(userName)
    }

    override suspend fun getAllDrugs(): List<Drug> {
        return firebaseSource.getAllDrugs()
    }

    override suspend fun getAllMeasures(): List<Measure> {
        return firebaseSource.getAllMeasures()
    }

    override suspend fun getAllActivities(): List<Activity> {
        return firebaseSource.getAllActivities()
    }

    override suspend fun getAllCares(): List<Care> {
        return firebaseSource.getAllCares()
    }

    //TODO Need to transfer the get function from above to below.
    override fun getLiveDrugList(userId: String): MutableLiveData<List<Drug>> {
        return firebaseSource.getLiveDrugList(userId)
    }

    override fun getLiveMeasureList(userId: String): MutableLiveData<List<Measure>> {
        return firebaseSource.getLiveMeasureList(userId)
    }

    override fun getLiveActivityList(userId: String): MutableLiveData<List<Activity>> {
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

    override fun postActivity(activity: Activity) {
        return firebaseSource.postActivity(activity)
    }

    override fun postCare(care: Care) {
        return firebaseSource.postCare(care)
    }

    override fun postDrugRecord(id: String, drugLog: DrugLog) {
        return firebaseSource.postDrugRecord(id, drugLog)
    }

    override fun postMeasureRecord(id: String, measureLog: MeasureLog) {
        return firebaseSource.postMeasureRecord(id, measureLog)
    }

    override fun postActivityRecord(id: String, activityLog: ActivityLog) {
        return firebaseSource.postActivityRecord(id, activityLog)
    }

    override fun postCareRecord(id: String, careLog: CareLog) {
        return firebaseSource.postCareRecord(id, careLog)
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

    override fun getLiveNote(groupId: String): MutableLiveData<List<Note>> {
        return firebaseSource.getLiveNote(groupId)
    }

    override fun getLiveCalenderItem(groupId: String): MutableLiveData<List<CalenderItem>> {
        return firebaseSource.getLiveCalenderItem(groupId)
    }

}