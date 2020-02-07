# IonicMicrCameraPreviewPlugin

---
title: Ionic Camera Preview for Micr Outline 
description: Cordova plugin for scan cheque micr code using camera preview with custom overlay view
---

# cordova-plugin-raqmiyat-micrcamerapreview

## Installation
    ionic cordova plugin add cordova-plugin-raqmiyat-micrcamerapreview (or)
    ionic cordova plugin add https://github.com/AndroidManikandan5689/IonicMicrCameraPreviewPlugin.git

## Supported Platforms
- Android

### How to Use
```
declare var MicrCameraPreview: any; //paste it below the import section

//button event
openMicrCamera() { 
MicrCameraPreview.openCamera("", (data) => {
      console.log(data); //captured image file path
    }, (error => {
      console.log(error);
})
}
```