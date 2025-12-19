# MotoSense

> Final project for the *Edge Computing in the IoT* – Università della Svizzera italiana, Fall 2025
---

## Team
- [Lesinigo Simone](https://github.com/simone-lesinigo)
- [Pinciroli Luca](https://github.com/Pynci)
- [Santoro Chiara](https://github.com/chiaaxx)
- [Sanvito Marco](https://github.com/koalas11)

## How to build & run

### Prerequisites

- Android Studio Otter (version 2025.2.2) or later
- JDK 17 or later
- An Android phone
- The Arduino Device

### Building and running the app

1. Clone the repository:
   ```bash
   git clone https://github.com/koalas11/MotoSense.git
   ```

2. Open the project in Android Studio.
3. Sync the Gradle files to download dependencies.
4. Connect your Android phone via USB with USB debugging enabled to Android Studio.
5. Click the "Run" button to build and deploy the app.
6. Use the application on your phone to connect to the Arduino device and visualize sensor data.

### Important Code Locations

- **Bluetooth Communication**: `composeApp/src/androidMain/kotlin/org/lpss/motosense/module/BluetoothLowEnergyManager.android.kt`
- **Sensor Data Processing (Unpacking and Small Processing)**: `composeApp/src/commonMain/kotlin/org/lpss/motosense/model/DeviceData.kt`
- **Sensor Data Processing (Processing)**: `composeApp/src/commonMain/kotlin/org/lpss/motosense/viewmodel/DeviceViewModel.kt`
