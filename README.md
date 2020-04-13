# MediaPlayer

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

