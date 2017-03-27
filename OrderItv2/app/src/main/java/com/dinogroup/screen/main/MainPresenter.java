package com.dinogroup.screen.main;

import android.os.Bundle;

import com.dinogroup.SharedPreferencesKeys;
import com.dinogroup.model.User;
import com.dinogroup.repository.JsonSharedPreferencesRepository;
import com.dinogroup.screen.home.HomeScreen;
import com.dinogroup.screen.splash.SplashScreen;
import com.dinogroup.util.flow.FlowOwner;
import com.dinogroup.util.logging.Logger;
import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Singleton;

import flow.Parcer;
import mortar.Blueprint;

@Singleton
public class MainPresenter extends FlowOwner<Blueprint, MainView> {
	private static final Logger LOG = Logger.getLogger(MainPresenter.class);

	private final JsonSharedPreferencesRepository sharedPreferences;
	private final Bus bus;

	@Inject
	public MainPresenter(Parcer<Object> parcer, JsonSharedPreferencesRepository sharedPreferences, Bus bus) {
		super(parcer);
		this.sharedPreferences = sharedPreferences;
		this.bus = bus;
	}

	@Override public void onLoad(Bundle savedInstanceState) {
		LOG.debug("Registering with otto to receive bus events");
		bus.register(this);

		super.onLoad(savedInstanceState);
	}

	@Override public void dropView(MainView view) {
		LOG.debug("Un-registering with otto");
		bus.unregister(this);

		super.dropView(view);
	}

	@Override
	protected Blueprint getFirstScreen() {
		if (userAccountExists()) {
			return new HomeScreen();
		} else {
			LOG.warn("No user account found, redirecting to splash screen");
			//quicker testing n development
//			return new HomeScreen();
			return new SplashScreen();
		}
	}

	protected boolean userAccountExists() {
		return sharedPreferences.getObject(SharedPreferencesKeys.USER_ACCOUNT, User.class) != null;
	}
}
