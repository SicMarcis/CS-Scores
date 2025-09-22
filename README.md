# Scores
Scores is an app which demonstrates Android development with Jetpack Compose, Koin, Coroutines, Jetpack (Room, ViewModel) and Material Design with DarkMode. 
Also shows approach to modularization with Clean Architecture.

## Architecture
Based on Clean Architecture separates app into different layers which makes application more scalable friendly and testable as well.
Layers are separated to different modules to force keeping separation clear.

## Tech Stack
- Minimum SDK level 26.
- [Kotlin](https://kotlinlang.org/) based, utilizing [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous operations.
- [Koin](https://dagger.dev/hilt/): Facilitates dependency injection.
- Jetpack Libraries:
    - Jetpack Compose: Android’s modern toolkit for declarative UI development.
    - Lifecycle: Observes Android lifecycles and manages UI states upon lifecycle changes.
    - ViewModel: Manages UI-related data and is lifecycle-aware, ensuring data survival through configuration changes.
    - Navigation: Facilitates screen navigation.
    - Room: Constructs a database with an SQLite abstraction layer for seamless database access.
    - Firebase: Backend services real-time database.
  

