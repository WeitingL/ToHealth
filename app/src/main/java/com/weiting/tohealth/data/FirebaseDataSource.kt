package com.weiting.tohealth.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.weiting.tohealth.PublicApplication.Companion.application
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

object FirebaseDataSource : FirebaseSource {

    override fun login(userName: String): MutableLiveData<User> {
        val database = application.database
        val user = MutableLiveData<User>()

        database.collection("users")
            .whereEqualTo("name", userName)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                for (document in value!!) {
                    user.value = document.toObject(User::class.java)
                }
            }
        return user
    }

    override fun signIn(userName: String) {
        val data = User(
            name = userName,
            id = application.database.collection("users").document().id
        )

        application.database.collection("users")
            .document(data.id!!)
            .set(data)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${data.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }

    }

    override suspend fun getAllDrugs(): List<Drug> = suspendCoroutine { continuation ->
        val list = mutableListOf<Drug>()
        val database = application.database

        database.collection("drugs")
            .whereEqualTo("userId", "Weiting")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Drug::class.java)
                list += dataList

//                Log.i("drugsList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override suspend fun getAllMeasures(): List<Measure> = suspendCoroutine { continuation ->
        val list = mutableListOf<Measure>()
        val database = application.database

        database.collection("measures")
            .whereEqualTo("userId", "Weiting")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Measure::class.java)
                list += dataList

//                Log.i("measuresList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override suspend fun getAllActivities(): List<Activity> = suspendCoroutine { continuation ->
        val list = mutableListOf<Activity>()
        val database = application.database

        database.collection("activity")
            .whereEqualTo("userId", "Weiting")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Activity::class.java)
                list += dataList

//                Log.i("activityList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override suspend fun getAllCares(): List<Care> = suspendCoroutine { continuation ->
        val list = mutableListOf<Care>()
        val database = application.database

        database.collection("cares")
            .whereEqualTo("userId", "Weiting")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Care::class.java)
                list += dataList

