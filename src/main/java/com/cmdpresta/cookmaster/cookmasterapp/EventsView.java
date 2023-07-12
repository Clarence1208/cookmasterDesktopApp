package com.cmdpresta.cookmaster.cookmasterapp;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EventsView {

    private Stage primaryStage;
    private Accordion eventsAccordion;
    private Scene eventsScene;
    private String authToken;

    public EventsView(Stage primaryStage, String authToken) {
        this.primaryStage = primaryStage;
        this.authToken = authToken;
        createEventsAccordion();
    }

    public void show() {
        fetchEventsData();
        primaryStage.setScene(eventsScene);
        primaryStage.setTitle("Events");
        primaryStage.show();
    }

    private void createEventsAccordion() {
        // Create UI components for events
        eventsAccordion = new Accordion();

        // Create the button to go back to the menu
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> {
            // Return to the menu view
            MenuView menuView = new MenuView(primaryStage, authToken);
            menuView.show();
        });

        // Create the top section of the events view
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(15));
        topSection.getChildren().add(backButton);

        // Create the center section of the events view
        ScrollPane scrollPane = new ScrollPane(eventsAccordion);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        // Create the main layout for the events view
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topSection);
        borderPane.setCenter(scrollPane);

        // Create the scene for events view
        eventsScene = new Scene(borderPane);
    }

    private void fetchEventsData() {
        try {
            String endpoint = Api.API_EVENTS;
            URL url = new URL(endpoint);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Process the response data
                JSONArray eventsArray = new JSONArray(response.toString());
                for (int i = 0; i < eventsArray.length(); i++) {
                    JSONObject eventObj = eventsArray.getJSONObject(i);
                    String name = eventObj.getString("name");

                    // Create the content for the event
                    Label eventLabel = new Label("Name: " + name);
                    VBox eventContent = new VBox();
                    eventContent.getChildren().add(new Label("Status: " + eventObj.optString("status")));
                    eventContent.getChildren().add(new Label("Event Type: " + eventObj.getString("eventType")));
                    eventContent.getChildren().add(new Label("Event Date: " + eventObj.getString("eventDate")));
                    eventContent.getChildren().add(new Label("Start Time: " + eventObj.getString("startTime")));
                    eventContent.getChildren().add(new Label("End Time: " + eventObj.getString("endTime")));
                    eventContent.getChildren().add(new Label("Location: " + eventObj.getString("location")));
                    eventContent.getChildren().add(new Label("Max Participants: " + eventObj.getInt("maxParticipants")));
                    eventContent.getChildren().add(new Label("Current Participants: " + eventObj.getInt("currentParticipants")));
                    eventContent.getChildren().add(new Label("Price: " + eventObj.getDouble("price")));
                    eventContent.getChildren().add(new Label("Description: " + eventObj.getString("description")));
                    eventContent.getChildren().add(new Label("Create Date: " + eventObj.getString("createdDate")));
                    JSONObject userObj = eventObj.optJSONObject("user");
                    if (userObj != null) {
                        String userLastName = userObj.optString("lastName");
                        String userFirstName = userObj.optString("firstName");
                        eventContent.getChildren().add(new Label("User: " + userLastName + " " + userFirstName));
                    }

                    // Create the titled pane for the event
                    TitledPane eventPane = new TitledPane("Name: " + name, eventContent);
                    eventPane.setAnimated(false);

                    eventsAccordion.getPanes().add(eventPane);
                }
            } else {
                System.out.println("Failed to fetch events data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
