package com.moc.chitchat;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 */
public class Main extends Application {

     
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome to ChitChat");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene1 = new Scene(grid, 400, 280);
        Stage stage = primaryStage;
        stage.setScene(scene1);

        Text scenetitle = new Text("Login Details");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Username: "); //USERNAME
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1); //TEXT FIELD TO TAKE THE NAME

        Label pw = new Label("Password: ");
        grid.add(pw, 0, 2);

        PasswordField pwsword = new PasswordField();
        grid.add(pwsword, 1, 2);

        Button btn = new Button("Log in");
        HBox hdbox = new HBox(10);
        hdbox.setAlignment(Pos.BOTTOM_RIGHT);
        hdbox.getChildren().add(btn);
        grid.add(hdbox, 1, 4);
        Text message = new Text();

        grid.add(message,1,5);
        ///////////////////////////////////second
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(25, 25, 25, 25));
        Scene scene2 = new Scene(grid2, 400, 300);

        Text scene2title = new Text("Sign in Details");
        scene2title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid2.add(scene2title, 0, 0, 2, 1);

        Label user2Name = new Label("Username: "); //USERNAME
        grid2.add(user2Name, 0, 1);

        TextField user2TextField = new TextField();
        grid2.add(user2TextField, 1, 1); //TEXT FIELD TO TAKE THE NAME

        Label pw1 = new Label("Password: ");
        grid2.add(pw1, 0, 2);

        PasswordField pwsword1 = new PasswordField();
        grid2.add(pwsword1, 1, 2);

        Label pw2 = new Label("Re-Password: ");
        grid2.add(pw2, 0, 3);

        PasswordField pwsword2 = new PasswordField();
        grid2.add(pwsword2, 1, 3);


        Button btn2 = new Button("Sign in");
        Button btncancel = new Button("Cancel");
        HBox hdbox2 = new HBox(10);
        hdbox2.setAlignment(Pos.BOTTOM_RIGHT);
        hdbox2.getChildren().add(btncancel);
        hdbox2.getChildren().add(btn2);
        grid2.add(hdbox2, 1, 4);


        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String password = pwsword.getText();
                String username = userTextField.getText();
                if (password.length() < 8) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Password format");
                    alert.setContentText("Your password is less than 8 characters");
                    alert.show();

                } else {
                    //message.setText("Wrong Credentials");
                    ChatMain chatmain = new ChatMain();
                    chatmain.setVisible(true);
                    primaryStage.close();
                  //  stage.setScene(scene2);
                }

                System.out.println("My name is " + username + " and my password  " + password);
            }
        });

        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String passwordsgn1 = pwsword1.getText();
                String passwordsgn2 = pwsword2.getText();
                String usernamesgn = user2TextField.getText();
                String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+!=])(?=\\S+$).{8,}$";//has at least one digit, at least one lowercase, at least one upper case, at least one special character,no whitespaces,8 char length

                if (passwordsgn1.matches(pattern))//checks the pattern
                {


                    if (!passwordsgn1.contentEquals(passwordsgn2)) {
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setTitle("Passwords do not match");
                        alert1.setContentText("Type the same passwords");
                        alert1.showAndWait();
                    }


                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Wrong Credentials");
                    alert.setContentText("The password must contains at least 8 characters \n letters uper and low, numbers, special characters");
                    alert.showAndWait();
                }


                stage.setScene(scene2);
                GsonMsg gsonmsg = new GsonMsg();
                gsonmsg.setUsername(usernamesgn);
                gsonmsg.setPassword(passwordsgn1);
                Gson gson = new Gson();
                gson.toJson(gsonmsg);
                System.out.println(gson.toJson(gsonmsg));
                GsonMsg jsonmsg1 = gson.fromJson(gson.toJson(gsonmsg), GsonMsg.class);

                System.out.println("to username m einai " + jsonmsg1.getPassword());
               // try {
                    //httpconnection request = new httpconnection();
                    //request.sendPOST(gson.toJson(jsonmsg));
                //} catch (IOException e) {
                  //  e.printStackTrace();
                //}

            }
        });
        btncancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(scene1);

            }
        });
        stage.show();
    }
}
