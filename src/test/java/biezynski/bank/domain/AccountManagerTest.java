package biezynski.bank.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import biezynski.bank.domain.Account;
import biezynski.bank.domain.AccountManager;
import biezynski.bank.domain.AccountRepository;

import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class AccountManagerTest {

	AccountManager accountManager = new AccountManager();

	@Mock
	AccountRepository accountRepositoryMock;

	@Mock
	Account acc;

	@Before
	public void setup() {
		accountManager.setAccountRepository(accountRepositoryMock);
// acc.setOwnerID("123456789");
	}

	@Test
	public void givenAccountDoesNotExistThenCreateSuccessfully() {
		boolean result = accountManager.createAccount("123456789");
		//tworzone jest konto o numerze 123456789 
		Assert.assertThat(result, is(true));
		//po stworzeniu konta wyniki jest zapisywany jako zmienna boolowska jezeli konto zostalo stworzone wartosc rowna sie true
		Mockito.verify(accountRepositoryMock, Mockito.times(1)).addAccount(Mockito.any(Account.class));
		//metoda veryfy sprawdza, czy pewne zachowanie miało miejsce co najmniej raz

		//sprawdzane jest czy wykonana zostala metoda createAccount dokładnie jeden raz
	}

	@Test
	public void givenAccountAlreadyExistsThenReturnFalse() {
		Mockito.when(accountRepositoryMock.findAccount("123456789")).thenReturn(new Account());
		//Mockujemy aby konto o numerze ID 123456789 bylo w mapi kont
		boolean result = accountManager.createAccount("123456789");
		//próbujemy stworzyc konto o numerze 123456789 ale takie juz jest w mapie konts
		Assert.assertThat(result, is(false));
		//poniewaz takie konto juz jest w mapie kont zwraca wartosc false
		Mockito.verify(accountRepositoryMock, Mockito.never()).addAccount(Mockito.any(Account.class));
		//metoda veryfy sprawdza, czy pewne zachowanie miało miejsce okreslona ilosc razy w tym wypadku czy wogule sie odbyla

		//sprawdzane jest czy wykonana zostala metoda addAccount w tym wypadku nie powinna zostac 
		//wykonana poniewaz konto o podanym ID juz istnieje
	}

	@Test
	public void givenDepositDoneRight() {

		acc = new Account("123456789");
		////tworzymy konto o ID rownym 123456789
		Mockito.when(accountRepositoryMock.findAccount("123456789")).thenReturn(acc);
		//jezeli bedziemy wyszukiwac konta metoda findAccount to ma zwrocic nasze stworzone konto acc
		BigDecimal v = acc.getBalance();
		//ustala w zmienne v stan konta
		
		// accountManager.createAccount("123456789");
		// Mockito.verify(accountRepositoryMock,Mockito.times(1)).addAccount(Mockito.any(Account.class));
		// Assert.assertEquals(v, BigDecimal.valueOf(0));

		accountManager.makeDeposit("123456789", "10");
		//wplacamy na konto 123456789 wartosc 10
		Assert.assertEquals(v.add(BigDecimal.valueOf(10)), acc.getBalance());
		//sprawdzamy czy po wplaceniu na konto 10 faktycznie na koncie znajduje sie 10,
		//pierwszym parametrem jest spodziewana wartosc natomiast druga rzeczywista
		
		
		// Assert.assertEquals(accountManager.getAccountBalance("123456789"), 10);

	}
	

	@Test
	public void givenDepositDoneNull() {

		//Mock
//		Account acc = new Account("123456799");
//		acc = Mockito.mock(Account.class);

		Mockito.when(accountRepositoryMock.findAccount("123456799")).thenReturn(null);
		//metoda findAccount prubuje znalezc konto o numerze 123456799 ale nie znajduje dlatego jest zwracany null
		accountManager.makeDeposit("123456799", "10");
		//wplacamy na nieistniejace konto 123456799 wartosc 10
		Assert.assertEquals(null, acc.getBalance()); 
		//sprawdzamy jaki jest stan salda konta oczekujemy nulla
		
		
	//	Mockito.verify(acc,Mockito.times(0)).deposit(acc.getBalance());
		
		

	}

	@Test
	public void getListOfAccounts() {

		//Account acc = new Account("123456789");
		//Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn((Account) Mockito.anyCollectionOf(Account.class));
		
		ArrayList accs = new ArrayList();
//		Assert.assertEquals(accs, accountManager.getAllAccounts());
		
		Account acc1 = new Account("123456789");
		accs.add(acc1);

		Mockito.when(accountRepositoryMock.allAccounts()).thenReturn(accs);
		//mockowana jest metoda allAccounts kiedy zostanie wywolana ma zwrocic liste accs
		Assert.assertEquals(accs.size(), accountManager.getAllAccounts().size());
		//sprawdza czy rozmar arraylisty accs jest taki sam jaki wskaze metoda getAllAccounts().size()
	}

	@Test
	public void getAccountTest() {

		Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(acc);
		//kiedy wykonywana jest metoda findAccount ma zwrocic Konto(acc)
		Assert.assertNotEquals(accountManager.getAccount(Mockito.anyString()), null);
		//sprawdzany jest czy getAccount cos zwroci wykorzystujac metode assertNotEquals
	}

	@Test
	public void givenWithdrawDoneWithAccount() {

		acc = new Account("123456789");
	//	Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(acc);

		BigDecimal v = acc.getBalance();
		accountManager.makeDeposit("123456789", "10");
		accountManager.makeWithdraw("123456789", "10");
		//wplacamy a nastepnie wyplacamy na konto 123456789 wartosc 10
		Assert.assertEquals(v, acc.getBalance());
		//sprawdzamy czy wyplacanie z konta dziala poprawne czy najpierw musimy wplacic, potem wyplacic a na koncu sprawdzamy
		//czy saldo jest takie samo
	}

	@Test
	public void givenWithdrawDoneWithNullAccount() {

		acc = Mockito.mock(Account.class);
		Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(null);
		
	}

	@Test
	public void givenWithdrawDoneWithAccountWithoutFunds() {

		acc = new Account("123456789");
		
		Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(acc);
		
		accountManager.makeDeposit("123456789", "5");
		BigDecimal v = acc.getBalance();
		
		accountManager.makeWithdraw("123456789", "10");
		Assert.assertEquals(v, acc.getBalance());
		//kwota na koncie jest ywyniewystarczajaca do dokonania transakcji wyplacania (na koncie jest 5 a chcemy wyplacic 10)
		
	}

	@Test
	public void givenTransferDoneWithAccountsSucess() {

		Account acc1 = new Account("123456789");
		Account acc2 = new Account("987654321");
		//tworzone sa dwa konta
		Mockito.when(accountRepositoryMock.findAccount("123456789")).thenReturn(acc1);
		Mockito.when(accountRepositoryMock.findAccount("987654321")).thenReturn(acc2);
		
		accountManager.makeDeposit("123456789", "50");
		//wplacamy na konto 123456789 50
		accountManager.makeTransfer(acc1.getOwnerID(), acc2.getOwnerID(), "25");
		//robimy transfer z konta 123456789 na konto 987654321 wartosc 25
		Assert.assertEquals(acc1.getBalance(), acc2.getBalance());
		//sprawdzana jest wartosc  na kontach jezeli 50 wplacilismy na jedno a potem przelalismy 25 na drugie to oba 
		//powinny miec po 25
	}

	@Test
	public void givenAccountBalanceSucess() {

		Account acc1 = new Account("123456789");
		
		Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(acc1);
		//kiedy wywolywana jest metoda findAccount ma zwrocic konto acc1 o numerze 123456789
		
		acc1.deposit(BigDecimal.valueOf(50));
		//wplacane jest 50 na konto acc1
		accountManager.getAccountBalance(acc1.getOwnerID());
		//sprawdzany jest stan konta na acc1
		Assert.assertNotEquals(null, acc1.getBalance());
		//acc1 nie moze byc rowne null (assertNotEquals)
	}

	@Test
	public void givenAccountBalanceNull() {

		Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(null);
		//metoda findAccount zwraca konto null
		accountManager.getAccountBalance(acc.getOwnerID());
		//sprawdza stan konta
		Assert.assertEquals(null, acc.getBalance());
		//stan konta powinien wynosic null
	}

	@Test
	public void givenDeleteAccountSucess() {

		Account acc1 = new Account("123456789");
		//tworzone jest konto o numerze 123456789
		Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(acc1);
		//metoda findAccount zwraca acc1
		Boolean b = accountManager.deleteAccount("123456789");
		//metoda deleteAccount kasuje konto o numerze 123456789
		Assert.assertEquals(true, b);
		//sprawdzany jest czy nastapilo skasowanie skasowanie konta
	}

	@Test
	public void givenDeleteAccountFail() {

		//acc = Mockito.mock(Account.class);
		Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(null);
		//metoda findAccount zwraca null konto
		Boolean b = accountManager.deleteAccount(acc.getOwnerID());
		//nastepuje próba skasowania konta
		Assert.assertEquals(false, b);
		//assercja zwraca false poniewaz nie moze skasowac konta

	}

}