//                Log.i("caresList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override fun getLiveDrugList(userId: String): MutableLiveData<List<Drug>> {

        val drugList = MutableLiveData<List<Drug>>()

        application.database.collection("drugs")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }
                val list = mutableListOf<Drug>()
                for (document in value!!) {
                    val data = document.toObject(Drug::class.java)

                    list.add(data)
                }
                drugList.value = list
            }
        return drugList
    }

    override fun getLiveMeasureList(userId: String): MutableLiveData<List<Measure>> {
        val measureList = MutableLiveData<List<Measure>>()

        application.database.collection("measures")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Measure>()

                for (document in value!!) {
                    val data = document.toObject(Measure::class.java)
                    list.add(data)
                }

                measureList.value = list
            }
        return measureList
    }

    override fun getLiveActivityList(userId: String): MutableLiveData<List<Activity>> {
        val activityList = MutableLiveData<List<Activity>>()

        application.database.collection("activity")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Activity>()

                for (document in value!!) {
                    val data = document.toObject(Activity::class.java)
                    list.add(data)
                }

                activityList.value = list
            }
        return activityList
    }

    override fun getLiveCareList(userId: String): MutableLiveData<List<Care>> {
        val careList = MutableLiveData<List<Care>>()

        application.database.collection("cares")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Care>()

                for (document in value!!) {
                    val data = document.toObject(Care::class.java)
                    list.add(data)
                }

                careList.value = list
            }
        return careList
    }


    override fun postDrug(drug: Drug) {

        val database = application.database

        drug.id = database.collection("drugs").document().id
        database.collection("drugs").document(drug.id!!)
            .set(drug)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${drug.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postMeasure(measure: Measure) {
        val database = application.database

        measure.id = database.collection("measures").document().id
        database.collection("measures").document(measure.id!!)
            .set(measure)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${measure.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postActivity(activity: Activity) {
        val database = application.database

        activity.id = database.collection("activity").document().id
        database.collection("activity").document(activity.id!!)
            .set(activity)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${activity.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postCare(care: Care) {
        val database = application.database

        care.id = database.collection("cares").document().id
        database.collection("cares").document(care.id!!)
            .set(care)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${care.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }


    override fun postDrugRecord(id: String, drugLog: DrugLog) {

        val database = application.database

        drugLog.id = database.collection("drugs").document(id).collection("drugLogs").document().id
        database.collection("drugs").document(id).collection("drugLogs").document(drugLog.id!!)
            .set(drugLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${drugLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postMeasureRecord(id: String, measureLog: MeasureLog) {
        val database = application.database

        measureLog.id =
            database.collection("measures").document(id).collection("measuresLogs").document().id
        database.collection("measures").document(id).collection("measuresLogs")
            .document(measureLog.id!!)
            .set(measureLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${measureLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postActivityRecord(id: String, activityLog: ActivityLog) {
        val database = application.database

        activityLog.id =
            database.collection("activity").document(id).collection("activityLogs").document().id
        database.collection("activity").document(id).collection("activityLogs")
            .document(activityLog.id!!)
            .set(activityLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${activityLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postCareRecord(id: String, careLog: CareLog) {
        val database = application.database

        careLog.id = database.collection("cares").document(id).collection("careLogs").document().id
        database.collection("cares").document(id).collection("careLogs").document(careLog.id!!)
            .set(careLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${careLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun getDrugRecord(itemId: String, createTime: Timestamp): List<DrugLog> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<DrugLog>()
            val database = application.database

            database.collection("drugs").document(itemId).collection("drugLogs")
                .limit(100)
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(DrugLog::class.java)
                    list += dataList

                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getMeasureRecord(itemId: String, createTime: Timestamp): List<MeasureLog> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<MeasureLog>()
            val database = application.database

            database.collection("measures").document(itemId).collection("measuresLogs")
                .limit(100)
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(MeasureLog::class.java)
                    list += dataList

                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getActivityRecord(
        itemId: String,
        createTime: Timestamp
    ): List<ActivityLog> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<ActivityLog>()
            val database = application.database

            database.collection("activity").document(itemId).collection("activityLogs")
                .limit(100)
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(ActivityLog::class.java)
                    list += dataList

                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }


    override suspend fun getCareRecord(itemId: String, createTime: Timestamp): List<CareLog> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<CareLog>()
            val database = application.database

            database.collection("cares").document(itemId).collection("careLogs")
                .limit(100)
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(CareLog::class.java)
                    list += dataList

                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override fun createGroup(group: Group) {
        val database = application.database

        database.collection("groups").document(group.id!!)
            .set(group)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${group.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }

    }

    override fun getNewGroupId(): String {
        return application.database.collection("groups").document().id
    }

    override fun joinGroup(member: Member, groupId: String) {
        val database = application.database

        database.collection("groups").document(groupId)
            .collection("members").document()
            .set(member)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "store success",
                    "DocumentSnapshot added in group by user_ID: ${member.userId}"
                )
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }

        database.collection("users").document(member.userId!!)
            .update("groupList", FieldValue.arrayUnion(groupId))
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "update success",
                    "DocumentSnapshot added in group by user_ID: ${member.userId}"
                )
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun checkIsRelationExist(userId: String, groupId: String): Boolean =
        suspendCoroutine { continuation ->
            application.database.collection("users")
                .whereArrayContains("groupList", groupId)
                .get()
                .addOnSuccessListener { result ->

                    when (result.isEmpty) {
                        true -> continuation.resume(false)
                        false -> continuation.resume(true)
                    }
                }
        }

    override suspend fun checkIsGroupExist(groupId: String): Boolean =
        suspendCoroutine { continuation ->
            val database = application.database

            database.collection("groups")
                .whereEqualTo("id", groupId)
                .get()
                .addOnSuccessListener { result ->

                    when (result.isEmpty) {
                        true -> continuation.resume(false)
                        false -> continuation.resume(true)
                    }
                }
        }

    override suspend fun getGroups(groupId: String): List<Group> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Group>()
            val database = application.database

            database.collection("groups")
                .whereEqualTo("id", groupId)
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(Group::class.java)
                    list += dataList

                    Log.i("GroupList", list.toString())
                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getMember(groupId: String): List<Member> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Member>()
            val database = application.database

            database.collection("groups").document(groupId)
                .collection("members")
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(Member::class.java)
                    list += dataList

                    Log.i("MemberList", list.toString())
                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getNote(groupId: String): List<Note> = suspendCoroutine { continuation ->
        val list = mutableListOf<Note>()
        val database = application.database

        database.collection("groups").document(groupId)
            .collection("notes")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Note::class.java)
                list += dataList

                Log.i("NoteList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override suspend fun getCalenderItem(groupId: String): List<CalenderItem> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<CalenderItem>()
            val database = application.database

            database.collection("groups").document(groupId)
                .collection("calenderItems")
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(CalenderItem::class.java)
                    list += dataList

//                    Log.i("CalenderItemList", list.toString())
                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override fun getLiveMember(groupId: String): MutableLiveData<List<Member>> {
        val memberList = MutableLiveData<List<Member>>()

        application.database.collection("groups").document(groupId)
            .collection("members")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Member>()

                for (document in value!!) {
                    val data = document.toObject(Member::class.java)
                    list.add(data)
                }

                memberList.value = list
            }
        return memberList
    }

    override fun getLiveNote(groupId: String): MutableLiveData<List<Note>> {
        val notesList = MutableLiveData<List<Note>>()

        application.database.collection("groups").document(groupId)
            .collection("notes")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Note>()

                for (document in value!!) {
                    val data = document.toObject(Note::class.java)
                    list.add(data)
                }

                notesList.value = list
            }
        return notesList
    }

    override fun getLiveCalenderItem(groupId: String): MutableLiveData<List<CalenderItem>> {
        val calenderItemsList = MutableLiveData<List<CalenderItem>>()

        application.database.collection("groups").document(groupId)
            .collection("calenderItems")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<CalenderItem>()

                for (document in value!!) {
                    val data = document.toObject(CalenderItem::class.java)
                    list.add(data)
                }

                calenderItemsList.value = list
            }
        return calenderItemsList
    }

    override fun postNote(note: Note, groupId: String) {
        val database = application.database

        note.id = database.collection("groups").document(groupId)
            .collection("notes").document().id

        database.collection("groups").document(groupId)
            .collection("notes").document(note.id!!)
            .set(note)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${note.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postCalenderItem(calenderItem: CalenderItem, groupId: String) {
        val database = application.database

        calenderItem.id = database.collection("groups")
            .document(groupId).collection("calenderItems").document().id

        database.collection("groups").document(groupId)
            .collection("calenderItems").document(calenderItem.id!!)
            .set(calenderItem)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${calenderItem.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun getLiveChatMessage(
        userId: String,
        groupId: String
    ): MutableLiveData<List<Chat>> {
        val chatItemsList = MutableLiveData<List<Chat>>()

        application.database.collection("chats")
            .whereEqualTo("groupId", groupId)
            .orderBy("createTimestamp", Query.Direction.ASCENDING)
            .limit(50)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Chat>()

                Log.i("LiveChatValue", "$value")

                for (document in value!!) {
                    val data = document.toObject(Chat::class.java)
                    Log.i("LiveChat", "$data")
                    list.add(data)
                }

                chatItemsList.value = list
            }
        return chatItemsList

    }

    override fun postChatMessage(chat: Chat) {
        val database = application.database

        chat.id = database.collection("chats").document().id

        database.collection("chats").document(chat.id!!)
            .set(chat)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${chat.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun getUserInfo(userId: String): User = suspendCoroutine { continuation ->

        application.database.collection("users").document(userId)
            .get()
            .addOnSuccessListener { result ->

                val data = result.toObject(User::class.java)

                if (data != null) {
                    Log.i("CalenderItemList", data.toString())
                    continuation.resume(data)
                }

            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override fun editStock(itemId: String, num: Int) {
        application.database.collection("drugs").document(itemId)
            .update("stock", num)
            .addOnSuccessListener { documentReference ->
                Log.d("update success", "DocumentSnapshot with ID: ${itemId}")
            }
            .addOnFailureListener { e ->
                Log.w("update failure", "Error update document", e)
            }
    }
}