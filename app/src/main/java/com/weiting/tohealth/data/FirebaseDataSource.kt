package com.weiting.tohealth.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.PublicApplication.Companion.application
import com.weiting.tohealth.R
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FirebaseDataSource : FirebaseSource {

    private const val USERS = "users"
    private const val DRUGS = "drugs"
    private const val DRUG_LOGS = "drugLogs"
    private const val MEASURES = "measures"
    private const val MEASURE_LOGS = "measureLogs"
    private const val EVENTS = "events"
    private const val EVENT_LOGS = "eventLogs"
    private const val CARES = "cares"
    private const val CARE_LOGS = "careLogs"
    private const val GROUPS = "groups"
    private const val MEMBERS = "members"
    private const val NOTES = "notes"
    private const val REMINDERS = "reminders"
    private const val ALERT_MESSAGES = "alertMessages"
    private const val CHATS = "chats"
    private const val ID = "id"
    private const val USER_ID = "userId"
    private const val GROUP_ID = "groupId"
    private const val CREATED_TIME = "createdTime"

    override fun getLiveUser(userId: String): MutableLiveData<User> {
        val database = application.database
        val user = MutableLiveData<User>()

        database.collection(USERS)
            .whereEqualTo(ID, userId)
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

    override suspend fun getUser(userId: String): Result<User> = suspendCoroutine { continuation ->
        val database = application.database

        database.collection(USERS).document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val data = task.result.toObject(User::class.java)
                    continuation.resume(Result.Success(data ?: User()))

                } else {
                    task.exception?.let {

                        Log.w(
                            "Error to get data",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                }
            }
    }

    override fun signIn(user: User) {
        application.database.collection(USERS)
            .document(user.id!!)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${user.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun getAllDrugs(userId: String): Result<List<Drug>> =
        suspendCoroutine { continuation ->
            val database = application.database
            val list = mutableListOf<Drug>()

            database.collection(DRUGS)
                .whereEqualTo(USER_ID, userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val data = document.toObject(Drug::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getDrug(itemId: String): Result<Drug> =
        suspendCoroutine { continuation ->
            application.database.collection(DRUGS).document(itemId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val data = task.result.toObject(Drug::class.java)
                        continuation.resume(Result.Success(data ?: Drug()))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getAllMeasures(userId: String): Result<List<Measure>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Measure>()
            val database = application.database

            database.collection(MEASURES)
                .whereEqualTo(USER_ID, userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(Measure::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getAllEvents(userId: String): Result<List<Event>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Event>()
            val database = application.database

            database.collection(EVENTS)
                .whereEqualTo(USER_ID, userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(Event::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getAllCares(userId: String): Result<List<Care>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Care>()
            val database = application.database

            database.collection(CARES)
                .whereEqualTo(USER_ID, userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(Care::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getMeasure(itemId: String): Result<Measure> =
        suspendCoroutine { continuation ->
            application.database.collection(MEASURES).document(itemId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val data = task.result.toObject(Measure::class.java)
                        continuation.resume(Result.Success(data ?: Measure()))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override fun getLiveDrugs(userId: String): MutableLiveData<List<Drug>> {

        val drugList = MutableLiveData<List<Drug>>()

        application.database.collection(DRUGS)
            .whereEqualTo(USER_ID, userId)
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

        application.database.collection(MEASURES)
            .whereEqualTo(USER_ID, userId)
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

        application.database.collection(EVENTS)
            .whereEqualTo(USER_ID, userId)
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

        application.database.collection(CARES)
            .whereEqualTo(USER_ID, userId)
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

        drug.id = database.collection(DRUGS).document().id
        database.collection(DRUGS).document(drug.id!!)
            .set(drug)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${drug.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postMeasure(measure: Measure) {
        val database = application.database

        measure.id = database.collection(MEASURES).document().id
        database.collection(MEASURES).document(measure.id!!)
            .set(measure)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${measure.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postEvent(event: Event) {
        val database = application.database

        event.id = database.collection(EVENTS).document().id
        database.collection(EVENTS).document(event.id!!)
            .set(event)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${event.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postCare(care: Care) {
        val database = application.database

        care.id = database.collection(CARES).document().id
        database.collection(CARES).document(care.id!!)
            .set(care)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${care.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun updateDrug(drug: Drug) {
        application.database.collection(DRUGS).document(drug.id!!)
            .set(drug, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${drug.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun updateMeasure(measure: Measure) {
        application.database.collection(MEASURES).document(measure.id!!)
            .set(measure, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${measure.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun updateEvent(event: Event) {
        application.database.collection(EVENTS).document(event.id!!)
            .set(event, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${event.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun updateCare(care: Care) {
        application.database.collection(CARES).document(care.id!!)
            .set(care, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${care.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun getDrugLogId(itemId: String): Result<String> = suspendCoroutine {
        try {
            val database = application.database
            val id = database.collection(DRUGS).document(itemId).collection(DRUG_LOGS)
                .document().id
            it.resume(Result.Success(id))
        } catch (e: Exception) {
            it.resume(Result.Error(e))
        }
    }

    override fun postDrugLog(id: String, drugLog: DrugLog) {

        val database = application.database

        database.collection(DRUGS).document(id).collection(DRUG_LOGS).document(drugLog.id!!)
            .set(drugLog)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${drugLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun deleteDrugLog(drugId: String, drugLogId: String) {
        val database = application.database

        database.collection(DRUGS).document(drugId)
            .collection(DRUG_LOGS).document(drugLogId)
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

    override suspend fun getMeasureLogId(itemId: String): Result<String> = suspendCoroutine {
        try {
            val database = application.database
            val id = database.collection(MEASURES).document(itemId).collection(MEASURE_LOGS)
                .document().id
            it.resume(Result.Success(id))
        } catch (e: Exception) {
            it.resume(Result.Error(e))
        }

    }

    override fun postMeasureLog(id: String, measureLog: MeasureLog) {
        val database = application.database

        database.collection(MEASURES).document(id).collection(MEASURE_LOGS)
            .document(measureLog.id!!)
            .set(measureLog)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${measureLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postEventLog(id: String, eventLog: EventLog) {
        val database = application.database

        eventLog.id =
            database.collection(EVENTS).document(id).collection(EVENT_LOGS).document().id
        database.collection(EVENTS).document(id).collection(EVENT_LOGS)
            .document(eventLog.id!!)
            .set(eventLog)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${eventLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postCareLog(id: String, careLog: CareLog) {
        val database = application.database

        careLog.id =
            database.collection(CARES).document(id).collection(CARE_LOGS).document().id
        database.collection(CARES).document(id).collection(CARE_LOGS).document(careLog.id!!)
            .set(careLog)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${careLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun getDrugLogs(itemId: String): Result<List<DrugLog>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<DrugLog>()
            val database = application.database

            database.collection(DRUGS).document(itemId).collection(DRUG_LOGS)
                .orderBy(CREATED_TIME, Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(DrugLog::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))

                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getMeasureLogs(itemId: String): Result<List<MeasureLog>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<MeasureLog>()
            val database = application.database

            database.collection(MEASURES).document(itemId).collection(MEASURE_LOGS)
                .orderBy(CREATED_TIME, Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(MeasureLog::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getMeasureLog(itemId: String, itemsLogId: String): Result<MeasureLog> =
        suspendCoroutine { continuation ->
            application.database.collection(MEASURES).document(itemId)
                .collection(MEASURE_LOGS).document(itemsLogId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val data = task.result.toObject(MeasureLog::class.java)
                        continuation.resume(Result.Success(data ?: MeasureLog()))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getEventLogs(itemId: String): Result<List<EventLog>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<EventLog>()
            val database = application.database

            database.collection(EVENTS).document(itemId).collection(EVENT_LOGS)
                .orderBy(CREATED_TIME, Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(EventLog::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getCareLogs(itemId: String): Result<List<CareLog>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<CareLog>()
            val database = application.database

            database.collection(CARES).document(itemId).collection(CARE_LOGS)
                .orderBy(CREATED_TIME, Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result) {
                            val data = doc.toObject(CareLog::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override fun getLiveDrugLogs(itemId: String): MutableLiveData<List<DrugLog>> {
        val drugLogsList = MutableLiveData<List<DrugLog>>()

        application.database.collection(DRUGS)
            .document(itemId)
            .collection(DRUG_LOGS)
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
        val measureLogsList = MutableLiveData<List<MeasureLog>>()

        application.database.collection(MEASURES)
            .document(itemId)
            .collection(MEASURE_LOGS)
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
                measureLogsList.value = list
            }

        return measureLogsList
    }

    override fun getLiveEventLogs(itemId: String): MutableLiveData<List<EventLog>> {
        val eventLogsList = MutableLiveData<List<EventLog>>()

        application.database.collection(EVENTS)
            .document(itemId)
            .collection(EVENT_LOGS)
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
                eventLogsList.value = list
            }

        return eventLogsList
    }

    override fun getLiveCareLogs(itemId: String): MutableLiveData<List<CareLog>> {
        val careLogsList = MutableLiveData<List<CareLog>>()

        application.database.collection(CARES)
            .document(itemId)
            .collection(CARE_LOGS)
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

        database.collection(GROUPS).document(group.id!!)
            .set(group)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${group.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun getNewGroupId(): Result<String> {
        return try {
            Result.Success(application.database.collection(GROUPS).document().id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun joinGroup(member: Member, groupId: String) {
        val database = application.database

        member.id = database.collection(GROUPS).document(groupId)
            .collection(MEMBERS).document().id

        database.collection(GROUPS).document(groupId)
            .collection(MEMBERS).document(member.id!!)
            .set(member)
            .addOnSuccessListener {
                Log.d(
                    "store success",
                    "DocumentSnapshot added in group by user_ID: ${member.userId}"
                )
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }

        database.collection(USERS).document(member.userId!!)
            .update("groupList", FieldValue.arrayUnion(groupId))
            .addOnSuccessListener {
                Log.d(
                    "update success",
                    "DocumentSnapshot added in group by user_ID: ${member.userId}"
                )
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override suspend fun checkIsRelationExist(userId: String, groupId: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            application.database.collection(USERS).document(userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result.toObject(User::class.java)
                        when (user?.groupList?.contains(groupId)) {
                            true -> continuation.resume(Result.Success(true))
                            false -> continuation.resume(Result.Success(false))
                        }

                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun checkIsGroupExist(groupId: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            val database = application.database

            database.collection(GROUPS)
                .whereEqualTo(ID, groupId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        when (task.result.isEmpty) {
                            true -> continuation.resume(Result.Success(false))
                            false -> continuation.resume(Result.Success(true))
                        }
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getGroups(groupId: String): Result<List<Group>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Group>()
            val database = application.database

            database.collection(GROUPS)
                .whereEqualTo(ID, groupId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(Group::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getMembers(groupId: String): Result<List<Member>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Member>()
            val database = application.database

            database.collection(GROUPS).document(groupId)
                .collection(MEMBERS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(Member::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getNotes(groupId: String): Result<List<Note>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Note>()
            val database = application.database

            database.collection(GROUPS).document(groupId)
                .collection(NOTES)
                .orderBy(CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(Note::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override suspend fun getReminders(groupId: String): Result<List<Reminder>> =
        suspendCoroutine { continuation ->
            val list = mutableListOf<Reminder>()
            val database = application.database

            database.collection(GROUPS).document(groupId)
                .collection(REMINDERS)
                .orderBy(CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            val data = doc.toObject(Reminder::class.java)
                            list.add(data)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Error to get data",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(application.getString(R.string.something_wrong)))
                    }
                }
        }

    override fun getLiveMembers(groupId: String): MutableLiveData<List<Member>> {
        val memberList = MutableLiveData<List<Member>>()

        application.database.collection(GROUPS).document(groupId)
            .collection(MEMBERS)
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

    override fun updateMemberInfo(groupId: String, member: Member) {
        application.database.collection(GROUPS).document(groupId)
            .collection(MEMBERS).document(member.id!!)
            .set(member, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${member.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun getLiveNotes(groupId: String): MutableLiveData<List<Note>> {
        val notesList = MutableLiveData<List<Note>>()

        application.database.collection(GROUPS).document(groupId)
            .collection(NOTES)
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

        application.database.collection(GROUPS).document(groupId)
            .collection(REMINDERS)
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

        note.id = database.collection(GROUPS).document(groupId)
            .collection(NOTES).document().id

        database.collection(GROUPS).document(groupId)
            .collection(NOTES).document(note.id!!)
            .set(note)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${note.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postReminder(reminder: Reminder, groupId: String) {
        val database = application.database

        reminder.id = database.collection(GROUPS)
            .document(groupId).collection(REMINDERS).document().id

        database.collection(GROUPS).document(groupId)
            .collection(REMINDERS).document(reminder.id!!)
            .set(reminder)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${reminder.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun deleteNote(note: Note, groupId: String) {
        application.database.collection(GROUPS).document(groupId)
            .collection(NOTES).document(note.id!!)
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
        application.database.collection(GROUPS).document(groupId)
            .collection(REMINDERS).document(reminder.id!!)
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

        application.database.collection(CHATS)
            .whereEqualTo(GROUP_ID, groupId)
            .orderBy(CREATED_TIME, Query.Direction.ASCENDING)
            .limit(50)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Listen failed.", error.toString())
                    return@addSnapshotListener
                }

                val list = mutableListOf<Chat>()

                for (document in value!!) {
                    val data = document.toObject(Chat::class.java)
                    list.add(data)
                }

                chatItemsList.value = list
            }
        return chatItemsList
    }

    override fun postChatMessage(chat: Chat) {
        val database = application.database

        chat.id = database.collection(CHATS).document().id

        database.collection(CHATS).document(chat.id!!)
            .set(chat)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${chat.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun editStock(itemId: String, num: Float) {
        application.database.collection(DRUGS).document(itemId)
            .update("stock", num)
            .addOnSuccessListener {
                Log.d("update success", "DocumentSnapshot with ID: $itemId")
            }
            .addOnFailureListener { e ->
                Log.w("update failure", "Error update document", e)
            }
    }

    override fun postAlertMessage(alertMessage: AlertMessage) {
        val database = application.database

        alertMessage.id = database.collection(ALERT_MESSAGES).document().id

        database.collection(ALERT_MESSAGES).document(alertMessage.id!!)
            .set(alertMessage)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${alertMessage.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun getLiveAlertMessages(
        userIdList: List<String>
    ): MutableLiveData<List<AlertMessage>> {
        val notificationList = MutableLiveData<List<AlertMessage>>()

        application.database.collection(ALERT_MESSAGES)
            .whereIn(USER_ID, userIdList)
            .orderBy(CREATED_TIME, Query.Direction.DESCENDING)
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

        application.database.collection(ALERT_MESSAGES).document(alertMessage.id!!)
            .update("alreadySend", alertMessage.alreadySend)
            .addOnSuccessListener {
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

        application.database.collection(CHATS)
            .whereEqualTo(GROUP_ID, groupId)
            .orderBy(CREATED_TIME, Query.Direction.ASCENDING)
            .whereGreaterThan(CREATED_TIME, Timestamp(c.time))
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

        application.database.collection(CHATS).document(chat.id!!)
            .update("isReadList", chat.isReadList)
            .addOnSuccessListener {
                Log.d("store success", "DocumentSnapshot added with ID: ${chat.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }
}
