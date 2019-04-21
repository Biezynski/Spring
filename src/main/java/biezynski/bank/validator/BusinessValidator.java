package biezynski.bank.validator;

import java.math.BigDecimal;

public class BusinessValidator {

	private static final int ID_LENGTH = 11;

	public static boolean isIDValid(String idAccount) {
		if (idAccount != null && idAccount.length() == ID_LENGTH) {
			return true;
		}
		return false;
	}

	public static boolean isAmmountfValid(String ammount) {
		try {
			if (ammount != null
					&& new BigDecimal(ammount).compareTo(BigDecimal.ZERO) == 1) {
				return true;
			}

			return false;

		} catch (NumberFormatException e) {
			return false;
		}
	}
}