1. Compilation and execution of the app:
    To compile the app, you need to add the file "thirdParties.properties" to the root of the project after cloning it, and then just build and run the project.
    For CI/CD purposes, to be able to run Lint and testing, I added a GitHub secret with the API key, and in the workflow script, created the "thirdParties.properties"
    so the process can work.

2. Architectural decisions and libraries chosen:
    - MVVM architecture: I decided to use the MVVM pattern in this project since it's easier to implement and it adjusts properly for the size of the project,
                         it's easy to test and scalable for small to medium sized projects.
    - Retrofit: I implemented Retrofit vs Ktor since I don't need more control over network calls for this project, it's easier to use, I'm already familiarized with it
                so I don't need to learn a new library for a small project.
    - Room: It's easier to implement and develop, it proveds an extra abstraction layer over the database, and reduce errors.
    - Flows: I implemented Flow to move data from the Room database to the UI since it's not tied to the lifecycle of the app, we can use it in different layers of the
             architecture, and it's more powerful.
    - Hilt: It's Google recommended library to implement the Dependency Injection, since it's simpler than Dagger and for a project of this size it's preferable.
    - Arrow: I used the Either pattern from the Arrow library to manage network calls and implement the error handling.
    - Jetpack Compose: It's faster and declarative, so it's easier to read and understand for newcomers. It's more efficient than View inflation, and it's the 
                       Google preferred method of UI presentation.
    - Compose Adaptive Layout: I implemented the List-Detail Scaffold since it's from the Material3 library, the latest and most powerful one, it helps manage the navigation
                               between the list and the detail, the data transference, it's adaptive so if we want to use the app in tablets or bigger screens both Composables
                               will show and interact together.
    - Navigation Compose: I implemented Navigation Compose (it's implemented in the List-Detail Scaffold, but the idea was to use it by default in the rest of the app) because 
                          it centralize navigation management, make it much cleaner to use, read and understand and it's the recommended way to navigate with Jetpack Compose.
    - Glide: I implemented Glide simply because I already worked with it and know how to implement it so I can focus on more advanced features of the project.
    - MockK: I used MockK because it's language it's similar to Kotlin, has Coroutine support and I've used it before.
    - Turbine: I used Turbine to unit tests Flows sequentially and handle flow termination.
   
3. Solution scalability:
    The project is scalable since I can grow the 3 core aspects of the app without refactoring any code:
    - Architecture: I am using a MVVM architecture with the different layers of the app separated ,so each component has a single responsibility.
                    I also divided the layers in different Gradle modules, so different people can work in different modules and the compile time is reduced greatly.
                    This can be improved modularizing the different features, but since the scope of the project was quite small I didn't consider it necessary.
                    I am also using DI with Hilt, so the code is easier to test and flexible.
    - Performance:  I am using Coroutines to make asynchronous call more efficient.
                    I am also using Room with Flows to implement a Single Source of Truth pattern, cache the data and show it to the user as soon as possible.
                    With modules and Gradle, only the modules that has changed are compiled so build time is reduced.
    - Maintenance:  I am using Kotlin guidelines and naming conventions, so I can use Lint and even in the future implement Detekt to enforce those conventions.
                    I implemented Unit Tests and UI Test to ensure the code is stable and future developments won't break old code.
                    I implemented a simple CI/CD with Github Actions workflow to ensure new code is tested and reviewed before merging.

4. Improvements and new features:
    - Location features:
        - Search for locations
        - Get your actual location
        - Save and delete locations, and show a list of saved locations to select
    - Weather alerts:
        - Show weather alerts on days list according to selected location
    - Days List:
        - Show average probability of rain for each day
        - Add pull-down gesture to refresh the list weather prediction
    - Day Detail:
        - Show weather and temperatures hourly within the day
        - Add weather background image or animation
        - Add pull-down gesture to refresh the prediction
        - Add days carousel to change easily between days
    - Lunar calendar
        - Show when does the moon phases change
    - Weather historical data and averages
        - Create a historical data feature where you can check averages of weather and temperature per daily or monthly basis, with temperature charts of historical min/max 

    - Improvements:
        - Implement a Navigator to navigate to different Compose screens, right now I am using a ThreePaneScaffoldNavigator to navigate in the List-Detail Scaffold, but it's specific
          for that Scaffold, so I would implement a Navigator for the big picture when I implement new features.
        - Implement Detekt to enforce Kotlin and Android coding standards.
        - Create feature modules.