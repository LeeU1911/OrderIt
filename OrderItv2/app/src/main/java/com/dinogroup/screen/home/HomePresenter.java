package com.dinogroup.screen.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dinogroup.R;
import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;
import com.dinogroup.model.TableItem;
import com.dinogroup.model.TableItemAdapter;
import com.dinogroup.screen.status.StatusScreen;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseViewPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import flow.Flow;

/**
 * Created by EVL1HC on 1/18/2017.
 */

public class HomePresenter extends BaseViewPresenter<HomeView>{

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
            getTableData();
            view.addTableItemListener();
        }
    }

    private void configureActionBar() {
        ActionBarConfig config = new ActionBarConfig.Builder()
                .title("Tables")
                .visible(true)
                .enableHomeAsUp(false)
                .build();
        actionBarOwner.setConfig(config);
    }

    private void getTableData() {
        // Get database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("biz/1/tables");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                TableItem table = dataSnapshot.getValue(TableItem.class);
                if (table != null) {
                    Log.d(TAG, "Table name is: " + table.getName());
                    //display list of ponds
                    getView().updateTableList(table);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LOG.error(TAG, "Failed to read value.", databaseError.toException());
                Toast.makeText(getView().getContext(), "Failed to load data.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    public void addTable(String businessID, String tableID, String tableName, String tableStatus) {
        HomeView view = getView();
        int nTableStatus = 0;
        try {
            nTableStatus = Integer.parseInt(tableStatus);
        } catch (NumberFormatException e) {
            view.onNumberInputError(e);
            return;
        }
        TableItem tableItem = new TableItem(businessID, tableID, tableName, nTableStatus);
        // Get database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("biz/1/tables");

        tableID = databaseReference.push().getKey();
        tableItem.setId(tableID);
        databaseReference.child(tableID).setValue(tableItem);

    }

    public void StatusScreenNavigation(String tableID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("biz").child("1").child("processingTable1").setValue(tableID);

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("biz/1/processingTable");
//        databaseReference.removeValue();
//        databaseReference.push().setValue(tableID);

        flow.goTo(new StatusScreen());
    }
}
