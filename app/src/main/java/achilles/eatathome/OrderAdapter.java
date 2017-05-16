package achilles.eatathome;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> myList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView label1, label2, label3, label5, label6, tvOD, tvComplaints, tvQ, tvDC, tvGT, tvIT, tvDT, tvTransNo, tvOrderNo;
        RelativeLayout rlExtra;

        public ViewHolder(View rowView) {
            super(rowView);
            label1 = (TextView) rowView.findViewById(R.id.label1);
            label2 = (TextView) rowView.findViewById(R.id.label2);
            label3 = (TextView) rowView.findViewById(R.id.label3);
            label5 = (TextView) rowView.findViewById(R.id.label5);
            label6 = (TextView) rowView.findViewById(R.id.label6);
            tvOD = (TextView) rowView.findViewById(R.id.tvOD);
//            tvComplaints = (TextView) rowView.findViewById(R.id.tvComplaints);
            tvDC = (TextView) rowView.findViewById(R.id.tvDC);
            tvDT = (TextView) rowView.findViewById(R.id.tvDT);
            tvQ = (TextView) rowView.findViewById(R.id.tvQ);
            tvGT = (TextView) rowView.findViewById(R.id.tvGT);
            tvIT = (TextView) rowView.findViewById(R.id.tvIT);
            tvTransNo = (TextView) rowView.findViewById(R.id.tvTransNo);
            tvOrderNo = (TextView) rowView.findViewById(R.id.tvOrderNo);
            rlExtra = (RelativeLayout) rowView.findViewById(R.id.rlExtra);
            ViewGroup.LayoutParams params = rlExtra.getLayoutParams();
            params.height = 0;
            rlExtra.setLayoutParams(params);
        }
    }

    public OrderAdapter(ArrayList<HashMap<String, String>> myDataset) {
        myList = myDataset;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.label2.setText("\u20B9 " + myList.get(position).get("amount"));
        holder.label3.setText(myList.get(position).get("items"));
        Log.w(TAG, "onBindViewHolder: " +  myList.get(position).get("mname"));
        holder.label1.setText(myList.get(position).get("mname"));
        String date = myList.get(position).get("date");
        if (myList.get(position).get("time").equalsIgnoreCase("1")) {
            holder.label5.setText("Lunch, " + date);
        } else if(myList.get(position).get("time").equalsIgnoreCase("2")){
            holder.label5.setText("Dinner, " + date);
        }
        if(myList.get(position).get("status").equals("0")){
            holder.label6.setText("SCHEDULED");
        }else if(myList.get(position).get("status").equals("1")){
            holder.label6.setText("DELIVERED");
        }else if(myList.get(position).get("status").equals("2")){
            holder.label6.setText("NOT DELIVERED");
            holder.label6.setTextColor(Color.parseColor("#E74A29"));
        }
        holder.tvQ.setText(myList.get(position).get("quantity"));
        holder.tvIT.setText(myList.get(position).get("amount"));
        holder.tvDC.setText(myList.get(position).get("deliv"));
        holder.tvDT.setText(myList.get(position).get("disc"));
        holder.tvGT.setText(myList.get(position).get("total"));
        if(myList.get(position).get("payby").equalsIgnoreCase("1")){
            holder.tvTransNo.setText("Transaction # " +myList.get(position).get("tid"));
        }else if(myList.get(position).get("payby").equalsIgnoreCase("2")){
            holder.tvTransNo.setText("Cash on Delivery");
        }
        holder.tvOrderNo.setText("Order # " +myList.get(position).get("oid"));
        holder.tvOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tvOD.getText().toString().equals("SHOW DETAILS")){
                    ViewGroup.LayoutParams params = holder.rlExtra.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    holder.rlExtra.setLayoutParams(params);
                    holder.tvOD.setText("HIDE DETAILS");

                }else{
                    ViewGroup.LayoutParams params = holder.rlExtra.getLayoutParams();
                    params.height = 0;
                    holder.rlExtra.setLayoutParams(params);
                    holder.tvOD.setText("SHOW DETAILS");
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }

}