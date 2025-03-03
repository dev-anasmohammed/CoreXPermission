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

## Usage Of Library

1- [Supported Predefined Permissions](#predefined-permissions)<br/>
2- [Supported Predefined Permission Categories](#predefined-permission-categories)<br/>
3- [How to Request Permissions](#how-to-request-permission)<br/>
4- [Handling Permission Request Results](#how-to-request-handle-result)<br/>

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

Category is a group of permissions under one topic used to streamline the process of requesting multiple permissions simultaneously.. 

| #  | Category     | SubCategory          | Permissions                                                                                                                                    |
|----|--------------|----------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| 01 | Notification | -                    |                                                                                                                                                |
| 02 | Location     | Standard             | FineLocation, CoarseLocation                                                                                                                   |
|    |              | LowAccurateTracking  | FineLocation, CoarseLocation, Notification                                                                                                     |
|    |              | HighAccurateTracking | FineLocation, CoarseLocation, Notification, BackgroundLocation, ForegroundService, ForegroundServiceLocation, RequestIgnoreBatteryOptimization |


## How to request permission

You can request permissions directly using [Predefined Permissions](#predefined-permissions) or by selecting a permission category from  [Predefined Permission Categories](#predefined-permission-categories).

## How to request handle result




