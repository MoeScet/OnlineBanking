package com.fdm.OnlineBanking.Controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdm.OnlineBanking.Enum.AnnualIncome;
import com.fdm.OnlineBanking.Enum.CreditCardName;
import com.fdm.OnlineBanking.Enum.InstallmentMonths;
import com.fdm.OnlineBanking.Model.BankAccount;
import com.fdm.OnlineBanking.Model.BankAccountTransaction;
import com.fdm.OnlineBanking.Model.CreditCard;
import com.fdm.OnlineBanking.Model.CreditCardTransaction;
import com.fdm.OnlineBanking.Model.User;
import com.fdm.OnlineBanking.Security.MyUserDetailsService;
import com.fdm.OnlineBanking.Security.UserPrincipal;
import com.fdm.OnlineBanking.Service.BankAccountService;
import com.fdm.OnlineBanking.Service.BankAccountTransactionService;
import com.fdm.OnlineBanking.Service.CreditCardService;
import com.fdm.OnlineBanking.Service.CreditCardTransactionService;
import com.fdm.OnlineBanking.Service.ForexService;
import com.fdm.OnlineBanking.Service.UserService;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@Controller
public class ControllerClass {

	@Autowired
	private UserService userService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private BankAccountTransactionService bankAccountTransactionService;
	@Autowired
	private CreditCardTransactionService creditCardTransactionService;
	@Autowired
	private ForexService forexService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String goToIndexPage() {
//		return "index";
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String goToLogInPage() {
		return "index";
	}
	
	@GetMapping("/errorlogin")
	public String errorMessage(Model model) {
		model.addAttribute("a", true);
		return "index";
	}
	
	@GetMapping("/register")
	public String goToRegisterPage() {	
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(@RequestParam("firstName")String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
			@RequestParam("address") String address, @RequestParam("postalCode") long postalCode, @RequestParam("birthDate") LocalDate dob, @RequestParam("income") AnnualIncome income,
			@RequestParam("password") String pw, @RequestParam("checkPassword") String checkpw, 
			HttpSession session, Model model) {
		

		boolean errorMessage = true;
		boolean identicalUsername = true;
		boolean underage = true;


		LocalDate currentDate = LocalDate.now();
		int age = Period.between(dob, currentDate).getYears();
		if (age >= 18) {
			underage = false;
			} 

		if (userService.checkUsername(username).isEmpty()) {
				identicalUsername = false;
			}

		if (pw.equals(checkpw)) {
			errorMessage = false;
			}

		if (errorMessage == false && identicalUsername == false && underage == false) {
			User user = new User(username,pw,firstName,lastName,address,postalCode,mobile,email,dob,income);
			String encodedPw = passwordEncoder.encode(pw);
			user.setPassword(encodedPw);
			
			BankAccount bankAccount = new BankAccount();
			long bankAccNo = (long)(Math.random()*(1000000000-99999999+1)+99999999);
			Optional<BankAccount> bankAccountSameNo = bankAccountService.findByBankAccNo(bankAccNo);
							
			while (bankAccountSameNo.isPresent()) {
				bankAccNo = (long)(Math.random()*(1000000000-99999999+1)+99999999);
				bankAccountSameNo = bankAccountService.findByBankAccNo(bankAccNo);
				}
							
			bankAccount.setBankAccNo(bankAccNo);
					
			bankAccount.setUser(user);
			bankAccountService.saveAccount(bankAccount);
			session.setAttribute("bankAccount", bankAccount);
			user.setBankAccount(bankAccount);
			userService.saveUser(user);
			return "redirect:/login";
		} else 
		{
			model.addAttribute("firstName", firstName);
			model.addAttribute("lastName", lastName);
			model.addAttribute("username", username);
			model.addAttribute("email", email);
			model.addAttribute("mobile", mobile);
			model.addAttribute("address", address);
			model.addAttribute("postalCode", postalCode);
			model.addAttribute("dob", dob);
		}

		model.addAttribute("identicalUsername", identicalUsername);
		model.addAttribute("underage", underage);
		model.addAttribute("errorMessage", errorMessage);
		return "register";
	}

	
//	@GetMapping("/login")
//	public String goToLogInPage() {
//		return "login";
//	}
	
	
// DO NOT uncomment this
//	@GetMapping("/home")
//	public String goToLoggedIn() {
//		return "home";
//	}
//	
	/*
	 * @all: 
	 * Please add this one liner code and change its parameters accordingly for the features you are working with:
	 * 
	 * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 * bankAccountTransactionService.addEntry([BankAccount] bankAccount, [long] recipientId (if Transfer) or 0 (if deposit/withdraw), [LocalDateTime] Transaction Date, [String] "Deposit"/"Withdraw"/"Transfer", [Double][Negative if withdraw/transfer, Positive if deposit] Transaction Amount);
	 * creditCardTransactionService.addEntry([CreditCard] creditCard, [LocalDateTime] Transaction Date, [String] "EXTERNAL MERCHANTS"/"CC PAYMENT", [String] Merchant Name (if external merchant) or "Thank you for choosing ${name of credit card}" (if CCPayment), [Double][Negative if payable, Positive if payment] Transaction Amount);
	 * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 *
	 * Examples:
	 * bankAccountTransactionService.addEntry(ba1, 0,  LocalDateTime.now(), "Deposit", 125.12);
	 * bankAccountTransactionService.addEntry(ba1, 0,  LocalDateTime.of(2023, Month.AUGUST, 29, 19, 30, 40), "Withdraw", -125.12);
	 * bankAccountTransactionService.addEntry(ba1, 15,  LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40), "Transfer", -125.12);
	 * creditCardTransactionService.addEntry(cc1, LocalDateTime.of(2022, Month.OCTOBER, 31, 19, 30, 40), "EXTERNAL MERCHANTS", "Zalora HK", -125.12);
	 * creditCardTransactionService.addEntry(cc1, LocalDateTime.of(2022, Month.OCTOBER, 31, 22, 30, 40), "CC Payment", "Thank you for choosing HSBC Smart Credit Card", 280);
	 *
	 *
	 * NB: for the bank account and credit card object, please inject existing ones from db (by using BAService or CCService class). If instantiate new BA or CC object wont work.
	 * Thanks Zhao Wei for pointing it out
	 * 
	 * Thank you all
	 * -Daryl
	 *
	 */
	
	@GetMapping("/transaction/bankAccount")
	public String goToBankAccountTransactionHistory(@AuthenticationPrincipal UserPrincipal principal, Model model) {
		User user = principal.getUser();
		
		//Long userId = user.getUserId();
		List<BankAccountTransaction> bankAccountTransaction = bankAccountTransactionService.findAllBankAccountTransactions();
		
		
		List<BankAccountTransaction> bankAccountTransCurrentUser = new ArrayList<BankAccountTransaction>();
		
		for (BankAccountTransaction i : bankAccountTransaction) {
			if (i.getBankAccount().getAccountId() == user.getBankAccount().getAccountId()) {
				i.setRIDS(i.getReceipientId());
				i.setSIDS(i.getBankAccount().getBankAccNo(), i.getTransactionType());
				bankAccountTransCurrentUser.add(i);
			}
			if (i.getTransactionType().equals("Transfer") && i.getReceipientId() == user.getBankAccount().getBankAccNo()) {
				i.setRIDS(i.getReceipientId());
				i.setSIDS(i.getBankAccount().getBankAccNo(), i.getTransactionType());
				bankAccountTransCurrentUser.add(i);
			}
		}
		
		List<String> getMonthsToFilter = bankAccountTransactionService.findAllMonths(bankAccountTransCurrentUser);
		
		Comparator<BankAccountTransaction> sortTransactionByTime = (t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime());
		Collections.sort(bankAccountTransCurrentUser, sortTransactionByTime);
		
		for (BankAccountTransaction i : bankAccountTransCurrentUser) {
			System.out.println(i.recipientIdString);
		}
		
		model.addAttribute("BATransaction", bankAccountTransCurrentUser);
		model.addAttribute("monthsToFilter",getMonthsToFilter);
		model.addAttribute("firstName", user.getFirstName());
		return "bank-account-transaction";
	}
	
	@PostMapping("/transaction/bankAccount")
	public String goToFilteredMonthsPageBA(
			@AuthenticationPrincipal UserPrincipal principal,
			@RequestParam(required = false) String monthYearName,
			Model model,
			HttpSession session) {
		User user = principal.getUser();		
		
		List<BankAccountTransaction> filteredBATransaction = bankAccountTransactionService.getTransactionsOnThisMonthYear(monthYearName);
		
		List<BankAccountTransaction> bankAccountTransCurrentUser = new ArrayList<BankAccountTransaction>();
		
		for (BankAccountTransaction i : filteredBATransaction) {
			if (i.getBankAccount().getAccountId() == user.getBankAccount().getAccountId()) {
				i.setRIDS(i.getReceipientId());
				i.setSIDS(i.getBankAccount().getBankAccNo(), i.getTransactionType());
				bankAccountTransCurrentUser.add(i);
			}
			if (i.getTransactionType().equals("Transfer") && i.getReceipientId() == user.getBankAccount().getBankAccNo()) {
				i.setRIDS(i.getReceipientId());
				i.setSIDS(i.getBankAccount().getBankAccNo(), i.getTransactionType());
				bankAccountTransCurrentUser.add(i);
			}
		}
		model.addAttribute("filteredBAT", bankAccountTransCurrentUser);
		model.addAttribute("monthYear", monthYearName);
		model.addAttribute("firstName", user.getFirstName());
		return "bank-account-transaction-filtered";
		
	}
	
	@GetMapping("/transaction/creditCard/{card}")
	public String goToCreditCardTransactionHistory(@AuthenticationPrincipal UserPrincipal principal, Model model, @PathVariable("card") String creditCardName) {
		User user = principal.getUser();
		
		List<CreditCardTransaction> creditCardTransaction = creditCardTransactionService.findAllCreditCardTransactions(creditCardService.findCardByUserAndType(user, CreditCardName.valueOf(creditCardName)).getCardId());
		List<String> getMonthsToFilter = creditCardTransactionService.findAllMonths(creditCardTransaction);
		
		Comparator<CreditCardTransaction> sortTransactionByTime = (t1, t2) -> t2.getTransactionTime().compareTo(t1.getTransactionTime());
		Collections.sort(creditCardTransaction, sortTransactionByTime);
		
		model.addAttribute("CCTransaction",creditCardTransaction);
		model.addAttribute("CCMap", creditCardName);
		model.addAttribute("CCName", CreditCardName.valueOf(creditCardName).getCreditCardName());
		model.addAttribute("monthsToFilter",getMonthsToFilter);
		model.addAttribute("firstName", user.getFirstName());
		
		return "credit-card-transaction";
	}
	
	@PostMapping("/transaction/creditCard/{card}")
	public String goToFilteredMonthsPageCC(
			@AuthenticationPrincipal UserPrincipal principal,
			@RequestParam(required = false) String monthYearName,
			@PathVariable("card") String creditCardName,
			Model model,
			HttpSession session) {
		User user = principal.getUser();
		
		List<CreditCardTransaction> creditCardTransaction = creditCardTransactionService.findAllCreditCardTransactions(creditCardService.findCardByUserAndType(user, CreditCardName.valueOf(creditCardName)).getCardId());
		List<CreditCardTransaction> filteredCCTransaction = creditCardTransactionService.getTransactionsOnThisMonthYear(monthYearName, creditCardTransaction);
		
		model.addAttribute("filteredCCT", filteredCCTransaction);
		model.addAttribute("CCMap", creditCardName);
		model.addAttribute("CCName", CreditCardName.valueOf(creditCardName).getCreditCardName());
		model.addAttribute("monthYear", monthYearName);
		model.addAttribute("firstName", user.getFirstName());
		return "credit-card-transaction-filtered";
		
	}
	
	
	// for credit card page

	@GetMapping("/home")
	public String goToCreditCardPage(@AuthenticationPrincipal UserPrincipal principal, Model model, HttpSession session) {
		User user = userService.findUser(principal.getUsername());
		model.addAttribute("user", user);
		session.setAttribute("curr_user", user.getUserId());
			
		List<CreditCard> userCreditCards = user.getCreditCard();
		
		for(CreditCard card: userCreditCards) {
			
			List<CreditCardTransaction> transactions = creditCardTransactionService.findAllCreditCardTransactions(card.getCardId());
			double payable = 0;
			for (CreditCardTransaction ccTransaction: transactions) {
				payable += ccTransaction.getAmount();
			}
			card.setCurrentAmountPayable(Math.abs(payable));
			creditCardService.updateCreditCard(card);
		}
		
		
		List<CreditCardName> bankCreditCards = Arrays.asList(CreditCardName.values());
		List<CreditCardName> creditCardApplied = new ArrayList<>();
		List<CreditCardName> creditCardNotApplied = new ArrayList<>(bankCreditCards);
		
		if (userCreditCards.isEmpty()) {
			model.addAttribute("noCreditCard", true);
		}
		
		else {
			double availableCreditLimit = user.getAvailableCreditLimit();
			double creditLimit = user.getCreditLimit();
			
			// round available credit limit
			BigDecimal bd = BigDecimal.valueOf(availableCreditLimit);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			String roundedACL = String.format("%.2f", bd.doubleValue());
			
			// round credit limit
			BigDecimal bd2 = BigDecimal.valueOf(creditLimit);
			bd2 = bd2.setScale(2, RoundingMode.HALF_UP);
			String roundedCL = String.format("%.2f", bd2.doubleValue());
			
			model.addAttribute("availableCreditLimit", roundedACL);
			model.addAttribute("creditLimit", roundedCL);
			model.addAttribute("userCreditCards", userCreditCards);
		}
		
		
		for (CreditCard userCreditCard : userCreditCards) {
			creditCardApplied.add(userCreditCard.getCardName());
		}
		creditCardNotApplied.removeAll(creditCardApplied);
		
		// for credit card application recommendation
		model.addAttribute("creditCardNotApplied", creditCardNotApplied);
			
		// remove recommendation section if user has applied all cards
		if (!creditCardNotApplied.isEmpty() && !user.getIncome().equals(AnnualIncome.lowIncome) ) {
			model.addAttribute("recommendationNeeded", true);
		}

		Long accountId = user.getBankAccount().getAccountId();
		model.addAttribute("accountId", accountId);
		BankAccount account = user.getBankAccount();
		model.addAttribute("account",account);
		return "homepage"; 
	}
	
	
	// for credit card application page
	@GetMapping("/creditcard/apply/{card}")
	public String goToCreditCardApplicationPage(@PathVariable("card") CreditCardName creditCardName, @AuthenticationPrincipal UserPrincipal principal, Model model) {
		User user = principal.getUser();
		model.addAttribute("user", user);
		
		// to add attribute for bank account tab
		Long accountId = user.getBankAccount().getAccountId();
		model.addAttribute("accountId", accountId);
		BankAccount account = user.getBankAccount();
		model.addAttribute("account",account);
		
		model.addAttribute("creditCardApplying", creditCardName);
		model.addAttribute("buttonDisabled", null);
		
		// check
		List<CreditCard> userCreditCards = user.getCreditCard();
		List<CreditCardName> creditCardApplied = new ArrayList<>();
		for (CreditCard userCreditCard : userCreditCards) {
			creditCardApplied.add(userCreditCard.getCardName());
		}
		
		if (creditCardApplied.contains(creditCardName)) {
			model.addAttribute("submittedApplication", true);
			model.addAttribute("buttonDisabled", "disabled");
		}
		
		return "creditcard-application";
	}
	
	
	@PostMapping("/creditcard/apply/{card}")
	public String submitApplication(@PathVariable("card") CreditCardName creditCardName, @AuthenticationPrincipal UserPrincipal principal, Model model) {
		String username = principal.getUsername();
		creditCardService.saveCreditCard(username, creditCardName);
		
		User user = userService.findUser(principal.getUsername());
		
		model.addAttribute("submittedApplication", true);
		model.addAttribute("buttonDisabled", "disabled");
		

		model.addAttribute("user", user);
		model.addAttribute("creditCardApplying", creditCardName);
		// to add attribute for bank account tab
		Long accountId = user.getBankAccount().getAccountId();
		model.addAttribute("accountId", accountId);
		BankAccount account = user.getBankAccount();
		model.addAttribute("account",account);

		return "creditcard-application";
	}

	
	
	

	//Creditcard payment
	@GetMapping("/creditcard/payment/{card}")
	public String goToPayment(@PathVariable("card") CreditCardName creditCardName, Model model,
			@AuthenticationPrincipal UserPrincipal principal) {
		User user = principal.getUser();
		BankAccount account = bankAccountService.findBankAccount(user.getBankAccount().getAccountId());
		model.addAttribute("account", account);
		model.addAttribute("creditCardName", creditCardName);
		
		model.addAttribute("user", user);
		
		
		
		// check
		List<CreditCard> userCreditCards = user.getCreditCard();
		CreditCard creditCard = creditCardService.findCardByUserAndType(user, creditCardName);
//		for (CreditCard userCreditCard : userCreditCards) {
//			if (userCreditCard.getCardName().equals(creditCardName)) {
//				creditCard = userCreditCard;
//	
//				break;
//			}
//			
//		}
		model.addAttribute("creditcard", creditCard);
		
		return "credit-card-payment";
	}

// this is conflicts, i commented out instead of deleting it in case accidentally remove something important by Qi En
	



	



@PostMapping("/creditcard/payment/{card}")
public String makePayment(@RequestParam("amount")String amount,@PathVariable("card")CreditCardName creditCardName,Model model,
		@AuthenticationPrincipal UserPrincipal principal, @RequestParam String color) {
	
	model.addAttribute("creditCardName", creditCardName);
	User user = principal.getUser();
	
	String username = user.getUsername();
	User currentUser = userService.findUser(username);
	BankAccount account =currentUser.getBankAccount();
	double amountDouble = Double.parseDouble(amount);
	
	CreditCard creditCardDetail = creditCardService.findCardByUserAndType(currentUser, creditCardName); 
	model.addAttribute("creditcard", creditCardDetail);
	LocalDateTime now = LocalDateTime.now();
	
	
	// to add attribute for bank account tab
	Long accountId = user.getBankAccount().getAccountId();
	model.addAttribute("accountId", accountId);
	
	model.addAttribute("account",account);
	model.addAttribute("user", user);
	
	// if amount is positive, paying off cc debt/payment
	
	
	
	//add account details
	double amountBalance = account.getBalance();		
//	BankAccount bank = new BankAccount();
	if (color.equals("account")) {
	// if paying off less than or equal to what is outstanding
		if(amountDouble <= creditCardDetail.getOutstandingBalance() && amountDouble > 0 && amountBalance >= amountDouble) {
			
			double currentAvailableCredit = creditCardDetail.getAvailableCredit();
			double updatedAvailableCredit = currentAvailableCredit + amountDouble;
			
			currentUser.setAvailableCreditLimit(updatedAvailableCredit);
			List<CreditCard> creditCardsUnderUser = creditCardService.findAllCreditCardsByUser(currentUser);
			for (CreditCard card: creditCardsUnderUser ) {
				card.setAvailableCredit(updatedAvailableCredit);
			}
			
			double currentOutstandingBalance = creditCardDetail.getOutstandingBalance();
			double updatedOutstandingBalance = currentOutstandingBalance - amountDouble;
			double updateUserBalance = amountBalance - amountDouble;
			
			creditCardDetail.setOutstandingBalance(updatedOutstandingBalance);
			account.setBalance(updateUserBalance);
			

			
			String transactionType = "Paying off CC";
			Long accountNumber = 0L;
			bankAccountTransactionService.addEntry(account, accountNumber, now, transactionType,amountDouble);

			creditCardTransactionService.addEntry(creditCardDetail, now, "CC Payment", "Paying off CC", amountDouble);
			
			
			// current amount payable
			List<CreditCardTransaction> transactions = creditCardTransactionService.findAllCreditCardTransactions(creditCardDetail.getCardId());
			double payable = 0;
			for (CreditCardTransaction ccTransaction: transactions) {
				payable += ccTransaction.getAmount();
			}
			creditCardDetail.setCurrentAmountPayable(Math.abs(payable));
			creditCardService.updateCreditCard(creditCardDetail);


			
			boolean ccPaymentSuccessful = true;
			model.addAttribute("ccPaymentSuccessful",ccPaymentSuccessful);
			return "credit-card-payment";
		}
		
		// if trying to pay off more than you owe
		else {
			
			boolean ccPaymentFailedAccBal = true;
			model.addAttribute("ccPaymentFailedAccBal",ccPaymentFailedAccBal);
			return "credit-card-payment";
		}
	}
	
	else if (color.equals("otherCard")) {
		// if paying off less than or equal to what is outstanding
		if(amountDouble <= creditCardDetail.getOutstandingBalance() && amountDouble > 0 ) {
			double currentAvailableCredit = creditCardDetail.getAvailableCredit();
			double updatedAvailableCredit = currentAvailableCredit + amountDouble;
			
			currentUser.setAvailableCreditLimit(updatedAvailableCredit);
			List<CreditCard> creditCardsUnderUser = creditCardService.findAllCreditCardsByUser(currentUser);
			for (CreditCard card: creditCardsUnderUser ) {
				card.setAvailableCredit(updatedAvailableCredit);
			}
			
			double currentOutstandingBalance = creditCardDetail.getOutstandingBalance();
			double updatedOutstandingBalance = currentOutstandingBalance - amountDouble;
			
			
			creditCardDetail.setOutstandingBalance(updatedOutstandingBalance);
			

			
			creditCardTransactionService.addEntry(creditCardDetail, now, "CC Payment", "Paying off CC", amountDouble);
			
			// current amount payable
			List<CreditCardTransaction> transactions = creditCardTransactionService.findAllCreditCardTransactions(creditCardDetail.getCardId());
			double payable = 0;
			for (CreditCardTransaction ccTransaction: transactions) {
				payable += ccTransaction.getAmount();
			}
			creditCardDetail.setCurrentAmountPayable(Math.abs(payable));
			creditCardService.updateCreditCard(creditCardDetail);
			
			boolean ccPaymentSuccessful = true;
			model.addAttribute("ccPaymentSuccessful",ccPaymentSuccessful);
			return "credit-card-payment";
		}
		
		// if trying to pay off more than you owe
		else {
			
			boolean ccPaymentFailed = true;
			model.addAttribute("ccPaymentFailed",ccPaymentFailed);
			return "credit-card-payment";
		}
	}
	else {
		
		boolean ccPaymentFailed = true;
		model.addAttribute("ccPaymentFailed",ccPaymentFailed);
		return "credit-card-payment";
	}
}




				
				
						
					
					

					

		
		
	
	
	
	
	//Bank Account Transfer Page
		@GetMapping("/transferpage/{id}")
		public String goToTransferPage(@PathVariable("id") Long accountId, Model model, HttpSession httpsession, @AuthenticationPrincipal UserPrincipal principal) {
			BankAccount bankAccount = bankAccountService.findBankAccount(accountId);
			model.addAttribute("bankAccount", bankAccount);
			httpsession.setAttribute("current-session", accountId);
			
			// for displaying creditcard tab when it is clicked on the transaction page
			goToCreditCardPage(principal, model, httpsession);
			
			return "bank-account-transfer";
		    
		}
		
		@PostMapping("/transferpage/transfer")
		public String TransferFunds(@RequestParam Long accountId,@RequestParam Double amount, Model model, HttpSession httpsession, @AuthenticationPrincipal UserPrincipal principal) {
//			Optional<BankAccount> bankAccount = bankAccountService.findAccountTransfer(accountId);
			Optional<BankAccount> bankAccount = bankAccountService.findByBankAccNo(accountId);
			
			Long currentBankAccountId = (Long) httpsession.getAttribute("current-session");
			BankAccount currentBankAccount = bankAccountService.findBankAccount(currentBankAccountId);
		
			
//			boolean accountNotExist = true;
//			boolean notEnoughMoney = true;
//			boolean notNegativeAmount = true;
			
			boolean accountNotExist = false;
			boolean notEnoughMoney = false;
			boolean negativeAmount = false;
			
			if(bankAccount.isEmpty()) {
				accountNotExist = true;
			}
			
			if(currentBankAccount.getBalance() < amount) {
				notEnoughMoney = true;
			}
			
			if(amount < 0) {
				negativeAmount = true;
			}
			
			if (!accountNotExist && !notEnoughMoney && !negativeAmount) {
				currentBankAccount.minusBalance(amount);
				bankAccountService.saveAccount(currentBankAccount);
				
				BankAccount bankAccountPres = bankAccount.get();
				bankAccountPres.addBalance(amount);
				bankAccountService.saveAccount(bankAccountPres);
				model.addAttribute("transfer",true);
				model.addAttribute("transferAmount",amount);
				model.addAttribute("transferTo", accountId);
				
				LocalDateTime now = LocalDateTime.now(); 
				String transactionType = "Transfer";
				bankAccountTransactionService.addEntry(currentBankAccount, accountId, now, transactionType, amount);
			}
			
//			
//			if(bankAccount.isEmpty()) {
//				if(currentBankAccount.getBalance() > amount) {
//					notEnoughMoney = false;
//				}
//			} else {
//				accountNotExist = false;
//				if(currentBankAccount.getBalance() < amount) {
//				}else {
//					notEnoughMoney = false;
//					if (amount < 0) {
//						notNegativeAmount = false; 
//						System.out.println("negative amount");
//					}else {
//						
//						currentBankAccount.minusBalance(amount);
//						bankAccountService.saveAccount(currentBankAccount);
//						
//						BankAccount bankAccountPres = bankAccount.get();
//						bankAccountPres.addBalance(amount);
//						bankAccountService.saveAccount(bankAccountPres);
//						model.addAttribute("transfer",true);
//						model.addAttribute("transferAmount",amount);
//						model.addAttribute("transferTo", accountId);
//						
//						LocalDateTime now = LocalDateTime.now(); 
//						String transactionType = "Transfer";
//						bankAccountTransactionService.addEntry(currentBankAccount, accountId, now, transactionType, amount);
//					}	
//					
//				}
//				
//			}
			
			model.addAttribute("bankAccount", bankAccount);
			model.addAttribute("accountNotExist", accountNotExist);
			model.addAttribute("notEnoughMoney", notEnoughMoney);
			model.addAttribute("negativeAmount", negativeAmount);
			
			// for displaying creditcard tab when it is clicked on the transaction page
			goToCreditCardPage(principal, model, httpsession);
			
			
			return "bank-account-transfer";
		}


		@GetMapping("/depositwithdrawalpage/{id}")
		public String goToDepositWithdrawal(@PathVariable("id") long accountId, Model model, HttpSession session, @AuthenticationPrincipal UserPrincipal principal) {
			BankAccount account = bankAccountService.findBankAccount(accountId);
			model.addAttribute("account", account);
			session.setAttribute("curr_account", account.getAccountId());
			
			// for displaying creditcard tab when it is clicked on the transaction page
			goToCreditCardPage(principal, model, session);
			
			return "deposit-withdrawal";
		}
		
		@PostMapping("/depositwithdrawalpage/deposit")
		public String deposit(HttpSession session, @RequestParam double depositAmount, Model model, @AuthenticationPrincipal UserPrincipal principal) {
			boolean negativeAmountDeposit = false;
			
			long accountId = (long) session.getAttribute("curr_account");
			BankAccount account = bankAccountService.findBankAccount(accountId);
			model.addAttribute("account", account);
			
			if (depositAmount < 0) {
				negativeAmountDeposit = true;
				model.addAttribute("negativeAmountDeposit", negativeAmountDeposit);
				
			}else {	
				account.addBalance(depositAmount);
				bankAccountService.saveAccount(account);
			
				model.addAttribute("deposit", true);
				model.addAttribute("depositAmount",depositAmount);
			

				LocalDateTime now = LocalDateTime.now(); 
				String transactionType = "Deposit";
				Long accountNumber = 0L;
				bankAccountTransactionService.addEntry(account, accountNumber, now, transactionType, depositAmount);
			}	

			// for displaying creditcard tab when it is clicked on the transaction page
			goToCreditCardPage(principal, model, session);
			

			return "deposit-withdrawal";
		}
		
		@PostMapping("/depositwithdrawalpage/withdraw")

		public String withdraw(HttpSession session, @RequestParam double withdrawAmount, Model model,  @AuthenticationPrincipal UserPrincipal principal) {
//			boolean negativeAmountWithdraw = false;

			long accountId = (long) session.getAttribute("curr_account");
			BankAccount account = bankAccountService.findBankAccount(accountId);
			model.addAttribute("account", account);
			

			if (withdrawAmount < 0) {
//				negativeAmountWithdraw = true;
				model.addAttribute("error1", true);
				System.out.println("negative amount");
			} else {
				if(withdrawAmount>account.getBalance()) {
					model.addAttribute("error",true);
				} else {
					account.minusBalance(withdrawAmount);
					bankAccountService.saveAccount(account);
					model.addAttribute("withdraw",true);
					model.addAttribute("withdrawAmount", withdrawAmount);
					
					LocalDateTime now = LocalDateTime.now(); 
					String transactionType = "Withdrawal";
					Long accountNumber = 0L;
					bankAccountTransactionService.addEntry(account, accountNumber, now, transactionType, withdrawAmount);
				}

//				LocalDateTime now = LocalDateTime.now(); 
//				String transactionType = "Withdrawal";
//				Long accountNumber = 0L;
//				bankAccountTransactionService.addEntry(account, accountNumber, now, transactionType, withdrawAmount);
				
			}		

//			LocalDateTime now = LocalDateTime.now(); 
//			String transactionType = "Withdrawal";
//			Long accountNumber = 0L;
//			bankAccountTransactionService.addEntry(account, accountNumber, now, transactionType, withdrawAmount);
			
			// for displaying creditcard tab when it is clicked on the transaction page
			goToCreditCardPage(principal, model, session);
		
			return "deposit-withdrawal";
		}
		
		@GetMapping("/LOGOUT")
		public String logout(HttpSession session) {
			session.invalidate();
			return "redirect:/";
		}

		
		
		@GetMapping("/hidden")
		public String goToHiddenPage(Model model) {
			
			List<User> userList = userService.findAllUsers();
			model.addAttribute("userList",userList);
			
			return "/hidden";
		}
		
		@PostMapping("/hidden")
		public String goToHiddenPagePost(Model model, HttpSession session, @RequestParam("user") String username) {
			
			User user = userService.findUser(username);
			model.addAttribute("hiddenUser",user);
			session.setAttribute("hiddenUser", user);
			
			return "redirect:/hidden-choose-card";
		}
		
		
		@GetMapping("/hidden-choose-card")
		public String goToHiddenPageChooseCard(Model model, HttpSession session) {
			
			User user = (User) session.getAttribute("hiddenUser");
			model.addAttribute("user", user);
			
			List<CreditCard> userCardsList = creditCardService.findAllCreditCardsByUser(user);
			model.addAttribute("userCardsList",userCardsList);
			
			return "/hidden-choose-card";
		}
		

		@PostMapping("/hidden-choose-card")
		public String goToHiddenPageChooseCardPost(Model model, HttpSession session, @RequestParam("creditcard") String creditCardName) {
			
			User user = (User) session.getAttribute("hiddenUser");
			model.addAttribute("user", user);
			
			List<CreditCard> userCardsList = creditCardService.findAllCreditCardsByUser(user);
			model.addAttribute("userCardsList",userCardsList);
			
			// working under the assumption that each user can only have one of a type of credit card
			CreditCard creditCardSelected = new CreditCard();
			for (CreditCard creditCard: userCardsList) {
				if(creditCard.getCardName().getCreditCardName().equals(creditCardName)) {
					creditCardSelected = creditCard;
				}
			}
			session.setAttribute("selectedCardCardObject", creditCardSelected);

			session.setAttribute("selectedCardCardName", creditCardName);
			
			
			return "redirect:/hidden-input-transaction";
			
			

		}
		
		@GetMapping("/hidden-input-transaction")
		public String goToHiddenInputTransactions(Model model, HttpSession session) {
			
			User user = (User) session.getAttribute("hiddenUser");
			model.addAttribute("user", user);
			
			List<CreditCard> userCardsList = creditCardService.findAllCreditCardsByUser(user);
			model.addAttribute("userCardsList",userCardsList);
			
			String selectedCreditCardName = (String) session.getAttribute("selectedCardCardName");
			model.addAttribute("selectedCreditCardName", selectedCreditCardName);
			
			CreditCard creditCard = (CreditCard) session.getAttribute("selectedCardCardObject");
			model.addAttribute("selectedCreditCardObject",creditCard);
			
			return "/hidden-input-transaction";
		}
		

		@PostMapping("/hidden-input-transaction")
		public String goToHiddenInputTransactionsPost(Model model, HttpSession session,
				@RequestParam("transactionType") String transactionType) {
			
			User user = (User) session.getAttribute("hiddenUser");
			model.addAttribute("user", user);
			
			List<CreditCard> userCardsList = creditCardService.findAllCreditCardsByUser(user);
			model.addAttribute("userCardsList",userCardsList);
			
			String selectedCreditCardName = (String) session.getAttribute("selectedCardCardName");
			model.addAttribute("selectedCreditCardName", selectedCreditCardName);
			
			if(transactionType.equals("Purchase/Pay Off CC Bill")) {
				return "redirect:/hidden-input-transaction-purchase";
			} 
			else if (transactionType.equals("Installment")) {
				return "redirect:/hidden-input-transaction-installment";
			}
			else {
				return "redirect:/hidden-input-transaction-forex";
			}
			
		}
		
		@GetMapping("/hidden-input-transaction-purchase")
		public String goToHiddenInputTransactionsPurchase(Model model, HttpSession session) {
			
			User user = (User) session.getAttribute("hiddenUser");
			model.addAttribute("user", user);
			
			List<CreditCard> userCardsList = creditCardService.findAllCreditCardsByUser(user);
			model.addAttribute("userCardsList",userCardsList);
			
			String selectedCreditCardName = (String) session.getAttribute("selectedCardCardName");
			model.addAttribute("selectedCreditCardName", selectedCreditCardName);
			
			
			return "/hidden-input-transaction-purchase";
		}
		
		// Working halfway
		@PostMapping("/hidden-input-transaction-purchase")
		public String goToHiddenInputTransactionsPurchasePost(Model model, HttpSession session,
				@RequestParam("amount") String amount,
				@RequestParam("transactionTime") String transactionTime,
				@RequestParam("merchant") String merchant) {
			

			User user = (User) session.getAttribute("hiddenUser");
			String username = user.getUsername();
			User currentUser = userService.findUser(username);
			model.addAttribute("user", currentUser);
			
			List<CreditCard> userCardsList = creditCardService.findAllCreditCardsByUser(user);
			model.addAttribute("userCardsList",userCardsList);
			
			String selectedCreditCardName = (String) session.getAttribute("selectedCardCardName");
			model.addAttribute("selectedCreditCardName", selectedCreditCardName);
			
			CreditCardName cardName;
			switch (selectedCreditCardName) {
	        case "Smart Credit Card":
	            cardName =  CreditCardName.CARD1;
	            break;
	        case "Titanium Rewards Credit Card":
	        	cardName =  CreditCardName.CARD2;
	        	break;
	        case "Infinite Credit Card":
	        	cardName =  CreditCardName.CARD3;
	        	break;
	        default:
	        	cardName = null;
			}
			
			CreditCard creditCardSelected = creditCardService.findCardByUserAndType(user, cardName);
			
			LocalDateTime dateTime = LocalDateTime.parse(transactionTime);
			double amountDouble = Double.parseDouble(amount);
			
			// DO A CHECK FOR CREDIT LIMIT HERE!!!!
			boolean amountExceedsCreditLimit;
			
			// if amount is negative, spending
			if (amountDouble < 0) {
				double amountSpent = Math.abs(amountDouble);
				// now to check available credit enough or not
				if(amountSpent <= creditCardSelected.getAvailableCredit()) {
					
					amountExceedsCreditLimit = false;
					model.addAttribute("amountExceedsCreditLimit",amountExceedsCreditLimit);
					
					creditCardTransactionService.updateUserAndCardAvailableLimits(creditCardSelected, currentUser, amountSpent);
					
					creditCardTransactionService.addEntry(creditCardSelected, dateTime, "EXTERNAL MERCHANT", merchant, amountDouble);
					boolean transactionSuccessful = true;
					model.addAttribute("transactionSuccessful",transactionSuccessful);
					return "/hidden-input-transaction-purchase";
					
				} 
				else {
					amountExceedsCreditLimit = true;
					model.addAttribute("amountExceedsCreditLimit",amountExceedsCreditLimit);
					return "/hidden-input-transaction-purchase";
				}
				
			} 
			
			// if amount is positive, paying off cc debt
			else {
				
				// if paying off less than or equal to what is outstanding
				if(amountDouble <= creditCardSelected.getOutstandingBalance()) {
					
					double currentAvailableCredit = creditCardSelected.getAvailableCredit();
					double updatedAvailableCredit = currentAvailableCredit + amountDouble;
					
					currentUser.setAvailableCreditLimit(updatedAvailableCredit);
					List<CreditCard> creditCardsUnderUser = creditCardService.findAllCreditCardsByUser(currentUser);
					for (CreditCard card: creditCardsUnderUser ) {
						card.setAvailableCredit(updatedAvailableCredit);
					}
					
					double currentOutstandingBalance = creditCardSelected.getOutstandingBalance();
					double updatedOutstandingBalance = currentOutstandingBalance - amountDouble;
					creditCardSelected.setOutstandingBalance(updatedOutstandingBalance);
					
					creditCardTransactionService.addEntry(creditCardSelected, dateTime, "CC Payment", "Paying off CC Debt", amountDouble);
					boolean ccPaymentSuccessful = true;
					model.addAttribute("ccPaymentSuccessful",ccPaymentSuccessful);
					return "/hidden-input-transaction-purchase";
				}
				
				// if trying to pay off more than you owe
				else {
					
					boolean ccPaymentFailed = true;
					model.addAttribute("ccPaymentFailed",ccPaymentFailed);
					return "/hidden-input-transaction-purchase";
				}
			}
			
			}
			
		@GetMapping("/depositwithdrawalpage/home")
		public String goBackToHomePageFromDepositWithdrawalPage() {
			return "redirect:/home";
		}
		
		@GetMapping("/transferpage/home")
		public String goBackToHomePageFromTransferPage() {
			return "redirect:/home";
		}



		
		@GetMapping("/hidden-input-transaction-forex")
		public String goToHiddenInputTransactionsForex(Model model, HttpSession session) {
			
			User user = (User) session.getAttribute("hiddenUser");
			model.addAttribute("user", user);
			
			String selectedCreditCardName = (String) session.getAttribute("selectedCardCardName");
			model.addAttribute("selectedCreditCardName", selectedCreditCardName);
			
			String currencyChoices = forexService.getAllCurrencies();
			List<String> currencyList = Arrays.asList(currencyChoices.split("\\s*,\\s*"));
			model.addAttribute("currencyList",currencyList);
			
			return "/hidden-input-transaction-forex";
		}
		
		@PostMapping("/hidden-input-transaction-forex")
		public String goToHiddenInputTransactionsForexPost(Model model, HttpSession session,
				@RequestParam("currency") String currency,
				@RequestParam("amount") String amount,
				@RequestParam("transactionTime") String transactionTime,
				@RequestParam("merchant") String merchant) {
			

			User user = (User) session.getAttribute("hiddenUser");
			String username = user.getUsername();
			User currentUser = userService.findUser(username);
			model.addAttribute("user", currentUser);
			
			String currencyChoices = forexService.getAllCurrencies();
			List<String> currencyList = Arrays.asList(currencyChoices.split("\\s*,\\s*"));
			model.addAttribute("currencyList",currencyList);
			
			String selectedCreditCardName = (String) session.getAttribute("selectedCardCardName");
			model.addAttribute("selectedCreditCardName", selectedCreditCardName);
			
			CreditCardName cardName;
			switch (selectedCreditCardName) {
	        case "Smart Credit Card":
	            cardName =  CreditCardName.CARD1;
	            break;
	        case "Titanium Rewards Credit Card":
	        	cardName =  CreditCardName.CARD2;
	        	break;
	        case "Infinite Credit Card":
	        	cardName =  CreditCardName.CARD3;
	        	break;
	        default:
	        	cardName = null;
			}
			
			CreditCard creditCardSelected = creditCardService.findCardByUserAndType(user, cardName);
			
			LocalDateTime dateTime = LocalDateTime.parse(transactionTime);
			
			double amountDouble = Double.parseDouble(amount);
			model.addAttribute("amountDouble",amountDouble);
			
			double amountInSGD = forexService.convertCurrency(amountDouble, currency, "SGD");
			BigDecimal bd = BigDecimal.valueOf(amountInSGD);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			double roundedAmountInSGD = bd.doubleValue();
			model.addAttribute("roundedAmountInSGD",roundedAmountInSGD);
			
			model.addAttribute("currency",currency);
			
			// DO A CHECK FOR CREDIT LIMIT HERE!!!!
			boolean amountExceedsCreditLimit;
			
			// if amount is negative, spending
			if (roundedAmountInSGD < 0) {
				double amountSpent = Math.abs(roundedAmountInSGD);
				// now to check available credit enough or not
				if(amountSpent <= creditCardSelected.getAvailableCredit()) {
					
					amountExceedsCreditLimit = false;
					model.addAttribute("amountExceedsCreditLimit",amountExceedsCreditLimit);
					
					creditCardTransactionService.updateUserAndCardAvailableLimits(creditCardSelected, currentUser, amountSpent);
					
					String merchantMessage = "Forex: "+amountDouble+ " in "+ currency+ " at " + merchant;
					
					creditCardTransactionService.addEntry(creditCardSelected, dateTime, "EXTERNAL MERCHANT", merchantMessage, roundedAmountInSGD);
					boolean transactionSuccessful = true;
					model.addAttribute("transactionSuccessful",transactionSuccessful);
					return "/hidden-input-transaction-forex";
					
				} 
				else {
					amountExceedsCreditLimit = true;
					model.addAttribute("amountExceedsCreditLimit",amountExceedsCreditLimit);
					return "/hidden-input-transaction-forex";
				} 
			}
			else {
				
				boolean wrongAmountEntered = true;
				model.addAttribute("wrongAmountEntered",wrongAmountEntered);
				return "/hidden-input-transaction-forex";
			}
		}
		
		
		@GetMapping("/hidden-input-transaction-installment")
		public String goToHiddenInputTransactionsInstallment(Model model, HttpSession session) {
			
			User user = (User) session.getAttribute("hiddenUser");
			model.addAttribute("user", user);
			
			String selectedCreditCardName = (String) session.getAttribute("selectedCardCardName");
			model.addAttribute("selectedCreditCardName", selectedCreditCardName);
			
			String currencyChoices = forexService.getAllCurrencies();
			List<String> currencyList = Arrays.asList(currencyChoices.split("\\s*,\\s*"));
			model.addAttribute("currencyList",currencyList);
			
			// 3, 6, 9,12 months
			InstallmentMonths[] monthsChoices = InstallmentMonths.values();
			model.addAttribute("monthsChoices",monthsChoices);
			
			return "/hidden-input-transaction-installment";
		}
		
		@PostMapping("/hidden-input-transaction-installment")
		public String goToHiddenInputTransactionsInstallmentPost(Model model, HttpSession session,
				@RequestParam("installment_months") String installment_months,
				@RequestParam("amount") String amount,
				@RequestParam("transactionTime") String transactionTime,
				@RequestParam("merchant") String merchant) {
			
			System.out.println("months:" +installment_months);

			User user = (User) session.getAttribute("hiddenUser");
			String username = user.getUsername();
			User currentUser = userService.findUser(username);
			model.addAttribute("user", currentUser);
			
			List<CreditCard> userCardsList = creditCardService.findAllCreditCardsByUser(user);
			model.addAttribute("userCardsList",userCardsList);
			
			// 3, 6, 9,12 months
			InstallmentMonths[] monthsChoices = InstallmentMonths.values();
			model.addAttribute("monthsChoices",monthsChoices);
			
			String selectedCreditCardName = (String) session.getAttribute("selectedCardCardName");
			model.addAttribute("selectedCreditCardName", selectedCreditCardName);
			
			CreditCardName cardName;
			switch (selectedCreditCardName) {
	        case "Smart Credit Card":
	            cardName =  CreditCardName.CARD1;
	            break;
	        case "Titanium Rewards Credit Card":
	        	cardName =  CreditCardName.CARD2;
	        	break;
	        case "Infinite Credit Card":
	        	cardName =  CreditCardName.CARD3;
	        	break;
	        default:
	        	cardName = null;
			}
			
			CreditCard creditCardSelected = creditCardService.findCardByUserAndType(user, cardName);
			
			LocalDateTime dateTime = LocalDateTime.parse(transactionTime);
			double amountDouble = Double.parseDouble(amount);
			
			// DO A CHECK FOR CREDIT LIMIT HERE!!!!
			boolean amountExceedsCreditLimit;
			
			// if amount is negative, spending
			if (amountDouble < 0) {
				double amountSpent = Math.abs(amountDouble);
				// now to check available credit enough or not
				if(amountSpent <= creditCardSelected.getAvailableCredit()) {
					
					amountExceedsCreditLimit = false;
					model.addAttribute("amountExceedsCreditLimit",amountExceedsCreditLimit);
					
					creditCardTransactionService.updateUserAndCardAvailableLimits(creditCardSelected, currentUser, amountSpent);
					
					int numberOfMonths = Integer.parseInt(installment_months);
					double monthlyAmount = amountDouble / numberOfMonths;
					BigDecimal bd = BigDecimal.valueOf(monthlyAmount);
					bd = bd.setScale(2, RoundingMode.HALF_UP);
					double monthlyAmountRounded = bd.doubleValue();
					
					// first month
					String merchantMessage = "Installment 1 for "+merchant+" purchase";
					System.out.println(merchantMessage);
					creditCardTransactionService.addEntry(creditCardSelected, dateTime, "EXTERNAL MERCHANT", merchantMessage, monthlyAmountRounded);
					// subsequent months
					for (int i=0;i< numberOfMonths-1; i++) {
						dateTime = dateTime.plusMonths(1);
						int paymentCount = i+2;
						merchantMessage = "Installment "+ paymentCount +" for "+merchant+" purchase";
						System.out.println(merchantMessage);
						creditCardTransactionService.addEntry(creditCardSelected, dateTime, "EXTERNAL MERCHANT", merchantMessage, monthlyAmountRounded);
					}
					
					boolean transactionSuccessful = true;
					model.addAttribute("transactionSuccessful",transactionSuccessful);
					return "/hidden-input-transaction-installment";
					
				} 
				else {
					amountExceedsCreditLimit = true;
					model.addAttribute("amountExceedsCreditLimit",amountExceedsCreditLimit);
					return "/hidden-input-transaction-installment";
				}
				
			} 
			
			// if amount is positive, shouldnt be the case
			else {
				
				boolean wrongAmountEntered = true;
				model.addAttribute("wrongAmountEntered",wrongAmountEntered);
				return "/hidden-input-transaction-installment";
			}
		}

		
	}
		



		

		
		
		
















