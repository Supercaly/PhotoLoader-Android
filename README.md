# PhotoLoader
The android library that allow users to choose any photo from their devices or cameras, show them a list and get the uri of the photos <br/>
<img src="https://raw.githubusercontent.com/Supercaly/PhotoLoader/master/screen-empty.png" width="200">
<img src="https://raw.githubusercontent.com/Supercaly/PhotoLoader/master/screen-data.png" width="200">
<img src="https://raw.githubusercontent.com/Supercaly/PhotoLoader/master/screen-error.png" width="200">

## Now Using Androidx
Starting from the version 1.2.1 we are using androidx so make sure to add this two lines in the gradle.properties
```
android.useAndroidX=true
android.enableJetifier=true
```

## Installation
[![](https://jitpack.io/v/Supercaly/PhotoLoader.svg)](https://jitpack.io/#Supercaly/PhotoLoader)

Add it in your root build.gradle at the end of repositories

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency
#### Gradle
```kotlin
implementation 'com.github.Supercaly:PhotoLoader:1.0.0'
```
#### Maven

```xml
<dependency>
  <groupId>com.github.Supercaly</groupId>
  <artifactId>PhotoLoader</artifactId>
  <version>1.2.1</version>
</dependency>
```

## Usage
PhotoLoader has been redesigned so you no longer need to use the fragment, but simply add the PhotoLoader widget
to your view's .xml file

```xml
<com.supercaly.library.PhotoLoader
    android:id="@+id/photo_loader"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:max_photo_num="8"
    app:folder_name="Folder Name"
    app:done_button_title="Done"
    app:limit_message="You've reached the maximum limit!!!"/>
```

then in your activity's onCreate

```kotlin
val photoLoader = findViewById(R.id.photo_loader)
photoLoader.setImageBuilder(this@MainActivity)
```

#### Get the photos
To get the selected images you have different methods
```kotlin
photoLoader.images { data -> /*use the images*/ }

val photo: ArrayList<String> = photoLoader.images
```

#### Set the error
It's possible to set an error to notify the user, if there is no photo selected this will change the button color

```kotlin
photoloader.error = true
photoloader.error = false
```

#### There are selected photos?
To know at any time if you have selected photos use
```kotlin
val isEmpty = photoloader.isEmpty()
```
## Thanks
The image picking part is made possible by the ***Image Picker*** library by nguyenhoanglam.
Find it at https://github.com/nguyenhoanglam/ImagePicker

All the miages are rendered by the ***Picasso*** library by Square http://square.github.io/picasso/ <br/>
and the circle effect are given by the ***Picasso Transformation*** library by wasabeef https://github.com/wasabeef/picasso-transformations

## Apps that use PhotoLoader
Please send a pull request if you would like to be added here.

## License
```
Copyright 2018 Lorenzo Calisti.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```