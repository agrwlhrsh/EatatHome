package achilles.eatathome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CookSignup extends AppCompatActivity {

    EditText etAcNumber, etAcName, etIFSC, etPincode, etState, etCity, etBuildNo, etArea, etLandmark;
    TextView tvContinue;

    String name = "";
    String email = "";
    String pass = "";
    String phone = "";
    String otp = "1234";
    String type = "";
    String acno = "";
    String acname = "";
    String ifsc = "";
    String pincode = "";
    String state = "";
    String city = "";
    String buildno = "";
    String area = "";
    String landmark = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_signup);

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
        etPincode = (EditText)findViewById(R.id.etPincode);
        etState = (EditText)findViewById(R.id.etState);
        etCity = (EditText)findViewById(R.id.etCity);
        etBuildNo = (EditText)findViewById(R.id.etBuildNo);
        etArea = (EditText)findViewById(R.id.etArea);
        etLandmark = (EditText)findViewById(R.id.etLandmark);

        tvContinue = (TextView)findViewById(R.id.tvContinue);

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acno = etAcNumber.getText().toString().trim();
                acname = etAcName.getText().toString().trim();
                ifsc = etIFSC.getText().toString().trim();
                pincode = etPincode.getText().toString().trim();
                state = etState.getText().toString().trim();
                city = etCity.getText().toString().trim();
                buildno = etBuildNo.getText().toString().trim();
                area = etArea.getText().toString().trim();
                landmark = etLandmark.getText().toString().trim();
                if(acno.length() > 0 && acname.length()>0 && ifsc.length()>0 &&
                        pincode.length()>0 && state.length()>0 && city.length()>0 &&
                        buildno.length()>0 && area.length()>0 && landmark.length()>0 ){
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
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.redoxygen.net/sms.dll?Action=SendSMS&AccountId=CI00192044&Email=agarwal.harshnu@gmail.com&Password=XAec1kq0&Recipient="+phone+"&Message=Welcome%20to%20Eat@Home%20Family.%20Your%20OTP%20for%20registration%20is%20"+otp;
// Request a string response from the provided URL.
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
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
