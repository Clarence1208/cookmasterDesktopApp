package com.cmdpresta.cookmaster.cookmasterapp;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Event {
    private String id;
    private String name;
    private String status;
    private String eventType;
    private LocalDateTime eventDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private int maxParticipants;
    private int currentParticipants;
    private double price;
    private String description;
    private String imageName;
    private String imageContentType;
    private LocalDateTime createdDate;
    private User user;
    private String comments;
    private Object materialReservations;

    // Constructors, getters, and setters

    // Constructor for creating an empty Event object
    public Event() {
    }

    // Constructor for creating an Event object with required fields
    public Event(String id, String name, String status, String eventType, LocalDateTime eventDate, LocalDateTime startTime, LocalDateTime endTime, String location, int maxParticipants, int currentParticipants, double price, String description, String imageName, String imageContentType, LocalDateTime createdDate, User user, String comments, Object materialReservations) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.price = price;
        this.description = description;
        this.imageName = imageName;
        this.imageContentType = imageContentType;
        this.createdDate = createdDate;
        this.user = user;
        this.comments = comments;
        this.materialReservations = materialReservations;
    }

    public static Event fromJson(JSONObject jsonObject) {
        Event event = new Event();
        event.setId(jsonObject.getString("id"));
        event.setName(jsonObject.getString("name"));
        event.setStatus(jsonObject.optString("status"));
        event.setEventType(jsonObject.getString("eventType"));
        event.setEventDate(parseLocalDateTime(jsonObject.getString("eventDate")));
        event.setStartTime(parseLocalDateTime(jsonObject.getString("startTime")));
        event.setEndTime(parseLocalDateTime(jsonObject.getString("endTime")));
        event.setLocation(jsonObject.getString("location"));
        event.setMaxParticipants(jsonObject.getInt("maxParticipants"));
        event.setCurrentParticipants(jsonObject.getInt("currentParticipants"));
        event.setPrice(jsonObject.getDouble("price"));
        event.setDescription(jsonObject.getString("description"));
        event.setImageName(jsonObject.optString("imageName"));
        event.setImageContentType(jsonObject.optString("imageContentType"));
        event.setCreatedDate(parseLocalDateTime(jsonObject.getString("createdDate")));
        event.setUser(User.fromJson(jsonObject.optJSONObject("user")));
        return event;
    }

    private static LocalDateTime parseLocalDateTime(String dateString) {
        try {
            if (dateString.endsWith("Z")) {
                dateString = dateString.substring(0, dateString.length() - 1);
            }else{
                dateString = dateString.substring(0, dateString.length() - 6);
            }
            // remove the T between date and time
            dateString = dateString.replace("T", " ");
            // parse the date string of this format 2023-05-16 06:31:04
            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Failed to parse LocalDateTime from input: " + dateString, dateString, 0);
        }
    }

    // get completion percentage
    public int getCompletionPercentage(){
        return (int) (this.currentParticipants * 100 / this.maxParticipants);
    }



    public boolean isTasting(){
        return this.eventType.equals("tasting");
    }

    public boolean isMeeting(){
        return this.eventType.equals("meeting");
    }








    // Define other constructors, getters, and setters as needed


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Object getMaterialReservations() {
        return materialReservations;
    }

    public void setMaterialReservations(Object materialReservations) {
        this.materialReservations = materialReservations;
    }

    // Inner class representing the User associated with the event
    public static class User {
        private String createdBy;
        private LocalDateTime createdDate;
        private String lastModifiedBy;
        private LocalDateTime lastModifiedDate;
        private String id;
        private String login;
        private String firstName;
        private String lastName;
        private String email;
        private boolean activated;
        private String langKey;
        private Object imageUrl;

        // Constructors, getters, and setters


        // Constructor for creating an empty User object
        public User() {
        }

        // Constructor for creating a User object with required fields
        public User(String createdBy, LocalDateTime createdDate, String lastModifiedBy, LocalDateTime lastModifiedDate, String id, String login, String firstName, String lastName, String email, boolean activated, String langKey, Object imageUrl) {
            this.createdBy = createdBy;
            this.createdDate = createdDate;
            this.lastModifiedBy = lastModifiedBy;
            this.lastModifiedDate = lastModifiedDate;
            this.id = id;
            this.login = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.activated = activated;
            this.langKey = langKey;
            this.imageUrl = imageUrl;
        }

        public static User fromJson(JSONObject jsonObject) {
            if (jsonObject == null) {
                return null;
            }

            User user = new User();
            user.setId(jsonObject.getString("id"));
            user.setCreatedBy(jsonObject.optString("createdBy"));
            user.setCreatedDate(parseLocalDateTime(jsonObject.getString("createdDate")));
            user.setLastModifiedBy(jsonObject.optString("lastModifiedBy"));
            user.setLastModifiedDate(parseLocalDateTime(jsonObject.getString("lastModifiedDate")));
            user.setLogin(jsonObject.getString("login"));
            user.setFirstName(jsonObject.getString("firstName"));
            user.setLastName(jsonObject.getString("lastName"));
            user.setEmail(jsonObject.getString("email"));
            user.setActivated(jsonObject.getBoolean("activated"));
            user.setLangKey(jsonObject.optString("langKey"));
            user.setImageUrl(jsonObject.optString("imageUrl"));

            return user;
        }

        // Define other constructors, getters, and setters as needed


        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public LocalDateTime getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public LocalDateTime getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isActivated() {
            return activated;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }

        public String getLangKey() {
            return langKey;
        }

        public void setLangKey(String langKey) {
            this.langKey = langKey;
        }

        public Object getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(Object imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
