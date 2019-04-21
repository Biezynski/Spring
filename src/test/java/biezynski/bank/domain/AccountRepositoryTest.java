package biezynski.bank.domain;

import java.nio.file.FileAlreadyExistsException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import biezynski.bank.domain.Account;
import biezynski.bank.domain.AccountRepository;

@RunWith(JUnit4.class)
public class AccountRepositoryTest {
	
	AccountRepository accountRepository;
	
	@Before
	public void setup() {
		accountRepository = new AccountRepository();
		
		// tworze testowe konta w metodzie before czyli przed kazdym testem
		accountRepository.addAccount(createAccount("06444870976"));
		accountRepository.addAccount(createAccount("06444870977"));
		
	}
	
	@Test
	public void testAllAccountsReturnsSuccessful() {
		int size = accountRepository.allAccounts().size();
		//przypisuje do zmiennej typu size ile kont znajduje sie w repozytorium kont
		Assert.assertEquals(2, size);
		//sprawdza ile kont zostalo stworzonych (powinny byc 2 wg metody before)
	}
	
	@Test
	public void testFindAccountReturnsExistingAccount() {
		Account account = accountRepository.findAccount("06444870976");
		//przypisuje konto do zmiennej account
		Assert.assertNotNull(account);
		//sprawdza czy account jest nullem (jest jest dlatego test przechodzi)
	}
	
//	@Test(expected = FileAlreadyExistsException.class)
//	public void testFindAccountReturnsNullWhenAccountDoesNotExist() {
//		accountRepository.findAccount("1111111111");
		
//	}
	
	@Test
	public void testDeleteAccountSuccessful() {
		accountRepository.deleteAccount(createAccount("06444870976"));
		//kasuje konto o numerze 06444870976
		Assert.assertEquals(1, accountRepository.allAccounts().size());
		//po skasowaniu jednego konta w liscie kont powinno zostac jedno (06444870977) patrz @Before
		Account account = accountRepository.findAccount("06444870977");
		//sprawdza czy znajduje sie to konto 06444870977 metoda findAccount
		Assert.assertNotNull(account);
		//dla pewnosci sprawdza to asercja czy account nie jest nullem
	}
		
	private Account createAccount(String ID) {
		Account a = new Account();
		a.setOwnerID(ID);
		return a;
	}
}