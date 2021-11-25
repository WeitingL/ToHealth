package com.weiting.tohealth.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.weiting.tohealth.R
import com.weiting.tohealth.data.Measure
import com.weiting.tohealth.data.MeasureLog
import java.text.SimpleDateFormat
import java.util.*

object Util {

    fun toUnit(int: Int?): String {
        return when (int) {
            0 -> "毫克"
            1 -> "毫升"
            2 -> "針劑"
            3 -> "單位"
            4 -> "滴"
            5 -> "片"
            6 -> "顆"
            7 -> "湯匙"
            8 -> "袋/包"
            9 -> "個"
            10 -> "次"
            else -> "其他單位"
        }
    }

    fun toStatus(int: Int?): String {
        return when (int) {
            0 -> "執行中"
            1 -> "暫停執行"
            2 -> "中止執行"
            else -> "未知狀態"
        }
    }

    fun toFooter(int: Int?): String {
        return when (int) {
            0 -> "留"
            1 -> "關心您"
            2 -> "紀錄"
            3 -> "通知"
            4 -> "祝福"
            5 -> "改"
            else -> "What?"
        }
    }

    fun toMeasureType(int: Int?): String {
        return when (int) {
            0 -> "血壓"
            1 -> "飯前血糖"
            2 -> "飯後血糖"
            3 -> "血氧"
            4 -> "體重"
            5 -> "體溫"
            else -> "What?"
        }
    }

    fun toEventType(int: Int?): String {
        return when (int) {
            0 -> "健身"
            1 -> "走路(健走"
            2 -> "物理治療"
            3 -> "跑步"
            4 -> "呼吸訓練"
            5 -> "心理諮詢"
            6 -> "冥想"
            7 -> "瑜珈(伸展)"
            8 -> "眼部運動"
            9 -> "中醫治療"
            10 -> "洗腎"
            else -> "What?"
        }
    }

    fun toCareType(int: Int?): String {
        return when (int) {
            0 -> "心情關懷"
            1 -> "疼痛關懷"
            2 -> "症狀關懷"
            3 -> "睡眠關懷"
            else -> "What?"
        }
    }

    fun setDrugDrawable(int: Int?): Int {
        return when (int) {
            0 -> R.drawable.medicine
            1 -> R.drawable.medicine
            2 -> R.drawable.syringe
            3 -> R.drawable.medicine
            4 -> R.drawable.pipette
            5 -> R.drawable.medicine
            6 -> R.drawable.pills
            7 -> R.drawable.medicine
            8 -> R.drawable.medicine
            9 -> R.drawable.pills
            10 -> R.drawable.medicine
            else -> R.drawable.medicine
        }
    }

    fun setMeasureDrawable(int: Int?): Int {
        return when (int) {
            0 -> R.drawable.blood_pressure
            1 -> R.drawable.sugar_blood_level
            2 -> R.drawable.sugar_blood_level
            3 -> R.drawable.loupe
            4 -> R.drawable.body_scale
            5 -> R.drawable.thermometer
            else -> R.drawable.loupe
        }
    }

    fun setEventType(int: Int?): Int {
        return when (int) {
            0 -> R.drawable.strength
            1 -> R.drawable.running
            2 -> R.drawable.doctor
            3 -> R.drawable.running
            4 -> R.drawable.exercise
            5 -> R.drawable.hospital_sign
            6 -> R.drawable.exercise
            7 -> R.drawable.exercise
            8 -> R.drawable.eye
            9 -> R.drawable.doctor
            10 -> R.drawable.hospital_sign
            else -> R.drawable.hospital_sign
        }
    }

