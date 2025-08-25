package com.saveetha.fitnesschallenge.service;

public class LoginResponse {
    private String status;
    private String message;
    private User user;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public static class User {
        private int id;
        private String full_name;  // match your JSON key
        private String email;
        private String role;       // add this field

        public int getId() {
            return id;
        }

        public String getName() {
            return full_name;  // match your JSON key
        }

        public String getEmail() {
            return email;
        }
        public String getRole() {
            return role;
        }
    }
}
