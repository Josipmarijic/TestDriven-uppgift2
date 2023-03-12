package org.example;

public class Card {

    private String cardNr;
    private String userId;
    private String pin;
    private int logInAttempts;
    private boolean isLocked;
    private boolean isLoggedIn;

    public Card(String cardNr, String user, String pin) {
        this.cardNr = cardNr;
        this.userId = user;
        this.pin = pin;
        isLocked = false;
        logInAttempts = 0;
        isLoggedIn = false;
    }
    public Card(String cardNr){
        this.cardNr = cardNr;
    }

    public String getCardNr() {
        return cardNr;
    }

    public boolean isLoggedIn(boolean isLoggedIn) {
        return isLoggedIn = isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public void setCardNr(String cardNr) {
        this.cardNr = cardNr;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getLogInAttempts() {
        return logInAttempts;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLogInAttempts() {
        this.logInAttempts++;
    }

    public void setLocked() {
        isLocked = true;
    }
}


