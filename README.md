## About The Project

TODO

## Architecture Guidelines 
I am trying to follow common clean code principles for android architecture: https://developer.android.com/topic/architecture

TODO: ADD DDD EXPLANAITION, ADD TDD EXPLANAITION, CLEAN ARCHITECTURE, USEFUL LINKS.

example projects

sotial network app (ddd feature layers example): https://github.com/philipplackner/SocialNetworkTwitch/tree/development/app/src/main/java/com/plcoding/socialnetworktwitch

clean architecture app: https://github.com/philipplackner/CleanArchitectureNoteApp/tree/app 

ddd real life example app: https://gitlab.com/flexsentlabs/tip_prorotype/-/tree/master/app/src/main/java/com/flexsentlabs/tip 

stock market example app: https://github.com/philipplackner/StockMarketApp/tree/final

spotify clone example app: https://github.com/philipplackner/SpotifyCloneYT/tree/Part19-SongFragment2

## Project Stracture

### Dependencies

In this project, I manage dependencies using the dependencyResolutionManagement block in the settings.gradle file. Specifically, I define a versionCatalog called libs that contains references to the versions of various libraries that my project depends on, such as navigation, material, appcompat, and so on.

To make it easier to refer to these versions elsewhere in my project, I create aliases for them using the alias function. For example, I create an alias called navigation-fragment that refers to androidx.navigation:navigation-fragment-ktx at version 2.4.1, and an alias called material that refers to com.google.android.material:material at version 1.8.0.

By defining all these versions and aliases in the settings.gradle file, I can easily use them in my build.gradle files without repeating them multiple times. This helps me keep my dependency management organized and consistent across my project.

The libraries I use in this project include:

 * androidx.navigation: used for fragment navigation within the app.
 * com.google.android.material: used for a library of pre-designed UI components such as buttons, cards, and sliders.
 * junit:junit: used for unit testing of the app code.
 * androidx.test.espresso: used for UI testing of the app by simulating user interactions such as clicks and scrolls.
 * androidx.room: persistence library provides an abstraction layer over SQLite.
 * kotlinx-coroutines-android: concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
 * io.insert-koin::dependency injection framework.

### Architecture

Clean Architecture is a software design philosophy that aims to create software that is easy to maintain and extend over time. It is based on the SOLID principles of object-oriented programming, which emphasize the importance of single responsibility, open-closed principle, Liskov substitution principle, interface segregation, and dependency inversion. In Clean Architecture, the application is separated into distinct layers that represent different levels of abstraction and responsibility, which helps to ensure that each layer can be modified independently without affecting the others.

DDD (Domain Driven Design) is a software development approach that focuses on the core business logic and rules of an application. It emphasizes the importance of modeling the problem domain using a rich domain model and a ubiquitous language that is shared by all members of the development team. DDD also advocates for the use of bounded contexts to manage the complexity of large applications, and for the separation of concerns between the domain logic and the infrastructure.

TDD (Test-Driven Development) is a software development technique that involves writing automated tests before writing the code that will be tested. It helps to ensure that the code is correct, reliable, and maintainable by catching errors early in the development process. TDD also encourages developers to write code that is modular, loosely coupled, and testable.

MVVM (Model-View-ViewModel) is a design pattern that is commonly used in modern Android applications. It separates the UI (View) from the application logic (ViewModel) and the data (Model). This separation of concerns helps to make the code more modular and maintainable.

Based on these concepts, the application is structured using Clean Architecture principles, with separate layers for the domain, dependency injection (DI), data, and UI. Each feature is further divided into these layers to promote separation of concerns and modularity. The use of DDD emphasizes the importance of modeling the problem domain, while TDD helps to ensure that the code is correct and maintainable. Finally, the use of MVVM separates the UI from the application logic and data, which makes the code more modular and easier to maintain.

## Git Guildelines 
Here I am trying to implement all the common principles of Git Flow: https://nvie.com/posts/a-successful-git-branching-model/

As for now "Main" branch is empty and there are no "Release Branches", because I haven't posted anything on the Google Play platform yet. And the current "Develop" version of this app doesn't sutisfy all the user's needs yet. Therefore "Develop" branch acts as a working code source as for this moment, where all the latest updates are mearged into.

### Merge request branch creation

When you have finished writing code and want to commit:

- Create a new branch feature/TICKET-123_handle_order_loading

Where "TICKET-123" is the task/bug name and number and "handle order loading" is the title. 
Name of the branch can be: feature, bugfix, refactor (depending on the use case).
Don't forget that you need to branch off the current "develop" branch.
 
### Commit comments
 
Use the CHANGELOG notation https://keepachangelog.com/ru/1.0.0/ for marking [ADDED], [CHANGED], 
[REMOVED] or [FIXED] code.

Use the following template for commit messaging: 
```
TICKET-123 - handle order loading
[ADDED] OrderInteractor for order downloading
[CHANGED] file downloading via CDN
[REMOVED] ProfileActivity
[FIXED] image caching on disk
````

### Merge request reviewers

After you have created your feature/JIRA-123_add_avatar_loading branch from the current develop branch 
and pushed it to remote. Tag appropriate people to review your merge request.

## Libraries 

TODO
