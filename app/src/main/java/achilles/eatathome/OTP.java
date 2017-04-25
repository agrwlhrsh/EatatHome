package achilles.eatathome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class OTP extends Activity {

    private static final String SIGNUPCUST_URL = "http://eatathome.pe.hu/SignupC.php";
    private static final String SIGNUPSUPP_URL = "http://eatathome.pe.hu/SignupS.php";
    String type = "";
    String name = "";
    String email = "";
    String pass = "";
    String phone = "";
    String otp = "";
    String ip_otp = "";
    String acno = "";
    String acname = "";
    String ifsc = "";
    String aid = "0";
    String address = ";;;;;";

    EditText etOTP;
    TextView tvApprove;
    TextView tvResend;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        session = new SessionManager(getApplicationContext());

        final Intent intent = getIntent();
        type = intent.getStringExtra("type");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        pass = intent.getStringExtra("pass");
        otp = intent.getStringExtra("otp");
        if(type.equals("SUPP")){
            acno = intent.getStringExtra("acno");
            acname = intent.getStringExtra("acname");
            ifsc = intent.getStringExtra("ifsc");
            address = intent.getStringExtra("address");
        }

        etOTP = (EditText)findViewById(R.id.etOTP);
        tvApprove = (TextView)findViewById(R.id.tvApprove);
        tvResend = (TextView)findViewById(R.id.tvResend);

        tvApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip_otp = etOTP.getText().toString().trim();
                if(ip_otp.equalsIgnoreCase(otp)){
                    if(type.equals("CUST")){
                        signupCUST();
                    }else{
                        signupSUPP();
                    }
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(OTP.this);
                    alert.setTitle("Error");
                    alert.setMessage("Incorrect OTP, try again...");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            }
        });
    }

    private void signupSUPP() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUPSUPP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String id = jsonResponse.getString("id");
                            String balance = jsonResponse.getString("balance");
                            if(success) {
                                Toast.makeText(OTP.this,"REGISTRATION SUCCESSFUL",Toast.LENGTH_LONG ).show();
                                Intent inten = new Intent(OTP.this, Profile.class);
                                session.createLoginSession(email, name, phone, address, id, "", "", "",balance,type,aid);
                                startActivity(inten);
                            }
                            else {
                                Toast.makeText(OTP.this,"REGISTRATION FAILED",Toast.LENGTH_LONG ).show();
                            }
                        }
                        catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(OTP.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                Log.w(TAG, "getParams: " + email + "," + pass+ "," + phone + "," + name + "," + acno +"," + acname+"," + ifsc +"," + address);
                map.put("email", email);
                map.put("pass", pass);
                map.put("phone",phone);
                map.put("name",name);
                map.put("acno",acno);
                map.put("acname",acname);
                map.put("ifsc",ifsc);
                map.put("address",address);
                map.put("aid", aid);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void signupCUST() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUPCUST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String id = jsonResponse.getString("id");
                            String balance = jsonResponse.getString("balance");
                            if(success) {
                                session.createLoginSession(email, name, phone, address, id, "", "", "",balance,type, aid);
                                Intent inten = new Intent(OTP.this, BrowseMenu.class);
                                startActivity(inten);
                                Toast.makeText(OTP.this,"REGISTRATION SUCCESSFUL",Toast.LENGTH_LONG ).show();
                            }
                            else {
                                Toast.makeText(OTP.this,"REGISTRATION FAILED",Toast.LENGTH_LONG ).show();
                            }
                        }
                        catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(OTP.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("email", email);
                map.put("pass", pass);
                map.put("phone",phone);
                map.put("name",name);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}