package achilles.eatathome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Cook extends AppCompatActivity {
    RelativeLayout ic1, ic4;
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

    private int mYear,mMonth,mDay,mHour;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HashMap<String, String>> sup_menuList;
    private static final String TAG = "Cook";
    private static final String SMENU_URL = "http://eatathome.pe.hu/SMenu_list.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

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

        ic1 = (RelativeLayout)findViewById(R.id.ic1);
        ic4 = (RelativeLayout)findViewById(R.id.ic4);

        ic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(Cook.this, UploadMenu.class);
                startActivity(inten);
            }
        });

        ic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(Cook.this, Profile.class);
                startActivity(inten);
            }
        });
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        sup_menuList = new ArrayList<>();
        Log.w(TAG, "onCreate: Compulsory Call" );
        recyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);
        showMenuList();
    }
    private void showMenuList() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMENU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(TAG, "onResponse: " + response );
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            progressDialog.dismiss();
                            if(success) {
                                JSONArray order = jsonResponse.getJSONArray("menu");
                                for (int i = 0; i < order.length(); i++) {
                                    JSONObject c = order.getJSONObject(i);
                                    HashMap<String, String> hash_list = new HashMap<>();
                                    hash_list.put("mid", c.getString("mid"));
                                    hash_list.put("time",c.getString("time"));
                                    hash_list.put("date", c.getString("date"));
                                    hash_list.put("quan",c.getString("quan"));
                                    hash_list.put("items", c.getString("items"));
                                    hash_list.put("mname",c.getString("mname"));
                                    hash_list.put("cost",c.getString("cost"));
                                    hash_list.put("veg", c.getString("veg"));
                                    hash_list.put("sold", c.getString("sold"));
                                    String date = c.getString("date");
                                    String time = c.getString("time");
                                    boolean flag = false;
                                    StringTokenizer st = new StringTokenizer(date,"-");
                                    int dd = Integer.parseInt(st.nextToken().toString());
                                    int mm = Integer.parseInt(st.nextToken().toString());
                                    int yy = Integer.parseInt(st.nextToken().toString());
                                    Log.d(TAG, "onResponse: "+dd+" "+mDay+" " +mm+" " +mMonth+" "+yy+" "+mYear+" " + mHour);
                                    if(yy>mYear){
                                        flag = true;
                                    }else{
                                        if(mm > mMonth){
                                            flag = true;
                                        }else{
                                            if(dd > mDay){
                                                flag = true;
                                            }else{
                                                if(dd == mDay){
                                                    if( mHour < 12){
                                                        flag = true;
                                                    }else{
                                                        if(mHour >=12 && mHour < 19 && time.equals("2")){
                                                            flag = true;
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                    if(flag){
                                        sup_menuList.add(hash_list);
                                    }
                               }
                                Log.w(TAG, "onResponse: " + sup_menuList.toString() );
                                recyclerView.setHasFixedSize(true);
                                mLayoutManager = new LinearLayoutManager(Cook.this);
                                recyclerView.setLayoutManager(mLayoutManager);
                                cAdapter = new CookAdapter(sup_menuList);
                                recyclerView.setAdapter(cAdapter);
                            }
                            else{
                                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Cook.this);
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
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Cook.this);
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
    }}
