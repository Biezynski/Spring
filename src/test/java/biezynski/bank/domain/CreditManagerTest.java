package biezynski.bank.domain;

import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import biezynski.bank.domain.Account;
import biezynski.bank.domain.AccountRepository;
import biezynski.bank.domain.CreditManager;

@RunWith(MockitoJUnitRunner.class)
public class CreditManagerTest {
	
	CreditManager creditManager = new CreditManager();

	@Mock
	AccountRepository accountRepositoryMock;
	
	@Before
	public void setup() {
		creditManager.setAccountRepository(accountRepositoryMock);
	}
	
	@Test
	public void givenMakeLoanSucess() {
		
		Account acc = new Account("123456789");
		acc.deposit(new BigDecimal("10000"));
		//tworzone jest konto 123456789 a nastepnie wplacono wartosc 10000
		
		BigDecimal cash = new BigDecimal(3000);
		Mockito.when(accountRepositoryMock.findAccount(Mockito.anyString())).thenReturn(acc);
		//metoda findAccount zwraca nam konto acc

		creditManager.makeLoan(acc.getOwnerID(), cash);
		//symulujemy metode makeLoan do konta o saldzie 10000 bierzemy pozyczke cash o wartosci 3000
		Assert.assertEquals(new BigDecimal("13000.00"), acc.getBalance());
		//sprawdzamy czy konto acc ma fakcznie spodziewana wartosc na koncie wynoszaca 13000
		
		
//		Account account = new Account("089");
//		account.deposit(new BigDecimal("20000.00"));
//		Mockito.when(accountRepositoryMock.findAccount("089")).thenReturn(account);
//		creditManager.makeLoan("089", new BigDecimal("100"));
//		Assert.assertEquals(new BigDecimal("20100.00"), account.getBalance());
		
	}


}
