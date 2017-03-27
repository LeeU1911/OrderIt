package com.dinogroup.screen.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;
import com.dinogroup.model.PasswordAuthentication;
import com.dinogroup.screen.home.HomeScreen;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseViewPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import flow.Flow;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

class LoginPresenter extends BaseViewPresenter<LoginView> {

	private static final Logger LOG = Logger.getLogger(LoginPresenter.class);
	private final String TAG = "LoginPresenter";
	private ActionBarOwner actionBarOwner;
	private final Validator validator;

	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;

    private final Flow flow;

	@Inject
	LoginPresenter(Flow flow, ActionBarOwner actionBarOwner, Validator validator) {
        this.flow = flow;
		this.actionBarOwner = actionBarOwner;
		this.validator = validator;
	}

	@Override
	public void dropView(LoginView view) {
		super.dropView(view);
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}

	@Override
	protected void onLoad(Bundle savedInstanceState) {
		super.onLoad(savedInstanceState);

		LoginView view = getView();
		if (view != null) {
			configureActionBar();
		}
		mAuth = FirebaseAuth.getInstance();
		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
				} else {
					Log.d(TAG, "onAuthStateChanged: signed_out");
				}
			}
		};
		mAuth.addAuthStateListener(mAuthListener);
	}

	private void configureActionBar() {
		ActionBarConfig config = new ActionBarConfig.Builder()
				.title("Login")
				.build();
		actionBarOwner.setConfig(config);
	}

	public void login(String email, String password) {
		LoginView view = getView();

		PasswordAuthentication passwordAuthentication = new PasswordAuthentication(trimToEmpty(email), trimToEmpty(password));
		Set<ConstraintViolation<PasswordAuthentication>> errors = validator.validate(passwordAuthentication);

		if (errors.isEmpty()) {
			mAuth.signInWithEmailAndPassword(email, password)
					.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

							// If sign in fails, display a message to the user. If sign in succeeds
							// the auth state listener will be notified and logic to handle the
							// signed in user can be handled in the listener.
							if (!task.isSuccessful()) {
								Log.w(TAG, "signInWithEmail", task.getException());
								Toast.makeText(getView().getContext(), "Login failed.",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getView().getContext(), "Login successfully.",
										Toast.LENGTH_SHORT).show();
								flow.goTo(new HomeScreen());
							}
						}
					});
		}else{
			view.onValidationError(errors);
		}
	}
}
