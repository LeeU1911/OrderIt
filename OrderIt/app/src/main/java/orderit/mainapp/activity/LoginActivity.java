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
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import orderit.mainapp.utility.JSONParser;
import orderit.mainapp.R;
import orderit.mainapp.application.AppProcess;

public class LoginActivity extends Activity {
    Button btnLogin;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;

    private int background; /** id of the image drawable */
    private int layoutId; /** id of the activity layout */
    private ImageView loginIcon; /** login icon */
    private int loginIconImg; /** id of the image drawable */
    private RelativeLayout layout; /** the layout of the activity */

    private static String KEY_SUCCESS = "response";
    private AppProcess appProc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /** Set display of controls programmatically */
        setControlDisplay();
    }

    private void setControlDisplay() {
        inputUsername = (EditText) findViewById(R.id.loginUser);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginIcon = (ImageView) findViewById(R.id.login_icon);

        background = R.drawable.login;
        loginIconImg = R.drawable.login_icon;
        layoutId = R.id.relativelogin;
        layout = (RelativeLayout) findViewById(layoutId);

        appProc = (AppProcess)getApplication();
        appProc.setBackground(layout, background); /** free last background, and store new one */
        //appProc.setImage(loginIcon, loginIconImg); /** free last image, and store new one */

        Typeface face=Typeface.createFromAsset(getAssets(), "fonts/FuturaStd-Light.ttf");

        RelativeLayout.LayoutParams paramUser;
        RelativeLayout.LayoutParams paramPassword;
        RelativeLayout.LayoutParams paramLoginBtn;
        RelativeLayout.LayoutParams paramLoginIcon;

        paramUser = (RelativeLayout.LayoutParams) inputUsername.getLayoutParams();
        paramUser.leftMargin = 10;
        paramUser.rightMargin = 10;
        paramUser.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramUser.height = 130;
        paramUser.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramUser.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramUser.addRule(RelativeLayout.CENTER_IN_PARENT);

        paramPassword = (RelativeLayout.LayoutParams) inputPassword.getLayoutParams();
        paramPassword.leftMargin = 10;
        paramPassword.rightMargin = 10;
        paramPassword.topMargin = paramUser.bottomMargin + 15;
        paramPassword.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramPassword.height = 130;
        paramPassword.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramPassword.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        paramLoginBtn = (RelativeLayout.LayoutParams) btnLogin.getLayoutParams();
        paramLoginBtn.rightMargin = 10;
        paramLoginBtn.topMargin = paramPassword.bottomMargin + 20;
        paramLoginBtn.width = 330;
        paramLoginBtn.height = 130;
        paramLoginBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        paramLoginIcon = (RelativeLayout.LayoutParams) loginIcon.getLayoutParams();
        paramLoginIcon.leftMargin = 10;
        paramLoginIcon.bottomMargin = paramUser.topMargin + 20;
        paramLoginIcon.width = 250;
        paramLoginIcon.height = 250;
        paramLoginIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        inputUsername.setLayoutParams(paramUser);
        inputUsername.setBackgroundColor(Color.parseColor("#f8ad19"));
        inputUsername.setAlpha((float) (0.7));
        inputUsername.setHint("user");
        inputUsername.setHintTextColor(Color.parseColor("#ffffff"));
        inputUsername.setTextColor(Color.parseColor("#ffffff"));
        inputUsername.setTextSize(20);
        inputUsername.setTypeface(face);
        inputUsername.setGravity(Gravity.CENTER_VERTICAL);

        inputPassword.setLayoutParams(paramPassword);
        inputPassword.setBackgroundColor(Color.parseColor("#f8ad19"));
        inputPassword.setAlpha((float) (0.8));
        inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        inputPassword.setHint("password");
        inputPassword.setHintTextColor(Color.parseColor("#ffffff"));
        inputPassword.setTextColor(Color.parseColor("#ffffff"));
        inputPassword.setTextSize(20);
        inputPassword.setTypeface(face);
        inputPassword.setGravity(Gravity.CENTER_VERTICAL);

        btnLogin.setLayoutParams(paramLoginBtn);
        btnLogin.setBackgroundColor(Color.parseColor("#f8ad19"));
        btnLogin.setAlpha((float) (0.8));
        btnLogin.setText("sign in");
        btnLogin.setTextColor(Color.parseColor("#ffffff"));
        btnLogin.setTextSize(20);
        btnLogin.setTypeface(face);
        btnLogin.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);

        loginIcon.setLayoutParams(paramLoginIcon);
        loginIcon.setAlpha((float) (0.8));

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