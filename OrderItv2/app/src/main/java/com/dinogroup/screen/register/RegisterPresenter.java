package com.dinogroup.screen.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dinogroup.SharedPreferencesKeys;
import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;
import com.dinogroup.model.Business;
import com.dinogroup.model.MenuCategory;
import com.dinogroup.model.MenuItem;
import com.dinogroup.model.TableItem;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
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

        final String finalName = trimToEmpty(name);
        final String finalEmail = trimToEmpty(email);

        UserWithPassword user = new UserWithPassword(trimToEmpty(name), trimToEmpty(email), trimToEmpty(password));
        Set<ConstraintViolation<UserWithPassword>> errors = validator.validate(user);

        if (errors.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(getView().getContext(), "Account creation failed.",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "createUserWithEmail",task.getException());
                            } else {
                                Toast.makeText(getView().getContext(), "Account created.",
                                        Toast.LENGTH_SHORT).show();
                                String curBuzId = makeNewBusinessData(finalName, finalEmail);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("businesses").child("CurrentBusiness").setValue(curBuzId);
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

    private String makeNewBusinessData(String name, String email) {
        Business newBuz = new Business();
        newBuz.setName(name);
        newBuz.setEmail(email);
        newBuz.setPhone("+841274452813");
        newBuz.setAddress("Ho Chi Minh city");
        newBuz.setType(1);  // Restaurant

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("businesses");
        String buzId = databaseReference.push().getKey();

        newBuz.setId(buzId);
        databaseReference.child(buzId).setValue(newBuz);

        // Make table data
        databaseReference = FirebaseDatabase.getInstance().getReference("businesses/"+buzId+"tables");
        {
            for (int n=1;n<=10;n++) {
                // Make Table data
                TableItem tableItem = new TableItem();
                tableItem.setBusinessId(buzId);
                tableItem.setName("Table "+n);
                tableItem.setStatus(0);
                String tableId = databaseReference.push().getKey();
                tableItem.setId(tableId);
                databaseReference.child(tableId).setValue(tableItem);
            }
        }

        // Make menu data
        databaseReference = FirebaseDatabase.getInstance().getReference("businesses/"+buzId+"menus");
        {
            // Starter
            MenuCategory menuCatStarter = new MenuCategory();
            menuCatStarter.setName("Starter");
            String cateId = databaseReference.push().getKey();
            menuCatStarter.setId(cateId);
            databaseReference.child(cateId).setValue(menuCatStarter);
            databaseReference = FirebaseDatabase.getInstance().getReference("businesses/"+buzId+"menus/"+cateId);
            for (int n=1;n<=10;n++) {
                MenuItem menuItem = new MenuItem();
                menuItem.setName("Starter"+n);
                menuItem.setAveMakeTime(10);
                menuItem.setAveMakeTimeUnit("min");
                menuItem.setPrice(2);
                menuItem.setPriceUnit("USD");
                menuItem.setCategoryId(cateId);

                String menuId = databaseReference.push().getKey();
                menuItem.setId(menuId);
                databaseReference.child(menuId).setValue(menuItem);
            }

            // Main
            MenuCategory menuCatMain = new MenuCategory();
            menuCatMain.setName("Main");
            cateId = databaseReference.push().getKey();
            menuCatMain.setId(cateId);
            databaseReference.child(cateId).setValue(menuCatMain);
            databaseReference = FirebaseDatabase.getInstance().getReference("businesses/"+buzId+"menus/"+cateId);
            for (int n=1;n<=10;n++) {
                MenuItem menuItem = new MenuItem();
                menuItem.setName("Main"+n);
                menuItem.setAveMakeTime(10);
                menuItem.setAveMakeTimeUnit("min");
                menuItem.setPrice(2);
                menuItem.setPriceUnit("USD");
                menuItem.setCategoryId(cateId);

                String menuId = databaseReference.push().getKey();
                menuItem.setId(menuId);
                databaseReference.child(menuId).setValue(menuItem);
            }

            // Dessert
            MenuCategory menuCatDessert = new MenuCategory();
            menuCatDessert.setName("Dessert");
            cateId = databaseReference.push().getKey();
            menuCatDessert.setId(cateId);
            databaseReference.child(cateId).setValue(menuCatDessert);
            databaseReference = FirebaseDatabase.getInstance().getReference("businesses/"+buzId+"menus/"+cateId);
            for (int n=1;n<=10;n++) {
                MenuItem menuItem = new MenuItem();
                menuItem.setName("Dessert"+n);
                menuItem.setAveMakeTime(10);
                menuItem.setAveMakeTimeUnit("min");
                menuItem.setPrice(2);
                menuItem.setPriceUnit("USD");
                menuItem.setCategoryId(cateId);

                String menuId = databaseReference.push().getKey();
                menuItem.setId(menuId);
                databaseReference.child(menuId).setValue(menuItem);
            }

            // Drink
            MenuCategory menuCatDrink = new MenuCategory();
            menuCatDrink.setName("Drink");
            cateId = databaseReference.push().getKey();
            menuCatDrink.setId(cateId);
            databaseReference.child(cateId).setValue(menuCatDrink);
            databaseReference = FirebaseDatabase.getInstance().getReference("businesses/"+buzId+"menus/"+cateId);
            for (int n=1;n<=10;n++) {
                MenuItem menuItem = new MenuItem();
                menuItem.setName("Drink"+n);
                menuItem.setAveMakeTime(10);
                menuItem.setAveMakeTimeUnit("min");
                menuItem.setPrice(2);
                menuItem.setPriceUnit("USD");
                menuItem.setCategoryId(cateId);

                String menuId = databaseReference.push().getKey();
                menuItem.setId(menuId);
                databaseReference.child(menuId).setValue(menuItem);
            }
        }

        return buzId;
    }
}
