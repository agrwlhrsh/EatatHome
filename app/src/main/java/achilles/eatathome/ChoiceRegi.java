package achilles.eatathome;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ChoiceRegi extends AppCompatActivity {

    CheckBox cbHungry;
    CheckBox cbServe;
    TextView tvContinue;

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_regi);

        cbHungry = (CheckBox)findViewById(R.id.cbHungry);
        cbServe = (CheckBox)findViewById(R.id.cbServe);
        tvContinue = (TextView)findViewById(R.id.tvContinue);

        cbHungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbServe.isChecked()){
                    cbServe.setChecked(false);
                }
            }
        });

        cbServe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbHungry.isChecked()){
                    cbHungry.setChecked(false);
                }
            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbHungry.isChecked()){
                    Intent inten = new Intent(ChoiceRegi.this, Signup.class);
                    inten.putExtra("type","CUST");
                    startActivity(inten);
                }else if(cbServe.isChecked()){
                    Intent inten = new Intent(ChoiceRegi.this, Signup.class);
                    inten.putExtra("type","SUPP");
                    startActivity(inten);
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(ChoiceRegi.this);
                    alert.setMessage("Please Select any 1 to Continue...");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            }
        });
    }
}