package orderit.mainapp.activity;

/**
 * Created by Blackcool on 12/23/15.
 */

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import orderit.mainapp.utility.JSONParser;
import orderit.mainapp.R;

public class LoginActivity extends Activity {
    Button btnLogin;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;

    private static String KEY_SUCCESS = "response";

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
                /** Sign in with username and password */
                loginUser(email, password);
            }
        });
    }

    private void loginUser(String username, String password){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        AsyncServerCall asyncServerCall = new AsyncServerCall();
        asyncServerCall.execute(params);
    }

    class AsyncServerCall extends AsyncTask<List<NameValuePair>, Void, String> {

        @Override
        protected String doInBackground(List<NameValuePair>... list) {
            String loginURL = "http://192.168.1.6:8080";
            JSONObject json = new JSONParser().getJSONFromUrl(loginURL, list[0]);
            /** return JSON object */
            return json.toString();
        }

        protected void onPostExecute(String result){
            JSONObject json = null;
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                loginErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    if (res.equalsIgnoreCase("success")) {
                        /** Jump to table list screen */
                        Intent dashboard = new Intent(getApplicationContext(), TableListActivity.class);
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
    }
}