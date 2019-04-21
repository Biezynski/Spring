package biezynski.bank.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
class AccountRepository {

	private static final Map<String, Account> accounts = new ConcurrentHashMap<String, Account>();

	/**
	 * zwraca liste wszystkich kont
	 */
	public List<Account> allAccounts() {
		return new ArrayList<Account>(accounts.values());
	}

	/** 
	 * znajduje konkretne konto na podstawie ownerID 
	 */
	public Account findAccount(String ownerID) {
		if (accounts.containsKey(ownerID)) {
			return accounts.get(ownerID);
		}

		return null;
	}

	/**
	 * kasuje konkretne konto
	 */
	public void deleteAccount(Account c) {
		if (accounts.containsValue(c)) {

			Iterator<Entry<String, Account>> it = accounts.entrySet().iterator();

			while (it.hasNext()) {

				Entry<String, Account> item = it.next();

				if (item.getKey().equals(c.getOwnerID())) {
					it.remove();
				}
			}
		}
	}

	/**
	 * dodaje nowe konto do mapy -> Map<String, Account> accounts = new ConcurrentHashMap<String, Account>();
	 */
	public void addAccount(Account c) {
		accounts.put(String.valueOf(c.getOwnerID()), c);
	}

	static void clearAccounts() {
		accounts.clear();
	}
}