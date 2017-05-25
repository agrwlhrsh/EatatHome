package achilles.eatathome;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> myList = new ArrayList<>();

    static Callback callback;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMName, tvIName, tvTime, tvDate, tvCost, tvDelete, tvUpload;

        public ViewHolder(View rowView) {
            super(rowView);
            tvMName = (TextView) rowView.findViewById(R.id.tvMName);
            tvIName= (TextView) rowView.findViewById(R.id.tvIName);
            tvTime= (TextView) rowView.findViewById(R.id.tvTime);
            tvDate= (TextView) rowView.findViewById(R.id.tvDate);
            tvCost= (TextView) rowView.findViewById(R.id.tvCost);
            //tvDelete= (TextView) rowView.findViewById(R.id.tvDelete);
//            tvUpload= (TextView) rowView.findViewById(R.id.tvUpload);
        }
    }

    public UploadAdapter(ArrayList<HashMap<String, String>> myDataset, Callback callback) {
        this.callback = callback;
        myList = myDataset;
    }

    @Override
    public UploadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String mid = myList.get(position).get("mid");
        final String mname = myList.get(position).get("mname");
        final String items = myList.get(position).get("items");
        final int time = Integer.parseInt(myList.get(position).get("time"));
        final String date = myList.get(position).get("date");
        final String cost = myList.get(position).get("cost");
        final String veg = myList.get(position).get("veg");
        final String quan = myList.get(position).get("quan");
        boolean flag = false;
        Log.w(TAG, "onBindViewHolder: " +mid+":"+ mname + items + time + date + cost);

        holder.tvMName.setText(mname);
        holder.tvIName.setText(items);
        if(time == 1){
            holder.tvTime.setText("LUNCH");
        }else{
            holder.tvTime.setText("DINNER");
        }
        holder.tvDate.setText(date);
        holder.tvCost.setText("\u20B9 " + cost);
//        final Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        StringTokenizer st = new StringTokenizer(date,"-");
//        int dd = Integer.parseInt(st.nextToken().toString());
//        int mm = Integer.parseInt(st.nextToken().toString());
//        int yy = Integer.parseInt(st.nextToken().toString());
//        Log.w(TAG, "onBindViewHolder: " + dd + ":" + mDay + "|"+mm + ":" + mMonth +"|"+yy + ":" + mYear);
//        if(mYear > yy){
//            flag = true;
//        }else{
//            if(mMonth>mm){
//                flag = true;
//            }else{
//                if(mDay>dd){
//                    flag = true;
//                }
//            }
//        }
//        if(flag) {
//            holder.tvUpload.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    HashMap<String, String> cart = new HashMap<String, String>();
//                    cart.put("mid", mid);
//                    cart.put("mname", mname);
//                    cart.put("items", items);
//                    cart.put("date", date);
//                    cart.put("cost", cost);
//                    cart.put("veg", veg);
//                    cart.put("time", time + "");
//                    cart.put("quan", quan);
//                    callback.onUploadClick(cart);
//
//                }
//            });
//        }else{
//            holder.tvUpload.setTextColor(Color.parseColor("#EEEEEE"));
//        }
    }

    public static void setCallback(UploadAdapter.Callback callback1){
        callback = callback1;
    }

    public interface Callback {
        void onUploadClick(HashMap<String,String> cart);
//        void onDeleteClick(String mid);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }
}