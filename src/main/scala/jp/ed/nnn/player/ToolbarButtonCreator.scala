package jp.ed.nnn.player

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.Button
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent

object ToolbarButtonCreator {

  // ボタンを生成する
  def create(imagePath: String, eventHandler: EventHandler[ActionEvent]): Button = {
    val buttonImage = new Image(getClass.getResourceAsStream(imagePath))
    val button = new Button()
    button.setGraphic(new ImageView(buttonImage))
    button.setStyle("-fx-background-color: Black")
    // ボタンが押されたときのアクション
    button.setOnAction(eventHandler)
    // ボタンにマウスを重ねた際のスタイル
    button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler[MouseEvent]() {
      override def handle(event: MouseEvent): Unit = {
        button.setStyle("-fx-body-color: Black")
      }
    })
    // ボタンからマウスが離れた際のスタイル
    button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler[MouseEvent]() {
      override def handle(event: MouseEvent): Unit = {
        button.setStyle("-fx-background-color: Black")
      }
    })
    button
  }

}
