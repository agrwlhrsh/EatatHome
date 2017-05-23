package achilles.eatathome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CookSignup extends AppCompatActivity {

    private static final String GETAREA_URL = "http://eatathome.pe.hu/SelectLoc.php";

    EditText etAcNumber, etAcName, etIFSC,  etBuildNo, etLandmark;
    TextView tvContinue, tvArea;
    Spinner spArea;

    String name = "";
    String email = "";
    String pass = "";
    String phone = "";
    String otp = "1234";
    String type = "";
    String acno = "";
    String acname = "";
    String ifsc = "";
    String buildno = "";
    String area = "";
    String pincode = "";
    String state = "";
    String city = "";

    String landmark = "";

    ArrayAdapter<String> dataAdapter;
    List<String> categories = new ArrayList<String>();
    private String area_select;
    private String TAG = "TAG";
    private int aid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_signup);

        getAreaList();

        final Intent intent = getIntent();
        type = intent.getStringExtra("type");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        pass = intent.getStringExtra("pass");
        otp = intent.getStringExtra("otp");

        etAcNumber = (EditText)findViewById(R.id.etAcNumber);
        etAcName = (EditText)findViewById(R.id.etAcName);
        etIFSC = (EditText)findViewById(R.id.etIFSC);
        etBuildNo = (EditText)findViewById(R.id.etBuildNo);
        spArea = (Spinner)findViewById(R.id.spArea);
        etLandmark = (EditText)findViewById(R.id.etLandmark);
        tvArea = (TextView)findViewById(R.id.tvArea);
        tvContinue = (TextView)findViewById(R.id.tvContinue);

        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    area_select = "";
                }else {
                    tvArea.setText(categories.get(position));
                    area_select = categories.get(position).toString();

                    aid = position;
                    StringTokenizer st = new StringTokenizer(area_select, ",");
                    area = st.nextToken().toString() + "";
                    city = st.nextToken().toString() + "";
                    state = st.nextToken().toString() + "";
                    pincode = st.nextToken().toString() + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG, "onClick: " + aid);
                acno = etAcNumber.getText().toString().trim();
                acname = etAcName.getText().toString().trim();
                ifsc = etIFSC.getText().toString().trim();
                buildno = etBuildNo.getText().toString().trim();
                landmark = etLandmark.getText().toString().trim();
                if(acno.length() > 0 && acname.length()>0 && ifsc.length()>0 &&
                        area_select.length() > 0 && buildno.length()>0 && landmark.length()>0 ){
                    Intent inten = new Intent(CookSignup.this, OTP.class);
                    callSMS();
                    inten.putExtra("name",name);
                    inten.putExtra("email",email);
                    inten.putExtra("phone",phone);
                    inten.putExtra("pass",pass);
                    inten.putExtra("otp",otp);
                    inten.putExtra("type",type);
                    inten.putExtra("acno",acno);
                    inten.putExtra("acname",acname);
                    inten.putExtra("ifsc",ifsc);
                    inten.putExtra("aid",aid+"");
                    String address = buildno + ";" + landmark + ";" + area + ";" + city + ";" + state  + ";" + pincode;
                    inten.putExtra("address",address);
                    inten.addCategory(Intent.CATEGORY_HOME);
                    inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(inten);
                    finish();
                }else{
                    Toast.makeText(CookSignup.this,"Fill all Fields",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void callSMS(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.redoxygen.net/sms.dll?Action=SendSMS&AccountId=CI00192463&Email=twinki.sarkar30@gmail.com&Password=Kqk4sLq8&Recipient="+phone+"&Message=Welcome%20to%20Eat@Home%20Family.%20Your%20OTP%20for%20registration%20is%20"+otp;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(CookSignup.this,"OTP sent Succesfully",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CookSignup.this,"Error in sending OTP: " + error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }


    private void getAreaList() {
        final ProgressDialog progressDialog = new ProgressDialog(CookSignup.this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GETAREA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(TAG, "onResponse: " + response );
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            progressDialog.dismiss();
                            if(success) {
                                JSONArray order = jsonResponse.getJSONArray("area");
                                for (int i = 0; i < order.length(); i++) {
                                    JSONObject c = order.getJSONObject(i);
                                    categories.add(c.getString("aname")+", "+c.getString("ctname")
                                            +", "+c.getString("stname")+", "+ c.getString("pincode"));
                                }
                                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spArea.setAdapter(dataAdapter);
                            }
                            else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                alert.setTitle("Error");
                                alert.setMessage("Some Error Occurred. Try Later!!!");
                                alert.setPositiveButton("OK",null);
                                alert.show();
                            }
                        }
                        catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.w(TAG, "onResponse: JSONException: \n\n" + e.getMessage().toString() );
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(TAG, "onErrorResponse: " + error.getMessage().toString() );
                        progressDialog.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                        alert.setTitle("Error");
                        alert.setMessage(error.getMessage().toString());
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }){
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
