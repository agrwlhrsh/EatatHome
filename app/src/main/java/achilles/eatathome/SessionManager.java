package achilles.eatathome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager
{
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_IFSC = "ifsc";
    public static final String KEY_ACNO = "acno";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_ACNAME = "acname";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ID = "id";
    public static final String KEY_AID = "aid";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String name, String phone, String address, String id, String acname, String acno, String ifsc, String balance, String type, String aid){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_ACNAME, acname);
        editor.putString(KEY_ACNO, acno);
        editor.putString(KEY_IFSC, ifsc);
        editor.putString(KEY_BALANCE, balance);
        editor.putString(KEY_TYPE, type);
        editor.putString(KEY_AID, aid);

        editor.commit();
    }

    public void updateBalance(String balance){
        editor.putString(KEY_BALANCE, balance);
        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        user.put(KEY_ACNAME, pref.getString(KEY_ACNAME, null));
        user.put(KEY_ACNO, pref.getString(KEY_ACNO, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_IFSC, pref.getString(KEY_IFSC, null));
        user.put(KEY_BALANCE, pref.getString(KEY_BALANCE, null));
        user.put(KEY_TYPE, pref.getString(KEY_TYPE, null));
        user.put(KEY_AID, pref.getString(KEY_AID, null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}