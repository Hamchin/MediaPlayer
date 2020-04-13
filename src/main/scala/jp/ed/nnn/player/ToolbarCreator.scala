package jp.ed.nnn.player

import java.lang

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.{Label, TableView}
import javafx.scene.layout.HBox
import javafx.scene.media.MediaView
import javafx.stage.Stage
import javafx.util.Duration
import jp.ed.nnn.player.SizeConstants._

object ToolbarCreator {

  def create(mediaView: MediaView, tableView: TableView[Movie], timeLabel: Label, scene: Scene, primaryStage: Stage): HBox = {
    // ツールバー
    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")
    // 前へ戻るボタン
    val preButton = ToolbarButtonCreator.create("images/first.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val mediaPlayer = mediaView.getMediaPlayer
        if (mediaPlayer != null) MoviePlayer.playPre(tableView, mediaView, timeLabel)
      }
    })

    // 早戻しボタン
    val backButton = ToolbarButtonCreator.create("images/back.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val mediaPlayer = mediaView.getMediaPlayer
        if (mediaPlayer != null) mediaPlayer.seek(mediaPlayer.getCurrentTime.subtract(new Duration(10000)))
      }
    })

    // 再生ボタン
    val playButton = ToolbarButtonCreator.create("images/play.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val selectionModel = tableView.getSelectionModel
        val mediaPlayer = mediaView.getMediaPlayer
        if (mediaPlayer != null && !selectionModel.isEmpty) mediaPlayer.play()
      }
    })

    // 停止ボタン
    val pauseButton = ToolbarButtonCreator.create("images/pause.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val mediaPlayer = mediaView.getMediaPlayer
        if (mediaPlayer != null) mediaPlayer.pause()
      }
    })

    // 早送りボタン
    val forwardButton = ToolbarButtonCreator.create("images/forward.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val mediaPlayer = mediaView.getMediaPlayer
        val duration = new Duration(10000)
        if (mediaPlayer != null) {
          if (mediaPlayer.getTotalDuration.greaterThan(mediaPlayer.getCurrentTime.add(duration))) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime.add(duration))
          }
        }
      }
    })

    // 次へ進むボタン
    val nextButton = ToolbarButtonCreator.create("images/last.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val mediaPlayer = mediaView.getMediaPlayer
        if (mediaPlayer != null) MoviePlayer.playNext(tableView, mediaView, timeLabel)
      }
    })

    // フルスクリーンボタン
    val fullscreenButton = ToolbarButtonCreator.create("images/fullscreen.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        primaryStage.setFullScreen(true)
        mediaView.fitHeightProperty().unbind()
        mediaView.fitHeightProperty().bind(scene.heightProperty())
        mediaView.fitWidthProperty().unbind()
        mediaView.fitWidthProperty().bind(scene.widthProperty())
      }
    })

    // フルスクリーン解除時のサイズを制御する
    primaryStage.fullScreenProperty().addListener(new ChangeListener[lang.Boolean] {
      override def changed(observable: ObservableValue[_ <: lang.Boolean], oldValue: lang.Boolean, newValue: lang.Boolean): Unit = {
        if (!newValue) {
          mediaView.fitHeightProperty().unbind()
          mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))
          mediaView.fitWidthProperty().unbind()
          mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
        }
      }
    })

    // ツールバーにアイテムを追加する
    toolBar.getChildren.addAll(preButton, backButton, playButton, pauseButton, forwardButton, nextButton, fullscreenButton, timeLabel)

    toolBar
  }

}
