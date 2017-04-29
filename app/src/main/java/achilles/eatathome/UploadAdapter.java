package achilles.eatathome;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> myList = new ArrayList<>();
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMName, tvIName, tvTime, tvDate, tvCost, tvDelete, tvUpload;

        public ViewHolder(View rowView) {
            super(rowView);
            tvMName = (TextView) rowView.findViewById(R.id.tvMName);
             tvIName= (TextView) rowView.findViewById(R.id.tvIName);
             tvTime= (TextView) rowView.findViewById(R.id.tvTime);
             tvDate= (TextView) rowView.findViewById(R.id.tvDate);
             tvCost= (TextView) rowView.findViewById(R.id.tvCost);
             tvDelete= (TextView) rowView.findViewById(R.id.tvDelete);
             tvUpload= (TextView) rowView.findViewById(R.id.tvUpload);
        }
    }

    public UploadAdapter(ArrayList<HashMap<String, String>> myDataset) {
        myList = myDataset;
    }

    @Override
    public UploadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String mname = myList.get(position).get("mname");
        final String items = myList.get(position).get("items");
        final String time = myList.get(position).get("time");
        final String date = myList.get(position).get("date");
        final String cost = myList.get(position).get("cost");
        final String veg = myList.get(position).get("veg");
        final String quan = myList.get(position).get("quan");

        holder.tvMName.setText(mname);
        holder.tvIName.setText(items);
        holder.tvTime.setText(time);
        holder.tvDate.setText(date);
        holder.tvCost.setText(cost);

        holder.tvUpload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
//                openDialog(mname, items, time, date, cost, veg, quan);
            }
        });
    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void openDialog(String mname, String items, String time, String date, String cost, String veg, String quan){
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View subView = inflater.inflate(R.layout.menu_form, null);
//        final TextView  etDate = (TextView) subView.findViewById(R.id.etDate);
//        final EditText etMenu = (EditText)subView.findViewById(R.id.etMenu);
//        final EditText  etItem = (EditText)subView.findViewById(R.id.etItem);
//        final EditText  etQuan = (EditText)subView.findViewById(R.id.etQuan);
//        final EditText  etCost = (EditText)subView.findViewById(R.id.etCost);
//        final TextView tvV = (TextView)subView.findViewById(R.id.tvV);
//        final TextView tvNV = (TextView)subView.findViewById(R.id.tvNV);
//        final TextView tvLunch = (TextView)subView.findViewById(R.id.tvLunch);
//        final TextView tvDinner = (TextView)subView.findViewById(R.id.tvDinner);
//        final TextView tvUpload = (TextView)subView.findViewById(R.id.tvUpload);
//        final TextView tvCancel = (TextView)subView.findViewById(R.id.tvCancel);
//
//        etItem.setText(items);
//        etMenu.setText(mname);
//        etQuan.setText(quan);
//        etCost.setText(cost);
//        etDate.setText(date);
//        if(veg.equals("1")){
//
//        }
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setView(subView);
//        final AlertDialog alertDialog = builder.create();
//        builder.show();
//
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//
//        tvUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mname = etMenu.getText().toString().trim();
//                date = etDate.getText().toString().trim();
//                item = etItem.getText().toString().trim();
//                cost = etCost.getText().toString().trim();
//                quan = etQuan.getText().toString().trim();
//                if(menu.length()<1 || item.length()<1 || cost.length()<1 || quan.length()<1 || veg.equals("-1") || time.equals("0")){
//                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
//                    alert.setMessage("Fields Cannot be Empty. Please fill in all details of Menu...");
//                    alert.setPositiveButton("OK",null);
//                    alert.show();
//                }else{
//                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
//                    alert.setMessage("Are you sure you want to Continue??");
//                    alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            alertDialog.dismiss();
//                            uploadMenu();
//                        }
//                    });
//                    alert.setNegativeButton("No", null);
//                    alert.show();
//                }
//            }
//        });
//        tvLunch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvLunch.setBackgroundResource(R.drawable.selectbackground);
//                tvLunch.setTextColor(Color.WHITE);
//                tvDinner.setBackgroundResource(R.drawable.textcorner);
//                tvDinner.setTextColor(Color.BLACK);
//                time = "1";
//            }
//        });
//        tvDinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvLunch.setBackgroundResource(R.drawable.textcorner);
//                tvLunch.setTextColor(Color.BLACK);
//                tvDinner.setBackgroundResource(R.drawable.selectbackground);
//                tvDinner.setTextColor(Color.WHITE);
//                time = "2";
//            }
//        });
//        tvV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvV.setBackgroundResource(R.drawable.vegbackground);
//                tvNV.setBackgroundResource(R.drawable.textcorner);
//                veg = "0";
//            }
//        });
//        tvNV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvV.setBackgroundResource(R.drawable.textcorner);
//                tvNV.setBackgroundResource(R.drawable.nonvegbackground);
//                veg = "1";
//            }
//        });
//        final Calendar myCalendar = Calendar.getInstance();
//
//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                // myCalendar.add(Calendar.DATE, 0);
//                String myFormat = "dd-MM-yyyy"; //In which you need put here
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
//                etDate.setText(sdf.format(myCalendar.getTime()));
//            }
//        };
//
//        etDate.setOnClickListener(new View.OnClickListener() {
//
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dpd = new DatePickerDialog(context,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                etDate.setText(dayOfMonth + "-"+(monthOfYear + 1) + "-" + year);
//                            }
//                        }, mYear, mMonth, mDay);
//
//                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                dpd.show();
//            }
//        });
//    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }

}