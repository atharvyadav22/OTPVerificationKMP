package com.atharvyadav22.otpverificationkmp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

private const val TAG = "SmsReader"
private val OTP_PATTERN: Pattern = Pattern.compile("(\\d{4,8})")


@Composable
actual fun SmsReader(
    onOtpReceived: (String) -> Unit
) {
    val context = LocalContext.current
    val smsRetrieverClient = remember { SmsRetriever.getClient(context) }

    val smsConsentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "Consent launcher result received. Result code: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val message = result.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            Log.d(TAG, "Successfully retrieved message: '$message'")
            message?.let {
                val matcher = OTP_PATTERN.matcher(it)
                if (matcher.find()) {
                    val otp = matcher.group(0)!!
                    Log.d(TAG, "Extracted OTP: $otp")
                    onOtpReceived(otp)
                } else {
                    Log.w(TAG, "Could not extract OTP from message.")
                }
            }
        } else {
            Log.w(TAG, "SMS consent denied by user or launcher failed.")
        }
    }

    val smsBroadcastReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d(TAG, "BroadcastReceiver onReceive triggered. Action: ${intent.action}")
                if (SmsRetriever.SMS_RETRIEVED_ACTION != intent.action) return

                val extras = intent.extras ?: return

                val status = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    extras.getParcelable(SmsRetriever.EXTRA_STATUS, Status::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    extras.getParcelable(SmsRetriever.EXTRA_STATUS)
                }

                Log.d(TAG, "Retrieved status code: ${status?.statusCode}")
                when (status?.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        Log.d(TAG, "Status is SUCCESS. Awaiting user consent.")
                        val consentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT, Intent::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT)
                        }
                        consentIntent?.let {
                            try {
                                Log.d(TAG, "Launching consent intent.")
                                smsConsentLauncher.launch(it)
                            } catch (e: Exception) {
                                Log.e(TAG, "Error launching consent intent", e)
                            }
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        Log.w(TAG, "SMS consent timed out after 5 minutes.")
                    }
                    else -> {
                        Log.w(TAG, "Received unhandled status code: ${status?.statusCode}")
                    }
                }
            }
        }
    }

    DisposableEffect(context) {
        Log.d(TAG, "SmsReader composable is now active. Starting listener.")

        smsRetrieverClient.startSmsUserConsent(null)
            .addOnSuccessListener { Log.d(TAG, "SMS User Consent client started successfully.") }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to start SMS User Consent client.", e)
            }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        val receiverFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            0
        }

        ContextCompat.registerReceiver(context, smsBroadcastReceiver, intentFilter, receiverFlags)
        Log.d(TAG, "BroadcastReceiver registered.")

        onDispose {
            Log.d(TAG, "SmsReader composable is being disposed. Unregistering receiver.")
            context.unregisterReceiver(smsBroadcastReceiver)
        }
    }
}
