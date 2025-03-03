# CoreXPermission [Under Development]

CoreXPermission is a library designed to simplify permission handling in Android applications. It supports Android versions starting from 8.0 (API 26) to 15.0 (API 35).

<img src="https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat" height="22" valign="middle"> &nbsp;&nbsp;
<img src="https://img.shields.io/badge/License-MIT-yellow.svg" height="22" valign="middle">&nbsp;&nbsp;

## Getting started

This library is available on Maven Central, install it by adding the following dependency to
your <b>build.gradle</b>:

```gradle
repositories {
  google()
  mavenCentral()
}

implementation("io.github.dev-anasmohammed:CoreXPermission:1.0.1")
```

## Contents

0- [Sample of usage](#sample-of-usage)<br/>
1- [Supported Predefined Permissions](#predefined-permissions)<br/>
2- [Supported Predefined Permission Categories](#predefined-permission-categories)<br/>
3- [How to Request Permissions](#how-to-request-permission)<br/>
4- [Handling Permission Request Results](#how-to-handle-result)<br/>
5- [Manifest Checking](#how-to-handle-result)<br/>
6- [Customization Dialog](#how-to-handle-result)<br/>

## Sample of usage

```kotlin
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .request { allGranted, grantedList, deniedList ->
                //handle result of request here 
            }
```

## Predefined Permissions

The library currently supports the following permissions. More permissions will be added in future updates.

| #  | Permission                       | Value                                                   |
|----|----------------------------------|---------------------------------------------------------|
| 01 | Notification                     | android.permission.POST_NOTIFICATIONS                   |
| 02 | FineLocation                     | android.permission.ACCESS_FINE_LOCATION                 |
| 03 | CoarseLocation                   | android.permission.ACCESS_COARSE_LOCATION               |
| 04 | BackgroundLocation               | android.permission.ACCESS_BACKGROUND_LOCATION           |
| 05 | RequestIgnoreBatteryOptimization | android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS |
| 06 | ForegroundService                | android.permission.FOREGROUND_SERVICE                   |
| 07 | ForegroundServiceLocation        | android.permission.FOREGROUND_SERVICE_LOCATION          |
| 08 | Camera                           | android.permission.CAMERA                               |
| 09 | RecordingAudio                   | android.permission.RECORD_AUDIO                         |

## Predefined Permission Categories

Category is a group of permissions under one topic used to streamline the process of requesting multiple permissions simultaneously.
Also each one of these categories has their strings english and arabic changed according to the topic.

| #  | Category     | SubCategory          | Permissions                                                                                                                                    |
|----|--------------|----------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| 01 | Notification | -                    |                                                                                                                                                |
| 02 | Location     | Standard             | FineLocation, CoarseLocation                                                                                                                   |
|    |              | LowAccurateTracking  | FineLocation, CoarseLocation, Notification                                                                                                     |
|    |              | HighAccurateTracking | FineLocation, CoarseLocation, Notification, BackgroundLocation, ForegroundService, ForegroundServiceLocation, RequestIgnoreBatteryOptimization |


## How to request permission

You can request permissions directly using [Predefined Permissions](#predefined-permissions) or by selecting a permission category from  [Predefined Permission Categories](#predefined-permission-categories).

[By Using Permissions Directly]

```kotlin
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .request { allGranted, grantedList, deniedList ->
                //handle result of request here 
            }
```

```kotlin
        CoreXPermission.init(this)
            .permissions(listOf(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio))
            .request { allGranted, grantedList, deniedList ->
                //handle result of request here 
            }
```

[By Using Permissions Category]
```kotlin
        CoreXPermission.init(this)
            .permissionCategory(PermissionCategory.Location.LowAccurateTracking)
            .request { allGranted, grantedList, deniedList ->
                //handle result of request here 
            }
```

## How to handle result

You can handle result by using callback or interface 

[By Using Callbacks]

```kotlin
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .request { allGranted, grantedList, deniedList ->
                //handle result of request here 
            }
```

[By Using Interface]

```kotlin
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .request(object : PermissionRequestCallback {
                override fun onPermissionResult(
                    allGranted: Boolean,
                    grantedList: List<Permission>,
                    deniedList: List<Permission>
                ) {
                    //handle result of request here 
                }
            })
```

Implement interface on your Activity/Fragment <b>PermissionRequestCallback</b>

```kotlin
        CoreXPermission.init(this)
           .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
           .request(this)
```

## Manifest Checking 

This feature checks if developer declare the requested permission and hardware feature in manifest or not. 
Library checks if you don't declare permissions in the manifest it throw exception and give you the required tags to put it in manifest.
Also for camera it throw exception if you don't declare the hardware feature.

You can disable it if you want, by default its enabled.

```kotlin
        CoreXPermission.init(this)
            .permissions(listOf(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio) , false)
            .request { allGranted, grantedList, deniedList ->
                //handle result of request here 
            }
```

```kotlin
        CoreXPermission.init(this)
            .permissionCategory(PermissionCategory.Location.LowAccurateTracking , false)
            .request { allGranted, grantedList, deniedList ->
                //handle result of request here 
            }
```

Example of Exception

```xml
     FATAL EXCEPTION: main
     Process: com.dev.anasmohammed.corex.corexPermission, PID: 7487
     java.lang.RuntimeException: Unable to start activity ComponentInfo{com.dev.anasmohammed.corex.corexPermission/com.dev.anasmohammed.corex.corexPermission.MainActivity}: java.lang.Exception: 
     Some Permissions not declared in manifest add them:
     <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```
```xml
     FATAL EXCEPTION: main
     Process: com.dev.anasmohammed.corex.corexPermission, PID: 7685
     java.lang.RuntimeException: Unable to start activity ComponentInfo{com.dev.anasmohammed.corex.corexPermission/com.dev.anasmohammed.corex.corexPermission.MainActivity}: java.lang.Exception: 
     The hardware feature android.hardware.camera is not declared in the manifest!
     <uses-feature
          android:name="android.hardware.camera"
          android:required="false" />
```