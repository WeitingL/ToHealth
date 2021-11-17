package com.weiting.tohealth

import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.Timestamp
import com.weiting.tohealth.homepage.ItemDataType
import com.weiting.tohealth.homepage.TodayItemAdapter

@BindingAdapter("TimeStampForWelcomeSlogan")
fun bindTimeStampForWelcomeSlogan(textView: TextView, timestamp: Timestamp) {
    when {
        getTimeStampToTimeInt(timestamp) in 600..1000 -> {
            textView.text = PublicApplication.application.getString(R.string.homepage_morningtitle)
        }
        getTimeStampToTimeInt(timestamp) in 1000..1400 -> {
            textView.text = PublicApplication.application.getString(R.string.homepage_noontitle)
        }
        getTimeStampToTimeInt(timestamp) in 1401..1800 -> {
            textView.text =
                PublicApplication.application.getString(R.string.homepage_afternoontitle)
        }
        getTimeStampToTimeInt(timestamp) in 1801..2300 -> {
            textView.text = PublicApplication.application.getString(R.string.homepage_nighttitle)
        }
        else -> {
            textView.text = PublicApplication.application.getString(R.string.homepage_sleeptitle)
        }
    }
}

@BindingAdapter("TimeStampForWelcomeLottie")
fun bindTimeStampForWelcomeLottie(lottieAnimationView: LottieAnimationView, timestamp: Timestamp) {
    when {
        getTimeStampToTimeInt(timestamp) in 600..1000 -> {
            lottieAnimationView.setAnimation(R.raw.sunrise)
        }
        getTimeStampToTimeInt(timestamp) in 1000..1400 -> {
            lottieAnimationView.setAnimation(R.raw.sunny)
        }
        getTimeStampToTimeInt(timestamp) in 1401..1800 -> {
            lottieAnimationView.setAnimation(R.raw.sunset)
        }
        getTimeStampToTimeInt(timestamp) in 1801..2300 -> {
            lottieAnimationView.setAnimation(R.raw.weather_night)
        }
        else -> {
            lottieAnimationView.setAnimation(R.raw.sleeping)
        }
    }
}