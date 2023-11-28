package com.fdm.OnlineBanking.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.OnlineBanking.Enum.AnnualIncome;
import com.fdm.OnlineBanking.Enum.CreditCardName;
import com.fdm.OnlineBanking.Enum.CreditLimit;
import com.fdm.OnlineBanking.Model.CreditCard;
import com.fdm.OnlineBanking.Model.User;
import com.fdm.OnlineBanking.Repository.CreditCardRepository;
import com.fdm.OnlineBanking.Repository.UserRepository;

@Service
public class CreditCardService {
	
	@Autowired
	private CreditCardRepository creditCardRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	
	
	public List<CreditCard> findAllCreditCards() {
		return creditCardRepo.findAll();		
	}
	
	public CreditCard findCreditCard(Long cardId) {
		return creditCardRepo.findById(cardId).get();
	}
	
	public List<CreditCard> findAllCreditCardsByUser(User user) {
		return creditCardRepo.findAllByUser(user);
	}
	
	public void saveCreditCard(String username, CreditCardName creditCardName) {
		LocalDate paymentDate = LocalDate.now().plusMonths(1);
		
		User user = userService.findUser(username);
		
		CreditCard creditCard = new CreditCard(user, creditCardName, 0, 0, paymentDate);
		creditCardRepo.save(creditCard);
		
		// assign user with credit limit for the first time
		if (user.getCreditLimit() == 0) {
			switch (user.getIncome()) {
//				case lowIncome:
//					user.setCreditLimit(CreditLimit.lowIncome.getCreditLimit());
//					user.setAvailableCreditLimit(CreditLimit.lowIncome.getCreditLimit());
//					break;
				case MiddleIncome:
					user.setCreditLimit(CreditLimit.MiddleIncome.getCreditLimit());
					user.setAvailableCreditLimit(CreditLimit.MiddleIncome.getCreditLimit());
					break;
				case HighIncome:
					user.setCreditLimit(CreditLimit.HighIncome.getCreditLimit());
					user.setAvailableCreditLimit(CreditLimit.HighIncome.getCreditLimit());
					break;
				case HighestIncome:
					user.setCreditLimit(CreditLimit.HighestIncome.getCreditLimit());
					user.setAvailableCreditLimit(CreditLimit.HighestIncome.getCreditLimit());
					break;
			}
		}
		userRepo.save(user);
		
		// assign user's available credit to the card
		double currentAvailableCredit = user.getAvailableCreditLimit();
		CreditCard updatedCard = creditCardRepo.findByUserAndCardName(user, creditCardName);
		updatedCard.setAvailableCredit(currentAvailableCredit);
		creditCardRepo.save(updatedCard);
	}
	
//	public List<CreditCard> findAllCreditCardsByUser(User user){
//		return creditCardRepo.findAllByUser(user);
//	}
	
	public CreditCard findCardByUserAndType(User user, CreditCardName creditCardName) {
		
		return creditCardRepo.findByUserAndCardName(user, creditCardName);
		
	}
	
	public void updateCreditCard(CreditCard creditCard) {
		creditCardRepo.save(creditCard);
	}
}
