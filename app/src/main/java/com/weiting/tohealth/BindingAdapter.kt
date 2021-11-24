package com.weiting.tohealth

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.weiting.tohealth.NavigationDestination.*
import com.weiting.tohealth.notificationpage.NotificationRecord
import com.weiting.tohealth.notificationpage.NotificationRecordAdapter
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt

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

@BindingAdapter("bindNotificationRecyclerView")
fun bindNotificationRecyclerView(recyclerView: RecyclerView, list: List<NotificationRecord>) {
    val adapter = NotificationRecordAdapter()
    adapter.submitList(list)
    recyclerView.adapter = adapter
}

@BindingAdapter("bindNotificationType")
fun bindNotificationType(imageView: ImageView, type: Int) {
    imageView.setImageResource(
        when (type) {
            4 -> {
                R.drawable.loupe
            }
            5 -> {
                R.drawable.warning
            }
            6 -> {
                R.drawable.medicine
            }
            else -> {
                R.drawable.hospital_sign
            }
        }
    )
}

@BindingAdapter("navigationDestination")
fun bindNavigationDestination(textView: TextView, navigationDestination: NavigationDestination) {
    textView.text = when (navigationDestination) {
        GroupFragment -> GroupFragment.title
        LoginFragment -> LoginFragment.title
        HomeFragment -> HomeFragment.title
        FastAddFragment -> FastAddFragment.title
        MyGroupFragment -> MyGroupFragment.title
        MyStatisticFragment -> MyStatisticFragment.title
        MyManageFragment -> MyManageFragment.title
        ItemEditFragment -> ItemEditFragment.title
        ItemUpdateFragment -> ItemUpdateFragment.title
        EditNoteAndReminderFragment -> EditNoteAndReminderFragment.title
        MeasureRecordFragment -> MeasureRecordFragment.title
        CareRecordFragment -> CareRecordFragment.title
        GroupMemberStatisticFragment -> GroupMemberStatisticFragment.title
        GroupMemberMenageFragment -> GroupMemberMenageFragment.title
        NotificationFragment -> NotificationFragment.title
        OtherFragment -> OtherFragment.title
    }
}

@BindingAdapter("navigationDestinationWithToolBar")
fun bindNavigationDestinationWithToolBar(
    toolbar: Toolbar,
    navigationDestination: NavigationDestination
) {
    toolbar.visibility = when (navigationDestination) {
        GroupFragment -> View.VISIBLE
        LoginFragment -> View.GONE
        HomeFragment -> View.VISIBLE
        FastAddFragment -> View.VISIBLE
        MyGroupFragment -> View.VISIBLE
        MyStatisticFragment -> View.VISIBLE
        MyManageFragment -> View.VISIBLE
        ItemEditFragment -> View.VISIBLE
        ItemUpdateFragment -> View.VISIBLE
        EditNoteAndReminderFragment -> View.VISIBLE
        MeasureRecordFragment -> View.VISIBLE
        CareRecordFragment -> View.VISIBLE
        GroupMemberStatisticFragment -> View.VISIBLE
        GroupMemberMenageFragment -> View.VISIBLE
        NotificationFragment -> View.VISIBLE
        OtherFragment -> View.VISIBLE
    }
}

@BindingAdapter("navigationDestination")
fun bindNavigationDestination(
    bottomNavigationView: BottomNavigationView,
    navigationDestination: NavigationDestination
) {
    bottomNavigationView.visibility = when (navigationDestination) {
        GroupFragment -> View.GONE
        LoginFragment -> View.GONE
        HomeFragment -> View.VISIBLE
        FastAddFragment -> View.GONE
        MyGroupFragment -> View.VISIBLE
        MyStatisticFragment -> View.VISIBLE
        MyManageFragment -> View.VISIBLE
        ItemEditFragment -> View.GONE
        ItemUpdateFragment -> View.GONE
        EditNoteAndReminderFragment -> View.GONE
        MeasureRecordFragment -> View.GONE
        CareRecordFragment -> View.GONE
        GroupMemberStatisticFragment -> View.GONE
        GroupMemberMenageFragment -> View.GONE
        NotificationFragment -> View.GONE
        OtherFragment -> View.VISIBLE
    }
}
