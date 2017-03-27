package com.dinogroup.screen.status;

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
public class StatusView extends BaseView {
    private static final Logger LOG = Logger.getLogger(StatusPresenter.class);
    private final String TAG = "StatusPresenter";
    @Inject
    StatusPresenter presenter;

    /**
     * Variables
     */
    AlertDialog alertDialog;

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @OnClick(R.id.order_row)
    public void onOrderRowClicked() {
        presenter.navigatingToOrder();
    }

    @OnClick(R.id.bill_row)
    public void onBillRowClicked() {
        presenter.navigatingToBill();
    }
}
