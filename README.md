# MediaPlayer

## Install OpenJFX

1. Go to [OpenJFX](https://gluonhq.com/products/javafx/)
2. Download `JavaFX Mac OS X SDK`
3. Unzip the Zip file
4. Copy `javafx-sdk-11.0.2` to `/Library/Java/JavaVirtualMachines/`

## Setup in IntelliJ

1. Build -> Build Project
2. Run -> Profile -> Main -> Edit -> VM options:

```
--module-path "/Library/Java/JavaVirtualMachines/javafx-sdk-11.0.2/lib" --add-modules=javafx.controls,javafx.fxml,javafx.media
```

## Assembly

```
$ cd ~/IdeaProjects/MediaPlayer
$ sbt assembly
$ /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java -jar ~/IdeaProjects/MediaPlayer/target/scala-2.12/MediaPlayer.jar
```

