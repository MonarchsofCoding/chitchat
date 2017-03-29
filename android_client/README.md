## Android Client

### Setting up for Development

#### What do you need?
Simply, download Android Studio to your machine. During installation, do standart installation, not custom. The required SDK and tools will be downloaded by the installer itself. The setups for the project and the Android Emulator is explained below.

IDEA is ok as well, but setup for SDK and tools is not easy. Prefer Android Studio if possible.

**DO NOT USE ECLIPSE TO IMPORT ANDROID STUDIO PROJECTS!**

#### What to select/import?
You have to select/import the ChitChat folder, not the android_client folder. Otherwise the indexing can go crazy and many dependencies, including some core android stuff won't work, even you have the files inside the project :D

### Invoke tasks
When using the `invoke` tasks for this project, make sure that you have removed the `local.properties` file created by Android Studio that sets where the Android SDK is. You can do this by performing:
```
rm ChitChat/local.properties
```

#### E2E Tests
The e2e tests can't be run on OSX unfortunately as they require using more than one hypervisor. We use Docker and the Android Emulator for these E2E tests.

On a linux machine (with KVM), you can run:
```
cd e2e
invoke test
```

The video output will be at:
```
ChitChat/app/ChitChatAndroid.mp4
```
