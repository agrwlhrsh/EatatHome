package achilles.eatathome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    int qq = 0;
    int delivery = 0;
    int packing = 0;
    int discount = 0;
    float cost, wallet = 0;
    float grand_total,total = 0;
    float paybill = 0;
    boolean use_wallet = false;
    String n_name = "";
    String del_address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

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

        if(address.length() > 5){
            StringTokenizer st = new StringTokenizer(address,";");
            buildNo = st.nextToken().toString() + "";
            landmark = st.nextToken().toString() + "";
            area = st.nextToken().toString() + "";
            city = st.nextToken().toString() + "";
            state = st.nextToken().toString() + "";
            pincode = st.nextToken().toString() + "";
        }

        etName.setText(name);
        etBuildNo.setText(buildNo);
        etLandmark.setText(landmark);
        etArea.setText(area);
        etCity.setText(city);
        etState.setText(state);
        etPincode.setText(pincode);
        del_address = name+";"+buildNo+";"+landmark+";"+area+";"+city+";"+state+";"+pincode;
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

                    etBuildNo.setEnabled(false);
                    etLandmark.setEnabled(false);
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
                    del_address = n_name + ";"+n_buildNo + ";" + n_landmark + ";" + area + ";" + city + ";" + state  + ";" + pincode;
                }
            }
        });
        bPay.setVisibility(View.INVISIBLE);
        bCOD.setVisibility(View.INVISIBLE);

        cbWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbWallet.isChecked()){
                    if(grand_total > 0){
                        paybill = grand_total - wallet;
                        tvPay.setText(paybill+"");
                    }
                }else {
                    paybill = grand_total;
                    tvPay.setText(paybill+"");
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

        final Intent intent = getIntent();
        cart = (HashMap<String, String>) intent.getSerializableExtra("cart");
        mname = cart.get("mname");
        date = cart.get("date");
        time = cart.get("time");
        cost = Float.parseFloat(cart.get("cost"));
        left = Integer.parseInt(cart.get("left"));
        mid = cart.get("mid");

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

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
                            if (success) {
                                Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_LONG).show();
                                Intent inten = new Intent(PlaceOrder.this, Cart.class);
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
                map.put("payby", payby);
                map.put("status","0");
                map.put("address",del_address);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateBill() {
        if(qq > 0){
            bCOD.setVisibility(View.VISIBLE);
            bPay.setVisibility(View.VISIBLE);
        }else{

            bCOD.setVisibility(View.INVISIBLE);
            bPay.setVisibility(View.INVISIBLE);
        }
        tvQuantity.setText(qq+"");
        tvItemName.setText(mname);
        tvPrice.setText(cost + "");
        total = cost*qq;
        tvTotal.setText(total+"");
        if(total > 100){
            delivery = 0;
            packing = 0;
            discount = 0;
        }else{
            discount = 0;
            packing = 0;
            delivery = 20;
        }
        tvDelivery.setText(delivery + "");
        tvPacking.setText(packing+"");
        tvDiscount.setText(discount+"");
        grand_total = delivery + packing - discount + total;
        tvGT.setText(grand_total+"");
        wallet = Float.parseFloat(balance);
        tvWallet.setText(wallet + "");
        use_wallet = cbWallet.isChecked();
        if(use_wallet){
            paybill = grand_total - wallet;
        }else{
            paybill = grand_total;
        }
        tvPay.setText(paybill + "");
    }
}
