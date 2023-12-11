# Pepper @ University of Oulu

This repository contains the source code of the universal application running on University of Oulu's Pepper. This application contains a range of different screens for different purposes, and all iterations of our toolsets for promotional and experimental purposes.

## Applications

All of the applications, in reverse-chronological order:

### pepper_study_promotion

This latest application showcases Pepper's ability to converse offline, produce answers via GPT 3.5 Turbo and respond to touch via her touch sensors. This application allows Pepper to converse lightly about ITEE and UBICOMP and entertain the users, with the goal of attracting people to a cause, which at the time of the implementation is the ide collection survey for our first study.

### itee_promotion_offline (deprecated)

This application is first to implement an offline dialogue management system to allow Pepper to become a very responsive and helpful agent of ITEE, promoting UBICOMP and Sona.

### sona_promotion_gpt (deprecated)

This application builds on top of gpt_prototype's GPT3.5 Turbo implementation to allow Pepper to become a useful source of information about UBICOMP's Sona study participation management system.

### gpt_prototype (deprecated)

The first application allowing Pepper to interface with the OpenAI API, both via GPT3 (completion) and GPT3.5 Turbo (dialogue).

### control

This screen is meant to be used by the operator or developer to control the robot, for example to move it somewhere, make it say something or modify the configuration.

### home

This is the main screen of the universal application. It serves no particular purpose other than letting the operator launch the desired application.

## Packages

### fi.oulu.danielszabo.pepper.applications

This package contains all applications, including currently developed and legacy iterations, one-off and continuous projects. All of these can be launched from the application selector menu and while they share a lot of their tools, they all have different purpose.

### fi.oulu.danielszabo.pepper.tools

This package contains code shared by applications, for example robot controls, dialogue management, API abstractions, animation calls.

### fi.oulu.danielszabo.pepper.log

This package contains our own logging solution.

## Setup and Usage

## System Requirements
- Linux operating system
- Android Studio 3.4.2 OR Flamingo 2022.2.1 Patch 2
  - [Android Studio Download Archive](https://developer.android.com/studio/archive)  
- Pepper SDK 7
- Oracle JRE 8 (See below)
- Android Gradle Plugin 3.6.3
- Gradle 5.6.4
- KVM (see below)
- Pop!_OS 22.04 LTS
- Intel CPU

### KVM

1. Install KVM

`sudo apt install qemu-kvm`

2. Add yourself to the kvm group

`sudo adduser yourusername kvm`

Relink the correct libraries:

3. Navigate to the API lib folder

`cd /home/$USER/.local/share/Softbank Robotics/RobotSDK/API 7/tools/lib`

4. Back up the old library

`mv libz.so.1 libz.so.1.bak`

5. Relink the System one

`ln -s /usr/lib/x86_64-linux-gnu/libz.so libz.so.1`

6. Restart Android Studio & start the Emulator

### JDK 8

Download Oracle JRE: https://www.java.com/en/download/manual.jsp

And then launch Android Studio 3.6.3 using this JRE:

`STUDIO_JDK=/usr/local/java/ ~/dev/android-studio-3.6.3/bin/studio.sh`

## Potential Issues

### App not installing to emulator

```
Device emulator-5574 disconnected, monitoring stopped.
Error while Installing APK
```

Reinstall API 7, or reset to default state (keep clean copy).

### Compilation Failure: Unrecognized Attribute Name 
If you have multiple installations of JDK, please ensure that you add the appropriate version's location using the project structure settings.
