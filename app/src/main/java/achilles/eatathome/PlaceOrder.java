package achilles.eatathome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.StringTokenizer;

public class PlaceOrder extends AppCompatActivity {

    private static final String PLACEORDER_URL = "http://eatathome.pe.hu/PlaceOrder.php";
    private static final String TAG = "TEST";
    HashMap<String, String> cart;

    TextView tvBack, tvEdit, tvItemName, tvQuantity, tvPrice, tvTotal, tvPacking, tvDelivery;
    TextView tvDiscount, tvGT, tvWallet, tvPay;
    Button bCOD, bPay;
    EditText etName, etArea, etBuildNo, etPincode, etLandmark, etState, etCity;
    ImageView ivDown,ivUp;
    CheckBox cbWallet;

    String type = "",name = "", phone = "", id = "", email = "",address = "", acno = "", acname = "", ifsc = "", balance = "", aid = "";
    String buildNo = "", landmark = "", area = "", pincode = "", city = "", state = "";
    String n_buildNo = "", n_landmark = "";

    SessionManager session;

    String mid, mname, date, time, tid = "0", payby = "2" ;
    int left;
    int sold;
    int qq = 0;
    int delivery = 0;
    int packing = 0;
    int discount = 0;
    float s_balance = 0;
    float cost, wallet = 0;
    float grand_total,total = 0;
    float paybill = 0;
    String n_name = "";
    String del_address = "";
    String delAddr = "";
    String sid = "";
    private float new_wallet = 0;
    private String sup_address = "";
    private String dbid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        final Intent intent = getIntent();
        cart = (HashMap<String, String>) intent.getSerializableExtra("cart");
        delAddr = intent.getStringExtra("delAddr");
        dbid = intent.getStringExtra("dbid");

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
        tvQuantity = (TextView)findViewById(R.id.tvQuantity);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvTotal = (TextView)findViewById(R.id.tvTotal);
        tvPacking = (TextView)findViewById(R.id.tvPacking);
        tvDelivery = (TextView)findViewById(R.id.tvDelivery);
        tvDiscount = (TextView)findViewById(R.id.tvDiscount);
        tvGT = (TextView)findViewById(R.id.tvGT);
        tvWallet = (TextView)findViewById(R.id.tvWallet);
        tvPay = (TextView)findViewById(R.id.tvPay);
        tvItemName = (TextView)findViewById(R.id.tvItemName);
        etName = (EditText)findViewById(R.id.etName);
        etBuildNo= (EditText)findViewById(R.id.etBuildNo);
        etLandmark= (EditText)findViewById(R.id.etLandmark);
        etArea= (EditText)findViewById(R.id.etArea);
        etPincode= (EditText)findViewById(R.id.etPincode);
        etState= (EditText)findViewById(R.id.etState);
        etCity= (EditText)findViewById(R.id.etCity);
        bPay = (Button)findViewById(R.id.bPay);
        bCOD = (Button)findViewById(R.id.bCOD);
        ivUp = (ImageView)findViewById(R.id.ivUp);
        ivDown = (ImageView)findViewById(R.id.ivDown);
        cbWallet = (CheckBox)findViewById(R.id.cbWallet);

        StringTokenizer st = new StringTokenizer(delAddr,",");
        area = st.nextToken().toString() + "";
        city = st.nextToken().toString() + "";
        state = st.nextToken().toString() + "";
        pincode = st.nextToken().toString() + "";

