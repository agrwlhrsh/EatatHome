package achilles.eatathome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.StringTokenizer;

public class Address extends AppCompatActivity {
    private static final String UPDATEADDR_URL = "http://eatathome.pe.hu/UpdateAddr.php";

    private static final String TAG = "Address";
    EditText etBuildNo, etLandmark, etArea, etPincode, etCity, etState;
    TextView tvBack, tvEdit;

    SessionManager session;

    String type = "",name = "", phone = "", id = "", email = "",address = "", acno = "", acname = "", ifsc = "", balance = "", aid = "";

    String buildNo = "", landmark = "", area = "", pincode = "", city = "", state = "";
    String n_buildNo = "", n_landmark = "", n_area = "", n_pincode = "", n_city = "", n_state = "", n_address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

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
        aid = user.get(SessionManager.KEY_AID);

        tvBack = (TextView)findViewById(R.id.tvBack);
        tvEdit = (TextView)findViewById(R.id.tvEdit);
        etArea = (EditText)findViewById(R.id.etArea);
        etPincode = (EditText)findViewById(R.id.etPincode);
        etBuildNo = (EditText)findViewById(R.id.etBuildNo);
        etLandmark = (EditText)findViewById(R.id.etLandmark);
        etCity = (EditText)findViewById(R.id.etCity);
        etState = (EditText)findViewById(R.id.etState);

        Log.w(TAG, "onCreate:" + address );
        if(address.length() > 5){
            StringTokenizer st = new StringTokenizer(address,";");
            buildNo = st.nextToken().toString() + "";
            landmark = st.nextToken().toString() + "";
            area = st.nextToken().toString() + "";
            city = st.nextToken().toString() + "";
            state = st.nextToken().toString() + "";
            pincode = st.nextToken().toString() + "";
        }


        etBuildNo.setText(buildNo);
        etLandmark.setText(landmark);
        etArea.setText(area);
        etCity.setText(city);
        etState.setText(state);
        etPincode.setText(pincode);

        etBuildNo.setEnabled(false);
        etLandmark.setEnabled(false);
        etArea.setEnabled(false);
        etCity.setEnabled(false);
        etState.setEnabled(false);
        etPincode.setEnabled(false);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvEdit.getText().toString().equalsIgnoreCase("EDIT")){

                    etBuildNo.setEnabled(true);
                    etLandmark.setEnabled(true);
                    etArea.setEnabled(true);
                    etCity.setEnabled(true);
                    etState.setEnabled(true);
                    etPincode.setEnabled(true);

                    etBuildNo.setTextColor(Color.parseColor("#333333"));
                    etLandmark.setTextColor(Color.parseColor("#333333"));
                    etArea.setTextColor(Color.parseColor("#333333"));
                    etCity.setTextColor(Color.parseColor("#333333"));
                    etState.setTextColor(Color.parseColor("#333333"));
                    etPincode.setTextColor(Color.parseColor("#333333"));

                    tvEdit.setText("SAVE");
                    tvEdit.setTextColor(Color.parseColor("#33aa33"));
                }else{

                    etBuildNo.setEnabled(false);
                    etLandmark.setEnabled(false);
                    etArea.setEnabled(false);
                    etCity.setEnabled(false);
                    etState.setEnabled(false);
                    etPincode.setEnabled(false);

                    etBuildNo.setTextColor(Color.parseColor("#aaaaaa"));
                    etLandmark.setTextColor(Color.parseColor("#aaaaaa"));
                    etArea.setTextColor(Color.parseColor("#aaaaaa"));
                    etCity.setTextColor(Color.parseColor("#aaaaaa"));
                    etState.setTextColor(Color.parseColor("#aaaaaa"));
                    etPincode.setTextColor(Color.parseColor("#aaaaaa"));
                    n_area = etArea.getText().toString().trim();
                    n_buildNo = etBuildNo.getText().toString().trim();
                    n_landmark = etLandmark.getText().toString().trim();
                    n_pincode = etPincode.getText().toString().trim();
                    n_state = etState.getText().toString().trim();
                    n_city = etCity.getText().toString().trim();
                    tvEdit.setText("EDIT");
                    tvEdit.setTextColor(Color.parseColor("#b80000"));
                    n_address = n_buildNo + ";" + n_landmark + ";" + n_area + ";" + n_city + ";" + n_state  + ";" + n_pincode;
                    updateAddr();
                }
            }
        });
    }

    private void updateAddr() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATEADDR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent inten = new Intent(Address.this, Profile.class);
                                session.createLoginSession(email, name, phone, n_address, id, acname, acno, ifsc,balance,type,aid);
                                startActivity(inten);
                                inten.addCategory(Intent.CATEGORY_HOME);
                                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(inten);
                                Toast.makeText(Address.this, "UPDATE SUCCESSFUL",Toast.LENGTH_LONG ).show();
                                finish();
                            } else {
                                Toast.makeText(Address.this, "UPDATE FAILED", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Address.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type", type);
                map.put("address", n_address);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
