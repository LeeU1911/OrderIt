package com.dinogroup.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class UserWithPassword extends User {
	@NotBlank(message = "Please provide a password")
	@Length(min = 6, message = "Password must be at least 6 characters")
	private final String password;

	public UserWithPassword(String name, String email, String password) {
		super(name, email);

		this.password = password;
	}
}
