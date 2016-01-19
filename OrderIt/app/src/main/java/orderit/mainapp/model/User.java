package orderit.mainapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tri Trieu on 10/01/2016.
 */
public class User {
    String userName, password;
    int businessId, roleId;
    Date createDate, modifiedDate;

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setBusinessId(int businessIdId){
        this.businessId = businessIdId;
    }

    public int getBusinessId(){
        return this.businessId;
    }

    public void setRoleId(int roleId){
        this.roleId = roleId;
    }

    public int getRoleId(){
        return this.roleId;
    }

    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }

    public String getCreateDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(this.createDate);
    }

    public  void setModifiedDate(Date modifiedDate){
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(this.modifiedDate);
    }

    private String convert() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
