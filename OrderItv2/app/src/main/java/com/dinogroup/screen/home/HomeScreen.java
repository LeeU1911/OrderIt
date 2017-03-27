package com.dinogroup.screen.home;

import com.dinogroup.R;
import com.dinogroup.screen.login.LoginScreen;
import com.dinogroup.screen.main.MainScreen;

import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;

@Layout(R.layout.view_home)
public class HomeScreen implements Blueprint, HasParent<LoginScreen> {
    @Override
    public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
        return new Module();
    }

    @Override
    public LoginScreen getParent() {
        return new LoginScreen();
    }

    @dagger.Module(
            injects = HomeView.class,
            addsTo = MainScreen.Module.class
    )
    class Module {
    }
}
