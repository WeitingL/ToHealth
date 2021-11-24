package com.weiting.tohealth.mygrouppage.dialogs

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.weiting.tohealth.databinding.DialogQrcodeBinding

class QRCodeDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogQrcodeBinding.inflate(inflater, container, false)
        val groupId = QRCodeDialogArgs.fromBundle(requireArguments()).groupId
        val size = 512

        val qrCode = QRCodeWriter().encode(groupId, BarcodeFormat.QR_CODE, size, size)
        val imageRQCode = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (qrCode[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
        binding.imageView8.setImageBitmap(imageRQCode)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog!!.window?.setLayout(width, width)

    }


}