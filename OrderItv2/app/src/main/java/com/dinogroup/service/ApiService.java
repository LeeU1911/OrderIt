package com.dinogroup.service;

import com.dinogroup.model.PasswordAuthentication;
import com.dinogroup.model.User;
import com.dinogroup.model.UserToken;
import com.dinogroup.model.UserWithPassword;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import rx.Observable;

/**
 * A sample interface to an API that might exist on your server.
 */
public interface ApiService {

  /**
   * Registers a new user account.
   */
  @POST("/user")
  Observable<User> register(@Body UserWithPassword user);

  /**
   * Perform a user login.
   */
  @POST("/user/login")
  Observable<UserToken> login(@Body PasswordAuthentication authentication);

  /**
   * Perform a user logout.
   */
  @POST("user/logout")
  Observable<Void> logout(@Header("Authorization") UserToken token);
}
