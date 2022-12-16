package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
 @FXML
    AnchorPane AnchorPain;
 @FXML
 public TextField Text_F_Message;
 @FXML
 public    Button Btn_Send;
 @FXML
 public   ScrollPane Scrol_Pain_Message;
 @FXML
 public VBox Vbox_Message;
 @FXML
 public Label Lable_Title;
 @FXML
 ScrollPane Scroll_Pain_Chat;
 @FXML
 VBox Vbox_Chat;
 @FXML
 Button Btn_Add_Chat;
@FXML

 public Server server ;

      @Override
    public void initialize(URL location , ResourceBundle resources) {
              server = new Server (3000);
          System.out.println("Wating for connection ..");
          server.connect();

 Vbox_Message.heightProperty().addListener(new ChangeListener<Number>() {
     @Override
     public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
         Scrol_Pain_Message.setVvalue((Double) newValue);
     }
 });

 server.receiveMessage(Vbox_Message);
 Btn_Send.setOnAction(new EventHandler<ActionEvent>() {
     @Override
     public void handle(ActionEvent event) {
         String MessageToSend = Text_F_Message.getText();
         if (!MessageToSend.isEmpty()){
             HBox hBox = new HBox();
             hBox.setAlignment(Pos.CENTER_RIGHT);
             hBox.setPadding(new Insets(5,5,5,10));
             Text text = new Text(MessageToSend);
             TextFlow textFlow = new TextFlow(text);
             textFlow.setStyle("-fx-color:#FFFFFF;"+
                     "-fx-background-color:#4e4e4e;"+
                     "-fx-font:20 arial ;" +
                     "-fx-background-radius : 20px; ");
             textFlow.setPadding(new Insets(5,10,5,10));
             text.setFill(Color.color(0.934,0.945,0.996));
             hBox.getChildren().add(textFlow);
             Vbox_Message.getChildren().add(hBox);

             server.SendMessage(MessageToSend);
             Text_F_Message.clear();

         }
     }
 });

    }
    public static void add_Text_To_Chat(String messageFromClient , VBox vbox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color : rgb(233,233,235);" +
                "-fx-font:20 arial ;"+
                "-fx-background-radius : 20px; ");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() { //Thread TO Add New Message To Chat
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
            }
        });
    }
}
