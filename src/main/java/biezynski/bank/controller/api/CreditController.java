package biezynski.bank.controller.api;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import biezynski.bank.domain.Account;
import biezynski.bank.domain.AccountManager;
import biezynski.bank.domain.CreditManager;
import biezynski.bank.response.AccountDTO;
import biezynski.bank.response.ResponseDTO;
import biezynski.bank.response.ResponseMessage;
import biezynski.bank.validator.BusinessValidator;

@Controller
@RequestMapping("api/accounts/credit")
public class CreditController {

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private CreditManager creditManager;

	@RequestMapping(value = "/{ownerID}/loan", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseBody
	public ResponseDTO<AccountDTO> makeLoan(@PathVariable String ownerID, @RequestParam String value,
			HttpServletResponse response) {

		ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();

		if (BusinessValidator.isIDValid(ownerID)) {

			Account account = accountManager.getAccount(ownerID);

			if (account != null) {
				if (BusinessValidator.isAmmountfValid(value)) {

					/*
					 * pozyczka ...
					 */
					if (!account.hasPendingLoan()) {
						if (account.getBalance().compareTo(creditManager.getMinimumBalance()) >= 0) {

							BigDecimal loanAmmount = new BigDecimal(value);

							if (loanAmmount.compareTo(creditManager.getAllowedAmount(account)) <= 0) {
								creditManager.makeLoan(ownerID, loanAmmount);
								responseDTO.setData(new AccountDTO(account));
								responseDTO.setMessage(ResponseMessage.SUCCESS);

							} else {
								responseDTO.setMessage(ResponseMessage.LOAN_CREDIT_EXCEEDED);
							}

						} else {
							responseDTO.setMessage(ResponseMessage.LOAN_NOT_ENOUGH_BALANCE);
						}

					} else {
						responseDTO.setMessage(ResponseMessage.LOAN_PENDING_FOR_ACCOUNT);
					}

				} else {
					responseDTO.setMessage(ResponseMessage.INVALID_AMMOUNT);
				}

			} else {
				responseDTO.setMessage(ResponseMessage.ACCOUNT_NOT_FOUND);
			}

		} else {
			responseDTO.setMessage(ResponseMessage.INVALID_ID);
		}

		response.setStatus(HttpStatus.OK.value());

		return responseDTO;
	}
}