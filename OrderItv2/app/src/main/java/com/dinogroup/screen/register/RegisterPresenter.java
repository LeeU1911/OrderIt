package com.dinogroup.screen.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dinogroup.SharedPreferencesKeys;
import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;
import com.dinogroup.model.User;
import com.dinogroup.model.UserWithPassword;
import com.dinogroup.repository.JsonSharedPreferencesRepository;
import com.dinogroup.screen.home.HomeScreen;
import com.dinogroup.service.ApiService;
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
import rx.Subscription;
import rx.functions.Action1;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

class RegisterPresenter extends BaseViewPresenter<RegisterView> {

    private static final Logger LOG = Logger.getLogger(RegisterPresenter.class);
    private final String TAG = "RegisterPresenter";
    private final Flow flow;
    private final ActionBarOwner actionBarOwner;
    private final Validator validator;
    private final ApiService apiService;
    private final JsonSharedPreferencesRepository prefsRepository;

    private Subscription registerSubscription;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Inject
    RegisterPresenter(Flow flow, ActionBarOwner actionBarOwner, Validator validator, ApiService apiService, JsonSharedPreferencesRepository prefsRepository) {
        this.flow = flow;
        this.actionBarOwner = actionBarOwner;
        this.validator = validator;
        this.apiService = apiService;
        this.prefsRepository = prefsRepository;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);

        RegisterView view = getView();
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

    @Override
    public void dropView(RegisterView view) {
        if (registerSubscription != null && !registerSubscription.isUnsubscribed()) {
            registerSubscription.unsubscribe();
        }

        super.dropView(view);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void configureActionBar() {
        ActionBarConfig config = new ActionBarConfig.Builder()
                .title("Register")
                .build();
        actionBarOwner.setConfig(config);
    }

    public void register(String name, String email, String password) {
        RegisterView view = getView();

        UserWithPassword user = new UserWithPassword(trimToEmpty(name), trimToEmpty(email), trimToEmpty(password));
        Set<ConstraintViolation<UserWithPassword>> errors = validator.validate(user);

        if (errors.isEmpty()) {
            registerWithApi(user);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail",task.getException());
                                getView().onRegistrationError(task.getException());
                            } else {
                                Toast.makeText(getView().getContext(), "Account created.",
                                        Toast.LENGTH_SHORT).show();
                                flow.goTo(new HomeScreen());
                            }

                        }
                    });
        } else {
            view.onValidationError(errors);
        }
    }

    private void registerWithApi(UserWithPassword user) {
        registerSubscription = apiService.register(user)
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        storeUser(user);
                        flow.resetTo(new HomeScreen());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable thrown) {
                        getView().onRegistrationError(thrown);
                    }
                });
    }

    private void storeUser(User user) {
        prefsRepository.putObject(SharedPreferencesKeys.USER_ACCOUNT, user);
    }
}
