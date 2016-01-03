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

import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.database.DatabaseOpenHelper;
import orderit.mainapp.utility.JSONParser;
import orderit.mainapp.R;
import orderit.mainapp.application.AppProcess;

public class LoginActivity extends Activity {
    Button btnLogin;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;

    private int background;         /** id of the image drawable */
    private int layoutId;           /** id of the activity layout */
    private ImageView loginIcon;    /** login icon */
    private int loginIconImg;       /** id of the image drawable */
    private RelativeLayout layout;  /** the layout of the activity */
    private static String KEY_SUCCESS = "response";
    private AppProcess appProcess;

    DatabaseAccess databaseAccess = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseAccess = DatabaseAccess.getInstance(this);
        /** Set display of controls programmatically */
        //setControlDisplay();
        Button button = (Button)findViewById(R.id.btnLoggin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUserName = (EditText)findViewById(R.id.txtUserName);
                String userName = txtUserName.getText().toString();
                EditText txtPassword =(EditText)findViewById(R.id.txtPassword);
                String password = txtPassword.getText().toString();


                String _password = databaseAccess.SearchPassword(userName);
                if(password.equalsIgnoreCase(_password))//Logging successfully
                {
                    /** Jump to table list screen */
                    Intent dashboard = new Intent(getApplicationContext(), TableListActivity.class);
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);
                    /** Close activity */
                }
                else
                {
                    TextView error = (TextView)findViewById(R.id.lblLogginErrMsg);
                    error.setText("Tên đăng nhập hoặc mật khẩu không đúng");
                }

            }
        });
    }


    private void setControlDisplay() {
        inputUsername = (EditText) findViewById(R.id.txtUserName);
        inputPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLoggin);
        //loginIcon = (ImageView) findViewById(R.id.login_icon);
        //layout = (RelativeLayout) findViewById(R.id.relativeLogin);

        background = R.drawable.login;
        loginIconImg = R.drawable.login_icon;

        appProcess = (AppProcess)getApplication();
        appProcess.setBackground(layout, background); /** free last background, and store new one */

        Typeface face=Typeface.createFromAsset(getAssets(), "fonts/FuturaStd-Light.ttf");

        RelativeLayout.LayoutParams paramUser;
        RelativeLayout.LayoutParams paramPassword;
        RelativeLayout.LayoutParams paramLoginBtn;
        RelativeLayout.LayoutParams paramLoginIcon;

        float alpha = (float)(0.9);
        int editHeight = 130;
        int iconSize = 220;
        int margin = 30;

        paramUser = (RelativeLayout.LayoutParams) inputUsername.getLayoutParams();
        paramUser.leftMargin = margin;
        paramUser.rightMargin = margin;
        paramUser.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramUser.height = editHeight;
        paramUser.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramUser.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramUser.addRule(RelativeLayout.CENTER_IN_PARENT);

        paramPassword = (RelativeLayout.LayoutParams) inputPassword.getLayoutParams();
        paramPassword.leftMargin = margin;
        paramPassword.rightMargin = margin;
        paramPassword.topMargin = paramUser.bottomMargin + 15;
        paramPassword.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramPassword.height = editHeight;
        paramPassword.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramPassword.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        paramLoginBtn = (RelativeLayout.LayoutParams) btnLogin.getLayoutParams();
        paramLoginBtn.rightMargin = margin;
        paramLoginBtn.topMargin = paramPassword.bottomMargin + 20;
        paramLoginBtn.width = 330;
        paramLoginBtn.height = editHeight;
        paramLoginBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        paramLoginIcon = (RelativeLayout.LayoutParams) loginIcon.getLayoutParams();
        paramLoginIcon.leftMargin = margin;
        paramLoginIcon.bottomMargin = paramUser.topMargin + 20;
        paramLoginIcon.width = iconSize;
        paramLoginIcon.height = iconSize;
        paramLoginIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        inputUsername.setLayoutParams(paramUser);
        inputUsername.setBackgroundColor(Color.parseColor("#f8ad19"));
        inputUsername.setAlpha(alpha);
        inputUsername.setHint("user");
        inputUsername.setHintTextColor(Color.parseColor("#ffffff"));
        inputUsername.setTextColor(Color.parseColor("#ffffff"));
        inputUsername.setTextSize(20);
        inputUsername.setTypeface(face);
        inputUsername.setGravity(Gravity.CENTER_VERTICAL);

        inputPassword.setLayoutParams(paramPassword);
        inputPassword.setBackgroundColor(Color.parseColor("#f8ad19"));
        inputPassword.setAlpha(alpha);
        inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        inputPassword.setHint("password");
        inputPassword.setHintTextColor(Color.parseColor("#ffffff"));
        inputPassword.setTextColor(Color.parseColor("#ffffff"));
        inputPassword.setTextSize(20);
        inputPassword.setTypeface(face);
        inputPassword.setGravity(Gravity.CENTER_VERTICAL);

        btnLogin.setLayoutParams(paramLoginBtn);
        btnLogin.setBackgroundColor(Color.parseColor("#f8ad19"));
        btnLogin.setAlpha(alpha);
        btnLogin.setText("sign in");
        btnLogin.setTextColor(Color.parseColor("#ffffff"));
        btnLogin.setTextSize(20);
        btnLogin.setTypeface(face);
        btnLogin.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);

        loginIcon.setLayoutParams(paramLoginIcon);
        loginIcon.setAlpha(alpha);
        appProcess.setImage(loginIcon, loginIconImg); /** free last image, and store new one */

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
    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
        loginIcon.setImageBitmap(null);
    }

    class AsyncServerCall extends AsyncTask<List<NameValuePair>, Void, String> {

        @Override
        protected String doInBackground(List<NameValuePair>... list) {
            String loginURL = "https://desolate-castle-2567.herokuapp.com/";
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