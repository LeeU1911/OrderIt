package com.dinogroup.screen.splash;

import static org.mockito.Mockito.*;
import static com.dinogroup.test.util.ViewTestHelper.createView;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.dinogroup.R;

import dagger.Provides;
import flow.Layout;
import mortar.Blueprint;
import mortar.Mortar;
import com.dinogroup.TestApplicationModule;

@RunWith(RobolectricTestRunner.class)
public class SplashViewTest {

	@Inject SplashPresenter presenter;

	private SplashView view;

	@Before
	public void before() throws Exception {
		view = createView(SplashView.class).forScreen(new MockSplashScreen());
		Mortar.inject(view.getContext(), this);
	}

	@Test
	public void shouldLogon() throws Exception {
		view.loginButton.performClick();

		verify(presenter).login();
	}

	@Test
	public void shouldRegister() throws Exception {
		view.registerButton.performClick();

		verify(presenter).register();
	}

	@Layout(R.layout.view_splash)
	static class MockSplashScreen implements Blueprint {
		@Override
		public String getMortarScopeName() {
			return getClass().getName();
		}

		@Override
		public Object getDaggerModule() {
			return new Module();
		}

		@dagger.Module(
				injects = { SplashView.class, SplashViewTest.class },
				addsTo = TestApplicationModule.class
		)
		class Module {
			@Provides
			@Singleton
			SplashPresenter provideMockPresenter() {
				return mock(SplashPresenter.class, RETURNS_DEEP_STUBS);
			}
		}
	}
}