        etName.setText("");
        etBuildNo.setText(buildNo);
        etLandmark.setText(landmark);
        etArea.setText(area);
        etCity.setText(city);
        etState.setText(state);
        etPincode.setText(pincode);
        etName.setEnabled(false);
        etBuildNo.setEnabled(false);
        etLandmark.setEnabled(false);
        etArea.setEnabled(false);
        etCity.setEnabled(false);
        etState.setEnabled(false);
        etPincode.setEnabled(false);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvEdit.getText().toString().equalsIgnoreCase("EDIT")){
                    etName.setEnabled(true);
                    etBuildNo.setEnabled(true);
                    etLandmark.setEnabled(true);
                    etName.setTextColor(Color.parseColor("#333333"));
                    etBuildNo.setTextColor(Color.parseColor("#333333"));
                    etLandmark.setTextColor(Color.parseColor("#333333"));
                    tvEdit.setText("SAVE");
                    tvEdit.setTextColor(Color.parseColor("#33aa33"));
                }else{
                    etName.setEnabled(false);
                    etBuildNo.setEnabled(false);
                    etLandmark.setEnabled(false);
                    etName.setTextColor(Color.parseColor("#aaaaaa"));
                    etBuildNo.setTextColor(Color.parseColor("#aaaaaa"));
                    etLandmark.setTextColor(Color.parseColor("#aaaaaa"));
                    etArea.setTextColor(Color.parseColor("#aaaaaa"));
                    etCity.setTextColor(Color.parseColor("#aaaaaa"));
                    etState.setTextColor(Color.parseColor("#aaaaaa"));
                    etPincode.setTextColor(Color.parseColor("#aaaaaa"));
                    n_name = etName.getText().toString().trim();
                    n_buildNo = etBuildNo.getText().toString().trim();
                    n_landmark = etLandmark.getText().toString().trim();
                    tvEdit.setText("EDIT");
                    tvEdit.setTextColor(Color.parseColor("#b80000"));
                    if(n_name.length()>0 && n_buildNo.length()>0 && n_landmark.length()>0){
                        del_address = n_name + ";"+n_buildNo + ";" + n_landmark + ";" + area + ";" + city + ";" + state  + ";" + pincode;
                    }
                    else{
                        del_address = "";
                        AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrder.this);
                        alert.setMessage("Fill Your Address correctly, some Fields are missing");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }
            }
        });
        bPay.setVisibility(View.INVISIBLE);
        bCOD.setVisibility(View.INVISIBLE);

        cbWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qq>0){
                    if(cbWallet.isChecked()){
                        if(grand_total > 0){
                            if(wallet > grand_total){
                                paybill = 0;
                                new_wallet = wallet - grand_total;
                            }else{
                                paybill = grand_total - wallet;
                                new_wallet = 0;
                            }
                            tvPay.setText(paybill+"");
                        }
                    }else {
                        paybill = grand_total;
                        tvPay.setText(paybill+"");
                    }
                }
            }
        });

        ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qq < left){
                    qq = qq + 1;
                    updateBill();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrder.this);
                    alert.setMessage("Not Enough Stock");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            }
        });
        ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qq > 0){
                    qq = qq - 1;
                    updateBill();
                }
            }
        });

        mname = cart.get("mname");
        date = cart.get("date");
        time = cart.get("time");
        cost = Float.parseFloat(cart.get("cost"));
        left = Integer.parseInt(cart.get("left"));
        sold = Integer.parseInt(cart.get("sold"));
        mid = cart.get("mid");
        sid = cart.get("sid");
        s_balance = Float.parseFloat(cart.get("s_balance"));
        sup_address = cart.get("sup_address");
        //BUG id 33 resolved.. issue because updated at wrong place
        tvPrice.setText(cost+"");
        //BUG id 29 resolved.. Issue because update at wrong place..
        tvItemName.setText(mname);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAddress()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrder.this);
                    alert.setTitle("Confirm your Order");
                    alert.setMessage("Your Bill Amount is \u20B9" + paybill + "\n Click Confirm to place the Order");
                    alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generateBill();
                        }
                    });
                    alert.setNegativeButton("CANCEL", null);
                    alert.show();
                }
            }
        });
        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAddress()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrder.this);
                    alert.setTitle("Confirm your Order");
                    alert.setMessage("Your Bill Amount is \u20B9" + paybill + "\n Click Confirm to place the Order");
                    alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(PlaceOrder.this, Payment.class);
                            intent.putExtra("mid", mid);
                            intent.putExtra("cid", id);
                            intent.putExtra("time", time);
                            intent.putExtra("date", date);
                            intent.putExtra("quan", qq + "");
                            intent.putExtra("amount", total + "");
                            intent.putExtra("total", grand_total + "");
                            intent.putExtra("deliv", delivery + "");
                            intent.putExtra("disc", discount + "");
                            intent.putExtra("payby", "1");
                            intent.putExtra("status", "0");
                            intent.putExtra("address", del_address);
                            intent.putExtra("sid",sid);
                            intent.putExtra("s_balance",(s_balance+total)+"");
                            intent.putExtra("c_balance", new_wallet+"");
                            intent.putExtra("sold", (sold+qq)+"");
                            intent.putExtra("dbid",dbid);
                            intent.putExtra("sup_address",sup_address);
                            intent.putExtra("paybill",paybill+"");
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alert.setNegativeButton("CANCEL", null);
                    alert.show();
                }
            }
        });
    }

    private boolean checkAddress() {
        if(del_address.length()>0){
            return true;
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrder.this);
            alert.setMessage("Enter Your Delivery Address Details");
            alert.setPositiveButton("OK",null);
            alert.show();
            return false;
        }
    }

    private void generateBill() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing... Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PLACEORDER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String oid = jsonResponse.getString("oid");
                            String did = jsonResponse.getString("did");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_LONG).show();
                                Intent inten = new Intent(PlaceOrder.this, Bill.class);
                                inten.putExtra("oid",oid);
                                inten.putExtra("payment","COD");
                                //Bug id 38 Showing null in place of AMOUNT TO BE PAID..
                                inten.putExtra("amount",paybill+"");
                                inten.putExtra("tid","0");
                                inten.putExtra("did",did);
                                inten.addCategory(Intent.CATEGORY_HOME);
                                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(inten);
                            } else {
                                Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                Log.w(TAG, "getParams: "+ mid );
                Log.w(TAG, "getParams: "+ id );
                Log.w(TAG, "getParams: "+ tid );
                Log.w(TAG, "getParams: "+ time );
                Log.w(TAG, "getParams: "+ date );
                Log.w(TAG, "getParams: "+ qq );
                Log.w(TAG, "getParams: "+ total );
                Log.w(TAG, "getParams: "+ grand_total );
                Log.w(TAG, "getParams: "+ delivery );
                Log.w(TAG, "getParams: "+ discount );
                Log.w(TAG, "getParams: "+ payby );
                Log.w(TAG, "getParams: "+ "0" );
                Log.w(TAG, "getParams: "+ del_address );
                Log.w(TAG, "getParams: "+ (sold+qq) );
                Log.w(TAG, "getParams: "+ sid);
                Log.w(TAG, "getParams: "+ (s_balance+total) );
                Log.w(TAG, "getParams: "+ new_wallet );
                Log.w(TAG, "getParams: "+ sup_address );
                Log.w(TAG, "getParams: "+ dbid );
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
                map.put("payby", "2");
                map.put("status","0");
                map.put("address",del_address);
                map.put("sold",(sold+qq)+"");
                map.put("sid",sid);
                map.put("s_balance",(s_balance+total)+"");
                map.put("c_balance", new_wallet + "");
                map.put("sup_address",sup_address);
                map.put("dbid",dbid);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateBill() {
        // bug id 30 resolved. deliver cost updated wrong
        delivery = 0;
        packing = 0;
        discount = 0;
        if(qq > 0){
            bCOD.setVisibility(View.VISIBLE);
            bPay.setVisibility(View.VISIBLE);
            total = cost*qq;
            if(total > 100){
                delivery = 0;
            }else{
                delivery = 20;
            }
        }else{
            delivery = 0;
            total = 0;
            bCOD.setVisibility(View.INVISIBLE);
            bPay.setVisibility(View.INVISIBLE);
        }

        grand_total = delivery + packing - discount + total;
        tvQuantity.setText(qq+"");
        tvTotal.setText(total+"");
        tvDelivery.setText(delivery + "");
        tvPacking.setText(packing+"");
        tvDiscount.setText(discount+"");
        tvGT.setText(grand_total+"");
        tvPay.setText(grand_total+"");
        wallet = Float.parseFloat(balance);
        new_wallet = wallet;
        tvWallet.setText(wallet + "");
        new_wallet = wallet;
        if(cbWallet.isChecked()){
            if(grand_total > 0){
                if(wallet > grand_total){
                    paybill = 0;
                    new_wallet = wallet - grand_total;
                }else{
                    paybill = grand_total - wallet;
                    new_wallet = 0;
                }
            }else{
                paybill = 0;
            }
        }else {
            paybill = grand_total;
        }
        tvPay.setText(paybill + "");
    }
}
