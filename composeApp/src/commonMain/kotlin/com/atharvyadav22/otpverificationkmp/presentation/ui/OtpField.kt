package com.atharvyadav22.otpverificationkmp.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.atharvyadav22.otpverificationkmp.SmsReader
import com.atharvyadav22.otpverificationkmp.utils.OtpShape
import com.atharvyadav22.otpverificationkmp.utils.OtpState
import kotlin.math.roundToInt

/**
 * A composable function that provides a highly customizable OTP input field.
 * It handles user input, verification state, and error animations automatically.
 *
 * @param otpValue The current value of the OTP field.
 * @param onOtpValueChange A callback that is invoked when the OTP value changes.
 * @param otpLength The total number of digits in the OTP.
 * @param verifyOtp A suspend function that is called when the OTP is completely filled.
 * It should return `true` for a valid OTP and `false` otherwise.
 * @param onSuccess A callback that is invoked when the OTP is successfully verified.
 * @param onError A callback that is invoked when the OTP verification fails.
 * @param shape The visual shape of each OTP box (e.g., RoundedBox, Circle, UnderLine).
 * @param modifier The modifier to be applied to the layout.
 * @param textStyle The text style to be applied to the digits.
 * @param borderSize The size of the border for each box.
 */


@Composable
fun OtpTextField(
    otpValue: String,
    onOtpValueChange: (String) -> Unit,
    onSuccess: () -> Unit = {},
    onError: () -> Unit = {},
    otpLength: Int = 4,
    shape: OtpShape = OtpShape.RoundedBox,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(Color.Black),
    borderSize: Dp = 2.dp,
    verifyOtp: suspend (String) -> Boolean // backend verification
) {
    SmsReader { receivedOtp ->
        onOtpValueChange(receivedOtp)
    }

    var state by remember { mutableStateOf<OtpState>(OtpState.Idle) }
    val shakeOffset = remember { Animatable(0f) }


    // Trigger backend verification once OTP complete
    LaunchedEffect(otpValue) {
        if (otpValue.length == otpLength && state != OtpState.Success) {
            state = if (verifyOtp(otpValue)) OtpState.Success else OtpState.Error
        }
    }

    // Shake & callbacks
    LaunchedEffect(state) {
        when(state) {
            is OtpState.Error -> {
                onOtpValueChange("") // Use callback to clear value
                shakeOffset.animateTo(
                    targetValue = 20f,
                    animationSpec = repeatable(
                        iterations = 3,
                        animation = tween(100),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                shakeOffset.snapTo(0f)
                state = OtpState.Idle
                onError()
            }
            is OtpState.Success -> onSuccess()
            else -> Unit
        }
    }

    BasicTextField(
        value = otpValue,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() } && state != OtpState.Success && newValue.length <= otpLength) {
                onOtpValueChange(newValue)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Box(modifier = modifier.offset { IntOffset(shakeOffset.value.roundToInt(), 0) }) {
                OtpBoxes(
                    value = otpValue,
                    otpLength = otpLength,
                    shape = shape,
                    state = state,
                    textStyle = textStyle,
                    borderSize = borderSize
                )
            }
        }
    )
}

@Composable
private fun OtpBoxes(
    value: String,
    otpLength: Int,
    shape: OtpShape,
    state: OtpState,
    textStyle: TextStyle,
    borderSize: Dp) {
    val borderColor = when(state) {
        is OtpState.Success -> Color(0xFF81C784)
        is OtpState.Error -> Color(0xFFE57373)
        else -> Color.Black
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(otpLength) { index ->
            val digit = value.getOrNull(index)?.toString() ?: ""
            OtpBox(digit, borderColor, borderSize, textStyle, shape)
        }
    }
}

@Composable
private fun OtpBox(
    digit: String,
    borderColor: Color,
    borderSize: Dp,
    textStyle: TextStyle,
    shape: OtpShape,
    modifier: Modifier = Modifier
) {
    when(shape) {
        OtpShape.Underline -> Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.width(40.dp)
        ) {
            Text(digit, style = textStyle)
            Box(
                modifier = Modifier
                    .height(borderSize)
                    .fillMaxWidth()
                    .background(borderColor, RoundedCornerShape(50))
            )
        }
        OtpShape.RoundedBox -> Box(
            modifier = modifier.size(40.dp).border(borderSize, borderColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) { Text(digit, style = textStyle) }

        OtpShape.Circle -> Box(
            modifier = modifier.size(40.dp).border(borderSize, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) { Text(digit, style = textStyle) }
    }
}
