package achilles.eatathome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class ForgotPass extends AppCompatActivity {

    private static final String FORPASS_URL = "http://eatathome.pe.hu/For_pass.php";
    private static final String TAG = "Forgot Pass";
    String email = "";
    TextView tvContinue;
    EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        etEmail = (EditText)findViewById(R.id.etEmail);
        tvContinue = (TextView)findViewById(R.id.tvContinue);

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                update();
            }
        });

    }

    private void update() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FORPASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(TAG, "onResponse: " + response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            boolean exist = jsonResponse.getBoolean("exist");
                            boolean update = jsonResponse.getBoolean("update");
                            progressDialog.dismiss();

                            if (success) {
                                if (!exist) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPass.this);
                                    alert.setMessage("Email Id does not Exists. Please Register!");
                                    alert.setPositiveButton("OK", null);
                                    alert.show();
                                } else {
                                    if (update) {
                                        Intent inten = new Intent(ForgotPass.this, Login.class);
                                        inten.addCategory(Intent.CATEGORY_HOME);
                                        inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(inten);
                                        finish();
                                    } else {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPass.this);
                                        alert.setMessage("Could not Update or send email because of some Error! Please Try again Later..");
                                        alert.setPositiveButton("OK", null);
                                        alert.show();
                                    }
                                }
                            } else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPass.this);
                                alert.setTitle("Error");
                                alert.setMessage("Some Error Occurred. Try Later!!!");
                                alert.setPositiveButton("OK", null);
                                alert.show();
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPass.this);
                        alert.setTitle("Error");
                        alert.setMessage("No Internet Connection.. Please connect to the Internet to continue..");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("email", email);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
