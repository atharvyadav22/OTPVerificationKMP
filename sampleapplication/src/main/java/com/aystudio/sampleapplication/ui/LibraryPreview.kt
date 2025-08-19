package com.aystudio.sampleapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.atharvyadav22.otpverificationkmp.presentation.ui.OtpTextField
import com.atharvyadav22.otpverificationkmp.utils.OtpShape

@Composable
fun LibraryPreview(innerPadding: PaddingValues) {

    var otpValue1 by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            //Default
            OtpTextField(
                otpValue = otpValue1, onOtpValueChange = { otpValue1 = it },
                verifyOtp = {
                    /*

                    ----Option 1- Dummy Api Call----
                    val wasSuccessful: Boolean = dummyApiCall.checkOtpOnServer(userPhoneNumber, enteredOtp)
                    return@OtpTextField wasSuccessful

                    ----Option 2- Dummy Verification----
                    return@OtpTextField otpValue == "1234" //Boolean Call

                */

//              **** return@OtpTextField (Boolean) ****
                    return@OtpTextField true
                })

//         ******** Customizable Options ********

//      -------- Example 1: Circle Shape --------
            var otpValue2 by remember { mutableStateOf("") }

            OtpTextField(
                otpValue = otpValue2,
                onOtpValueChange = { otpValue2 = it },
                onSuccess = {
                    //Navigate To Screen
                },
                onError = {
                    //Retry Logic or Message
                },
                otpLength = 4,
                shape = OtpShape.Circle,
                modifier = Modifier,
                textStyle = MaterialTheme.typography.titleMedium.copy(Color.Black),
                borderSize = 2.dp,
                verifyOtp = { return@OtpTextField false })

//      -------- Example 2: Underline Shape --------
            var otpValue3 by remember { mutableStateOf("") }

            OtpTextField(
                otpValue = otpValue3,
                onOtpValueChange = { otpValue3 = it },
                onSuccess = {},
                onError = {},
                otpLength = 6,
                shape = OtpShape.Underline,
                modifier = Modifier,
                textStyle = MaterialTheme.typography.titleMedium.copy(Color.Black),
                borderSize = 2.dp,
                verifyOtp = { return@OtpTextField false })


        }
    }
}