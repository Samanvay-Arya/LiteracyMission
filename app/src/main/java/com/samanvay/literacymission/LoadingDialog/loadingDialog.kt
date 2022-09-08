package com.samanvay.literacymission.LoadingDialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.AlertDialog.Builder
import com.samanvay.literacymission.R

class loadingDialog(myActivity: Activity?) {
    private var activity: Activity? = myActivity
    private var dialog: AlertDialog? = null


    @SuppressLint("InflateParams")
    fun startLoadingDialog() {
        val builder = Builder(activity)
        val layoutInflater = activity!!.layoutInflater
        builder.setCancelable(false)
        builder.setView(layoutInflater.inflate(R.layout.loading_dialog, null))
        dialog = builder.create()
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }
}