package com.product.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Component
public class UserLoginService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	@Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        Optional<UserEntity> findOne = this.userRepository.findByUserId(userid);
        if(!findOne.isPresent()) {
        	new UsernameNotFoundException("없는 회원입니다");
        }
        UserEntity user = findOne.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        return new User(user.getUserId(), user.getPassword(), authorities);
    }
}
