package achilles.eatathome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class Orders extends AppCompatActivity {

    String type = "",name = "", phone = "", id = "", email = "",address = "", acno = "", acname = "", ifsc = "", balance = "", aid = "";

    TextView tvBack;

    private static final String ORDER_URL = "http://eatathome.pe.hu/Order_list.php";
    private static final String TAG = "Orders";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HashMap<String, String>> orderList;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

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
        orderList = new ArrayList<>();
        Log.w(TAG, "onCreate: Compulsory Call" );
        recyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);
        tvBack = (TextView)findViewById(R.id.tvBack);
        Log.w(TAG, "onCreate: after listview initalization " );
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getOrderList();
    }

    private void getOrderList() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(TAG, "onResponse: " + response );
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            progressDialog.dismiss();
                            if(success) {
                                JSONArray order = jsonResponse.getJSONArray("order");
                                for (int i = 0; i < order.length(); i++) {
                                    JSONObject c = order.getJSONObject(i);

                                    // tmp hash map for single hash_list
                                    HashMap<String, String> hash_list = new HashMap<>();

                                    // adding each child node to HashMap key => value
                                    hash_list.put("mid", c.getString("mid"));
                                    hash_list.put("time",c.getString("time"));
                                    hash_list.put("date", c.getString("date"));
                                    hash_list.put("payby",c.getString("payby"));
                                    hash_list.put("amount", c.getString("amount"));
                                    hash_list.put("deliv",c.getString("deliv"));
                                    hash_list.put("disc",c.getString("disc"));
                                    hash_list.put("total",c.getString("total"));
                                    hash_list.put("oid", c.getString("oid"));
                                    hash_list.put("status", c.getString("status"));
                                    hash_list.put("address", c.getString("address"));
                                    hash_list.put("quantity",c.getString("quantity"));
                                    hash_list.put("mname",c.getString("mname"));
                                    hash_list.put("items",c.getString("items"));
                                    hash_list.put("tid",c.getString("tid"));
                                    // adding hash_list to hash_list list
                                    orderList.add(hash_list);
                                    Log.w(TAG, "onResponse: Horaha hashlist" );
                                }
                                Log.w(TAG, "onResponse: " + orderList.toString() );
                                recyclerView.setHasFixedSize(true);
                                mLayoutManager = new LinearLayoutManager(Orders.this);
                                recyclerView.setLayoutManager(mLayoutManager);
                                cAdapter = new OrderAdapter(orderList);
                                recyclerView.setAdapter(cAdapter);
                            }
                            else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(Orders.this);
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(Orders.this);
                        alert.setTitle("Error");
                        alert.setMessage(error.getMessage().toString());
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                Log.w(TAG, "getParams:");
                map.put("id", id);
                map.put("type",type);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
