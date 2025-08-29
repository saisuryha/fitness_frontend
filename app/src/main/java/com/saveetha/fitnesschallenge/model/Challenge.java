package com.saveetha.fitnesschallenge.model;

public class Challenge {
    // These fields match the data in your list item
    private String userName;
    private String challengeTitle;
    private String challengeGoal;
    private String duration;
    // You can use int for drawable resources like R.drawable.ic_profile
    private int userAvatarResId;
    private int challengeIconResId;

    // Constructor
    public Challenge(String userName, String challengeTitle, String challengeGoal, String duration, int userAvatarResId, int challengeIconResId) {
        this.userName = userName;
        this.challengeTitle = challengeTitle;
        this.challengeGoal = challengeGoal;
        this.duration = duration;
        this.userAvatarResId = userAvatarResId;
        this.challengeIconResId = challengeIconResId;
    }

    // Getters for all fields
    public String getUserName() { return userName; }
    public String getChallengeTitle() { return challengeTitle; }
    public String getChallengeGoal() { return challengeGoal; }
    public String getDuration() { return duration; }
    public int getUserAvatarResId() { return userAvatarResId; }
    public int getChallengeIconResId() { return challengeIconResId; }
}
