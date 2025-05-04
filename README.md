# 📱 Receiptly – Final Year Project
![Platform](https://img.shields.io/badge/platform-Android-blue.svg)  
![Language](https://img.shields.io/badge/language-Kotlin-orange.svg)  
![License](https://img.shields.io/badge/license-Academic-lightgrey.svg)

### 👤 Student: Denis Miculescu
### 🆔 Student Number: 20098078

---

## 📖 Project Overview
**Receiptly** is an Android app that helps users scan, organize, and 
manage **pharmacy receipts**. It features OCR-based data extraction, 
cloud syncing with Firebase, and a prescription reminder system to support 
better health and spending habits.

---

## ✨ Features

<details>
  <summary>🧾 Receipt Management</summary>

- Upload or take pictures of pharmacy receipts
- Automatically extract data using OCR
- View, filter, and organize receipts
</details>

<details>
  <summary>🔔 Prescription Reminders</summary>

- Add reminders for repeat prescriptions
- Get timely notifications before they expire
</details>

<details>
  <summary>📍 Nearby Pharmacies</summary>

- View nearby pharmacies using Google Maps
- Integrates Google Places SDK to fetch real-time locations
</details>

<details>
  <summary>📦 Cloud Integration</summary>

- Firebase Authentication for user login
- Firestore to store receipt metadata
- Firebase Storage for storing receipt images
</details>

---

## 🛠️ Tech Stack

| Category           | Technology                          |
|--------------------|--------------------------------------|
| Language           | Kotlin                              |
| UI Framework       | Jetpack Compose                     |
| Backend            | Firebase (Auth, Firestore, Storage) |
| OCR                | Google ML Kit Text Recognition       |
| Maps               | Google Maps & Places SDK            |
| Notifications      | WorkManager                         |
| DI                 | Hilt                                |
| Architecture       | MVVM                                |

