package achilles.eatathome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

public class BrowseMenu extends AppCompatActivity {

    RelativeLayout ic2, ic4;
    SessionManager session;
    String name = "";
    String email = "";
    String id = "";
    String phone = "";
    String type = "";
    String acno = "";
    String acname = "";
    String ifsc = "";
    String balance = "";
    String address = "";
    String aid = "0";
    int veg = 0;
    TextView tvDefault;
    TextView tvArea;
    TextView tvAreaDet;
    ImageView ivVNV;
    Button bDefault;
    Button bCheckout;
    ScrollView svMenu;

    private static final String ORDER_URL = "http://eatathome.pe.hu/Menu_list.php";
    private static final String TAG = "Browse Menu";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HashMap<String, String>> menuList;
    ArrayList<HashMap<String, String>> menuVegList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_menu);

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

        ic2 = (RelativeLayout)findViewById(R.id.ic2);
        ic4 = (RelativeLayout)findViewById(R.id.ic4);

        ic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(BrowseMenu.this, Cart.class);
                startActivity(inten);
            }
        });

        ic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(BrowseMenu.this, Profile.class);
                startActivity(inten);
            }
        });

        ivVNV = (ImageView)findViewById(R.id.ivVNV);
        bDefault = (Button)findViewById(R.id.bDefault);
        bCheckout = (Button)findViewById(R.id.bCheckout);
        tvArea = (TextView)findViewById(R.id.tvArea);
        tvAreaDet = (TextView)findViewById(R.id.tvAreaDet);
        tvDefault = (TextView)findViewById(R.id.tvDefault);
        svMenu = (ScrollView)findViewById(R.id.svMenu);

        ivVNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(veg == 1){
                    veg = 0;
                    ivVNV.setImageResource(R.drawable.nonveg);
                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(BrowseMenu.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new MenuAdapter(menuList,null);
                    recyclerView.setAdapter(mAdapter);
                    MenuAdapter.Callback adapterListener = new MenuAdapter.Callback() {
                        @Override
                        public void onImageClick(final int left, final HashMap<String,String> cart, int select) {
                            if(select == 1){
                                bCheckout.setVisibility(View.VISIBLE);
                                if(left > 0){
                                    bCheckout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(BrowseMenu.this, PlaceOrder.class);
                                            intent.putExtra("cart", cart);
                                            startActivity(intent);
                                        }
                                    });
                                }else {
                                    bCheckout.setText("Out of Stock");
                                    bCheckout.setBackgroundColor(Color.parseColor("#726969"));
                                }

                            }else {
                                bCheckout.setVisibility(View.INVISIBLE);
                            }
                        }
                    };


                    MenuAdapter.setCallback(adapterListener);
                }else{
                    veg = 1;
                    ivVNV.setImageResource(R.drawable.veg);
                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(BrowseMenu.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new MenuAdapter(menuVegList,null);
                    recyclerView.setAdapter(mAdapter);
                    MenuAdapter.Callback adapterListener = new MenuAdapter.Callback() {
                        @Override
                        public void onImageClick(final int left, final HashMap<String,String> cart, int select) {
                            if(select == 1){
                                bCheckout.setVisibility(View.VISIBLE);
                                if(left > 0){
                                    bCheckout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(BrowseMenu.this, PlaceOrder.class);
                                            intent.putExtra("cart", cart);
                                            startActivity(intent);
                                        }
                                    });
                                }else {
                                    bCheckout.setText("Out of Stock");
                                    bCheckout.setBackgroundColor(Color.parseColor("#726969"));
                                }

                            }else {
                                bCheckout.setVisibility(View.INVISIBLE);
                            }
                        }
                    };


                    MenuAdapter.setCallback(adapterListener);
                }
            }
        });

        menuList = new ArrayList<>();

        menuVegList = new ArrayList<>();
        Log.w(TAG, "onCreate: Compulsory Call" );
        recyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);
        Log.w(TAG, "onCreate: after listview initalization " );
        getMenuList();

    }


    private void getMenuList() {

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
                                JSONArray menu = jsonResponse.getJSONArray("menu");
                                if(menu.length()>0){
                                    bDefault.setVisibility(View.INVISIBLE);
                                    svMenu.setVisibility(View.VISIBLE);
                                    tvDefault.setVisibility(View.INVISIBLE);
                                }
                                for (int i = 0; i < menu.length(); i++) {
                                    JSONObject c = menu.getJSONObject(i);
                                    // tmp hash map for single hash_list
                                    HashMap<String, String> hash_list = new HashMap<>();

                                    // adding each child node to HashMap key => value
                                    hash_list.put("mid", c.getString("mid"));
                                    hash_list.put("time",c.getString("time"));
                                    hash_list.put("date", c.getString("date"));
                                    hash_list.put("sid",c.getString("sid"));
                                    hash_list.put("sold", c.getString("sold"));
                                    hash_list.put("mname",c.getString("mname"));
                                    hash_list.put("quan",c.getString("quan"));
                                    hash_list.put("cost",c.getString("cost"));
                                    hash_list.put("veg", c.getString("veg"));
                                    hash_list.put("name", c.getString("name"));
                                    hash_list.put("rate",c.getString("rate"));
                                    hash_list.put("items",c.getString("items"));
                                    // adding hash_list to hash_list list
                                    menuList.add(hash_list);
                                    if(c.getString("veg").equalsIgnoreCase("1")){
                                        menuVegList.add(hash_list);
                                    }
                                    Log.w(TAG, "onResponse: Horaha hashlist" );
                                }
                                Log.w(TAG, "onResponse: " + menuList.toString() );
                                recyclerView.setHasFixedSize(true);
                                mLayoutManager = new LinearLayoutManager(BrowseMenu.this);
                                recyclerView.setLayoutManager(mLayoutManager);
                                mAdapter = new MenuAdapter(menuList,null);
                                recyclerView.setAdapter(mAdapter);
                                MenuAdapter.Callback adapterListener = new MenuAdapter.Callback() {
                                    @Override
                                    public void onImageClick(final int left, final HashMap<String,String> cart, int select) {
                                        if(select == 1){
                                            bCheckout.setVisibility(View.VISIBLE);
                                            if(left > 0){
                                                bCheckout.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(BrowseMenu.this, PlaceOrder.class);
                                                        intent.putExtra("cart", cart);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }else {
                                                bCheckout.setText("Out of Stock");
                                                bCheckout.setBackgroundColor(Color.parseColor("#726969"));
                                            }

                                        }else {
                                            bCheckout.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                };

                                MenuAdapter.setCallback(adapterListener);
                            }
                            else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(BrowseMenu.this);
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(BrowseMenu.this);
                        alert.setTitle("Error");
                        alert.setMessage(error.getMessage().toString());
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                Log.w(TAG, "getParams:" + aid);
                map.put("aid", aid);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
