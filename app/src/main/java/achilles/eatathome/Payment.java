package achilles.eatathome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class Payment extends AppCompatActivity {
    String mid, date, time, tid = "0", payby = "1" ;
    String del_address = "";
    String name, email, phone;

    SessionManager session;

    private static final String ADDTRANS_URL = "http://eatathome.pe.hu/AddTrans.php";
    private static final String PLACEORDER_URL = "http://eatathome.pe.hu/PlaceOrder.php";
    private HashMap<String, String> params = new HashMap<>();
    private String id;
    private String qq;
    private String total;
    private String grand_total;
    private String delivery;
    private String discount;
    String transtatus = "0";
    public String oid = "";
    String did = "";

    private String type = "";
    private String sold;
    private String s_balance;
    private String sid;
    private String c_balance;
    private String dbid;
    private String sup_address;
    private String paybill;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        email = user.get(SessionManager.KEY_EMAIL);
        phone = user.get(SessionManager.KEY_PHONE);
        id = user.get(SessionManager.KEY_ID);
        type = user.get(SessionManager.KEY_TYPE);

        final Intent inten = getIntent();
        paybill = inten.getStringExtra("paybill");
        mid = inten.getStringExtra("mid");
        id = inten.getStringExtra("cid");
        time = inten.getStringExtra("time");
        date = inten.getStringExtra("date");
        qq = inten.getStringExtra("quan");
        total = inten.getStringExtra("amount");
        grand_total = inten.getStringExtra("total");
        delivery = inten.getStringExtra("deliv");
        discount = inten.getStringExtra("disc");
        payby = inten.getStringExtra("payby");
        del_address = inten.getStringExtra("address");
        sid = inten.getStringExtra("sid");
        s_balance = inten.getStringExtra("s_balance");
        c_balance = inten.getStringExtra("c_balance");
        dbid = inten.getStringExtra("dbid");
        sup_address = inten.getStringExtra("sup_address");
        sold = inten.getStringExtra("sold");
        init();

        Intent intent = new Intent(this, MakePaymentActivity.class);
        intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_DEV);
        intent.putExtra(PayUMoney_Constants.PARAMS, params);

        startActivityForResult(intent, PayUMoney_Constants.PAYMENT_REQUEST);
    }

    private synchronized void init() {

        params.put(PayUMoney_Constants.KEY, "rjQUPktU");
        params.put(PayUMoney_Constants.TXN_ID, "TXN1234");
        params.put(PayUMoney_Constants.AMOUNT, paybill);
        params.put(PayUMoney_Constants.PRODUCT_INFO, "Eat@Home");
        params.put(PayUMoney_Constants.FIRST_NAME, name);
        params.put(PayUMoney_Constants.EMAIL, email);
        params.put(PayUMoney_Constants.PHONE, phone);
        params.put(PayUMoney_Constants.SURL, "https://www.payumoney.com/mobileapp/payumoney/success.php");
        params.put(PayUMoney_Constants.FURL, "https://www.payumoney.com/mobileapp/payumoney/failure.php");
        params.put(PayUMoney_Constants.UDF1, "");
        params.put(PayUMoney_Constants.UDF2, "");
        params.put(PayUMoney_Constants.UDF3, "");
        params.put(PayUMoney_Constants.UDF4, "");
        params.put(PayUMoney_Constants.UDF5, "");

        String hash = Utils.generateHash(params, "e5iIg1jwi8");

        params.put(PayUMoney_Constants.HASH, hash);
        params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayUMoney_Constants.PAYMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "PAYMENT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                transtatus = "1";
                tid = data.getStringExtra("result");
                generateBill(tid,transtatus);
                Log.w(TAG, "onActivityResult: oid" + oid );
                Intent inten = new Intent(Payment.this, Bill.class);
                inten.addCategory(Intent.CATEGORY_HOME);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inten.putExtra("oid",oid);
                inten.putExtra("payment","SUCCESSFUL");
                inten.putExtra("amount",grand_total);
                inten.putExtra("tid",tid);
                startActivity(inten);
                finish();

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "PAYMENT FAILED", Toast.LENGTH_SHORT).show();
                transtatus = "0";
                oid = "0";
                tid = data.getStringExtra("result");
                addTransaction(oid,tid, transtatus);
                Intent inten = new Intent(Payment.this, Bill.class);
                inten.addCategory(Intent.CATEGORY_HOME);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inten.putExtra("oid",oid);
                inten.putExtra("payment","FAILED");
                inten.putExtra("amount",grand_total);
                inten.putExtra("tid",tid);
                startActivity(inten);
                finish();
            }
        }
    }

    private void addTransaction(final String xx, final String tid, final String transtatus) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDTRANS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.w(TAG, "onResponse: " + response.toString() );
                            if (success) {
                            } else {
                                Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("tid", tid);
                map.put("oid", xx);
                map.put("time", time);
                map.put("amount", grand_total + "");
                map.put("transtatus", transtatus);
                map.put("transtype", "1");
                map.put("type", type);
                Log.w(TAG, "getParams: " + id + tid + "pp: "+xx + time + grand_total + transtatus + "1");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void generateBill(final String tid, final String transtatus) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PLACEORDER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.w(TAG, "onResponse: "+ response.toString() );
                            if (success) {

                                oid = jsonResponse.getString("oid");
                                did = jsonResponse.getString("did");
                                addTransaction(oid,tid, transtatus);
                                Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                oid = "-1";
                                Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("mid", mid);
                map.put("cid", id);
                map.put("tid",tid);
                map.put("time",time);
                map.put("date",date);
                map.put("quan",qq + "");
                map.put("amount",total + "");
                map.put("total",grand_total + "");
                map.put("deliv",delivery + "");
                map.put("disc", discount + "");
                map.put("payby", "1");
                map.put("status","0");
                map.put("address",del_address);
                map.put("sold",sold);
                map.put("sid",sid);
                map.put("s_balance", s_balance);
                map.put("c_balance",c_balance);
                map.put("sup_address",sup_address);
                map.put("dbid",dbid);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Log.w(TAG, "xxxxxxxxx : "+oid );

    }
}