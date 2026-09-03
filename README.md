# FINLY – Financial Friendly
### *Plan. Save. Achieve.*

**FINLY** is a modern Android application designed for students to take control of their financial future. It allows users to set multiple savings goals, track their progress in real-time, and receive smart, sustainable savings recommendations based on their weekly allowance.

---

## 🌟 Key Features

*   **🎓 Student-Focused Design**: Replaces generic banking layouts with a friendly, professional fintech aesthetic using deep navy and growth-green branding.
*   **📊 Multi-Saving Plan Module**: Create and manage multiple goals (e.g., "New Laptop", "Emergency Fund") simultaneously.
*   **⏱️ Real-Time Countdown**: Detailed tracking (Days, Hours, Minutes, Seconds) until your goal deadline to keep you motivated.
*   **💡 Smart Savings Engine**: Dynamic suggestions based on your **Weekly Allowance**:
    *   **Daily**: 10% recommendation.
    *   **Weekly**: 20% recommendation.
    *   **Monthly**: 30% recommendation.
*   **⚖️ Multi-Factor Suggestions**: Intelligently calculates the higher amount between your budget (allowance) and the minimum requirement to reach your goal by the target date.
*   **📴 Works Offline**: Your data is stored securely on your device using a local SQLite database—no internet connection required.
*   **🔐 Secure Authentication**: Personalized student accounts with login and signup functionality.

---

## 🛠️ Tech Stack

*   **Language**: Java
*   **UI Framework**: Android SDK with Material Design 3
*   **Architecture**: Activity and Fragment-based navigation with Jetpack View Binding
*   **Database**: SQLite (`DatabaseHelper`)
*   **Build System**: Gradle (Kotlin DSL)

---

## 🚀 How to Run

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/YOUR_USERNAME/Savings.git
    ```
2.  **Open in Android Studio**:
    - File -> Open -> Select the project folder.
3.  **Sync Gradle**:
    - Allow Android Studio to sync dependencies from the `build.gradle.kts` file.
4.  **Run on Device/Emulator**:
    - Ensure your device is running Android 7.0 (API 24) or higher.

---

## 📁 Project Structure

*   `app/src/main/java/com/example/loginapp`: Contains all logic for Activities, Adapters, Models, and Database management.
*   `app/src/main/res/layout`: Contains all XML UI definitions.
*   `app/src/main/res/drawable`: Branded assets including the multi-layered wave background and FINLY logo.

---

## 📝 License

This project is shared for educational purposes. Feel free to explore, learn, and contribute!
