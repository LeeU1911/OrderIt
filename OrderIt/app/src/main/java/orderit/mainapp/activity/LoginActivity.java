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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.utility.JSONParser;
import orderit.mainapp.utility.GZipIOStream;
import orderit.mainapp.R;
import orderit.mainapp.application.AppProcess;

public class LoginActivity extends Activity {
    Button btnLogin;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;
    RelativeLayout frame;

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

        btnLogin = (Button)findViewById(R.id.btnLoggin);
        loginErrorMsg = (TextView)findViewById(R.id.lblLogginErrMsg);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUserName = (EditText)findViewById(R.id.txtUserName);
                String userName = txtUserName.getText().toString();
                EditText txtPassword =(EditText)findViewById(R.id.txtPassword);
                String password = txtPassword.getText().toString();

                String _password = databaseAccess.SearchPassword(userName);
                /** Logging successfully */
                if(password.equalsIgnoreCase(_password)) {
                    /** Jump to table list screen */
                    Intent dashboard = new Intent(getApplicationContext(), TableListActivity.class);
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);
                    /** Close activity */
                }
                else {
                    /** Not found on local database , find on server */
                    loginUserOnServer(userName, password);
                }

            }
        });
    }

    private void setControlDisplay() {
        inputUsername = (EditText) findViewById(R.id.txtUserName);
        inputPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLoggin);
        loginIcon = (ImageView) findViewById(R.id.send_icon);
        layout = (RelativeLayout) findViewById(R.id.layoutLogin);
        frame = (RelativeLayout) findViewById(R.id.frame);

        background = R.drawable.login;
        loginIconImg = R.drawable.login_icon;

        appProcess = (AppProcess)getApplication();
        appProcess.setBackground(layout, background); /** free last background, and store new one */

        RelativeLayout.LayoutParams paramFrame;
        RelativeLayout.LayoutParams paramUser;
        RelativeLayout.LayoutParams paramPassword;
        RelativeLayout.LayoutParams paramLoginBtn;
        RelativeLayout.LayoutParams paramLoginIcon;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;
        int frameHeight = screenHeight*3/7;
        int margin = 30;
        int editHeight = (frameHeight - 30*2 - 20*2 - 15)/5;
        int iconSize = editHeight*2;
        int loginBtnSize = screenWidth/3;

        paramFrame = (RelativeLayout.LayoutParams) frame.getLayoutParams();
        paramFrame.leftMargin = 0;
        paramFrame.rightMargin = 0;
        paramFrame.topMargin = screenHeight*2/7;
        paramFrame.height = frameHeight;
        paramFrame.width = screenWidth;
        frame.setLayoutParams(paramFrame);

        paramUser = (RelativeLayout.LayoutParams) inputUsername.getLayoutParams();
        paramUser.leftMargin = margin;
        paramUser.rightMargin = margin;
        paramUser.topMargin = paramFrame.topMargin + 30 + iconSize + 20;
        paramUser.height = editHeight;
        paramUser.width = screenWidth - (2*margin);
        inputUsername.setLayoutParams(paramUser);

        paramPassword = (RelativeLayout.LayoutParams) inputPassword.getLayoutParams();
        paramPassword.leftMargin = margin;
        paramPassword.rightMargin = margin;
        paramPassword.topMargin = paramUser.topMargin + editHeight + 15;
        paramPassword.height = editHeight;
        paramPassword.width = paramUser.width;
        inputPassword.setLayoutParams(paramPassword);

        paramLoginBtn = (RelativeLayout.LayoutParams) btnLogin.getLayoutParams();
        paramLoginBtn.leftMargin = screenWidth - margin - loginBtnSize;
        paramLoginBtn.rightMargin = margin;
        paramLoginBtn.topMargin = paramPassword.topMargin + editHeight + 20;
        paramLoginBtn.width = loginBtnSize;
        paramLoginBtn.height = editHeight;
        btnLogin.setLayoutParams(paramLoginBtn);

        paramLoginIcon = (RelativeLayout.LayoutParams) loginIcon.getLayoutParams();
        paramLoginIcon.leftMargin = margin;
        paramLoginIcon.topMargin = paramUser.topMargin - iconSize - 20;
        paramLoginIcon.width = iconSize;
        paramLoginIcon.height = iconSize;
        loginIcon.setLayoutParams(paramLoginIcon);

        appProcess.setImage(loginIcon, loginIconImg); /** free last image, and store new one */
    }

    private void loginUserOnServer(String username, String password){
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            username = new GZipIOStream().compressString(username);
            password = new GZipIOStream().compressString(password);
        }
        catch (IOException e) {
        }

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
            System.out.println(json);
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
                        loginErrorMsg.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
                    }
                }
            } catch (JSONException e) {
            }
        }
    }
}