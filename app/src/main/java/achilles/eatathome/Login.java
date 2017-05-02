package achilles.eatathome;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Login extends AppCompatActivity {

    private static final String LOGIN_URL = "http://eatathome.pe.hu/Login.php";

    SessionManager session;

    EditText etEmail;
    EditText etPass;
    TextView tvForPass;
    TextView tvSignin;
    TextView tvSignUp;
    ImageView ivGoogle;
    ImageView ivFb;

    String email = "";
    String pass = "";

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        tvForPass = (TextView)findViewById(R.id.tvForPass);
        tvSignin = (TextView)findViewById(R.id.tvSignin);
        tvSignUp = (TextView)findViewById(R.id.tvSignUp);
        ivGoogle = (ImageView)findViewById(R.id.ivGoogle);
        ivFb = (ImageView)findViewById(R.id.ivFb);

        tvForPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(Login.this, ForgotPass.class);
                startActivity(inten);
            }
        });

        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                pass = etPass.getText().toString().trim();
                if(email.length()==0){
                    Toast.makeText(Login.this,"Email ID is Missing",Toast.LENGTH_LONG).show();
                }else if(pass.length()==0){
                    etPass.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etPass, InputMethodManager.SHOW_IMPLICIT);
                    Toast.makeText(Login.this,"Password is Missing",Toast.LENGTH_LONG).show();
                }else{
                    signIn();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {
        Intent inSignUp = new Intent(Login.this, ChoiceRegi.class);
        startActivity(inSignUp);
    }

    private void signIn() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean("error");
                            type = jsonResponse.getString("type");
                            boolean auth = jsonResponse.getBoolean("auth");
                            if(!error){
                                progressDialog.dismiss();
                                if(!type.equalsIgnoreCase("FAIL")){
                                    if(auth){
                                        String name = jsonResponse.getString("name");
                                        String id = jsonResponse.getString("id");
                                        String phone = jsonResponse.getString("phone");
                                        String address = jsonResponse.getString("address");
                                        String balance = jsonResponse.getString("balance");
                                        if(type.equalsIgnoreCase("CUST")){
                                            Intent inten = new Intent(Login.this, Profile.class);
                                            session.createLoginSession(email, name, phone, address, id, "", "", "",balance,type,"0");
                                            inten.addCategory(Intent.CATEGORY_HOME);
                                            inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(inten);
                                            finish();

                                        }else if(type.equalsIgnoreCase("SUPP")){
                                            String acname = jsonResponse.getString("acname");
                                            String acno = jsonResponse.getString("acno");
                                            String ifsc = jsonResponse.getString("ifsc");
                                            String aid = jsonResponse.getString("aid");
                                            Intent inten = new Intent(Login.this, Profile.class);
                                            session.createLoginSession(email, name, phone, address, id, acname, acno, ifsc,balance,type,aid);
                                            inten.addCategory(Intent.CATEGORY_HOME);
                                            inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(inten);
                                            finish();
                                        }else{
                                            Toast.makeText(Login.this,"Unknown Error Occurred..",Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        etPass.setText("");
                                        etPass.requestFocus();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(etPass, InputMethodManager.SHOW_IMPLICIT);
                                        Toast.makeText(Login.this,"Incorrect Password..",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    etEmail.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(etPass, InputMethodManager.SHOW_IMPLICIT);
                                    etEmail.setText("");
                                    etPass.setText("");
                                    Toast.makeText(Login.this,"User does not Exist.. Register to Login..",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(Login.this,"Connection Error! Please Try again Later..",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                        alert.setMessage("Check your Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("email", email);
                map.put("pass", pass);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
