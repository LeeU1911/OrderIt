package com.dinogroup.screen.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.dinogroup.R;
import com.dinogroup.model.TableItem;
import com.dinogroup.model.TableItemAdapter;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Presenter;

/**
 * Created by EVL1HC on 1/18/2017.
 */
public class OrderView extends BaseView {
    private static final Logger LOG = Logger.getLogger(OrderPresenter.class);
    private final String TAG = "OrderPresenter";
    @Inject
    OrderPresenter presenter;

    /**
     * Variables
     */
    AlertDialog alertDialog;

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
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
}