    fun toStringFromTimeStamp(timestamp: Timestamp?): String {
        return SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN).format(timestamp!!.toDate())
            .toString()
    }

    fun toDateFromTimeStamp(timestamp: Timestamp?): String {
        return SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).format(timestamp!!.toDate())
            .toString()
    }

    fun toDateWithoutYearFromTimeStamp(timestamp: Timestamp?): String {
        return SimpleDateFormat("MM/dd", Locale.TAIWAN).format(timestamp!!.toDate())
            .toString()
    }

    fun toTimeFromTimeStamp(timestamp: Timestamp?): String {
        return SimpleDateFormat("HH:mm", Locale.TAIWAN).format(timestamp!!.toDate()).toString()
    }

    fun toTimeInMilliFromPicker(y: Int, M: Int, d: Int, h: Int, m: Int): Long {
        val time = Calendar.getInstance()
        time.set(Calendar.YEAR, y)
        time.set(Calendar.MONTH, M)
        time.set(Calendar.DAY_OF_MONTH, d)
        time.set(Calendar.HOUR_OF_DAY, h)
        time.set(Calendar.MINUTE, m)
        return time.timeInMillis
    }

    fun toDateAndTimeFromMilliTime(millis: Long): String {
        val format = SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.TAIWAN)
        return format.format(Date(millis))
    }

    fun toDateFromMilliTime(millis: Long): String {
        val format = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
        return format.format(Date(millis))
    }

    fun toStringFromPeriod(data: Map<String, Int?>): String {

        return when (data["type"]) {
            0 -> {
                "每日執行"
            }
            1 -> {
                "每${data["N"]?.plus(1)}日執行"
            }
            2 -> {
                "每個 禮拜${toWeek(data["N"] ?: 0)}執行"
            }

            3 -> {
                "連續執行${toCycleValue(data["N"] ?: 0)}後，再暫停${toCycleValue(data["X"] ?: 0)}"
            }

            4 -> {
                "依需求執行"
            }

            else -> "Others."
        }
    }

    fun toCycleValue(int: Int): String {
        return when (int) {
            0 -> "1日"
            1 -> "2日"
            2 -> "3日"
            3 -> "4日"
            4 -> "5日"
            5 -> "6日"
            6 -> "7日"
            7 -> "8日"
            8 -> "9日"
            9 -> "10日"
            10 -> "11日"
            11 -> "12日"
            12 -> "13日"
            13 -> "2週"
            14 -> "3週"
            15 -> "1個月"
            else -> "What?!"
        }
    }

    fun toWeek(int: Int): String {
        return when (int) {
            0 -> "星期一"
            1 -> "星期二"
            2 -> "星期三"
            3 -> "星期四"
            4 -> "星期五"
            5 -> "星期六"
            6 -> "星期日"
            else -> "What?!"
        }
    }

    // 0000 -> 2359
    fun getTimeStampToTimeInt(timestamp: Timestamp): Int {
        val c = Calendar.getInstance()
        c.time = timestamp.toDate()
        return (c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE))
    }

    // 0101 -> 1231
    fun getTimeStampToDateInt(timestamp: Timestamp): Int {
        val c = Calendar.getInstance()
        c.time = timestamp.toDate()
        return ((c.get(Calendar.MONTH) + 1) * 100 + (c.get(Calendar.DAY_OF_MONTH)))
    }

    fun toPeriod(int: Int?): String {
        return when (int) {
            0 -> "每日數次"
            1 -> "每幾日執行"
            2 -> "每個禮拜幾執行"
            3 -> "連續幾天執行後，再暫停幾天"
            4 -> "依需求執行"
            else -> "What?!"
        }
    }

    fun toDay(int: Int?): String {
        return when (int) {
            0 -> "1日"
            1 -> "2日"
            2 -> "3日"
            3 -> "4日"
            4 -> "5日"
            5 -> "6日"
            else -> "What?!"
        }
    }

    fun toUnitForMeasure(int: Int?): String {
        return when (int) {
            1 -> "血糖 mg/dl"
            2 -> "血糖 mg/dl"
            3 -> "血氧 %"
            4 -> "公斤 Kg"
            5 -> "攝氏 °C"
            else -> "單位"
        }
    }

    fun transferCircleImage(imgView: ImageView, imgUri: String?) {

        if (!imgUri.isNullOrEmpty()) {
            Glide.with(imgView.context)
                .load(imgUri)
                .placeholder(R.drawable.user_1)
                .circleCrop()
                .into(imgView)
        }
    }

    fun toNotificationTextForMeasureLog(measure: Measure, measureLog: MeasureLog): String {
        return when (measure.type) {
            0 -> {
                "血壓異常! \n收縮壓: ${measureLog.record["X"]} mmHg \n舒張壓: ${measureLog.record["Y"]} mmHg \n心搏: ${measureLog.record["Z"]} bpm"
            }

            1 -> {
                "飯前血糖異常! \n測量值為 ${measureLog.record["X"]} mg/dl"
            }

            2 -> {
                "飯後血糖異常! \n測量值為 ${measureLog.record["X"]} mg/dl"
            }

            3 -> {
                "血氧異常! \n測量值為 ${measureLog.record["X"]} %"
            }

            5 -> {
                "體溫異常! \n測量值為 ${measureLog.record["X"]} °C"
            }

            else -> "未知測量項目異常"
        }
    }

    fun isToday(timestamp: Timestamp?): Boolean {
        return getTimeStampToDateInt(timestamp ?: Timestamp.now()) ==
            getTimeStampToDateInt(Timestamp.now())
    }
}
