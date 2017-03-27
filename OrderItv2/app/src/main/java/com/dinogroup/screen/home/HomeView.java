package com.dinogroup.screen.home;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.dinogroup.R;
import com.dinogroup.model.*;
import com.dinogroup.model.TableItem;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Presenter;

/**
 * Created by EVL1HC on 1/18/2017.
 */
public class HomeView extends BaseView {
    private static final Logger LOG = Logger.getLogger(HomePresenter.class);
    private final String TAG = "HomePresenter";
    @Inject HomePresenter presenter;

    /**
     * Variables
     */
    private TableItemAdapter tableItemAdapter;
    @InjectView(R.id.TableList) public ListView listView;
    private List<TableItem> globTableItems;
    AlertDialog alertDialog;

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    private void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_input_new_pond, this, false);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editTextBuzID = (EditText) promptView.findViewById(R.id.editTextBuzID);
        final EditText editTextTableID = (EditText) promptView.findViewById(R.id.editTextTableID);
        final EditText editTextTableName = (EditText) promptView.findViewById(R.id.editTextTableName);
        final EditText editTextTableStat = (EditText) promptView.findViewById(R.id.editTextTableStat);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String businessID = editTextBuzID.getText().toString();
                        String tableID = editTextTableID.getText().toString();
                        String tableName = editTextTableName.getText().toString();
                        String tableStat = editTextTableStat.getText().toString();
                        presenter.addTable(businessID, tableID, tableName, tableStat);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @OnClick(R.id.fab_add_pond_btn)
    public void onAddPondButtonClicked() {
        showInputDialog();
    }

    public void onNumberInputError(Throwable thrown) {
        alertDialog = showBasicAlertDialog(getString(R.string.dialog_error_title), thrown.getMessage());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void updateTableList(TableItem table) {
        if (tableItemAdapter != null) {
            globTableItems.add(table);
            tableItemAdapter.setTableItemListList(globTableItems);
            tableItemAdapter.notifyDataSetChanged();
        } else {
            createTableListView(table);
        }
    }

    public void createTableListView(TableItem tableItem) {
        if (tableItemAdapter != null) {
            tableItemAdapter = null;
        }
        globTableItems = new ArrayList<>();
        globTableItems.add(tableItem);
        tableItemAdapter = new TableItemAdapter(this.getContext(), globTableItems);
        listView.setAdapter(tableItemAdapter);
    }

    public void addTableItemListener() {
        /** Set event listener to ListView */
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Item click " + globTableItems.get(position).getId());
                presenter.StatusScreenNavigation(globTableItems.get(position).getId());
            }
        });
    }
}
