package orderit.mainapp.activity;

/**
 * Created by Blackcool on 12/23/15.
 */

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.util.DisplayMetrics;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

import orderit.mainapp.utility.ConnectionDetector;
import orderit.mainapp.model.User;
import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.utility.JSONParser;
import orderit.mainapp.utility.GZipIOStream;
import orderit.mainapp.R;
import orderit.mainapp.application.AppProcess;

public class LoginActivity extends Activity {
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";
    Button btnLogin;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;
    LinearLayout frame;

    private int background;         /** id of the image drawable */
    private ImageView loginIcon;    /** login icon */
    private int loginIconImg;       /** id of the image drawable */
    private RelativeLayout layout;    /** the layout of the activity */
    private static String KEY_SUCCESS = "response";
    private AppProcess appProcess;

    DatabaseAccess databaseAccess = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseAccess = DatabaseAccess.getInstance(this);

        /** Set display of controls programmatically */
        setControlDisplay();

        /** Action listener */
        inputPassword = (EditText) findViewById(R.id.txtPassword);
        inputPassword.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    OnLogin();
                    handled = true;
                }
                return handled;
            }
        });
    }

    /** Called when the user touches the Login button */
    public void onBtnLoginClick(View view) {
        /** Do something in response to button click */
        OnLogin();
    }

    public void OnLogin() {

        /** Get button handle */
        btnLogin = (Button)findViewById(R.id.btnLogin);
        /** Unable to click */
        btnLogin.setClickable(false);

        TextView loginErrorMsg;
        loginErrorMsg = (TextView) findViewById(R.id.lblLoginErrMsg);

        EditText txtUserName = (EditText)findViewById(R.id.txtUserName);
        //String userName = txtUserName.getText().toString();
        EditText txtPassword =(EditText)findViewById(R.id.txtPassword);
        //String password = txtPassword.getText().toString();
        String userName = "staff1";
        String password = "password";
        if(userName == null || userName.equals("") || password == null || password.equals(""))
        {
            loginErrorMsg.setText(R.string.msg_login_error);
        }
        else {
            //databaseAccess.open();
            //String _password = databaseAccess.SearchPassword(userName);
            //databaseAccess.close();
            ///** Logging successfully */
            //if (password.equalsIgnoreCase(_password)) {
            //    /** Jump to table list screen */
            //    Intent dashboard = new Intent(LoginActivity.this, TableListActivity.class);
            //    startActivity(dashboard);
            //}
            //} else {
            //    /** Check network connection */
            //    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            //    if (cd.isConnectingToInternet() == false) {
            //        loginErrorMsg.setText(R.string.msg_login_error);
            //        //startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            //    } else
            //    /** Not found on local database , find on server */
            //        loginUserOnServer(userName, password);
            //}
            loginUserOnServer(userName, password);
        }
        /** Unable to click */
        btnLogin.setClickable(true);

    }

    private void setControlDisplay() {

        loginIcon = (ImageView) findViewById(R.id.send_icon);
        layout = (RelativeLayout) findViewById(R.id.layoutLogin);

        background = R.drawable.login;
        loginIconImg = R.drawable.login_icon;

        appProcess = (AppProcess)getApplication();
        appProcess.setBackground(layout, background); /** free last background, and store new one */

        RelativeLayout.LayoutParams paramLoginIcon;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenHeight = metrics.heightPixels;
        int frameHeight = screenHeight*3/7;
        int editHeight = (frameHeight - 30*2 - 20*2 - 15)/5;
        int iconSize = editHeight*2;

        paramLoginIcon = (RelativeLayout.LayoutParams) loginIcon.getLayoutParams();
        paramLoginIcon.width = iconSize;
        paramLoginIcon.height = iconSize;
        loginIcon.setLayoutParams(paramLoginIcon);

        appProcess.setImage(loginIcon, loginIconImg); /** free last image, and store new one */
    }

    private void loginUserOnServer(String username, String password){
        /**List<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            username = new GZipIOStream().compressString(username);
            password = new GZipIOStream().compressString(password);
        }
        catch (IOException e) {
        }

        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
         */
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
            /**
            String loginURL = "http://orderit-server.herokuapp.com/";
            JSONObject json = new JSONParser().getJSONFromUrl(loginURL, list[0]);
            System.out.println(json);
            return json.toString();
            */
            databaseAccess.open();
            String _password = databaseAccess.SearchPassword(list[0].get(0).getValue());
            databaseAccess.close();
            if (list[0].get(1).getValue().equalsIgnoreCase(_password)) {
                /** Jump to table list screen */
                Intent dashboard = new Intent(LoginActivity.this, TableListActivity.class);
                startActivity(dashboard);
                return SUCCESS;
            }
            else {
                return FAILED;
            }
        }

        protected void onPostExecute(String result){
            /**JSONObject json = null;
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if (res.equalsIgnoreCase("success")) {
                        User user = new User();
                        inputPassword = (EditText) findViewById(R.id.txtPassword);
                        inputUsername = (EditText) findViewById(R.id.txtUserName);
                        user.setUserName(inputUsername.getText().toString());
                        user.setPassword(inputPassword.getText().toString());
                        user.setBusinessId(1);
                        user.setRoleId(1);
                        databaseAccess.open();
                        databaseAccess.InsertUser(user);
                        databaseAccess.close();
                        Intent dashboard = new Intent(LoginActivity.this, TableListActivity.class);
                        startActivity(dashboard);
                    } else {
                        loginErrorMsg.setText(R.string.msg_login_error);
                    }
                }
            } catch (JSONException e) {
            }*/
            if (result.equalsIgnoreCase(SUCCESS)) {
                /** Jump to table list screen */
                Intent dashboard = new Intent(LoginActivity.this, TableListActivity.class);
                startActivity(dashboard);
            }
        }
    }
}