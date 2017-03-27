package com.dinogroup.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class PasswordAuthentication {
  @NotBlank(message = "Please provide your email address")
  @Email(message = "Please provide a valid email address")
  private final String email;

  @NotBlank(message = "Please provide a password")
  private final String password;

  public PasswordAuthentication(String email, String password) {
    this.email = email;
    this.password = password;
  }

  @Override
  public String toString() {
    return email;
  }
}
