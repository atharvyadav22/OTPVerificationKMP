---

# 🔑 OTP Verification KMP

[![Maven Central](https://img.shields.io/maven-central/v/io.github.atharvyadav22/otp-verification-kmp.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.atharvyadav22/otp-verification-kmp)
[![License](https://img.shields.io/github/license/atharvyadav22/otpVerificationKmp)](LICENSE)

A **lightweight & customizable OTP/PIN input component** for **Kotlin Multiplatform (Android, iOS, Desktop, JVM)** built with Jetpack Compose.
✨ Features **auto SMS reading on Android**, animations, and full styling control.

---

## ✨ Features

* 🎨 Customizable UI → `RoundedBox`, `Circle`, `Underline` shapes
* 🤖 Android Auto SMS Reading → via **Google SMS User Consent API**
* 🔄 Smart States → Success ✅ / Error ❌ / Loading ⏳ with animations
* 📱 Cross-Platform → Android, iOS, Desktop, JVM with single codebase
* ⚡ Plug & Play → Drop in `OtpTextField` and connect to your backend

---

## 📦 Installation

### Step 1 — Add Maven Central

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

---

### Step 2 — Choose the right dependency

👉 **For multiplatform/shared projects** (recommended):

```kotlin
commonMain {
    dependencies {
        implementation("io.github.atharvyadav22:otp-verification-kmp:1.0.0")
    }
}
```

👉 **For Android-only projects**:

```kotlin
dependencies {
    implementation("io.github.atharvyadav22:otp-verification-kmp-android:1.0.0")
}
```

👉 **For JVM/Desktop-only projects**:

```kotlin
dependencies {
    implementation("io.github.atharvyadav22:otp-verification-kmp-jvm:1.0.0")
}
```

> ⚡ Always prefer the common `otp-verification-kmp` in KMP projects.
> Use platform-specific (`android`, `jvm`) only if your project is not multiplatform.

---


## 🚀 Usage Example

```kotlin
var otpValue by remember { mutableStateOf("") }

OtpTextField(
    otpValue = otpValue,
    onOtpValueChange = { otpValue = it },
    otpLength = 6,
    shape = OtpShape.RoundedBox,
    verifyOtp = { enteredOtp -> enteredOtp == "123456" 
        //Returns Boolean to verify 
        },
    onSuccess = { /* ✅ OTP verified Perform Navigation Logic*/ },
    onError = { /* ❌ Wrong OTP Call Resent Logic */ }
)
````
<img src="https://github.com/atharvyadav22/otpVerificationKmp/readme_assets/demo_usage.gif" width="300" alt="Demo usage">

👉 **See full demo with multiple styles here**:
[LibraryPreview.kt](https://github.com/atharvyadav22/otpVerificationKmp/blob/main/sampleapplication/src/main/java/com/aystudio/sampleapplication/ui/LibraryPreview.kt)


---

## 🛠️ API Reference

| Parameter          | Type                          | Description                                     |
| ------------------ | ----------------------------- | ----------------------------------------------- |
| `otpValue`         | `String`                      | Current OTP input.                              |
| `onOtpValueChange` | `(String) -> Unit`            | Called on OTP input change.                     |
| `otpLength`        | `Int`                         | Total number of OTP digits.                     |
| `verifyOtp`        | `suspend (String) -> Boolean` | Verification logic (backend/local).             |
| `onSuccess`        | `() -> Unit`                  | Triggered when OTP is correct.                  |
| `onError`          | `() -> Unit`                  | Triggered when OTP is invalid.                  |
| `shape`            | `OtpShape`                    | UI shape → `RoundedBox`, `Circle`, `Underline`. |
| `textStyle`        | `TextStyle`                   | Custom font style.                              |
| `borderSize`       | `Dp`                          | Border thickness.                               |

---

## 📌 Versioning

We follow **Semantic Versioning (SemVer)**:

* **MAJOR** = Breaking changes
* **MINOR** = New features (backward compatible)
* **PATCH** = Bug fixes

Check latest version → [![Maven Central](https://img.shields.io/maven-central/v/io.github.atharvyadav22/otp-verification-kmp.svg)](https://central.sonatype.com/artifact/io.github.atharvyadav22/otp-verification-kmp)

👉 All release tags & changelogs are available on [GitHub Releases](https://github.com/atharvyadav22/otpVerificationKmp/releases).

---

## 📜 License

```text
Copyright 2025 Atharv Yadav

Licensed under the Apache License, Version 2.0
```

---

