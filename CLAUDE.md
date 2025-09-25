# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Build Commands
```bash
./gradlew build          # Assembles and tests the project
./gradlew assemble       # Assemble main outputs for all variants
./gradlew clean          # Delete build directory
```

### Testing Commands
```bash
./gradlew test                    # Run all unit tests
./gradlew testDebugUnitTest      # Run debug unit tests only
./gradlew connectedAndroidTest   # Run instrumentation tests (requires device/emulator)
./gradlew check                  # Run all checks including tests and lint
```

### Lint Commands
```bash
./gradlew lint          # Run lint on default variant
./gradlew lintFix       # Run lint and apply safe fixes
./gradlew lintDebug     # Lint debug variant only
```

## Architecture

This Android app follows Clean Architecture with multi-module structure separating concerns into distinct layers.

### Module Structure
- **app**: Main Android application with navigation and DI setup
- **core/design-system**: Reusable UI components, themes, and design tokens
- **core/utils**: Shared utilities (BaseViewModel, Result wrapper, UI state interfaces)
- **data**: Data layer with repository implementations and data sources (Room + Firebase)
- **domain**: Business logic with use cases and domain models
- **feature/**: Feature-specific modules organized by list and detail features
  - Each feature has data/domain/presentation layers following dependency flow

### Dependency Injection (Koin)
All modules are configured in `ScoresApp.kt` with these key modules:
- `dataModule`: Repository implementations, Room database, Firebase setup
- `domainModule`: Use cases for core domain operations
- Feature modules: `listDomainModule`, `listPresentationModule`, `detailDomainModule`, `detailPresentationModule`

### Data Layer Architecture
- **Local Storage**: Room database with `ScoreDatabase` and `ScoreDao`
- **Remote Storage**: Firebase Firestore via `FirebaseSource`
- **Repository Pattern**: `ScoreRepositoryImpl` coordinates between local and remote sources
- **Base Classes**: `LocalSource`, `RemoteSource`, and `BaseSource` abstractions

### Key Patterns
- **Result Wrapper**: Custom `Result` type in `core/utils` for error handling
- **Base ViewModel**: `BaseViewModel` provides common ViewModel functionality
- **UI State Management**: `UiStateAware` and `UiActionAware` interfaces for consistent state handling
- **Feature Modules**: Each feature (list, detail) has complete separation of data/domain/presentation

### Technology Stack
- Kotlin with Coroutines and Flow for async operations
- Jetpack Compose with Material3 for UI
- Room with KSP for local database
- Firebase Firestore for remote storage
- Koin for dependency injection
- Navigation Compose for screen navigation

### Package Naming
Base package: `cz.sic` with feature-specific subpackages (`cz.sic.scores`, `cz.sic.list`, `cz.sic.detail`, etc.)

### Known Issues
The project has KSP version compatibility warnings (KSP 2.2.0-2.0.2 with Kotlin 2.2.20) that generate build warnings but don't affect functionality.