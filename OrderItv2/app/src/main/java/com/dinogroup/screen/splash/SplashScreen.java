package com.dinogroup.screen.splash;

import com.dinogroup.R;

import flow.Layout;
import mortar.Blueprint;
import com.dinogroup.screen.main.MainScreen;

@Layout(R.layout.view_splash)
public class SplashScreen implements Blueprint {
	@Override
	public String getMortarScopeName() {
		return getClass().getName();
	}

	@Override
	public Object getDaggerModule() {
		return new Module();
	}

	@dagger.Module(
			injects = SplashView.class,
			addsTo = MainScreen.Module.class
	)
	class Module {}
}
