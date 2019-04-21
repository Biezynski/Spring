package biezynski.bank.controller.api;

import java.util.ArrayList;
import java.util.List;

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
import biezynski.bank.response.AccountDTO;
import biezynski.bank.response.ResponseDTO;
import biezynski.bank.response.ResponseMessage;
import biezynski.bank.validator.BusinessValidator;

@Controller
@RequestMapping("api/accounts")
public class AccountController {

	private AccountManager accountManager;

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public ResponseDTO<AccountDTO> createAccount(@RequestParam(required = false) String ownerID,
			HttpServletResponse response) {

		ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();

		if (BusinessValidator.isIDValid(ownerID)) {
			if (accountManager.getAccount(ownerID) == null) {
				if (accountManager.createAccount(ownerID)) {

					responseDTO.setData(new AccountDTO(accountManager.getAccount(ownerID)));
					responseDTO.setMessage(ResponseMessage.SUCCESS);

				} else {
					responseDTO.setMessage(ResponseMessage.OPERATION_ERROR);
				}

			} else {
				responseDTO.setMessage(ResponseMessage.ACCOUNT_ALREADY_EXISTS);
			}

		} else {
			responseDTO.setMessage(ResponseMessage.INVALID_ID);
		}

		response.setStatus(HttpStatus.OK.value());

		return responseDTO;
	}

	@RequestMapping(value = "/{ownerID}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public ResponseDTO<AccountDTO> getAccount(@PathVariable String ownerID, HttpServletResponse response) {

		ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();

		if (BusinessValidator.isIDValid(ownerID)) {

			Account account = accountManager.getAccount(ownerID);

			if (account != null) {
				responseDTO.setData(new AccountDTO(account));
				responseDTO.setMessage(ResponseMessage.SUCCESS);
			} else {
				responseDTO.setMessage(ResponseMessage.ACCOUNT_NOT_FOUND);
			}

		} else {
			responseDTO.setMessage(ResponseMessage.INVALID_ID);
		}

		response.setStatus(HttpStatus.OK.value());

		return responseDTO;
	}

	@RequestMapping(value = "/{ownerID}/deposit", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseBody
	public ResponseDTO<AccountDTO> makeDeposit(@PathVariable String ownerID, @RequestParam String value,
			HttpServletResponse response) {

		ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();

		if (BusinessValidator.isIDValid(ownerID)) {
			if (accountManager.getAccount(ownerID) != null) {
				if (BusinessValidator.isAmmountfValid(value)) {
					accountManager.makeDeposit(ownerID, value);
					responseDTO.setData(new AccountDTO(accountManager.getAccount(ownerID)));
					responseDTO.setMessage(ResponseMessage.SUCCESS);

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

	@RequestMapping(value = "/{ownerID}/withdraw", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseBody
	public ResponseDTO<AccountDTO> makeWithdraw(@PathVariable String ownerID, @RequestParam String value,
			HttpServletResponse response) {

		ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();

		if (BusinessValidator.isIDValid(ownerID)) {
			if (accountManager.getAccount(ownerID) != null) {
				if (BusinessValidator.isAmmountfValid(value)) {
					accountManager.makeWithdraw(ownerID, value);
					responseDTO.setData(new AccountDTO(accountManager.getAccount(ownerID)));
					responseDTO.setMessage(ResponseMessage.SUCCESS);

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

	@RequestMapping(value = "/{ownerID}/transfer", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseBody
	public ResponseDTO<AccountDTO> makeTransfer(@PathVariable String ownerID, @RequestParam String targetID,
			@RequestParam String value, HttpServletResponse response) {

		ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();

		if (BusinessValidator.isIDValid(ownerID) && BusinessValidator.isIDValid(targetID)) {
			if (accountManager.getAccount(ownerID) != null && accountManager.getAccount(targetID) != null) {

				if (BusinessValidator.isAmmountfValid(value)) {
					accountManager.makeTransfer(ownerID, ownerID, value);
					responseDTO.setData(new AccountDTO(accountManager.getAccount(ownerID)));
					responseDTO.setMessage(ResponseMessage.SUCCESS);

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

	@RequestMapping(value = "/", method = RequestMethod.DELETE, produces = { "application/json" })
	@ResponseBody
	public ResponseDTO<AccountDTO> deleteAccount(@RequestParam(required = false) String ownerID,
			HttpServletResponse response) {

		ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();

		if (BusinessValidator.isIDValid(ownerID)) {
			if (accountManager.getAccount(ownerID) != null) {
				if (accountManager.deleteAccount(ownerID)) {
					responseDTO.setMessage(ResponseMessage.SUCCESS);
				} else {
					responseDTO.setMessage(ResponseMessage.OPERATION_ERROR);
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

	@RequestMapping(value = "/all", method = RequestMethod.DELETE, produces = { "application/json" })
	@ResponseBody
	public ResponseDTO<AccountDTO> deleteAllAccounts(HttpServletResponse response) {
		ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();

		accountManager.deleteAllAccounts();

		responseDTO.setMessage(ResponseMessage.SUCCESS);
		response.setStatus(HttpStatus.OK.value());

		return responseDTO;
	}

	public List<AccountDTO> getAllAccounts() {
		List<AccountDTO> accounts = new ArrayList<>();
		for (Account account : accountManager.getAllAccounts()) {
			accounts.add(new AccountDTO(account));
		}
		return accounts;
	}
	
	@Autowired
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}