# GeoJson Client App

This app simply fetches locations, pins them on map and tracks your location. Tracking stops when you get closer to a point by specified distance. 
When location response comes, they are parsed and filled in a map. This map holds locations by key which including integer part of latitude and longitude. 
For example; for a location with coordinates 46.444153 and 30.770661, key will be **46-30**. 
After parsing done, map will be like this: ```key: 46-30 -> value: [Location]```and goes on. Because there are a lot of locations and searching through them is not efficient. 
When your location changes a key generating from your location by same way. 
So, the app compares your location with just in that list corresponding to the key in the map.

* Used detekt for static code analysis.
* Used jacoco to generate test coverage report(Unfornately, missing a lot)
* Used dokka to generate html documentation pages.

There is a gradle task named **checkCode** to run detekt, jacoco and dokka tasks. Jacoco runs just unit tests because I didn't want to struggle to run emulator on CircleCI. 
If you want to run UI tests to you have to run *./gradlew connectedAndroidTest* command manually.

## Libraries and tools used

* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html)
* Android Support Libraries
* [Ktor Client](https://ktor.io/docs/getting-started-ktor-client.html)
* [Coroutines](https://developer.android.com/kotlin/coroutines)
* [Kotlinx.Serialization](https://github.com/Kotlin/kotlinx.serialization)
* [Google Maps](https://developers.google.com/maps/documentation/android-sdk/overview)

There is no dependency injection library because there are 2 pages. I injected repository instances to view models manually providing in ```ServiceLocator``` class.

## Requirements

* JDK 1.8
* [Android SDK](https://developer.android.com/studio/index.html)
* Min Android Version: API 26
* Target Android Version: API 29
* Latest Android SDK Tools and build tools.

## Running
* Clone this repository.
* Open containing folder in Android Studio.
* Click run :)

Before commit changes you should run *./gradlew checkCode*. If it will be successful then you commit.

## Localization
There are 2 language files in assets folder: ```language_en.json``` and ```language_tr.json```. I didn't use strings.xml because I want to change text resources dynamically. In this app, when app starting the json file is reading with last used language key, 'en' or 'tr'. But you can change to take it from api. If you set new language resources via ```LanguageResourceProvider``` you will change it in runtime.
