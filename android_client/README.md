## Android Client

### Setting up for Development

#### What do you need?
**If you're planning to run the application on beta or production build, ignore this paragraph**
If you don't have an Android phone, and thinking to run the application through a virtual machine, we strongly suggest you to use Windows or a Linux distribution, since Docker's virtual machine conflicts with Android Virtual Machine in Mac OSX devices. You can still run via selecting ARM images while creating the virtual machine but it will be unbearably slow. You can still run Android Studio on Mac OSX, and can run the application to debug it with an actual Android phone. 

**If you're planning to run the application on beta or production build with a Mac OSX device, shutdown Docker before you do.**

Simply, download Android Studio to your machine. During installation, do standart installation, not custom. The required SDK and tools will be downloaded by the installer itself. The setups for the project and the Android Emulator is explained below.

IntelliJ IDEA is ok as well, but setup for SDK and tools is not easy. Prefer Android Studio if possible.

**IMPORTING THIS PROJECT TO ECLIPSE WON'T WORK. THE STRUCTURE OF ANDROID STUDIO PROJECTS IS NOT DESIGNED TO WORK WITH BOTH ECLIPSE AND JETBRAINS PRODUCTS WITH ONLY IMPORTING. AVOID ECLIPSE TO IMPORT THE PROJECT!**

#### What to select/import?
You have to select/import the ChitChat folder, not the android_client folder. Otherwise the indexing can go crazy and many dependencies, including some core android stuff won't work, even you have the files inside the project.

#### Selecting the build flavour
Go to View -> Tool Windows and click on Build Variants. Then, from the opened window click on the desired build flavour. **Do not pick any release build flavour since you need a keystore to sign the app, and we removed it from our repository for security purposes.**

#### How to run the application?
Simply press the play button on the top bar, which is right next to 'app' text. A small window will appear asking which device to install and run the application. If you are using Android Studio for the first time, you probably won't have any virtual device in there. You can create one via clicking the "Create New Virtual Device" button. After clicking, select any phone, then select an image for your device. We as Monarchs of Coding suggest you to use API level 24, which is the most stable for the virtual devices and suggested by the Android Studio itself to us, but you are welcome to use anything from API 19 to 25. Just make sure you picked a x86 image, since ARM ones are unbearably slow. Make sure you installed the SDK for the API level as well. After picking the image, you'll have the verifying page. Click on advanced options and go all the way down to see the Enable Keyboard Input option. Check that checkbox if you want to use your keyboard as input throughout the virtual device. After that, click Finish to create the device. It will pop un the device selection page afterwards.

There can be errors about the virtual device x86 image because of the Intel Virtualization Technology (Intel VT-x). This requires some option changes in Windows' settings and in BIOS, so if you have a problem related to this make sure you open an issue for this GitHub repository to check and fix it thoroughly.

#### What if I want to install another SDK?
Go to Tools -> Android -> SDK Manager. You'll have a list of APIs for Android. Simply click on the checkbox of the API you want to install and click on Apply / OK. Android Studio will install the SDK for you.

#### Android Studio Says 'Default Activity not found'
Click on 'app' on the top bar. Then click on Edit Configurations. There will be a dropbox called 'Launch:'. Select Default Activity in that and click Apply / OK.

#### An unexpected behaviour happened or an error is not resolved, and we don't know what it is nor it is not clear what the error is...
It always helps to invalidate the cache and restart the IDE. Just go to File and click on Invalidate Caches / Restart.

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
#### Still having problems and this readme is not enough?
Open an issue for this GitHub repository. We'll have an eye on this so we will resolve it.
