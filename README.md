# weathery.mobile

<p align="center">  
 Weathery is an app that tells the users in a simple format whether it's going to rain on the current day or not and also the time it starts. It
 demonstrates modern Android development with Hilt, Coroutines, Flow, Jetpack libraries, and Material Design based on MVVM architecture.
</p>
</br>


## Download
 Still in development ⚠ ..


## Tech stack 
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - ViewBinding: View binding is a feature that allows you to more easily write code that interacts with views.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
- Architecture
  - MVVM (Model - View - ViewModel) with Clean Architecture
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [Moshi](https://github.com/square/moshi/): A modern JSON library for Kotlin and Java.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.

## Architecture
**Weathery** is based on the MVVM with clean architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).
<img src="https://user-images.githubusercontent.com/82452881/202274899-7b637311-a6e7-425f-8d32-0f7b89d5c4f1.png" align="center" width="350"/>


The overall architecture of **Weathery** is composed of three layers; the UI layer,domain and the data layer. Each layer has dedicated components and they have each different responsibilities, as defined below:

### Architecture Overview

![arch](https://user-images.githubusercontent.com/82452881/202274823-fb259855-6071-4f53-a3f4-6f4f98d10147.png)

- Each layer follows [unidirectional event/data flow](https://developer.android.com/topic/architecture/ui-layer#udf); the UI layer emits user events to the data layer, and the data layer exposes data as a stream to other layers.
- The data layer is designed to work independently from other layers and must be pure, which means it doesn't have any dependencies on the other layers.

With this loosely coupled architecture, you can increase the reusability of components and scalability of the app.

### UI Layer
![ui layer2](https://user-images.githubusercontent.com/82452881/202275088-291c7952-9770-4d04-8eb0-c2c8fc11ac78.png)


The UI layer consists of UI elements to configure screens that could interact with users and [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that holds app states and restores data when configuration changes.
- UI elements observe the data flow and binds them to the UI elements via [View Binding](https://developer.android.com/topic/libraries/data-binding), which is a essential part of the MVVM architecture. 

### Domain layer
  The domain layer is an optional layer that sits between the UI and data layers.
The domain layer is responsible for encapsulating complex business logic, or simple business logic that is reused by multiple ViewModels. This layer is optional because not all apps will have these requirements. It should be used only when needed—for example, to handle complexity or favor reusability.

### Data Layer
![data layer](https://user-images.githubusercontent.com/82452881/202275293-44e5ba41-d26f-4c52-b961-0850fa6d4861.png)

The data Layer consists of repositories, which include business logic, such as querying and requesting remote data from the network and also
determine how the app creates, stores, and changes data.
