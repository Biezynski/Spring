package biezynski.bank.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {

	private String ownerID;
	private BigDecimal balance;
	private boolean hasPendingLoan;

	public Account() {
		
	}
	
	public Account(String ownerID) {
		this.ownerID = ownerID;
		this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public boolean hasPendingLoan() {
		return hasPendingLoan;
	}

	public void setHasPendingLoan(boolean hasPendingLoan) {
		this.hasPendingLoan = hasPendingLoan;
	}

	public void deposit(BigDecimal value) {
		this.balance = this.balance.add(value).setScale(2, RoundingMode.HALF_EVEN);
	}

	public void withdraw(BigDecimal value) {
		this.balance = this.balance.subtract(value).setScale(2, RoundingMode.HALF_EVEN);
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
		Account other = (Account) obj;
		if (ownerID == null) {
			if (other.ownerID != null)
				return false;
		} else if (!ownerID.equals(other.ownerID))
			return false;
		return true;
	}
}