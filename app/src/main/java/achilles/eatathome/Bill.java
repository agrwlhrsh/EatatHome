package achilles.eatathome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Bill extends AppCompatActivity {

    TextView tvStatus;
    TextView tvBack;

    String tid = "";
    String status = "";
    String amount = "";
    String oid;
    String did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        final Intent inten = getIntent();
        tid = inten.getStringExtra("tid");
        status = inten.getStringExtra("payment");
        amount = inten.getStringExtra("amount");
        oid = inten.getStringExtra("oid");
        did = inten.getStringExtra("did");

        tvStatus = (TextView)findViewById(R.id.tvStatus);
        tvBack = (TextView)findViewById(R.id.tvBack);
        if(status.equalsIgnoreCase("COD")){
            tvStatus.setText("Cash On Delivery" + "\n Pay Rs. " + amount + " in cash to Delivery Person\nOrder Number #" + oid);
        }else{
            tvStatus.setText(status + "\n" + amount + "\n" + tid);
        }

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten  = new Intent(Bill.this, BrowseMenu.class);
                startActivity(inten);
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
