package orderit.mainapp;

/**
 * Created by Hieu Thien on 12/23/15.
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ValidateUser {

    private JSONParser jsonParser;

    private static String loginURL = "192.168.1.6:8080";

    private static String login_tag = "login";

    /** Constructor */
    public ValidateUser(){
        jsonParser = new JSONParser();
    }

    /** Login action with username and password */
    public JSONObject loginUser(String username, String password){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        /** return JSON object */
        return json;
    }

}
