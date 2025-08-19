package com.atharvyadav22.otpverificationkmp

import androidx.compose.runtime.Composable

@Composable
expect fun SmsReader(onOtpReceived: (String) -> Unit)