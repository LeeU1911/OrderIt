package com.dinogroup.screen.login;

import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.dinogroup.test.util.ViewTestHelper.mockView;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class LoginPresenterTest {

	@Mock ActionBarOwner actionBarOwner;

	@Captor ArgumentCaptor<ActionBarConfig> actionBarConfigCaptor;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);

		LoginView view = mockView(LoginView.class);

		LoginPresenter presenter = new LoginPresenter(null, actionBarOwner, null);
		presenter.takeView(view);
	}

	@Test
	public void shouldConfigureActionBarOnLoad() throws Exception {
		verify(actionBarOwner).setConfig(actionBarConfigCaptor.capture());

		ActionBarConfig config = actionBarConfigCaptor.getValue();
		assertThat(config.getTitle()).isEqualTo("Login");
		assertThat(config.isVisible()).isTrue();
	}
}
