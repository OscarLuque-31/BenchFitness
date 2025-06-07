# 🏋️‍♂️ BenchFitness

**BenchFitness** es una aplicación móvil para Android enfocada en usuarios de gimnasio que desean llevar un control completo y personalizado de su progreso. Desde la creación de rutinas hasta el seguimiento de estadísticas, la app ofrece todas las herramientas necesarias para optimizar el entrenamiento.

## 📱 Funcionalidades principales

- Autenticación con Firebase (email/contraseña y Google).
- Personalización de objetivos: perder grasa, mantener peso o ganar masa muscular.
- Biblioteca de ejercicios consultados desde una API propia.
- Creación y gestión de rutinas semanales.
- Seguimiento del progreso: peso corporal y rendimiento en ejercicios.
- Cálculos fitness: metabolismo basal, superávit, déficit calórico y RM.
- Marcado de ejercicios como favoritos.
- Interfaz moderna con Jetpack Compose.

## 🧰 Tecnologías utilizadas

### Frontend (App Android)
- **Kotlin**
- **Jetpack Compose**
- **Firebase** (Auth, Firestore Database)

### Backend (API de ejercicios)
- **Java**
- **Spring Boot**
- **MySQL** (Railway)
- **AWS S3** (almacenamiento de GIFs)
- **API REST** (Formato JSON)

### Entorno de desarrollo
- **Android Studio**
- **Eclipse**

## 🏗️ Arquitectura

- Arquitectura externa: Firebase (auth + datos), Railway (API + MySQL), AWS S3 (GIFs).
- Arquitectura interna de la app: **MVVM (Model - View - ViewModel)** para una estructura limpia, mantenible y escalable.

## ⚙️ Instalación y ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/OscarLuque-31/BenchFitness.git
