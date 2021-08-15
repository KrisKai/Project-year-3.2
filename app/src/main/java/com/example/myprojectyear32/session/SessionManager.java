package com.example.myprojectyear32.session;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_DOB = "DoB";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENUMBER = "phonenumber";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_CONNECTION = "connection";
    public static final String KEY_LIGHTINGLR = "lightLR";
    public static final String KEY_FANLR = "fanLR";
    public static final String KEY_DOORLR = "doorLR";
    public static final String KEY_SENSORLR = "sensorLR";


    public SessionManager(Context context){
        preferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public void createLoginSession(String userName, String passWord, String firstName, String lastName, String phoneNum, String email, String dob, String gender, String connectCode){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_USERNAME,userName);
        editor.putString(KEY_PASSWORD,passWord);
        editor.putString(KEY_LASTNAME,lastName);
        editor.putString(KEY_FIRSTNAME,firstName);
        editor.putString(KEY_PHONENUMBER,phoneNum);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_DOB,dob);
        editor.putString(KEY_GENDER,gender);
        editor.putString(KEY_CONNECTION,connectCode);
        editor.commit();
    }

    public void createConnection(String connectionCode){
        editor.putString(KEY_CONNECTION, connectionCode);

        editor.commit();
    }

    public void createStatus(String doorLR, String fanLR, String lightingLR, String sensorLR){
        editor.putString(KEY_DOORLR,doorLR);
        editor.putString(KEY_FANLR,fanLR);
        editor.putString(KEY_LIGHTINGLR,lightingLR);
        editor.putString(KEY_SENSORLR,sensorLR);
        editor.commit();
    }

    public HashMap<String,String> getUserDetailFromSession(){
        HashMap<String,String> userData = new HashMap<>();
        userData.put(KEY_USERNAME,preferences.getString(KEY_USERNAME,null));
        userData.put(KEY_PASSWORD,preferences.getString(KEY_PASSWORD,null));
        userData.put(KEY_FIRSTNAME,preferences.getString(KEY_FIRSTNAME,null));
        userData.put(KEY_LASTNAME,preferences.getString(KEY_LASTNAME,null));
        userData.put(KEY_PHONENUMBER,preferences.getString(KEY_PHONENUMBER,null));
        userData.put(KEY_EMAIL,preferences.getString(KEY_EMAIL,null));
        userData.put(KEY_DOB,preferences.getString(KEY_DOB,null));
        userData.put(KEY_GENDER,preferences.getString(KEY_GENDER,null));
        userData.put(KEY_CONNECTION,preferences.getString(KEY_CONNECTION,null));
        userData.put(KEY_DOORLR,preferences.getString(KEY_DOORLR,null));
        userData.put(KEY_LIGHTINGLR,preferences.getString(KEY_LIGHTINGLR,null));
        userData.put(KEY_FANLR,preferences.getString(KEY_FANLR,null));
        userData.put(KEY_SENSORLR,preferences.getString(KEY_SENSORLR,null));

        return userData;
    }

    public boolean checkLogin(){
        if(preferences.getBoolean(IS_LOGIN,true)){
            return true;
        }
        else {
            return false;
        }
    }

    public void logoutFromSession(){
        editor.clear();
        editor.commit();
    }
}
