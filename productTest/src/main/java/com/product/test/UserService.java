package com.product.test;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userrepository;
	
	public void userCreate(UserForm userForm) {
		this.userrepository.save(userForm.toEntity());
	}
}
