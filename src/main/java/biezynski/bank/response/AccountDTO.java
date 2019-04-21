package biezynski.bank.response;

import java.io.Serializable;

import biezynski.bank.domain.Account;

public class AccountDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ownerID;
	private String balance;
	private String hasPendingLoan;

	public AccountDTO() {

	}

	public AccountDTO(Account account) {
		this.ownerID = account.getOwnerID();
		this.balance = String.valueOf(account.getBalance());
		this.hasPendingLoan = String.valueOf(account.hasPendingLoan());
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getHasPendingLoan() {
		return hasPendingLoan;
	}

	public void setHasPendingLoan(String hasPendingLoan) {
		this.hasPendingLoan = hasPendingLoan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ownerID == null) ? 0 : ownerID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountDTO other = (AccountDTO) obj;
		if (ownerID == null) {
			if (other.ownerID != null)
				return false;
		} else if (!ownerID.equals(other.ownerID))
			return false;
		return true;
	}
}