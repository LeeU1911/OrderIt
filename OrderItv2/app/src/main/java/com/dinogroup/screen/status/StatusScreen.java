package com.dinogroup.screen.status;

import com.dinogroup.R;
import com.dinogroup.screen.home.HomePresenter;
import com.dinogroup.screen.main.MainScreen;

import javax.inject.Singleton;

import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;

@Layout(R.layout.activity_table_status)
public class StatusScreen implements Blueprint, HasParent<MainScreen> {

    @Override
    public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
        return new Module();
    }

    @Override
    public MainScreen getParent() {
        return new MainScreen();
    }

    @dagger.Module(
            injects = StatusView.class,
            addsTo = MainScreen.Module.class
    )
    class Module {
    }
}
