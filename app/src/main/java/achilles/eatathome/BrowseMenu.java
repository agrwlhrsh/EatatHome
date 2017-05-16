package achilles.eatathome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

public class BrowseMenu extends Activity {

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
    String delAddr = "";
    int veg = 0;
    TextView tvDefault;
    TextView tvArea;
    Spinner tvAreaDet;
    ImageView ivVNV;
    Button bDefault;
    Button bCheckout;
    ScrollView svMenu;

    private static final String GETAREA_URL = "http://eatathome.pe.hu/SelectLoc.php";
    private static final String ORDER_URL = "http://eatathome.pe.hu/Menu_list.php";

    private static final String TAG = "Browse Menu";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<HashMap<String, String>> menuList;
    ArrayList<HashMap<String, String>> menuVegList;
    ArrayList<HashMap<String, String>> areaList;
    List<String> categories = new ArrayList<String>();
    List<String> aidlist = new ArrayList<String>();
    List<String> anamelist = new ArrayList<String>();

    ArrayAdapter<String> dataAdapter;

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

        menuList = new ArrayList<>();
        menuVegList = new ArrayList<>();


        getAreaList();

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
        //bDefault = (Button)findViewById(R.id.bDefault);
        bCheckout = (Button)findViewById(R.id.bCheckout);
        tvArea = (TextView)findViewById(R.id.tvArea);
        tvAreaDet = (Spinner)findViewById(R.id.tvAreaDet);
        tvDefault = (TextView)findViewById(R.id.tvDefault);
        svMenu = (ScrollView)findViewById(R.id.svMenu);



        tvAreaDet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvArea.setText(anamelist.get(position));
                delAddr = categories.get(position);
                aid = aidlist.get(position);
                getMenuList(aid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ivVNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(veg == 1){
                    veg = 0;
                    ivVNV.setImageResource(R.drawable.nonveg);
                    showVegTypeList(menuList);
                }else{
                    veg = 1;
                    ivVNV.setImageResource(R.drawable.veg);
                    showVegTypeList(menuVegList);
                }
            }
        });

        Log.w(TAG, "onCreate: Compulsory Call" );
        recyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);
        Log.w(TAG, "onCreate: after listview initalization " );

    }

    private void showVegTypeList(ArrayList<HashMap<String, String>> menus) {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(BrowseMenu.this);
        Log.w(TAG, "onResponse: mLayoutManager = new LinearLayoutManager(BrowseMenu.this);"  );
        recyclerView.setLayoutManager(mLayoutManager);

        Log.w(TAG, "onResponse: mLayoutManager = new LinearLayoutManager(BrowseMenu.this);"  );
        mAdapter = new MenuAdapter1(menus,null);
        recyclerView.setAdapter(mAdapter);
        MenuAdapter1.Callback adapterListener = new MenuAdapter1.Callback() {
            @Override
            public void onImageClick(final int left, final HashMap<String,String> cart, int select) {
                if(select == 1){
                    bCheckout.setVisibility(View.VISIBLE);

                    if(left > 0){
                        bCheckout.setText("PROCEED TO CHECKOUT");
                        bCheckout.setBackgroundColor(Color.parseColor("#b80000"));
                        bCheckout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(BrowseMenu.this, PlaceOrder.class);
                                intent.putExtra("cart", cart);
                                intent.putExtra("delAddr",delAddr);
                                intent.putExtra("dbid",aid);
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

        MenuAdapter1.setCallback(adapterListener);
    }


    private void getMenuList(final String aid) {
        menuVegList.clear();
        menuList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(BrowseMenu.this);
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
                                if(menu.length()>0) {
                                    svMenu.setVisibility(View.VISIBLE);
                                    tvDefault.setVisibility(View.INVISIBLE);

                                    for (int i = 0; i < menu.length(); i++) {
                                        JSONObject c = menu.getJSONObject(i);
                                        // tmp hash map for single hash_list
                                        HashMap<String, String> hash_list = new HashMap<>();

                                        // adding each child node to HashMap key => value
                                        hash_list.put("mid", c.getString("mid"));
                                        hash_list.put("time", c.getString("time"));
                                        hash_list.put("date", c.getString("date"));
                                        hash_list.put("sid", c.getString("sid"));
                                        hash_list.put("sold", c.getString("sold"));
                                        hash_list.put("mname", c.getString("mname"));
                                        hash_list.put("quan", c.getString("quan"));
                                        hash_list.put("cost", c.getString("cost"));
                                        hash_list.put("veg", c.getString("veg"));
                                        hash_list.put("name", c.getString("name"));
                                        hash_list.put("items", c.getString("items"));
                                        hash_list.put("s_balance", c.getString("s_balance"));
                                        hash_list.put("sup_address", c.getString("sup_address"));

                                        // adding hash_list to hash_list list
                                        menuList.add(hash_list);
                                        if (c.getString("veg").equalsIgnoreCase("1")) {
                                            menuVegList.add(hash_list);
                                        }

                                        Log.w(TAG, "onResponse: Horaha hashlist");
                                    }
                                    Log.w(TAG, "onResponse: " + menuList.toString());
                                    if (veg == 0) {

                                        showVegTypeList(menuList);
                                    } else {
                                        showVegTypeList(menuVegList);
                                    }
                                }else {
                                    svMenu.setVisibility(View.INVISIBLE);
                                    tvDefault.setVisibility(View.VISIBLE);

                                }
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

    private void getAreaList() {
        Log.w(TAG, "getAreaList: start hua h h" );
        final ProgressDialog progressDialog = new ProgressDialog(BrowseMenu.this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Log.w(TAG, "getAreaList: happening"  );
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
                                    aidlist.add(c.getString("aid"));
                                    anamelist.add(c.getString("aname"));
                                    Log.w(TAG, "onResponse: Horaha hashlist"+categories.get(i) );
                                }

                                // Creating adapter for spinner
                                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner, categories);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                tvAreaDet.setAdapter(dataAdapter);
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