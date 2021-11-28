package com.weiting.tohealth.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.PublicApplication.Companion.application
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO Error handle add.

object FirebaseDataSource : FirebaseSource {

    override fun getLiveUser(userId: String): MutableLiveData<User> {
        val database = application.database
        val user = MutableLiveData<User>()

        database.collection("users")
            .whereEqualTo("id", userId)
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

    override suspend fun getUser(userId: String): User = suspendCoroutine { continuation ->
        val database = application.database

        database.collection("users").document(userId)
            .get()
            .addOnSuccessListener { result ->

                val data = result.toObject(User::class.java)
                continuation.resume(data ?: User())
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override fun signIn(user: User) {
        application.database.collection("users")
            .document(user.id!!)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${user.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun getAllDrugs(userId: String): List<Drug> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Drug>()
            val database = application.database

            database.collection("drugs")
                .whereEqualTo("userId", userId)
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

    override suspend fun getDrug(itemId: String): Drug =
        suspendCoroutine { continuation ->
            application.database.collection("drugs").document(itemId)
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        continuation.resume(it.toObject(Drug::class.java) ?: Drug())
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getAllMeasures(userId: String): List<Measure> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Measure>()
            val database = application.database

            database.collection("measures")
                .whereEqualTo("userId", userId)
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

    override suspend fun getAllEvents(userId: String): List<Event> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Event>()
            val database = application.database

            database.collection("events")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(Event::class.java)
                    list += dataList

//                Log.i("activityList", list.toString())
                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getAllCares(userId: String): List<Care> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Care>()
            val database = application.database

            database.collection("cares")
                .whereEqualTo("userId", userId)
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

    override suspend fun getMeasure(itemId: String): Measure =
        suspendCoroutine { continuation ->
            application.database.collection("measures").document(itemId)
                .get()
                .addOnSuccessListener {
                    continuation.resume(it?.toObject(Measure::class.java) ?: Measure())
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override fun getLiveDrugs(userId: String): MutableLiveData<List<Drug>> {

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

    override fun getLiveMeasures(userId: String): MutableLiveData<List<Measure>> {
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

    override fun getLiveEvents(userId: String): MutableLiveData<List<Event>> {
        val activityList = MutableLiveData<List<Event>>()

        application.database.collection("events")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Event>()

                for (document in value!!) {
                    val data = document.toObject(Event::class.java)
                    list.add(data)
                }

                activityList.value = list
            }
        return activityList
    }

    override fun getLiveCares(userId: String): MutableLiveData<List<Care>> {
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

    override fun postEvent(event: Event) {
        val database = application.database

        event.id = database.collection("events").document().id
        database.collection("events").document(event.id!!)
            .set(event)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${event.id}")
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

    override fun updateDrug(drug: Drug) {
        application.database.collection("drugs").document(drug.id!!)
            .set(drug, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${drug.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun updateMeasure(measure: Measure) {
        application.database.collection("measures").document(measure.id!!)
            .set(measure, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${measure.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun updateEvent(event: Event) {
        application.database.collection("events").document(event.id!!)
            .set(event, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${event.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun updateCare(care: Care) {
        application.database.collection("cares").document(care.id!!)
            .set(care, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${care.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun getDrugLogId(itemId: String): String = suspendCoroutine {

        val database = application.database
        val id = database.collection("drugs").document(itemId).collection("drugLogs")
            .document().id

        it.resume(id)
    }

    override fun postDrugLog(id: String, drugLog: DrugLog) {

        val database = application.database

        database.collection("drugs").document(id).collection("drugLogs").document(drugLog.id!!)
            .set(drugLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${drugLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun deleteDrugLog(drugId: String, drugLogId: String) {
        val database = application.database

        database.collection("drugs").document(drugId)
            .collection("drugLogs").document(drugLogId)
            .delete()
            .addOnSuccessListener {
                Log.d(
                    "Delete success",
                    "DocumentSnapshot successfully deleted! $drugLogId"
                )
            }
            .addOnFailureListener { e ->
                Log.w("Delete failure", "Error deleting document", e)
            }
    }

    override suspend fun getMeasureLogId(itemId: String): String = suspendCoroutine {

        val database = application.database
        val id = database.collection("measures").document(itemId).collection("measuresLogs")
            .document().id

        it.resume(id)
    }

    override fun postMeasureLog(id: String, measureLog: MeasureLog) {
        val database = application.database

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

    override fun postEventLog(id: String, eventLog: EventLog) {
        val database = application.database

        eventLog.id =
            database.collection("events").document(id).collection("eventsLogs").document().id
        database.collection("events").document(id).collection("eventsLogs")
            .document(eventLog.id!!)
            .set(eventLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${eventLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postCareLog(id: String, careLog: CareLog) {
        val database = application.database

        careLog.id =
            database.collection("cares").document(id).collection("careLogs").document().id
        database.collection("cares").document(id).collection("careLogs").document(careLog.id!!)
            .set(careLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${careLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun getDrugLogs(itemId: String): List<DrugLog> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<DrugLog>()
            val database = application.database

            database.collection("drugs").document(itemId).collection("drugLogs")
                .orderBy("createdTime", Query.Direction.DESCENDING)
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

    override suspend fun getMeasureLogs(itemId: String): List<MeasureLog> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<MeasureLog>()
            val database = application.database

            database.collection("measures").document(itemId).collection("measuresLogs")
                .orderBy("createdTime", Query.Direction.DESCENDING)
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

    override suspend fun getMeasureLog(itemId: String, itemsLogId: String): MeasureLog =
        suspendCoroutine { continuation ->
            application.database.collection("measures").document(itemId)
                .collection("measureLogs").document(itemsLogId)
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        continuation.resume(it.toObject(MeasureLog::class.java) ?: MeasureLog())
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getEventLogs(itemId: String): List<EventLog> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<EventLog>()
            val database = application.database

            database.collection("events").document(itemId).collection("eventsLogs")
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(EventLog::class.java)
                    list += dataList

                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getCareLogs(itemId: String): List<CareLog> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<CareLog>()
            val database = application.database

            database.collection("cares").document(itemId).collection("careLogs")
                .orderBy("createdTime", Query.Direction.DESCENDING)
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

    override fun getLiveDrugLogs(itemId: String): MutableLiveData<List<DrugLog>> {
        val drugLogsList = MutableLiveData<List<DrugLog>>()

        application.database.collection("drugs")
            .document(itemId)
            .collection("drugLogs")
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }
                val list = mutableListOf<DrugLog>()

                for (document in value!!) {
                    val data = document.toObject(DrugLog::class.java)

                    list.add(data)
                }
                drugLogsList.value = list
            }

        return drugLogsList
    }

    override fun getLiveMeasureLogs(itemId: String): MutableLiveData<List<MeasureLog>> {
        val MeasureLogsList = MutableLiveData<List<MeasureLog>>()

        application.database.collection("measures")
            .document(itemId)
            .collection("measureLogs")
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }
                val list = mutableListOf<MeasureLog>()

                for (document in value!!) {
                    val data = document.toObject(MeasureLog::class.java)

                    list.add(data)
                }
                MeasureLogsList.value = list
            }

        return MeasureLogsList
    }

    override fun getLiveEventLogs(itemId: String): MutableLiveData<List<EventLog>> {
        val EventLogsList = MutableLiveData<List<EventLog>>()

        application.database.collection("events")
            .document(itemId)
            .collection("eventLogs")
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }
                val list = mutableListOf<EventLog>()

                for (document in value!!) {
                    val data = document.toObject(EventLog::class.java)

                    list.add(data)
                }
                EventLogsList.value = list
            }

        return EventLogsList
    }

    override fun getLiveCareLogs(itemId: String): MutableLiveData<List<CareLog>> {
        val careLogsList = MutableLiveData<List<CareLog>>()

        application.database.collection("cares")
            .document(itemId)
            .collection("careLogs")
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }
                val list = mutableListOf<CareLog>()

                for (document in value!!) {
                    val data = document.toObject(CareLog::class.java)

                    list.add(data)
                }
                careLogsList.value = list
            }

        return careLogsList
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

        member.id = database.collection("groups").document(groupId)
            .collection("members").document().id

        database.collection("groups").document(groupId)
            .collection("members").document(member.id!!)
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
            application.database.collection("users").document(userId)
                .get()
                .addOnSuccessListener { result ->
                    val user = result.toObject(User::class.java)

                    when (user?.groupList?.contains(groupId)) {
                        true -> continuation.resume(true)
                        false -> continuation.resume(false)
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

//                    Log.i("GroupList", list.toString())
                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getMembers(groupId: String): List<Member> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Member>()
            val database = application.database

            database.collection("groups").document(groupId)
                .collection("members")
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(Member::class.java)
                    list += dataList

//                    Log.i("MemberList", list.toString())
                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override suspend fun getNotes(groupId: String): List<Note> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Note>()
            val database = application.database

            database.collection("groups").document(groupId)
                .collection("notes")
                .orderBy("createdTime", Query.Direction.DESCENDING)
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

    override suspend fun getReminders(groupId: String): List<Reminder> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Reminder>()
            val database = application.database

            database.collection("groups").document(groupId)
                .collection("reminders")
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->

                    val dataList = result.toObjects(Reminder::class.java)
                    list += dataList

//                    Log.i("CalenderItemList", list.toString())
                    continuation.resume(list)
                }
                .addOnFailureListener { e ->
                    Log.w("Error to get data", e)
                }
        }

    override fun getLiveMembers(groupId: String): MutableLiveData<List<Member>> {
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

//                Log.i("dataSource", memberList.value.toString())
                memberList.value = list
            }
        return memberList
    }

    override fun updateMemberInfo(groupId: String, member: Member) {
        application.database.collection("groups").document(groupId)
            .collection("members").document(member.id!!)
            .set(member, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${member.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun getLiveNotes(groupId: String): MutableLiveData<List<Note>> {
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

    override fun getLiveReminders(groupId: String): MutableLiveData<List<Reminder>> {
        val calenderItemsList = MutableLiveData<List<Reminder>>()

        application.database.collection("groups").document(groupId)
            .collection("reminders")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Reminder>()

                for (document in value!!) {
                    val data = document.toObject(Reminder::class.java)
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

    override fun postReminder(reminder: Reminder, groupId: String) {
        val database = application.database

        reminder.id = database.collection("groups")
            .document(groupId).collection("reminders").document().id

        database.collection("groups").document(groupId)
            .collection("calenderItems").document(reminder.id!!)
            .set(reminder)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${reminder.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun deleteNote(note: Note, groupId: String) {
        application.database.collection("groups").document(groupId)
            .collection("notes").document(note.id!!)
            .delete()
            .addOnSuccessListener {
                Log.d(
                    "Delete success",
                    "DocumentSnapshot successfully deleted!"
                )
            }
            .addOnFailureListener { e -> Log.w("Delete failure", "Error deleting document", e) }
    }

    override fun deleteReminder(reminder: Reminder, groupId: String) {
        application.database.collection("groups").document(groupId)
            .collection("reminders").document(reminder.id!!)
            .delete()
            .addOnSuccessListener {
                Log.d(
                    "Delete success",
                    "DocumentSnapshot successfully deleted!"
                )
            }
            .addOnFailureListener { e -> Log.w("Delete failure", "Error deleting document", e) }
    }

    override fun getLiveChatMessages(
        userId: String,
        groupId: String
    ): MutableLiveData<List<Chat>> {
        val chatItemsList = MutableLiveData<List<Chat>>()

        application.database.collection("chats")
            .whereEqualTo("groupId", groupId)
            .orderBy("createdTime", Query.Direction.ASCENDING)
            .limit(50)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Chat>()

//                Log.i("LiveChatValue", "$value")

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

    override fun editStock(itemId: String, num: Float) {
        application.database.collection("drugs").document(itemId)
            .update("stock", num)
            .addOnSuccessListener { documentReference ->
                Log.d("update success", "DocumentSnapshot with ID: $itemId")
            }
            .addOnFailureListener { e ->
                Log.w("update failure", "Error update document", e)
            }
    }

    override fun postAlertMessage(alertMessage: AlertMessage) {
        val database = application.database

        alertMessage.id = database.collection("alertMessages").document().id

        database.collection("alertMessages").document(alertMessage.id!!)
            .set(alertMessage)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${alertMessage.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun getLiveAlertMessageForService(
        userId: String
    ): MutableLiveData<List<AlertMessage>> {
        val notificationList = MutableLiveData<List<AlertMessage>>()

        val c = Calendar.getInstance()
        c.time = Timestamp.now().toDate()
        c.add(Calendar.DATE, -3)

        application.database.collection("alertMessages")
            .whereEqualTo("userId", userId)
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .whereGreaterThan("createdTime", Timestamp(c.time))
            .addSnapshotListener { value, error ->

                if (value?.metadata?.hasPendingWrites() != true) {
                    if (error != null) {
                        Log.e("Listen failed.", error.toString())
                        return@addSnapshotListener
                    }
                    val list = mutableListOf<AlertMessage>()

                    for (document in value!!) {
                        val data = document.toObject(AlertMessage::class.java)
                        list.add(data)
                    }
                    notificationList.value = list
                }
            }
        return notificationList
    }

    override fun getLiveAlertMessages(
        userIdList: List<String>
    ): MutableLiveData<List<AlertMessage>> {
        val notificationList = MutableLiveData<List<AlertMessage>>()

        application.database.collection("alertMessages")
            .whereIn("userId", userIdList)
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }
                val list = mutableListOf<AlertMessage>()

                for (document in value!!) {
                    val data = document.toObject(AlertMessage::class.java)

                    list.add(data)
                }
                notificationList.value = list
            }

        return notificationList
    }

    override fun postOnGetAlertMessagesForService(alertMessage: AlertMessage) {

        alertMessage.alreadySend.add(Firebase.auth.currentUser?.uid!!)

        application.database.collection("alertMessages").document(alertMessage.id!!)
            .update("alreadySend", alertMessage.alreadySend)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${alertMessage.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun getLiveChatMessagesForService(
        groupId: String
    ): MutableLiveData<List<Chat>> {
        val chatItemsList = MutableLiveData<List<Chat>>()

        val c = Calendar.getInstance()
        c.time = Timestamp.now().toDate()
        c.add(Calendar.DATE, -3)

        application.database.collection("chats")
            .whereEqualTo("groupId", groupId)
            .orderBy("createdTime", Query.Direction.ASCENDING)
            .whereGreaterThan("createdTime", Timestamp(c.time))
            .limit(50)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Chat>()

                for (document in value!!) {
                    val data = document.toObject(Chat::class.java)
                    if (!data.isReadList.contains(Firebase.auth.currentUser?.uid)) {
                        list.add(data)
                    }
                }
                chatItemsList.value = list
            }
        return chatItemsList
    }

    override fun postOnGetChatForService(chat: Chat) {

        chat.isReadList.add(Firebase.auth.currentUser?.uid!!)

        application.database.collection("chats").document(chat.id!!)
            .update("isReadList", chat.isReadList)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${chat.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }
}
