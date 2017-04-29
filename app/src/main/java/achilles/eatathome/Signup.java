package achilles.eatathome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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
import java.util.Random;

import static android.content.ContentValues.TAG;

public class Signup extends Activity {

    private static final String SIGNUPCHCK_URL =  "http://eatathome.pe.hu/UserCheck.php";
    EditText etName, etEmail, etPhone, etPass, etConfPass;
    CheckBox cbTnC;
    TextView tvRegister;

    String name = "";
    String email = "";
    String pass = "";
    String confPass = "";
    String phone = "";
    String otp = "";
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final Intent intent = getIntent();
        type = intent.getStringExtra("type");

        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etConfPass = (EditText)findViewById(R.id.etConfPass);
        cbTnC = (CheckBox)findViewById(R.id.cbTnC);
        tvRegister = (TextView)findViewById(R.id.tvRegister);
        otp = generateOTP();
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG, "onClick: Register Clicked TYPE = " + type );
                name = etName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                pass = etPass.getText().toString().trim();
                confPass = etConfPass.getText().toString().trim();
                if(name.length() == 0){
                    Toast.makeText(Signup.this,"Enter Name",Toast.LENGTH_LONG ).show();
                    etName.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etName, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    if(email.length() == 0){
                        Toast.makeText(Signup.this,"Enter Email ID",Toast.LENGTH_LONG ).show();

                        etEmail.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etEmail, InputMethodManager.SHOW_IMPLICIT);
                    }else{
                        if(phone.length() != 10){
                            Toast.makeText(Signup.this,"Enter Phone Number",Toast.LENGTH_LONG ).show();
                            etPhone.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(etPhone, InputMethodManager.SHOW_IMPLICIT);
                        }else {
                            if(!pass.equals(confPass) || pass.length()==0){
                                etPass.setText("");
                                etConfPass.setText("");
                                etPass.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(etPass, InputMethodManager.SHOW_IMPLICIT);
                                Toast.makeText(Signup.this,"Passwords DO NOt match",Toast.LENGTH_LONG ).show();
                            } else {
                                if(cbTnC.isChecked()){
                                    userExists(email);
                                }else {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(Signup.this);
                                    alert.setMessage("Please agree to the Terms and Condition..");
                                    alert.setPositiveButton("OK",null);
                                    alert.show();
                                }
                            }

                        }
                    }
                }
            }
        });
    }

    private void userExists(final String email) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUPCHCK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(TAG, "onResponse:" + response );
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            boolean exists = jsonResponse.getBoolean("exists");
                            Log.w(TAG, "onResponse: exists: " + exists + " Success : " + success);
                            progressDialog.dismiss();
                            Log.w(TAG, "onResponse: progress Dialog dismiss");
                            if(success){
                                Log.w(TAG, "onResponse: inside success");
                                if (!exists) {
                                    Log.w(TAG, "onResponse: inside exists");
                                    if (type.equals("CUST")) {
                                        Log.w(TAG, "onResponse: CUST");
                                        callSMS();
                                        Intent inten = new Intent(Signup.this, OTP.class);
                                        inten.putExtra("name", name);
                                        inten.putExtra("email", email);
                                        inten.putExtra("phone", phone);
                                        inten.putExtra("pass", pass);
                                        inten.putExtra("otp", otp);
                                        inten.putExtra("type", type);
                                        inten.addCategory(Intent.CATEGORY_HOME);
                                        inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(inten);
                                        finish();
                                    } else {
                                        Log.w(TAG, "onResponse: SUPP");
                                        Intent inten = new Intent(Signup.this, CookSignup.class);
                                        inten.putExtra("name", name);
                                        inten.putExtra("email", email);
                                        inten.putExtra("phone", phone);
                                        inten.putExtra("pass", pass);
                                        inten.putExtra("otp", otp);
                                        inten.putExtra("type", type);
                                        startActivity(inten);
                                    }
                                }
                                else {
                                    Log.w(TAG, "onResponse: email id exists");
                                    etEmail.requestFocus();
                                    etPass.setText("");
                                    etConfPass.setText("");
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(etEmail, InputMethodManager.SHOW_IMPLICIT);
                                    Toast.makeText(Signup.this, "Email id already exists...", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(Signup.this);
                                alert.setTitle("Error");
                                alert.setMessage("Some error in Databases");
                                alert.setPositiveButton("OK",null);
                                alert.show();
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(Signup.this);
                        alert.setMessage("No Internet Connection.. Please connect to the Internet to continue..");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("email", email);
                map.put("type",type);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void callSMS(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.redoxygen.net/sms.dll?Action=SendSMS&AccountId=CI00192044&Email=agarwal.harshnu@gmail.com&Password=XAec1kq0&Recipient="+phone+"&Message=Welcome%20to%20Eat@Home%20Family.%20Your%20OTP%20for%20registration%20is%20"+otp;
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Signup.this,"Error in sending OTP: " + error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public String generateOTP(){
        Random rand = new Random();
        return String.format("%04d", rand.nextInt(10000));
    }
}