package achilles.eatathome;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.HashMap;

public class Payment extends AppCompatActivity {

    private HashMap<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        init();

        Intent intent = new Intent(this, MakePaymentActivity.class);
        intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_DEV);
        intent.putExtra(PayUMoney_Constants.PARAMS, params);

        startActivityForResult(intent, PayUMoney_Constants.PAYMENT_REQUEST);
    }

    private synchronized void init() {

        params.put(PayUMoney_Constants.KEY, "rjQUPktU");
        params.put(PayUMoney_Constants.TXN_ID, "TXN1234");
        params.put(PayUMoney_Constants.AMOUNT, "228");
        params.put(PayUMoney_Constants.PRODUCT_INFO, "Food");
        params.put(PayUMoney_Constants.FIRST_NAME, "Harsh");
        params.put(PayUMoney_Constants.EMAIL, "agarwal.harshnu@gmail.com");
        params.put(PayUMoney_Constants.PHONE, "7004123636");
        params.put(PayUMoney_Constants.SURL, "https://www.payumoney.com/mobileapp/payumoney/success.php");
        params.put(PayUMoney_Constants.FURL, "https://www.payumoney.com/mobileapp/payumoney/failure.php");
        params.put(PayUMoney_Constants.UDF1, "");
        params.put(PayUMoney_Constants.UDF2, "");
        params.put(PayUMoney_Constants.UDF3, "");
        params.put(PayUMoney_Constants.UDF4, "");
        params.put(PayUMoney_Constants.UDF5, "");

        String hash = Utils.generateHash(params, "e5iIg1jwi8");

        params.put(PayUMoney_Constants.HASH, hash);
        params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayUMoney_Constants.PAYMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Payment Success,", Toast.LENGTH_SHORT).show();
//                String mm = data.getStringExtra("result");
//                Intent intent = new Intent(this,Resul.class);
//                intent.putExtra("result","success"+mm);
//                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment Failed | Cancelled.", Toast.LENGTH_SHORT).show();
//                String mm = data.getStringExtra("result");
//                Intent intent = new Intent(this,Resul.class);
//                intent.putExtra("result","fail: "+mm);
//                startActivity(intent);
            }
        }
    }
}