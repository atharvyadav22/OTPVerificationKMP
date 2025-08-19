package com.atharvyadav22.otpverificationkmp.utils

enum class OtpShape { Underline, RoundedBox, Circle }
sealed class OtpState { object Idle: OtpState(); object Success: OtpState(); object Error: OtpState() }


