package jp.ed.nnn.player

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.scene.control.{Label, TableView}
import javafx.scene.media.{MediaPlayer, MediaView}
import javafx.util.Duration

object MoviePlayer {

  // 動画再生
  def play(movie: Movie, tableView: TableView[Movie], mediaView: MediaView, timeLabel: Label): Unit = {
    // 以前の動画プレーヤーを破棄する
    if (mediaView.getMediaPlayer != null) {
      val oldPlayer = mediaView.getMediaPlayer
      oldPlayer.stop()
      oldPlayer.dispose()
    }

    // 新たな動画プレーヤー
    val mediaPlayer = new MediaPlayer(movie.media)
    // 動画の経過時間を更新する
    mediaPlayer.currentTimeProperty().addListener(new ChangeListener[Duration] {
      override def changed(observable: ObservableValue[_ <: Duration], oldValue: Duration, newValue: Duration): Unit = {
        timeLabel.setText(formatTime(mediaPlayer.getCurrentTime, mediaPlayer.getTotalDuration))
      }
    })
    // プレーヤーの起動時に時間を表示する
    mediaPlayer.setOnReady(new Runnable {
      override def run(): Unit = {
        timeLabel.setText(formatTime(mediaPlayer.getCurrentTime, mediaPlayer.getTotalDuration))
      }
    })
    // 動画の終了時に次の動画を再生する
    mediaPlayer.setOnEndOfMedia(new Runnable {
      override def run(): Unit = {
        playNext(tableView, mediaView, timeLabel)
      }
    })

    // ビューへの設定
    mediaView.setMediaPlayer(mediaPlayer)
    mediaPlayer.play()
  }

  sealed trait Track
  object Pre extends Track
  object Next extends Track

  // 指定の動画を再生する
  private[this] def playAt(track: Track, tableView: TableView[Movie], mediaView: MediaView, timeLabel: Label): Unit = {
    val selectionModel = tableView.getSelectionModel
    if (selectionModel.isEmpty) return
    val index = selectionModel.getSelectedIndex
    val changedIndex = track match {
      case Pre => (tableView.getItems.size() + index - 1) % tableView.getItems.size()
      case Next => (index + 1) % tableView.getItems.size()
    }
    selectionModel.select(changedIndex)
    val movie = selectionModel.getSelectedItem
    play(movie, tableView, mediaView, timeLabel)
  }

  // 前の動画を再生する
  def playPre(tableView: TableView[Movie], mediaView: MediaView, timeLabel: Label): Unit = {
    playAt(Pre, tableView, mediaView, timeLabel)
  }

  // 次の動画を再生する
  def playNext(tableView: TableView[Movie], mediaView: MediaView, timeLabel: Label): Unit = {
    playAt(Next, tableView, mediaView, timeLabel)
  }
}
