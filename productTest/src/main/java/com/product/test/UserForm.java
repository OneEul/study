package com.product.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserForm {
	
	@NotEmpty(message = "id는 필수항목입니다.")
	private String userId;
	
	@NotEmpty(message = "비밀번호는 필수항목입니다.")
	private String password;
	
	@NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
	private String password2;
	
	@NotEmpty(message = "이름은 필수항목입니다.")
	private String name;
	
	public UserEntity toEntity() {
		return UserEntity.builder().userId(userId).password(new BCryptPasswordEncoder().encode(password)).name(name).build();
	}
}
