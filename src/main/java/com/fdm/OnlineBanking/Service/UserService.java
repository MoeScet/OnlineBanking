package com.fdm.OnlineBanking.Service;

import org.springframework.stereotype.Service;

import com.fdm.OnlineBanking.Model.BankAccount;
import com.fdm.OnlineBanking.Model.User;
import com.fdm.OnlineBanking.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public List<User> findAllUsers() {
		return userRepo.findAll();		
	}
	
	public User findUser(String username) {
		return userRepo.findByUsername(username).get();
	}
	
	public Optional<User> checkUsername(String username){
		return userRepo.findByUsername(username);
	}
	
	
	public boolean registerNewUser(User user) {
		Optional<User> userOptional = userRepo.findByUsername(user.getUsername());
		if(userOptional.isEmpty()) {
			System.out.println("it is true");
			String pw = user.getPassword();
			String encodedPw = passwordEncoder.encode(pw);
			user.setPassword(encodedPw);
			userRepo.save(user);
			return true;
		} else {
			System.out.println("it is returning false");
			return false;
		}
	}
	
	public void saveUser(User user) {
		userRepo.save(user);
	}
	
	public boolean findVerifiedUser(String username) {
		Optional<User> userOptional = userRepo.findByUsername(username);
		if (userOptional.isPresent()) {
			return true;
		} else {
			return false;
		}
				
	}

	public User verfiedUserIdentity(String username) {
		return userRepo.findByUsername(username).get();
	}

	public Optional<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
}
