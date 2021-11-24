package com.weiting.tohealth.mygrouppage.dialogs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.databinding.QrcodeScanFragmentBinding

const val REQUEST_CODE = 1

class QRCodeScanDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = QrcodeScanFragmentBinding.inflate(inflater, container, false)
        val context = PublicApplication.application.applicationContext
        val viewModel = ViewModelProvider(this).get(QRCodeScanDialogViewModel::class.java)

        val barcodeDetector = BarcodeDetector.Builder(context)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        val cameraSource = CameraSource.Builder(context, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(300, 300)
            .build()

        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(p0: SurfaceHolder) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Need to alter
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
                } else {
                    cameraSource.start(p0)
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(p0: Detector.Detections<Barcode>) {
                val qrCodes = p0.detectedItems
                if (qrCodes.size() != 0) {
                    viewModel.getGroupId(qrCodes.valueAt(0).displayValue)
                }
            }
        })

        viewModel.groupIdFromQRCode.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                cameraSource.stop()
                findNavController().navigate(NavigationDirections.actionGlobalJoinGroupDialog(it))
                onDestroy()
            }
        }
        return binding.root
    }
}
