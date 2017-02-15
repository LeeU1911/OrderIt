package com.dinogroup.screen.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.dinogroup.R;
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
    @Inject
    HomePresenter presenter;


    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

}
