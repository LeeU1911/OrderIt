package orderit.mainapp;

/**
 * Created by Hieu Thien on 12/23/15.
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class ValidateUser {

    private JSONParser jsonParser;

    private static String loginURL = "http://10.0.2.2/android_login_api/";
    private static String registerURL = "http://10.0.2.2/android_login_api/";

    private static String login_tag = "login";
    private static String register_tag = "register";

    /** Constructor */
    public ValidateUser(){
        jsonParser = new JSONParser();
    }

    /** Login action with username and password */
    public JSONObject loginUser(String email, String password){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        /** return JSON object */
        return json;
    }

    public JSONObject registerUser(String name, String email, String password){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        /** return JSON object */
        return json;
    }

}
