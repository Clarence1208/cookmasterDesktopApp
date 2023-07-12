package com.cmdpresta.cookmaster.cookmasterapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class Events {
    private List<Event> events;

    public Events(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public static Events retrieveEvents(String authToken) {
        try {
            URL url = new URL(Api.API_EVENTS);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response and create the Events object
                JSONArray jsonArray = new JSONArray(response.toString());
                List<Event> eventList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Event event = Event.fromJson(jsonObject);
                    eventList.add(event);
                }
                return new Events(eventList);
            } else {
                // Handle API error response
                System.out.println("Failed to retrieve events. Response code: " + responseCode);
            }
        } catch (IOException e) {
            // Handle any exceptions that occurred during the API request
            e.printStackTrace();
        }
        return null;
    }

    //get number of events where type is "Tasting"
    public int getNumTastings() {
        int numTastings = 0;
        for (Event event : events) {
            if (event.isTasting()) {
                numTastings++;
            }
        }
        return numTastings;
    }

    //get number of events where type is "Meeting"
    public int getNumMeetings() {
        int numMeetings = 0;
        for (Event event : events) {
            if (event.isMeeting()) {
                numMeetings++;
            }
        }
        return numMeetings;
    }

    //get number of events where type is not "Tasting" or "Meeting"
    public int getNumOther() {
        int numOther = 0;
        for (Event event : events) {
            if (!event.isTasting() && !event.isMeeting()) {
                numOther++;
            }
        }
        return numOther;
    }

    // get TOP 5 events with the most attendees order by descending
    public List<Event> getTop5Events() {
        List<Event> top5Events = new ArrayList<>();
        List<Event> eventsCopy = new ArrayList<>(events);
        for (int i = 0; i < 5; i++) {
            Event maxEvent = eventsCopy.get(0);
            for (Event event : eventsCopy) {
                if (event.getCurrentParticipants() > maxEvent.getCurrentParticipants()) {
                    maxEvent = event;
                }
            }
            top5Events.add(maxEvent);
            eventsCopy.remove(maxEvent);
        }
        return top5Events;
    }

    // return the number of events that have a completion rate of more than x%
    public int getNumEventsWithCompletionRateGreaterThan(int x) {
        int numEvents = 0;
        for (Event event : events) {
            if (event.getCompletionPercentage() > x) {
                numEvents++;
            }
        }
        return numEvents;
    }
    // return the number of events that have a completion rate of less than x%
    public int getNumEventsWithCompletionRateLessThan(int x) {
        int numEvents = 0;
        for (Event event : events) {
            if (event.getCompletionPercentage() < x) {
                numEvents++;
            }
        }
        return numEvents;
    }

    private static List<Event> parseEventsFromJson(String json) {


        // Example parsing logic using org.json library:
        List<Event> eventsList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonEvent = jsonArray.getJSONObject(i);
            Event event = Event.fromJson(jsonEvent);
            eventsList.add(event);
        }

        return eventsList;
    }
}
