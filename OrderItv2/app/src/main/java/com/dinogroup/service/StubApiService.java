package com.dinogroup.service;

import android.annotation.TargetApi;
import android.os.Build;

import com.dinogroup.model.PasswordAuthentication;
import com.dinogroup.model.User;
import com.dinogroup.model.UserToken;
import com.dinogroup.model.UserWithPassword;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import retrofit.http.Body;
import retrofit.http.Header;
import rx.Observable;

import static java.util.UUID.randomUUID;

/**
 * An offline version of the ApiService which can be used to facilitate local development
 * and testing.
 */
public class StubApiService implements ApiService {

	private final Map<String, UserWithPassword> registeredUsers = new HashMap<>();

	@Override
	public Observable<User> register(@Body UserWithPassword user) {
		User newUser = new User(user.getName(), user.getEmail());
		registeredUsers.put(newUser.getEmail(), user);
		return Observable.just(newUser);
	}

	@Override
	public Observable<UserToken> login(@Body PasswordAuthentication authentication) {
		String username = getFieldValue(authentication, "username");
		String password = getFieldValue(authentication, "password");

		UserWithPassword userWithPassword = registeredUsers.get(username);
		if (userWithPassword != null) {
			if (StringUtils.equals(password, getFieldValue(userWithPassword, "password"))) {
				return Observable.just(new UserToken(username, randomUUID().toString()));
			}
		}

		return Observable.error(new RuntimeException("Username or password is incorrect."));
	}

	@Override
	public Observable<Void> logout(@Header("Authorization") UserToken token) {
		return Observable.empty();
	}


	@TargetApi(Build.VERSION_CODES.KITKAT)
	private String getFieldValue(Object object, String fieldName) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return (String) field.get(object);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
