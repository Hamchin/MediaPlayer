package jp.ed.nnn.player

import java.io.File
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.input.DragEvent
import javafx.scene.media.{Media, MediaPlayer}

class MovieFileDragDroppedEventHandler(movies: ObservableList[Movie]) extends EventHandler[DragEvent] {
  override def handle(event: DragEvent): Unit = {
    val db = event.getDragboard
    // 動画ファイルを動画クラスへ変換し追加
    if (db.hasFiles) {
      db.getFiles.toArray(Array[File]()).toSeq.foreach(f => {
        val filePath = f.getAbsolutePath
        val fileName = f.getName
        val media = new Media(f.toURI.toString)
        val player = new MediaPlayer(media)
        // プレーヤーを読み込み時間を取得したのち追加
        player.setOnReady(new Runnable {
          override def run(): Unit = {
            val time = formatTime(media.getDuration)
            val movie = Movie(System.currentTimeMillis(), fileName, time, filePath, media)
            while (movies.contains(movie)) movie.setId(movie.getId + 1L)
            movies.add(movie)
            player.dispose()
          }
        })
      })
    }
    event.consume()
  }
}
