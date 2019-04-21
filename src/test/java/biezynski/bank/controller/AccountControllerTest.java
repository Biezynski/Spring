package biezynski.bank.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import biezynski.bank.controller.api.AccountController;
import biezynski.bank.domain.Account;
import biezynski.bank.domain.AccountManager;
import biezynski.bank.domain.CreditManager;
import biezynski.bank.response.AccountDTO;
import biezynski.bank.response.ResponseDTO;
import biezynski.bank.response.ResponseMessage;
import biezynski.bank.validator.BusinessValidator;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

	AccountController accountController = new AccountController();// obsluga lewego panelu

	@Mock
	AccountManager accountManagerMock;// ten obiekt odpowiedzialny jest za operacje (deposit, withraw, delete account
	// itp)
	//sprawdza watunki wykonywane sa w accountController

	@Mock
	HttpServletResponse httpServletResponseMock; // mockoowany obiekt z biblioteki
	// javax.servlet.http.HttpServletResponse

	@Before // Before- Metoda ta zostanie uruchomiona przed każdym testem jednostkowym
	public void setup() {
		accountController.setAccountManager(accountManagerMock);// przekazuje mocka accountManagera do accountControlera
	}

	@Test
	public void createAccountAllowed() {
		Mockito.when(accountManagerMock.createAccount(Mockito.anyString())).thenReturn(true);
		//konstrukcja kiedy metoda x jest wywolana spodziewaj sie y uczymy metody jak sie maja zachowywac
		//kiedy wywolujemy metode createAccount i przekazujemy jej String spodziewamy sie true 
		//true uzyskujemy kiedy program sprawdzi czy istnieje juz ownerID jezeli jest wolny to zwraca wartosc true
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(null, new Account());
		//do metody getAccount z klasy AccountManager przekazujemy jako parametr String i spodziewamy sie
		//ze zwroci nam konkretne konto albo wartosc null
		ResponseDTO<AccountDTO> response = accountController.createAccount("12345678910", httpServletResponseMock);
		//btworzy konto o numerze 12345678910 dodaje do mapy kont
		Assert.assertEquals(ResponseMessage.SUCCESS.getCode(), response.getCode());
		//assertEquals to konstrukcja w ktorym jako pierwszy parametr spodziewamy sie wartosci oczekiwanej 
		//a drugim parametrem prosimy program o faktyczna odpowiedz wynikajacej z kodu naszego programu

		//Z klasy ResponseMessage pobieramy komunikat z nazwa "SUCCESS" i porownujemy czy response.getCode() zwroci nam
		//wlasnie taka wiadomosc
	}

	@Test
	public void createAccountNotAllowed() {
		Mockito.when(accountManagerMock.createAccount(Mockito.anyString())).thenReturn(false);
		//analogiczna sytuacja do poprzedniego testu natomiast teraz gdy chcemy stworzyc nowe konto
		//porownujemy ownerID z numerami juz instniejacymi i jezeli istnieje to spodziewamy sie false
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(null);
		//do metody getAccount z klasy AccountManager przekazujemy jako parametr dowolny String i spodziewamy sie
		//ze zwroci wartosc null
		ResponseDTO<AccountDTO> response = accountController.createAccount("12345678910", httpServletResponseMock);
		//bierze konto o numerze 12345678910 i zmokowanej odpowiedzi servleta
		Assert.assertEquals(ResponseMessage.OPERATION_ERROR.getCode(), response.getCode());
		//assertEquals to konstrukcja w ktorym jako pierwszy parametr spodziewamy sie wartosci oczekiwanej 
		//a drugim parametrem prosimy program o faktyczna odpowiedz wynikajacej z kodu naszego programu

		//Z klasy ResponseMessage pobieramy komunikat z nazwa "OPERATION_ERROR" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void createAccountWithExistAccount() {
		Mockito.when(accountManagerMock.getAccount("12345678910")).thenReturn(new Account("12345678910"));
		//kiedy wywolujemy metode getAccount zwraca nam nowe konto 
		ResponseDTO<AccountDTO> response = accountController.createAccount("12345678910", httpServletResponseMock);
		//bierze konto o numerze 12345678910 i zmokowanej odpowiedzi servleta
		Assert.assertEquals(ResponseMessage.ACCOUNT_ALREADY_EXISTS.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "ACCOUNT_ALREADY_EXISTS" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc

	}

	@Test
	public void createAccountWithWrongID() {
		ResponseDTO<AccountDTO> response = accountController.createAccount("1234567890", httpServletResponseMock);
		//bierze konto o numerze 1234567890 numer jest za krótki powinien miec 11 cyfr 
		Assert.assertEquals(ResponseMessage.INVALID_ID.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "INVALID_ID" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void getAccountFound() {
		Mockito.when(accountManagerMock.createAccount(Mockito.anyString())).thenReturn(true);
		//kiedy wywolujemy metode createAccount i przekazujemy jej String spodziewamy sie true 
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(new Account());
		//kiedy wywolujemy metode getAccount spodziewaj sie ze zwroci nam obiekt Konto (new Account)
		ResponseDTO<AccountDTO> response = accountController.getAccount("12345678910", httpServletResponseMock);
		//bierze konto o numerze 12345678910 i zmokowanej odpowiedzi servleta
		Assert.assertEquals(ResponseMessage.SUCCESS.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "SUCCESS" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void getAccountNotFound() {
		Mockito.when(accountManagerMock.createAccount(Mockito.anyString())).thenReturn(true);
		//kiedy wywolujemy metode createAccount i przekazujemy jej String metoda ma zwrocic true 
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(null);
		//do metody getAccount z klasy AccountManager przekazujemy jako parametr dowolny String i spodziewamy sie
		//ze zwroci wartosc null
		ResponseDTO<AccountDTO> response = accountController.getAccount("12345678910", httpServletResponseMock);
		//bierze konto o numerze 12345678910 niestety metoda getAccount zwraca null powinien pojawic sie blad
		Assert.assertEquals(ResponseMessage.ACCOUNT_NOT_FOUND.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "ACCOUNT_NOT_FOUND" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void getAccountWithWrongID() {
		ResponseDTO<AccountDTO> response = accountController.getAccount("1234567891", httpServletResponseMock);
		//tworzy konto o numerze 12345678910 i zmokowanej odpowiedzi servleta
		Assert.assertEquals(ResponseMessage.INVALID_ID.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "INVALID_ID" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeDepositAllowed() {
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(new Account());
		//kiedy wywolujemy metode getAccount spodziewaj sie ze zwroci nam obiekt Konto (new Account) 
		ResponseDTO<AccountDTO> response = accountController.makeDeposit("12345678910", "5", httpServletResponseMock);
		//@PathVariable String ownerID, @RequestParam String value, HttpServletResponse response
		//bierze konto o numerze 12345678910 i wplaca wartosc 5 ostatniom parametrem jest zmockowana odpowiedz servera
		Assert.assertEquals(ResponseMessage.SUCCESS.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "SUCCESS" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeDepositWrongAmount() {
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(new Account());
		//kiedy wywolujemy metode getAccount spodziewaj sie ze zwroci nam obiekt Konto (new Account) czyli znajdzie w 
		//mapie kont
		ResponseDTO<AccountDTO> response = accountController.makeDeposit("12345678910", "0", httpServletResponseMock);
		//bierze konto o numerze 12345678910 i wplaca wartosc 0 ostatniom parametrem jest zmockowana odpowiedz servera
		//nie mozemy wplacic wartosci 0, to nielogiczne bo nic nie wplacamy powinien pojawic sie blad
		Assert.assertEquals(ResponseMessage.INVALID_AMMOUNT.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "INVALID_AMMOUNT" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeDepositWrongAccount() {
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(null);
		//jezeli nie znajdzie konta w mapie(liscie) kont to zwraca null
		ResponseDTO<AccountDTO> response = accountController.makeDeposit("12345678910", "10", httpServletResponseMock);
		//bierze konto o numerze 12345678910 i wplaca wartosc 10 ostatniom parametrem jest zmockowana odpowiedz servera
		Assert.assertEquals(ResponseMessage.ACCOUNT_NOT_FOUND.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "ACCOUNT_NOT_FOUND" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeDepositWrongID() {
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(null);
		//jezeli nie znajdzie konta w "bazie" kont to zwraca null
		ResponseDTO<AccountDTO> response = accountController.makeDeposit("1234567890", "10", httpServletResponseMock);
		//bierze konto o numerze 1234567890 (numer konta zawiera 10 cyfr a powinien 11 dlatego nie znajdzie go) 
		//i wplaca wartosc 0 ostatniom parametrem jest zmocowana odpowiedz servera
		Assert.assertEquals(ResponseMessage.INVALID_ID.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "INVALID_ID" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeWithdrawAllowed() {
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(new Account());
		//mockujemy metode getAccount tak aby widziala konto w liscie (mapie) kont
		accountController.makeDeposit("12345678910", "15", httpServletResponseMock);
		//bierze konto o numerze 12345678910 i wplaca wartosc 15 ostatniom parametrem jest zmocowana odpowiedz servera
		ResponseDTO<AccountDTO> response = accountController.makeWithdraw("12345678910", "5", httpServletResponseMock);
		//Pierwszym parametrem wprowadzonym do metody makeWidraw jest poprawny numer konta nastepnie jest wyplacana z
		//tego konta wartosc 5 nalezy pamietaj ze wczesnije temu kontu zmockowalismy saldo na 15 wiec moze wyplacic 5 
		//ostatnim parametrem jest zmockowana odpowiedz servera
		Assert.assertEquals(ResponseMessage.SUCCESS.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "SUCCESS" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeWithdrawWrongAmount() {
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(new Account());
		//mockujemy metode getAccount tak aby widziala konto w bazie kont
		ResponseDTO<AccountDTO> response = accountController.makeWithdraw("12345678910", "0", httpServletResponseMock);
		//Pierwszym parametrem podanym do metody makeWithdraw jest poprawny numer istniejacego konta, drugim parametrem
		// jest 0 czy chcemy wyplacic 0 jest to operacja nielogiczna i powinien wyswietlic sie komunikat o bledzie
		//ostatnim parametrem podanym metodzie jest zmockowana odpowiedz servleta
		Assert.assertEquals(ResponseMessage.INVALID_AMMOUNT.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "INVALID_AMMOUNT" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeWithdrawWrongAccount() {
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(null);
		//jezeli nie znajdzie konta w "bazie" kont to zwraca null
		ResponseDTO<AccountDTO> response = accountController.makeWithdraw("12345678910", "10", httpServletResponseMock);
		//pobiera wartosc 0 z konta nieistniejacego o numerze 12345678910, trzecim parametrem jak wszedzie
		//jest zmockowana odpowiedz servleta
		Assert.assertEquals(ResponseMessage.ACCOUNT_NOT_FOUND.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "ACCOUNT_NOT_FOUND" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeWithdrawWrongID() {
		Mockito.when(accountManagerMock.getAccount(Mockito.anyString())).thenReturn(null);
		//jezeli nie znajdzie konta w "bazie" kont to zwraca null
		ResponseDTO<AccountDTO> response = accountController.makeWithdraw("1234567890", "10", httpServletResponseMock);
		//pierwszym parametrem jest konto o za krotkim numerze ID drugim jest kwota wyplacana trzecim jest zmokowana
		//odpowiedz servleta
		Assert.assertEquals(ResponseMessage.INVALID_ID.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "INVALID_ID" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeTransferAllowed() {
		Mockito.when(accountManagerMock.getAccount("12345678910")).thenReturn(new Account());
		Mockito.when(accountManagerMock.getAccount("10987654321")).thenReturn(new Account());
		//mockujemy metode getAccount tak aby widziala konta w liscie(mapie) kont
		//tworzone sa dwa konta o numerach 12345678910 oraz 10987654321
		accountController.makeDeposit("12345678910", "500", httpServletResponseMock);
		//wplacamy na konto o numerze 12345678910 wartosc 500 trzecim parametrem jest zmockowana odpowiedz servleta
		ResponseDTO<AccountDTO> response = accountController.makeTransfer("12345678910", "10987654321", "50",
				httpServletResponseMock);
		//pierwszym parametrem jest numer ID konta z ktorego chcemy przelac pieniadze, drugim parametrem
		//jest konto na ktore chcemy przelac pieniadze, trzecim parametrem jest wartosc 50 czwartym jest zmockowana 
		//odpowiedz servleta 
		Assert.assertEquals(ResponseMessage.SUCCESS.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "SUCCESS" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeTrasnferWrongAmount() {
		Mockito.when(accountManagerMock.getAccount("12345678910")).thenReturn(new Account());
		Mockito.when(accountManagerMock.getAccount("10987654321")).thenReturn(new Account());
		//mockujemy metode getAccount tak aby widziala konta w liscie(mapie) kont
		//tworzone sa dwa konta o numerach 12345678910 oraz 10987654321
		accountController.makeDeposit("12345678910", "500", httpServletResponseMock);
		//wplacamy na konto o numerze 12345678910 wartosc 500 trzecim parametrem jest zmockowana odpowiedz servleta
		ResponseDTO<AccountDTO> response = accountController.makeTransfer("12345678910", "10987654321", "0",
				httpServletResponseMock);
		//pierwszym parametrem jest numer ID konta z ktorego chcemy dokonac przelewu drugim parametrem jest konto
		//na ktore bedziemy przelewac pieniadze, trzecim jest kwota ale poniewaz chcemy przelac 0 to jest 
		//parametr niepoprawny i powinno to zostac zakomunikowane przez odpowiednia wiadomosc, czwartym parametrem
		// jest zmockowana odpowiedz servleta
		Assert.assertEquals(ResponseMessage.INVALID_AMMOUNT.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "INVALID_AMMOUNT" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc
	}

	@Test
	public void makeTrasnferWrongAccount() {
		Mockito.when(accountManagerMock.getAccount("12345678910")).thenReturn(null);
		Mockito.when(accountManagerMock.getAccount("10987654321")).thenReturn(new Account());
		//pierwszego konta nie ma w bazie (mapie, liscie) kont dlatego metoda getAccount zwraca null, drugie konto
		//zostalo znalezione
		accountController.makeDeposit("12345678910", "500", httpServletResponseMock);
		//na konto 12345678910 wplacane sa srodki w wysokosci 500 trzecim parametrem jest zmockowana odpowiedz servleta
		ResponseDTO<AccountDTO> response = accountController.makeTransfer("12345678910", "10987654321", "500",
				httpServletResponseMock);
		//pierwszym parametrem jest numer nieistniejace konto z ktorego probujemy przelac 500 na konto 
		//10987654321 ktore istnieje w naszej mapie kont, oczywiscie powinno zostac zakomunikowanie ze nie istnieje 
		//konto
		Assert.assertEquals(ResponseMessage.ACCOUNT_NOT_FOUND.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "ACCOUNT_NOT_FOUND" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc }
	}

	@Test
	public void makeTrasnferWrongID() {
		Mockito.when(accountManagerMock.getAccount("123456710")).thenReturn(new Account());
		Mockito.when(accountManagerMock.getAccount("109876541")).thenReturn(new Account());
		//mockujemy metode getAccount tak aby widziala konta w liscie(mapie) kont
		accountController.makeDeposit("123456710", "500", httpServletResponseMock);
		//na istniejace poprawne konto 123456710 wplacane sa srodki w wysokosci 500 
		ResponseDTO<AccountDTO> response = accountController.makeTransfer("12345678910", "109876541", "500",
				httpServletResponseMock);
		//prubujemy przelac 500 z nieistniejacego konta 
		Assert.assertEquals(ResponseMessage.INVALID_ID.getCode(), response.getCode());
		//sprawdzane jest czy wyswietlany jest blad
	}


	@Test
	public void deleteAccountAllowed() {
		Mockito.when(accountManagerMock.getAccount("12345678910")).thenReturn(new Account());
		//zostaje stworzona w mocku nowe konto
		Mockito.when(accountManagerMock.deleteAccount(Mockito.anyString())).thenReturn(true);
		//zmockowany obiekt uruchamia skasowanie konta, ktore konczy sie powodzeniem
		ResponseDTO<AccountDTO> response = accountController.deleteAccount("12345678910", httpServletResponseMock);
		//konto o numerze ID 12345678910 ktore istnieje w mapie(liscie) kont zostaje usuniete
		Assert.assertEquals(ResponseMessage.SUCCESS.getCode(), response.getCode());
		//Z klasy ResponseMessage pobieramy komunikat z nazwa "SUCCESS" i porownujemy czy response.getCode() 
		//zwroci nam wlasnie taka wiadomosc, zwroci nam taka wiadomosc poniewaz konto zostalo z powodzeniem usuniete
	}

	@Test
	public void deleteAccountNotAllowed() {
		Mockito.when(accountManagerMock.getAccount("12345678910")).thenReturn(new Account());
		//zostaje stworzona w mocku nowe konto o numerze 
		Mockito.when(accountManagerMock.deleteAccount(Mockito.anyString())).thenReturn(false);
		//próba skasowania konta nie powiodla sie, w momencie skasowania konta ma zostac zwrocone false
		ResponseDTO<AccountDTO> response = accountController.deleteAccount("12345678910", httpServletResponseMock);
		Assert.assertEquals(ResponseMessage.OPERATION_ERROR.getCode(), response.getCode());
	}

	@Test
	public void deleteAccountWrongUser() {
		Mockito.when(accountManagerMock.getAccount("12345678910")).thenReturn(null);
		//proba stworzenia konta ale zakonczona niepowodzeniem w efekcie nie ma konta o id 12345678910
		ResponseDTO<AccountDTO> response = accountController.deleteAccount("12345678910", httpServletResponseMock);
		//próba usuniecia konta o numerze 12345678910 ale nie ma takiego konta o podanym numerze a co za tym idzie nie mozna go usunac
		Assert.assertEquals(ResponseMessage.ACCOUNT_NOT_FOUND.getCode(), response.getCode());
		//Oczekujemy ze pojawi sie komunikat ACCOUNT_NOT_FOUND a potem patrzymy czy rzeczywiscie otrzymujemy taki komunikat wskutek dzialania programu
	}

	@Test
	public void deleteAccountWrongID() {
		ResponseDTO<AccountDTO> response = accountController.deleteAccount("1234567891", httpServletResponseMock);
		//if (BusinessValidator.isIDValid(ownerID)) zwrocil wartosc false dlatego spodziewamy sie wyswietlenia komunikatu INVALID_ID
		Assert.assertEquals(ResponseMessage.INVALID_ID.getCode(), response.getCode());
	}

	@Test
	public void deleteAllAccount() {
		ResponseDTO<AccountDTO> response = accountController.deleteAllAccounts(httpServletResponseMock);
		//w momencie wywolania metody deleteallaccounts z Controlera wyswietla sie komunikat o success
		Assert.assertEquals(ResponseMessage.SUCCESS.getCode(), response.getCode());
	}

	/*
	 * @Test public void getAllAccounts(){ List<AccountDTO> response =
	 * accountController.getAllAccounts(); //
	 * Assert.assertEquals(ResponseMessage.SUCCESS.getCode(), response.getCode()); }
	 */

}