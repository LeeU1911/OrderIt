package orderit.mainapp;

/**
 * Created by Blackcool on 12/23/15.
 */

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    Button btnLogin;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.loginUser);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);

        /** Sign in event */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                ValidateUser validateUser = new ValidateUser();
                /** Sign in with username and password */
                JSONObject json = validateUser.loginUser(email, password);
                /** Check result */
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        loginErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if (Integer.parseInt(res) == 1) {
                            /** Jump to table list screen */
                            Intent dashboard = new Intent(getApplicationContext(), TableList.class);
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                            /** Close activity */
                            finish();
                        } else {
                            /** Log in failed */
                            loginErrorMsg.setText("username or password is not correct.");
                        }
                    }
                } catch (JSONException e) {
                }
            }
        });
    }
}