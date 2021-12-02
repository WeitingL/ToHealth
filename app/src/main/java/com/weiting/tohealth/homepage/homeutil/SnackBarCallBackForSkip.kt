package com.weiting.tohealth.homepage.homeutil

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.weiting.tohealth.homepage.HomeViewModel

class SnackBarCallBackForSkip(private val viewModel: HomeViewModel): BaseTransientBottomBar.BaseCallback<Snackbar>(){

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
            viewModel.postSkipLog()
        }
    }
}