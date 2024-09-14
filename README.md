# VoiceMedicationAssistant-mobil

The **VoiceMedicationAssistant** is a mobile application designed to recognize medications using the device's camera and provide voice-guided information and usage instructions about the identified medication. This application leverages AI for medication recognition and uses text-to-speech for delivering important details about the medication.

## Features

- **Camera-Based Medication Recognition**: Detect and identify medications using the device's camera.
- **Medication Information & Usage Instructions**: Display medication information and usage instructions after detection, both on-screen and as audio through text-to-speech.
- **Clean and Responsive UI**: Optimized for mobile devices, offering a user-friendly experience.

## Technologies Used

- **Android Studio**: For mobile development using Java.
- **TensorFlow Lite**: Implemented to run the machine learning model for medication recognition.
- **Android Speech API**: Utilized for converting medication details to speech.
- **Roboflow**: For loading and running the trained medication recognition model.

## Installation

### Prerequisites

- **Android Studio**: Ensure that Android Studio is installed on your machine.
- **Android SDK**: Install the Android SDK for running the app on an emulator or physical device.

### Steps to Run the Project

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/VoiceMedicationAssistant-mobile.git
    ```

2. **Open the project in Android Studio**:
    - Open Android Studio and select **Open an existing project**.
    - Navigate to the cloned repository and open it.

3. **Configure the Roboflow API Key**:
    - Obtain a publishable API key from **Roboflow**.
    - Add your key to the Java file handling the model load:
    ```java
    String publishable_key = "your_publishable_key";
    ```

4. **Run the application**:
    - Connect a physical Android device or set up an Android Emulator in Android Studio.
    - Press the **Run** button or use the shortcut `Shift + F10` to install and run the app.

## Usage

1. Open the application on your Android device.
2. The camera will be activated, and the app will begin scanning for medication labels.
3. When a medication is detected, its information will appear on-screen and be read aloud via the text-to-speech feature.
4. Use the **Repeat** button to replay the medication's information and usage instructions.

## Screenshot

<img width="273" alt="Screenshot 2024-09-14 at 15 34 46" src="https://github.com/user-attachments/assets/c5c253ec-caf7-4d33-a0a8-38290c3cd0e9">


## Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue to discuss any changes or improvements you’d like to make.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.
