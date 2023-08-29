package com.walletmix.superapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.walletmix.superapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class ScannerActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_PERMISSION_CODE = 123
        const val CAMERA_PERMISSION_REQUEST_CODE = 101
        const val BASE_URL = "walletmix.com"
    }


    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityMainBinding
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        textToSpeech = TextToSpeech(this@ScannerActivity, this@ScannerActivity)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            startScanning()
        }


        // Parameters (default values)

        // ex. listOf(BarcodeFormat.QR_CODE)


        // Callbacks

    }

    private fun startScanning() {
//        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, binding.scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id

//        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,

        //only used for the QR type
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE)

        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not


        codeScanner.decodeCallback = DecodeCallback { result ->

            CoroutineScope(Dispatchers.Main).launch {
//                Toast.makeText(
//                    this@ScannerActivity,
//                    "Scan Result: ${result.text}",
//                    Toast.LENGTH_SHORT
//                ).show()
                codeScanner.stopPreview()
                checkTheResult(result)
            }
//            runOnUiThread {
//                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
//
//                // Inside the QRCodeAnalyzer class after detecting a valid QR code
////
//
////                if (it != null && it.text.startsWith("http")) {
////                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.text.toString()))
////                    startActivity(browserIntent)
////                }
//
//                //need to think about the threading.....
//
//                checkTheResult(it)
////                else {
//////                    val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(applicationContext)
//////                    alertBuilder.setCancelable(true)
//////                    alertBuilder.setTitle("Invalid QR CODE")
//////                    alertBuilder.setMessage("Please Scan Again.....")
//////                    alertBuilder.setPositiveButton("Continue") { dialog, _ ->
//////                        dialog.dismiss()
//////                    }
//////                    alertBuilder.create().show()
////                }
//
////                if (qrCodeValue != null && qrCodeValue.startsWith("http")) {
////                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(qrCodeValue))
////                    startActivity(browserIntent)
////                }
//
//
//            }


        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    this@ScannerActivity, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
//            runOnUiThread {
//                Toast.makeText(
//                    this, "Camera initialization error: ${it.message}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
        }

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkTheResult(result: Result) {
        if (result != null && result.text.contains(BASE_URL, ignoreCase = true)) {
            val intent = Intent(this@ScannerActivity, WebActivity::class.java)
            val url = result.text.toString()
            intent.putExtra("URL_KEY", url)
            startActivity(intent)
            codeScanner.stopPreview()
        } else {
            codeScanner.stopPreview()
            val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            alertBuilder.setCancelable(true)
            alertBuilder.setTitle("Invalid QR")
            alertBuilder.setMessage("Please Scan Again.....")
            alertBuilder.setPositiveButton("OK") { dialog, _ ->
                codeScanner.releaseResources()
                codeScanner?.startPreview()
                dialog.dismiss()
            }
            alertBuilder.create().show()
        }


//        CoroutineScope(Dispatchers.Main).launch {
//            if (result != null && result.text.contains(BASE_URL, ignoreCase = true)) {
//                val intent = Intent(this@ScannerActivity, WebActivity::class.java)
//                val url = result.text.toString()
//                intent.putExtra("URL_KEY", url)
//                startActivity(intent)
//                codeScanner.stopPreview()
//            } else {
//                codeScanner.releaseResources()
//                codeScanner.stopPreview()
//                Toast.makeText(
//                    this@ScannerActivity, "Invalid QR. \n" +
//                            "Please Scan QR again. ", Toast.LENGTH_SHORT
//                ).show()
//                codeScanner?.startPreview()
//            }
//
//
//
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
                startScanning()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
            codeScanner?.startPreview()
        }
    }

    override fun onStop() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onStop()
    }

    override fun onPause() {

        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
            codeScanner?.startPreview()
        }
        super.onPause()
    }

//    override fun onInit(status: Int) {
//
//        if (status == TextToSpeech.SUCCESS) {
//            val message = "Welcome to Your Activity. This is a voice message."
//            val bengaliMessage = getString(R.string.bengali_message)
//            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
//        } else {
//            val message = "Invalid message"
//            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        codeScanner.releaseResources()
        codeScanner.stopPreview()
//        textToSpeech.shutdown()
    }
}