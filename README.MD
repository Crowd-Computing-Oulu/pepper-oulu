# Pepper @ University of Oulu

This repository contains the source code of the universal application running on University of Oulu's Pepper. This application contains a range of different screens for different purposes, and all iterations of our toolsets for promotional and experimental purposes.

### Modules

TODO: Write about each module in the project

### Setup and Usage

TODO: Write step-by-step guide for setup and usage of the application

### System Requirements (Windows)

TODO: Write system requirements for Windows

### System Requirements (MacOS)

I have not been able to launch the emulator successfully on MacOS yet.

### System Requirements (Linux)

Android Studio 3.6.3
Pepper SDK 7
Oracle JRE 8 (See below)
Android Gradle Plugin 3.6.3
Gradle 5.6.4
KVM (see below)
Pop!_OS 22.04 LTS
Intel CPU

**KVM**
Install KVM
    `sudo apt install qemu-kvm`
Add yourself to the kvm group
    `sudo adduser yourusername kvm`
Relink the correct libraries
Navigate to the API lib folder
   `cd /home/$USER/.local/share/Softbank Robotics/RobotSDK/API 7/tools/lib`
Back up the old library
    `mv libz.so.1 libz.so.1.bak`
Relink the System one
    `ln -s /usr/lib/x86_64-linux-gnu/libz.so libz.so.1`
Restart Android Studio & start the Emulator

**JDK 8**
Download Oracle JRE: https://www.java.com/en/download/manual.jsp
And then launch Android Studio 3.6.3 using this JRE:
    `STUDIO_JDK=/usr/local/java/ ~/dev/android-studio-3.6.3/bin/studio.sh`

### Potential Issues

#### App not installing to emulator

```
Device emulator-5574 disconnected, monitoring stopped.
Error while Installing APK
```

Reinstall API 7, or reset to default state (keep clean copy).