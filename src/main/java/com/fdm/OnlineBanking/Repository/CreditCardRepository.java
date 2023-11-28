package com.fdm.OnlineBanking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.fdm.OnlineBanking.Enum.CreditCardName;

import com.fdm.OnlineBanking.Model.CreditCard;
import com.fdm.OnlineBanking.Model.User;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {


	List<CreditCard> findAllByUser(User user);

	CreditCard findByUserAndCardName(User user, CreditCardName creditCardName);
	
//	@Query("SELECT c FROM CreditCard c WHERE c.user.userId = :userId AND c.cardName = :cardName")
//	CreditCard findByUserAndCardName(@Param("userId") Long userId, @Param("cardName") String cardName);

}
