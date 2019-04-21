package biezynski.bank.domain;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountManager {
	
	private AccountRepository accountRepository;

	public boolean createAccount(String ownerID) {
		if (accountRepository.findAccount(ownerID) == null) {
			Account c = new Account(ownerID);
			accountRepository.addAccount(c);

			return true;
		}
		return false;
	}

	public List<Account> getAllAccounts() {
		return accountRepository.allAccounts();
	}

	public Account getAccount(String ownerID) {
		return accountRepository.findAccount(ownerID);
	}

	public void makeDeposit(String ownerID, String value) {
		Account c = accountRepository.findAccount(ownerID);
		if (c != null) {
			c.deposit(new BigDecimal(value));
		}
	}

	public void makeWithdraw(String ownerID, String value) {
		Account c = accountRepository.findAccount(ownerID);
		if (c != null) {
			BigDecimal amount = new BigDecimal(value);
			if (c.getBalance().compareTo(amount) >= 0) {
				c.withdraw(new BigDecimal(value));
			}
		}
	}

	public void makeTransfer(String originOwnerID, String targetOwnerID, String value) {
		Account origin = accountRepository.findAccount(originOwnerID);
		Account target = accountRepository.findAccount(targetOwnerID);

		if (origin != null && target != null) {
			BigDecimal amount = new BigDecimal(value);
			if (origin.getBalance().compareTo(amount) >= 0) {
				origin.withdraw(amount);
				target.deposit(amount);
			}
		}
	}

	public BigDecimal getAccountBalance(String ownerID) {
		Account c = accountRepository.findAccount(ownerID);
		if (c != null) {
			return c.getBalance();
		}
		return null;
	}

	public boolean deleteAccount(String ownerID) {
		Account c = accountRepository.findAccount(ownerID);
		if (c != null) {
			accountRepository.deleteAccount(c);
			return true;
		}
		return false;
	}
	
	public boolean deleteAllAccounts() {
		AccountRepository.clearAccounts();
		return true;
	}
	
	@Autowired
	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
}