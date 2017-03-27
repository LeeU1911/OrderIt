package com.dinogroup.screen.login;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

import com.dinogroup.R;
import com.dinogroup.model.PasswordAuthentication;
import com.dinogroup.util.mortar.BaseView;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Presenter;

public class LoginView extends BaseView {

	@Inject LoginPresenter presenter;

	@InjectView(R.id.email_field) EditText emailEditText;
	@InjectView(R.id.password_field) EditText passwordEditText;
	@InjectView(R.id.login_button) Button loginButton;

	public LoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected Presenter getPresenter() {
		return presenter;
	}

	public void onValidationError(Set<ConstraintViolation<PasswordAuthentication>> errors) {
		loginButton.setEnabled(true);

		for (ConstraintViolation<PasswordAuthentication> error : errors) {
			String property = error.getPropertyPath().toString();
			if (property.equals("email")) {
				emailEditText.setError(error.getMessage());
			} else if (property.equals("password")) {
				passwordEditText.setError(error.getMessage());
			}
		}
	}

	@OnClick(R.id.login_button)
	public void onLoginButtonClicked() {
//		loginButton.setEnabled(false);

		hideKeyboard();

		String email = emailEditText.getText().toString();
		String password = passwordEditText.getText().toString();

		presenter.login(email, password);
	}
}
