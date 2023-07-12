package com.cmdpresta.cookmaster.cookmasterapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;

public class LoginPage extends Application {

    private Stage primaryStage;
    private VBox loginLayout;
    private Scene loginScene;
    private MenuView menuView;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login Page");

        primaryStage.setWidth(1080);
        primaryStage.setHeight(720);

        createLoginLayout();

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void createLoginLayout() {
        // Create UI components
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        //log fail message
        Rectangle messageBox = new Rectangle(200, 20, Color.RED);
        Label messageLabel = new Label("");
        messageLabel.setTextFill(Color.WHITE);


        // StackPane to stack the label on top of the message box
        StackPane messageStackPane = new StackPane(messageBox, messageLabel);
        messageStackPane.setVisible(false);

        // Set up the layout
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, messageStackPane);

        // Set up the login button action
        loginButton.setOnAction(e -> {
            if (usernameField.getText().isEmpty()){
                messageLabel.setText("You must enter a username");
                messageStackPane.setVisible(true);
                return;
            }
            if (passwordField.getText().isEmpty()){
                messageLabel.setText("You must enter a password");
                messageStackPane.setVisible(true);
                return;
            }
            String username = usernameField.getText();
            String password = passwordField.getText();
            String token = authenticate(username, password);

            if (token != null) {
                System.out.println("Login successful \n" +
                        "Token: " + token);
            } else {
                messageLabel.setText("Username or password is incorrect");
                System.out.println("Login failed");
                messageStackPane.setVisible(true);
                return;
            }
            try {
                if (isUserAdmin(token)) {
                    System.out.println("User is admin");
                    openMenuView(token);
                } else {
                    System.out.println("User is not admin");
                    messageLabel.setText("!!!! User is not admin !!!! ");
                    messageStackPane.setVisible(true);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Create the scene for login layout
        loginLayout = vbox;
        loginScene = new Scene(loginLayout);
    }

    private String authenticate(String username, String password) {
        try {
            URL url = new URL(Api.LOGIN);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set the request parameters
            String grantType = URLEncoder.encode("password", "UTF-8");
            String clientId = URLEncoder.encode("web_app", "UTF-8");
            String encodedUsername = URLEncoder.encode(username, "UTF-8");
            String encodedPassword = URLEncoder.encode(password, "UTF-8");
            String parameters = "grant_type=" + grantType + "&client_id=" + clientId +
                    "&username=" + encodedUsername + "&password=" + encodedPassword;

            // Write the request parameters to the connection output stream
            connection.getOutputStream().write(parameters.getBytes());

            // Get the response from the connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response to extract the authentication token
            String jsonResponse = response.toString();
            // Extract the "access_token" value from the JSON response
            String accessToken = jsonResponse.substring(jsonResponse.indexOf(":") + 2, jsonResponse.indexOf(",") - 1);
            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isUserAdmin(String token) throws IOException {
        URL url = new URL(Api.API_PROFIL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray authorities = jsonObject.getJSONArray("authorities");

            for (int i = 0; i < authorities.length(); i++) {
                if (authorities.getString(i).equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void openMenuView(String token) {
        menuView = new MenuView(primaryStage, token);
        menuView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
