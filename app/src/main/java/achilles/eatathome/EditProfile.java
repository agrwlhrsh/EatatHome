package achilles.eatathome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class EditProfile extends AppCompatActivity {

    private static final String UPDATE_URL = "http://eatathome.pe.hu/Update.php";
    String type = "",name = "", phone = "", id = "", email = "",address = "", acno = "", acname = "", ifsc = "", balance = "", pass = "";
    String new_name = "", new_phone = "", new_pass = "", new_confpass = "";


    TextView tvBack, tvSave;
    EditText item1, item2, item3, item4, item5;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        type = user.get(SessionManager.KEY_TYPE);
        email = user.get(SessionManager.KEY_EMAIL);
        phone = user.get(SessionManager.KEY_PHONE);
        id = user.get(SessionManager.KEY_ID);
        address= user.get(SessionManager.KEY_ADDRESS);
        balance = user.get(SessionManager.KEY_BALANCE);
        acno = user.get(SessionManager.KEY_ACNO);
        acname= user.get(SessionManager.KEY_ACNAME);
        ifsc = user.get(SessionManager.KEY_IFSC);

        tvBack = (TextView)findViewById(R.id.tvBack);
        tvSave = (TextView)findViewById(R.id.tvSave);
        item1 = (EditText)findViewById(R.id.item1);
        item2 = (EditText)findViewById(R.id.item2);
        item3 = (EditText)findViewById(R.id.item3);
        item4 = (EditText)findViewById(R.id.item4);
        item5 = (EditText)findViewById(R.id.item5);

        item1.setText(phone);
        item3.setText(email);
        item2.setText(name);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_name = item2.getText().toString().trim();
                new_phone = item1.getText().toString().trim();
                new_pass = item4.getText().toString().trim();
                new_confpass = item5.getText().toString().trim();
                if(!(new_name.equals(name) && new_phone.equals(phone)) || new_pass.length()>0 || new_confpass.length() >0){
                    if(new_pass.equals(new_confpass)){
                        if(!(new_pass.length()>0)){
                            new_pass = pass;
                        }
                        update();
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);
                        alert.setTitle("Error");
                        alert.setMessage("Passwords do not match");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }else{

                    AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);
                    alert.setMessage("No changes made. Nothing to save..");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void update() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            progressDialog.dismiss();
                            if(success){
                                Intent inten = new Intent(EditProfile.this, Profile.class);
                                startActivity(inten);
                            }else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);
                                alert.setTitle("Error");
                                alert.setMessage("Some Error Occurred. Try Later!!!");
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);
                        alert.setTitle("Error");
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
                map.put("name",new_name);
                map.put("phone", new_phone);
                if(new_pass.length()>0){
                    map.put("pass",new_pass);
                }else {
                    map.put("pass", pass);
                }
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
