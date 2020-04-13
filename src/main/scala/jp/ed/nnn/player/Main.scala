package jp.ed.nnn.player

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{Label, TableCell, TableColumn, TableRow, TableView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Callback

import jp.ed.nnn.player.SizeConstants._

object Main extends App {
  Application.launch(classOf[Main], args: _*)
}

class Main extends Application {

  // アプリケーション起動時
  override def start(primaryStage: Stage): Unit = {
    // ビュー
    val mediaView = new MediaView()

    // 時間表示用ラベル
    val timeLabel = new Label()
    timeLabel.setText("00:00:00/00:00:00")
    timeLabel.setTextFill(Color.WHITE)

    // テーブル
    val tableView = new TableView[Movie]()
    tableView.setMinWidth(tableMinWidth)
    val movies = FXCollections.observableArrayList[Movie]()
    tableView.setItems(movies)
    // クリック時に動画を再生する
    tableView.setRowFactory(new Callback[TableView[Movie], TableRow[Movie]]() {
      override def call(param: TableView[Movie]): TableRow[Movie] = {
        val row = new TableRow[Movie]()
        row.setOnMouseClicked(new EventHandler[MouseEvent] {
          override def handle(event: MouseEvent): Unit = {
            if (event.getClickCount >= 1 && !row.isEmpty) MoviePlayer.play(row.getItem, tableView, mediaView, timeLabel)
          }
        })
        row
      }
    })

    // ファイル名カラム
    val fileNameColumn = new TableColumn[Movie, String]("ファイル名")
    fileNameColumn.setCellValueFactory(new PropertyValueFactory("fileName"))
    fileNameColumn.setPrefWidth(160)
    // 時間カラム
    val timeColumn = new TableColumn[Movie, String]("時間")
    timeColumn.setCellValueFactory(new PropertyValueFactory("time"))
    timeColumn.setPrefWidth(80)
    // 削除カラム
    val deleteActionColumn = new TableColumn[Movie, Long]("削除")
    deleteActionColumn.setCellValueFactory(new PropertyValueFactory("id"))
    deleteActionColumn.setPrefWidth(60)
    // 削除用クラス設置
    deleteActionColumn.setCellFactory(new Callback[TableColumn[Movie, Long], TableCell[Movie, Long]]() {
      override def call(param: TableColumn[Movie, Long]): TableCell[Movie, Long] = {
        new DeleteCell(movies, mediaView, tableView)
      }
    })

    // テーブルにカラムを追加する
    tableView.getColumns.setAll(fileNameColumn, timeColumn, deleteActionColumn)

    // レイアウト + シーン + ツールバー
    val baseBorderPane = new BorderPane()
    val scene = new Scene(baseBorderPane, mediaViewFitWidth + tableMinWidth, mediaViewFitHeight + toolBarMinHeight)
    val toolBar = ToolbarCreator.create(mediaView, tableView, timeLabel, scene, primaryStage)

    // レイアウト
    baseBorderPane.setStyle("-fx-background-color: Black")
    baseBorderPane.setCenter(mediaView)
    baseBorderPane.setBottom(toolBar)
    baseBorderPane.setRight(tableView)

    // シーン
    scene.setFill(Color.BLACK)
    mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
    mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))

    // シーンへのドラッグオーバーイベント
    scene.setOnDragOver(new MovieFileDragOverEventHandler(scene))

    // シーンへのドラッグドロップイベント
    scene.setOnDragDropped(new MovieFileDragDroppedEventHandler(movies))

    // ステージ
    primaryStage.setTitle("mp4ファイルをドラッグ&ドロップしてください")
    primaryStage.setScene(scene)
    primaryStage.show()
  }

}
