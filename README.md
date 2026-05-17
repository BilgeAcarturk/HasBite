# HasBite

HasBite is an AI-powered mobile recipe application developed with Kotlin and Jetpack Compose. The application allows users to explore recipes, generate AI-based meal suggestions, save favorite recipes, manage shopping lists, and personalize their profiles through a modern mobile interface.

---

# Core Features

## AI Features
- AI recipe generation
- Structured recipe parsing

## User System
- Firebase Authentication
- Profile personalization
- Private/Public accounts

## Recipe Management
- Favorites system
- Shopping lists
- Categorized collections

## Backend Features
- Firestore realtime sync
- Dynamic profile search

---

# Technologies Used

## Frontend
- Kotlin
- Jetpack Compose
- Material 3

## Backend & Services
- Firebase Authentication
- Firebase Firestore
- Gemini API

## Architecture
- MVVM Architecture
- StateFlow
- ViewModel

# Architecture Overview

The application follows an MVVM-based architecture using Jetpack Compose, StateFlow, and Firebase Firestore.

<img width="978" height="614" alt="MVVM Architecture Diagram" src="https://github.com/user-attachments/assets/ee0344d2-bce9-4723-b18d-f8e3ca9b0812" />

# Navigation Flow

The navigation structure is built using Jetpack Compose Navigation. Screens communicate through lightweight route parameters such as recipeId and categoryName.

<img width="900" alt="Navigation Flow Diagram" src="https://github.com/user-attachments/assets/f30e93c7-952f-413e-8387-b29bcdb79d78" />

---

# Project Structure

```text
app/
├── manifests/
│
├── kotlin+java/
│   └── com.hasbite.app/
│       ├── data/
│       │   ├── model/
│       │   │   ├── Recipe.kt
│       │   │   └── User.kt
│       │   │
│       │   └── repository/
│       │
│       ├── navigation/
│       │   ├── AppNavGraph.kt
│       │   ├── BottomNavItem.kt
│       │   └── Routes.kt
│       │
│       ├── network/
│       │   └── GeminiService.kt
│       │
│       ├── ui/
│       │   ├── screens/
│       │   ├── theme/
│       │   └── viewmodel/
│       │
│       └── MainActivity.kt
│
├── res/
│   ├── drawable/
│   ├── mipmap/
│   ├── values/
│   └── xml/
│
└── Gradle Scripts/
    ├── build.gradle.kts
    ├── settings.gradle.kts
    └── gradle.properties
```

# Installation & Running

## Clone Repository

```bash
git clone https://github.com/BilgeAcarturk/HasBite.git
```

## Setup

- Open the project in Android Studio
- Sync Gradle dependencies
- Configure Firebase
- Add your own `google-services.json` file inside:

```text
app/google-services.json
```

- Configure your Gemini API key

## Run On

- Android Emulator
- Physical Android Device

# Highlights

- Realtime Firestore architecture
- AI-powered dynamic recipe generation
- Context-aware navigation system
- Dynamic category filtering
- Reactive UI with StateFlow
- Private account visibility infrastructure

# The application uses Firebase services for:

User authentication
Firestore database operations
Realtime profile search
User profile storage
Favorites and saved recipes
AI Integration

HasBite integrates Google Gemini API for AI recipe generation.

# The AI system:

Generates recipes dynamically
Parses recipe structure
Separates ingredients and instructions
Returns formatted recipe content
Profile System

# The profile infrastructure includes:

Realtime user search
Prefix-based Firestore queries
Avatar personalization
Private account visibility control
Dynamic Compose profile rendering

# Challenges During Development

AI response parsing instability
Firestore realtime listener conflicts
Navigation state synchronization
Duplicate recipe prevention
Compose recomposition issues

# These issues were solved through:

Prefix-based Firestore queries
Shared ViewModel state management
Firestore rule updates
Unified avatar architecture
Compose recomposition optimization

# Team Members
Bilge Acartürk
Ece Günaydın
İrem Gül
Öykü Alabaş
Ayşe İrem Hüdaverdi
