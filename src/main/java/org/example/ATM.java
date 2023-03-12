package org.example;

public class ATM {

    private final Bank bank;
    public ATM(Bank bank){
        this.bank = bank;
    }

    public String getUserId(ATMRequest atmRequest){
        User user = bank.getUserId(atmRequest.getCard().getCardNr());
        return user.getUserId();
    }
    public String logIn(ATMRequest atmRequest){
        if (atmRequest.getCard().getPin().equals(atmRequest.getPinInput())){
            return "you are logged in";
        }

        return logInTries(atmRequest);
    }
    public String logInTries(ATMRequest atmRequest){
        int attempts = bank.logInTries(atmRequest.getCard().getCardNr());
        bank.setLoginTries(attempts+1);
        if (attempts == 3) atmRequest.getCard().setLocked();
        return switch (attempts){
            case 1 ->
                "wrong pin 2 tries left";
            case 2 ->
                 "wrong pin 1 try left";
            case 3 ->
                 "wrong pin your card is locked";


            default -> "";
        };

    }

    public String getCardStatus(ATMRequest atmRequest){
        if (bank.getCardBlockStatus(atmRequest.getCard().getCardNr())){
            return "your card is locked";
        }
        return "card is not locked";
    }

    public int getBalance(ATMRequest atmRequest){
        if (atmRequest.getCard().isLoggedIn(true)){
            return bank.getBalance(atmRequest.getCard().getCardNr());
        }
        return 0;
    }
    public void addBalance(ATMRequest atmRequest){
        bank.addBalance(atmRequest.getDeposit());
    }
    public String withDraw(ATMRequest atmRequest){
        if(getBalance(atmRequest) >= atmRequest.getWithdraw()){
            bank.withdraw(atmRequest.getWithdraw());
            return atmRequest.getWithdraw()+ "is withdrawn from your account";
        }else return "not enough money";
    }

    public String getBankName(ATMRequest atmRequest){
        return bank.getBank(atmRequest.getCard().getCardNr());
    }

}



