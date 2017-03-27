package com.dinogroup.screen.login;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.dinogroup.R;

import com.dinogroup.screen.main.MainScreen;
import com.dinogroup.screen.splash.SplashScreen;
import com.dinogroup.test.util.BlueprintVerifier;

@RunWith(RobolectricTestRunner.class)
public class LoginScreenTest {

	private LoginScreen screen;

	@Before
	public void before() throws Exception {
		screen = new LoginScreen();
	}

	@Test
	public void shouldDefineBlueprint() throws Exception {
		BlueprintVerifier.forScreen(screen)
				.injectsView(LoginView.class)
				.addsToModule(MainScreen.Module.class)
				.hasLayout(R.layout.view_login)
				.verify();
	}

	@Test
	public void shouldDefineParent() throws Exception {
		assertThat(screen.getParent()).isInstanceOf(SplashScreen.class);
	}
}
