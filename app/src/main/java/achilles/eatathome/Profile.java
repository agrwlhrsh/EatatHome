package achilles.eatathome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

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
    String aid = "";
    TextView tvName, tvPhone, tvEdit, item1, item2, item3, item4, item5, item6, item7, item8;
    RelativeLayout ic1, ic2, ic3, ic4;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        ic2 = (RelativeLayout)findViewById(R.id.ic2);

        ic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("SUPP")){
                Intent inten = new Intent(Profile.this, UploadMenu.class);
                startActivity(inten);
            }else{
                Intent inten = new Intent(Profile.this, BrowseMenu.class);
                startActivity(inten);
            }
            }
        });

        ic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("SUPP")){
                    Intent inten = new Intent(Profile.this, Cook.class);
                    startActivity(inten);
                }else{
                    Intent inten = new Intent(Profile.this, Cart.class);
                    startActivity(inten);
                }
            }
        });


        tvName = (TextView)findViewById(R.id.tvName);
        tvPhone = (TextView)findViewById(R.id.tvPhone);
        tvEdit = (TextView)findViewById(R.id.tvEdit);

        item1 = (TextView)findViewById(R.id.item1);
        item2= (TextView)findViewById(R.id.item2);
        item3  = (TextView)findViewById(R.id.item3);
        item4 = (TextView)findViewById(R.id.item4);
        item5 = (TextView)findViewById(R.id.item5);
        item6 = (TextView)findViewById(R.id.item6);
        item7 = (TextView)findViewById(R.id.item7);
        item8 = (TextView)findViewById(R.id.item8);


        tvName.setText(name);
        tvPhone.setText(phone);
        item8.setText("\u20B9 " + balance);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(Profile.this, EditProfile.class);
                startActivity(inten);

            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(Profile.this, Wallet.class);
                startActivity(inten);

            }
        });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(Profile.this, Orders.class);
                startActivity(inten);

            }
        });
        item7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(Profile.this, Address.class);
                startActivity(inten);

            }
        });
        item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(type.equals("SUPP")){
            Intent inten = new Intent(Profile.this, UploadMenu.class);
            startActivity(inten);
        }else{
            Intent inten = new Intent(Profile.this, BrowseMenu.class);
            startActivity(inten);
        }
    }
}
