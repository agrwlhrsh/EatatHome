package achilles.eatathome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class Wallet extends AppCompatActivity {


    String type = "",name = "", phone = "", id = "", email = "",address = "", acno = "", acname = "", ifsc = "", balance = "", aid = "";

    TextView tvBack, tvTBBal, tvBal, tvAdd, tvWithdraw;

    private static final String ZEROBAL_URL = "http://eatathome.pe.hu/ZeroBal.php";
    private static final String TRANS_URL = "http://eatathome.pe.hu/Trans_hist.php";
    private static final String TAG = "Wallet";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HashMap<String, String>> transList;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

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

        RelativeLayout llBal = (RelativeLayout)findViewById(R.id.llBal);

        tvBack = (TextView)findViewById(R.id.tvBack);
        tvTBBal = (TextView)findViewById(R.id.tvTBBal);
        tvBal = (TextView)findViewById(R.id.tvBal);
//        tvAdd = (TextView)findViewById(R.id.tvAdd);
        tvWithdraw = (TextView)findViewById(R.id.tvWithdraw);


        tvTBBal.setText("\u20B9 " + balance);
        tvBal.setText("BALANCE    \u20B9 " + balance);

        if(type.equalsIgnoreCase("CUST")){
            ViewGroup.LayoutParams params = llBal.getLayoutParams();
            params.height = 0;
            llBal.setLayoutParams(params);
            llBal.setVisibility(View.INVISIBLE);
        }

        tvWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBalance();
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        transList = new ArrayList<>();
        Log.w(TAG, "onCreate: Compulsory Call" );
        recyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);
        Log.w(TAG, "onCreate: after listview initalization " );
        getTransList();
    }

    private void clearBalance() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ZEROBAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(TAG, "onResponse: " + response );
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            progressDialog.dismiss();
                            if(success) {
                                session.updateBalance("0");
                                balance = "0";
                                tvTBBal.setText("\u20B9 " + balance);
                                tvBal.setText("BALANCE    \u20B9 " + balance);
                                AlertDialog.Builder alert = new AlertDialog.Builder(Wallet.this);
                                alert.setTitle("Yipieee!");
                                alert.setMessage("The Amount will be reflected in your bank A/c within 2 working days!!!");
                                alert.setPositiveButton("OK",null);
                                alert.show();
                            }
                            else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(Wallet.this);
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(Wallet.this);
                        alert.setMessage("Check your Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                Log.w(TAG, "getParams:");
                map.put("amount",balance);
                map.put("id", id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getTransList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TRANS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(TAG, "onResponse: " + response );
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String balance_new = jsonResponse.getString("balance");
                            session.updateBalance(balance_new);
                            balance = balance_new;
                            tvTBBal.setText("\u20B9 " + balance);
                            tvBal.setText("BALANCE    \u20B9 " + balance);
                            progressDialog.dismiss();
                            if(success) {
                                JSONArray trans = jsonResponse.getJSONArray("trans");
                                for (int i = 0; i < trans.length(); i++) {
                                    JSONObject c = trans.getJSONObject(i);

                                    // tmp hash map for single hash_list
                                    HashMap<String, String> hash_list = new HashMap<>();

                                    // adding each child node to HashMap key => value
                                    hash_list.put("tid", c.getString("tid"));
                                    hash_list.put("amount", c.getString("amount"));
                                    hash_list.put("oid", c.getString("oid"));
                                    hash_list.put("tstatus", c.getString("tstatus"));
                                    hash_list.put("date", c.getString("date"));
                                    hash_list.put("ttype", c.getString("ttype"));
                                    // adding hash_list to hash_list list
                                    transList.add(hash_list);
                                    Log.w(TAG, "onResponse: Horaha hashlist" );
                                }
                                recyclerView.setHasFixedSize(true);
                                mLayoutManager = new LinearLayoutManager(Wallet.this);
                                recyclerView.setLayoutManager(mLayoutManager);
                                cAdapter = new CustomAdapter(transList);
                                recyclerView.setAdapter(cAdapter);
                            }
                            else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(Wallet.this);
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(Wallet.this);
                        alert.setMessage("Check your Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                Log.w(TAG, "getParams:");
                map.put("id", id);
                map.put("type", type);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}