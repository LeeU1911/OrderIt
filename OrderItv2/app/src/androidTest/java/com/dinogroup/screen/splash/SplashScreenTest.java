package com.dinogroup.screen.splash;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.dinogroup.R;

import com.dinogroup.screen.main.MainScreen;
import com.dinogroup.test.util.BlueprintVerifier;

@RunWith(RobolectricTestRunner.class)
public class SplashScreenTest {
	@Test
	public void shouldDefineBlueprint() throws Exception {
		BlueprintVerifier.forScreen(new SplashScreen())
				.injectsView(SplashView.class)
				.addsToModule(MainScreen.Module.class)
				.hasLayout(R.layout.view_splash)
				.verify();
	}
}
