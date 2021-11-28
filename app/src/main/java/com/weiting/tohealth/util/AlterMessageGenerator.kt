package com.weiting.tohealth.util

import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.data.MeasureLog

/*
    The notification will be generated before log generate and after getting data from firebase.
 */

interface AlterMessageGenerator {

    fun isDrugExhausted(drug: Drug): Boolean {
        val currentStock = drug.stock - drug.dose

        return when (currentStock / drug.dose < 10) {
            true -> true
            false -> false
        }
    }

    // How to process the empty value?
    fun isBloodPressureAbnormal(measureLog: MeasureLog): Boolean {
        val systolicPressure = measureLog.record["X"] ?: 0
        val diastolicPressure = measureLog.record["Y"] ?: 0
        val heartRate = measureLog.record["Z"] ?: 0

        return when {
            systolicPressure == 0 -> true
            systolicPressure > 130 -> true
            diastolicPressure < 50 -> true
            heartRate < 60 -> true
            heartRate > 120 -> true
            else -> false
        }
    }

    fun isBeforeMealBloodSugarAbnormal(measureLog: MeasureLog): Boolean {
        val value = measureLog.record["X"] ?: 0

        return when {
            value == 0 -> true
            value > 120 -> true
            value < 60 -> true
            else -> false
        }
    }

    fun isAfterMealBloodSugarAbnormal(measureLog: MeasureLog): Boolean {
        val value = measureLog.record["X"] ?: 0

        return when {
            value == 0 -> true
            value > 200 -> true
            value < 60 -> true
            else -> false
        }
    }

    fun isSPO2Abnormal(measureLog: MeasureLog): Boolean {
        val sPO2 = measureLog.record["X"] ?: 0

        return when {
            sPO2 < 93 -> true
            else -> false
        }
    }

    fun isBodyTemperatureAbnormal(measureLog: MeasureLog): Boolean {
        val tm = measureLog.record["X"] ?: 0

        return when {
            tm < 36 -> true
            tm > 38 -> true
            else -> false
        }
    }
}
