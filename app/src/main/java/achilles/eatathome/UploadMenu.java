package achilles.eatathome;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class UploadMenu extends AppCompatActivity {

    private static final String EDITMENU_URL = "http://eatathome.pe.hu/EditMenu.php";
    private static final String UPLOADMENU_URL = "http://eatathome.pe.hu/UploadMenu.php";
    private static final String SMENU_URL = "http://eatathome.pe.hu/SMenu_list.php";
    private static final String TAG = "Upload Menu";
    private int mYear,mMonth,mDay,mHour;

    RelativeLayout ic1, ic4;
    SessionManager session;

    String date = "", menu = "", item = "", quan = "", cost = "", veg = "-1", time = "0";
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

    private RecyclerView recyclerView;
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HashMap<String, String>> sup_menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_menu);

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

        ic1 = (RelativeLayout)findViewById(R.id.ic2);
        ic4 = (RelativeLayout)findViewById(R.id.ic4);

        ic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(UploadMenu.this, Cook.class);
                startActivity(inten);
            }
        });

        ic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(UploadMenu.this, Profile.class);
                startActivity(inten);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
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
                                    sup_menuList.add(hash_list);
                                    Log.w(TAG, "onResponse: Horaha hashlist" );
                                }
                                Log.w(TAG, "onResponse: " + sup_menuList.toString() );
                                recyclerView.setHasFixedSize(true);
                                mLayoutManager = new LinearLayoutManager(UploadMenu.this);
                                recyclerView.setLayoutManager(mLayoutManager);
                                cAdapter = new UploadAdapter(sup_menuList,null);
                                recyclerView.setAdapter(cAdapter);
                                UploadAdapter.Callback adapterListener = new UploadAdapter.Callback() {
                                    @Override
                                    public void onUploadClick(HashMap<String, String> cart) {
                                        editMenu(cart);
                                    }
                                };
                                UploadAdapter.setCallback(adapterListener);
                            }
                            else{
                                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(UploadMenu.this);
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
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(UploadMenu.this);
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
    private void editMenu(HashMap<String, String> cart) {
        String mName = cart.get("mname");
        String mMid = cart.get("mid");
        String mDate = cart.get("date");
        String mTime = cart.get("time");
        String mQuan = cart.get("quan");
        String mCost = cart.get("cost");
        String mVeg = cart.get("veg");
        String mDesc = cart.get("items");

        openEditDialog(mMid, mDate, mTime, mName, mDesc, mQuan,mCost, mVeg);
    }
    private void openDialog(){
        LayoutInflater inflater = LayoutInflater.from(UploadMenu.this);
        View subView = inflater.inflate(R.layout.menu_form, null);
        final TextView  etDate = (TextView) subView.findViewById(R.id.etDate);
        final EditText  etMenu = (EditText)subView.findViewById(R.id.etMenu);
        final EditText  etItem = (EditText)subView.findViewById(R.id.etItem);
        final EditText  etQuan = (EditText)subView.findViewById(R.id.etQuan);
        final EditText  etCost = (EditText)subView.findViewById(R.id.etCost);
        final TextView tvV = (TextView)subView.findViewById(R.id.tvV);
        final TextView tvNV = (TextView)subView.findViewById(R.id.tvNV);
        final TextView tvLunch = (TextView)subView.findViewById(R.id.tvLunch);
        final TextView tvDinner = (TextView)subView.findViewById(R.id.tvDinner);
        final TextView tvUpload = (TextView)subView.findViewById(R.id.tvUpload);
        final TextView tvCancel = (TextView)subView.findViewById(R.id.tvCancel);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();
        builder.show();
        etDate.setText(mDay+"-"+mMonth+"-"+mYear);

        //BUD id 31 resolved.. Cancel was not working so called a new Intent.. Now working fine..
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(UploadMenu.this, UploadMenu.class);
                startActivity(inten);
            }
        });

        //BUG id 27 resolved.. Not working hence reopening the asme activity.. Now working fine..
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = etMenu.getText().toString().trim();
                date = etDate.getText().toString().trim();
                item = etItem.getText().toString().trim();
                cost = etCost.getText().toString().trim();
                quan = etQuan.getText().toString().trim();
                StringTokenizer st = new StringTokenizer(date,"-");
                int dd = Integer.parseInt(st.nextToken().toString());
                int mm = Integer.parseInt(st.nextToken().toString());
                int yy = Integer.parseInt(st.nextToken().toString());
                boolean flag = false;
                if(yy>mYear){
                    flag = true;
                }else{
                    if(mm > mMonth){
                        flag = true;
                    }else{
                        if(dd > mDay){
                            flag = true;
                        }else{
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
                if (flag == false) {
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(UploadMenu.this);
                    alert.setMessage("Sorry The time to upload Lunch has passed. Please Select a different time ");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else{
                    if(menu.length()<1 || item.length()<1 || cost.length()<1 || quan.length()<1 || veg.equals("-1") || time.equals("0")){
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(UploadMenu.this);
                        alert.setMessage("Fields Cannot be Empty. Please fill in all details of Menu...");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else{
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(UploadMenu.this);
                        alert.setMessage("Are you sure you want to Continue??");
                        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                alertDialog.dismiss();
                                uploadMenu();
                            }
                        });
                        alert.setNegativeButton("No", null);
                        alert.show();
                    }
                }

            }
        });
        tvLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLunch.setBackgroundResource(R.drawable.selectbackground);
                tvLunch.setTextColor(Color.WHITE);
                tvDinner.setBackgroundResource(R.drawable.textcorner);
                tvDinner.setTextColor(Color.BLACK);
                time = "1";
            }
        });
        tvDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLunch.setBackgroundResource(R.drawable.textcorner);
                tvLunch.setTextColor(Color.BLACK);
                tvDinner.setBackgroundResource(R.drawable.selectbackground);
                tvDinner.setTextColor(Color.WHITE);
                time = "2";
            }
        });
        tvV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvV.setBackgroundResource(R.drawable.vegbackground);
                tvNV.setBackgroundResource(R.drawable.textcorner);
                veg = "1";
            }
        });
        tvNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvV.setBackgroundResource(R.drawable.textcorner);
                tvNV.setBackgroundResource(R.drawable.nonvegbackground);
                veg = "0";
            }
        });
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                etDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(UploadMenu.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etDate.setText(dayOfMonth + "-"+(monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, (mMonth-1), mDay);

                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });
    }
    private void openEditDialog(final String mMid, final String mDate, final String mTime, final String mName, final String mDesc, final String mQuan, final String mCost, final String mVeg){
        LayoutInflater inflater = LayoutInflater.from(UploadMenu.this);
        View subView = inflater.inflate(R.layout.menu_form, null);
        final TextView  etDate = (TextView) subView.findViewById(R.id.etDate);
        final EditText  etMenu = (EditText)subView.findViewById(R.id.etMenu);
        final EditText  etItem = (EditText)subView.findViewById(R.id.etItem);
        final EditText  etQuan = (EditText)subView.findViewById(R.id.etQuan);
        final EditText  etCost = (EditText)subView.findViewById(R.id.etCost);
        final TextView tvV = (TextView)subView.findViewById(R.id.tvV);
        final TextView tvNV = (TextView)subView.findViewById(R.id.tvNV);
        final TextView tvLunch = (TextView)subView.findViewById(R.id.tvLunch);
        final TextView tvDinner = (TextView)subView.findViewById(R.id.tvDinner);
        final TextView tvUpload = (TextView)subView.findViewById(R.id.tvUpload);
        final TextView tvCancel = (TextView)subView.findViewById(R.id.tvCancel);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();
        builder.show();

        etMenu.setText(mName);
        etItem.setText(mDesc);
        etCost.setText(mCost);
        etDate.setText(mDate);
        etQuan.setText(mQuan);
        if(mTime.equalsIgnoreCase("1")){
            tvLunch.setBackgroundResource(R.drawable.selectbackground);
            tvLunch.setTextColor(Color.WHITE);
            tvDinner.setBackgroundResource(R.drawable.textcorner);
            tvDinner.setTextColor(Color.BLACK);
            time = "1";
        }else{
            tvLunch.setBackgroundResource(R.drawable.textcorner);
            tvLunch.setTextColor(Color.BLACK);
            tvDinner.setBackgroundResource(R.drawable.selectbackground);
            tvDinner.setTextColor(Color.WHITE);
            time = "2";
        }
        if(mVeg.equalsIgnoreCase("1")){
            tvV.setBackgroundResource(R.drawable.vegbackground);
            tvNV.setBackgroundResource(R.drawable.textcorner);
            veg = "1";
        }else{
            tvNV.setBackgroundResource(R.drawable.nonvegbackground);
            tvV.setBackgroundResource(R.drawable.textcorner);
            veg = "0";
        }


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = etMenu.getText().toString().trim();
                date = etDate.getText().toString().trim();
                item = etItem.getText().toString().trim();
                cost = etCost.getText().toString().trim();
                quan = etQuan.getText().toString().trim();

                if(menu.length()<1 || item.length()<1 || cost.length()<1 || quan.length()<1 || veg.equals("-1") || time.equals("0")){
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(UploadMenu.this);
                    alert.setMessage("Fields Cannot be Empty. Please fill in all details of Menu...");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else{
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(UploadMenu.this);
                    alert.setMessage("Are you sure you want to Continue??");
                    alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            uploadEditMenu(mMid, date, time, menu,item, quan,cost, veg);
                        }
                    });
                    alert.setNegativeButton("No", null);
                    alert.show();
                }
            }
        });
        tvLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLunch.setBackgroundResource(R.drawable.selectbackground);
                tvLunch.setTextColor(Color.WHITE);
                tvDinner.setBackgroundResource(R.drawable.textcorner);
                tvDinner.setTextColor(Color.BLACK);
                time = "1";
            }
        });
        tvDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLunch.setBackgroundResource(R.drawable.textcorner);
                tvLunch.setTextColor(Color.BLACK);
                tvDinner.setBackgroundResource(R.drawable.selectbackground);
                tvDinner.setTextColor(Color.WHITE);
                time = "2";
            }
        });
        tvV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvV.setBackgroundResource(R.drawable.vegbackground);
                tvNV.setBackgroundResource(R.drawable.textcorner);
                veg = "1";
            }
        });
        tvNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvV.setBackgroundResource(R.drawable.textcorner);
                tvNV.setBackgroundResource(R.drawable.nonvegbackground);
                veg = "0";
            }
        });
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                etDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(UploadMenu.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etDate.setText(dayOfMonth + "-"+(monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);

                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });
    }
    private void uploadEditMenu(final String mMid, final String mDate, final String mTime, final String mName, final String mDesc, final String mQuan, final String mCost, final String mVeg) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDITMENU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                Intent inten = new Intent(UploadMenu.this, UploadMenu.class);
                                startActivity(inten);
                                Toast.makeText(UploadMenu.this,"UPLOAD SUCCESSFUL",Toast.LENGTH_LONG ).show();
                            }
                            else {
                                Toast.makeText(UploadMenu.this,"UPLOAD FAILED",Toast.LENGTH_LONG ).show();
                            }
                        }
                        catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadMenu.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("mid", mMid);
                map.put("sid", id);
                map.put("mname",mName);
                map.put("items",mDesc);
                map.put("cost",mCost);
                map.put("time",mTime);
                map.put("date",mDate);
                map.put("veg",mVeg);
                map.put("quan",mQuan);
                map.put("aid",aid);
                Log.w(TAG, "getParams: " + mMid + ":"+ id + ":"+ mName + ":"+ mDesc + ":"+ mCost + ":"+ mTime + ":"+ mDate + ":"+ mVeg + ":"+ mQuan + ":"+ aid );
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void uploadMenu() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOADMENU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                Toast.makeText(UploadMenu.this,"Upload SUCCESSFUL",Toast.LENGTH_LONG ).show();
                                Intent inten = new Intent(UploadMenu.this, UploadMenu.class);
                                startActivity(inten);                            }
                            else {
                                Toast.makeText(UploadMenu.this,"Upload FAILED",Toast.LENGTH_LONG ).show();
                            }
                        }
                        catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadMenu.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("sid", id);
                map.put("mname",menu);
                map.put("items",item);
                map.put("cost",cost);
                map.put("time",time);
                map.put("date",date);
                map.put("veg",veg);
                map.put("quan",quan);
                map.put("aid",aid);
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
