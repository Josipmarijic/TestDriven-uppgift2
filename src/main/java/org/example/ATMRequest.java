package org.example;

import org.example.Card;

public class ATMRequest {
    private final Card card;
    private String pinInput;
    private int withdraw;
    private int deposit;
    private int balance;

    public ATMRequest(Card card) {
        this.card = card;
    }

    public ATMRequest(Card card, String pinInput) {
        this.card = card;
        this.pinInput = pinInput;
    }

    public ATMRequest(Card card, int amount) {
        this.card = card;
        this.balance = amount;
    }

    public ATMRequest(Card card, String pinInput, int withdraw, int deposit) {
        this.card = card;
        this.pinInput = pinInput;
        this.withdraw = withdraw;
        this.deposit = deposit;
    }

    public Card getCard() {
        return card;
    }



    public int getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(int withdraw) {
        this.withdraw = withdraw;
        balance -= withdraw;

    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getDeposit() {
        return deposit;
    }

    public int setDeposit(int deposit) {
        this.deposit = deposit;
        balance += deposit;
        return deposit;
    }

    public String getPinInput() {
        return pinInput;
    }

    public void setPinInput(String pinInput) {
        this.pinInput = pinInput;
    }
}