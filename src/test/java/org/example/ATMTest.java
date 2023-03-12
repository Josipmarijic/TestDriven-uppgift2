package org.example;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ATMTest {

    private Bank bank;
    private ATM atm;
    private ArgumentCaptor<Integer> addToBalanceArgumentCaptor,withdrawBalanceArgumentCaptor;

    @BeforeEach
    public void setUp() {
        bank = mock(Bank.class);
        atm = new ATM(bank);

        addToBalanceArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        withdrawBalanceArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
    }

    private static List<Arguments> testUsers(){
        return Arrays.asList(
//                Arguments.of(card number,user name,card pin code),
                Arguments.of("1234 5678 9012 3456","Johan Nilsson","1234"),
                Arguments.of("9012 3456 1234 5678","Sven Lundin", "2345"),
                Arguments.of("5678 9012 3456 1234","Sara Bengtsson", "4534"),
                Arguments.of("9012 1234 5678 3456","Bodil Bundin", "5667")
        );
    }

    @ParameterizedTest
    @MethodSource("testUsers")
    public void getUserFromBankByCheckingCardNumber(String cardNumber, String userName){
        ATMRequest atmRequest = new ATMRequest(new Card(cardNumber,"",""));
        when(bank.getUserId(cardNumber)).thenReturn(new User(userName));

        String expected = userName;
        String actual = atm.getUserId(atmRequest);

        assertEquals(expected,actual);
    }

    @ParameterizedTest
    @MethodSource("testUsers")
    public void loginUser(String cardNumber, String userId, String PIN){
        String cardPINATMinput = PIN;
        Card card = new Card(cardNumber,userId,PIN);
        ATMRequest atmRequest = new ATMRequest(card,cardPINATMinput);

        assertEquals("you are logged in", atm.logIn(atmRequest));
    }

    @Test
    public void loginWithWrongPasswordShouldReturnFalse(){
        Card card = new Card("12345678","josip","1234");
        String wrongPinInput = "1234";
        ATMRequest atmRequest = new ATMRequest(card,wrongPinInput);

        assertNotEquals("you are logged in", atm.logIn(atmRequest));
    }

    @Test
    public void loginOnceWithWrongPasswordShouldIncreaseFaultCounter(){
        Card card = new Card("12345678","josip","1234");
        String wrongCardPINATMinput = "1010";
        ATMRequest atmRequest = new ATMRequest(card,wrongCardPINATMinput);
        when(bank.logInTries("12345678")).thenReturn(1);

        String expected = "Wrong password, you have 2 more tries";
        String actual = atm.logIn(atmRequest);

        assertEquals(expected,actual);
    }

    @Test
    public void loginTwiceWithWrongPasswordShouldIncreaseFaultCounter(){
        Card card = new Card("12345678","josip","1234");
        String wrongCardPINATMinput = "1010";
        ATMRequest atmRequest = new ATMRequest(card,wrongCardPINATMinput);
        when(bank.logInTries("12345678")).thenReturn(2);

        String expected = "Wrong password, you have 1 more try";
        String actual = atm.logIn(atmRequest);

        assertEquals(expected,actual);
    }

    @Test
    public void loginThriceWithWrongPasswordShouldIncreaseFaultCounter(){
        Card card = new Card("12345678","josip","1234");
        String wrongCardPINATMinput = "1010";
        ATMRequest atmRequest = new ATMRequest(card,wrongCardPINATMinput);
        when(bank.logInTries("12345678")).thenReturn(3);

        String expected = "Wrong password 3 times, card is blocked";
        String actual = atm.logIn(atmRequest);

        assertEquals(expected,actual);
    }

    @Test
    public void checkIfCardIsBlockedWhenInserted(){
        Card card = new Card("12345678");

        ATMRequest atmRequest = new ATMRequest(card);
        when(bank.getCardBlockStatus("12345678")).thenReturn(true);

        String expected = "Card is blocked, you cannot login";
        String actual = atm.getCardStatus(atmRequest);

        assertEquals(expected,actual);
    }

    @Test
    public void checkIfCardIsNotBlockedWhenInserted(){
        Card card = new Card("12345678");

        ATMRequest atmRequest = new ATMRequest(card);
        when(bank.getCardBlockStatus("12345678")).thenReturn(false);

        String expected = "Card is not blocked, login allowed";
        String actual = atm.getCardStatus(atmRequest);

        assertEquals(expected,actual);
    }

    @Test
    public void getBalanceWhenCardIsLoggedIn(){
        Card card = new Card("12345678","josip","1234");
        card.isLoggedIn(true);
        ATMRequest atmRequest = new ATMRequest(card,"1234");

        int expected = 3000;
        when(bank.getBalance("12345678")).thenReturn(expected);

        int actual = atm.getBalance(atmRequest);

        assertEquals(expected,actual);
    }

    @Test
    public void addMoneyToAccountAndVerifyMethodIsRun(){
        Card card = new Card("12345678","josip","1234");
        int amountToAddToBalance = 500;
        ATMRequest atmRequest = new ATMRequest(card,"1234",amountToAddToBalance,0);

        atm.addBalance(atmRequest);

        verify(bank,times(1)).addBalance(addToBalanceArgumentCaptor.capture());

        assertEquals(Optional.of(amountToAddToBalance),addToBalanceArgumentCaptor.getValue());
    }

    @Test
    public void withdrawMoneyFromAccountAndVerifyMethodIsRun(){
        Card card = new Card("12345678","josip","1234");
        card.isLoggedIn(true);

        int amountToWithdrawFromBalance = 500;
        ATMRequest atMrequest = new ATMRequest(card,"1234",0,amountToWithdrawFromBalance);

        when(bank.getBalance("12345678")).thenReturn(3000);
        atm.withDraw(atMrequest);

        verify(bank,times(1)).withdraw(withdrawBalanceArgumentCaptor.capture());

        assertEquals(Optional.of(amountToWithdrawFromBalance),withdrawBalanceArgumentCaptor.getValue());
    }

    @Test
    public void withdrawTooMuchMoneyFromAccountAndGetCorrectFeedback(){
        Card card = new Card("12345678","josip","1234");
        card.isLoggedIn(true);

        int amountToWithdrawFromBalance = 5000;
        ATMRequest atMrequest = new ATMRequest(card,"1234",0,amountToWithdrawFromBalance);

        when(bank.getBalance("12345678")).thenReturn(3000);


        assertEquals("Not enough money",atm.withDraw(atMrequest));
    }



    @Test
    public void checkBankName() {
        try (MockedStatic<Bank> bankMockedStatic = mockStatic(Bank.class)) {
            Card card = new Card("12345678");
            ATMRequest atmRequest = new ATMRequest(card);

            bankMockedStatic.when(() -> bank.getBank(anyString())).thenReturn("Swedbank");

            String actual = atm.getBankName(atmRequest);

            assertEquals("Swedbank", actual);
        }


    }

}