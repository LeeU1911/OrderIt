package com.dinogroup.screen.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseViewPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import flow.Flow;

/**
 * Created by EVL1HC on 1/18/2017.
 */

public class HomePresenter extends BaseViewPresenter<HomeView> {

    private static final Logger LOG = Logger.getLogger(HomePresenter.class);
    private final String TAG = "HomePresenter";
    private ActionBarOwner actionBarOwner;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final Flow flow;

    @Inject
    HomePresenter(Flow flow, ActionBarOwner actionBarOwner) {
        this.flow = flow;
        this.actionBarOwner = actionBarOwner;
    }

    @Override
    public void dropView(HomeView view) {
        super.dropView(view);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
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
        HomeView view = getView();
        if (view != null) {
            configureActionBar();
        }

    }

    private void configureActionBar() {
        ActionBarConfig config = new ActionBarConfig.Builder()
                .title("Home")
                .visible(true)
                .enableHomeAsUp(false)
                .build();
        actionBarOwner.setConfig(config);
    }

}
