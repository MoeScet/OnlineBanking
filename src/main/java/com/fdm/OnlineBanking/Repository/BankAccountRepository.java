package com.fdm.OnlineBanking.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdm.OnlineBanking.Model.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {

	Optional<BankAccount> findByBankAccNo(long bankAccNo);

}
