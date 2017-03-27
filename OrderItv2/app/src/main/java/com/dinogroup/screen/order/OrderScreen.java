package com.dinogroup.screen.order;

import com.dinogroup.R;
import com.dinogroup.screen.main.MainScreen;
import com.dinogroup.screen.status.StatusScreen;

import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;

@Layout(R.layout.activity_table_order)
public class OrderScreen implements Blueprint, HasParent<StatusScreen> {
    @Override
    public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
        return new Module();
    }

    @Override
    public StatusScreen getParent() {
        return new StatusScreen();
    }

    @dagger.Module(
            injects = OrderView.class,
            addsTo = MainScreen.Module.class
    )
    class Module {
    }
}
