package achilles.eatathome;


import android.os.Build;
import android.support.annotation.RequiresApi;
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
            tvDelete= (TextView) rowView.findViewById(R.id.tvDelete);
            tvUpload= (TextView) rowView.findViewById(R.id.tvUpload);
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

    // Replace the contents of a view (invoked by the layout manager)
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

        Log.w(TAG, "onBindViewHolder: " + mname + items + time + date + cost);

        holder.tvMName.setText(mname);
        holder.tvIName.setText(items);
        if(time == 1){
            holder.tvTime.setText("LUNCH");
        }else{
            holder.tvTime.setText("DINNER");
        }
        holder.tvDate.setText(date);
        holder.tvCost.setText("\u20B9 " + cost);

        holder.tvUpload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HashMap<String, String> cart = new HashMap<String, String>();
                cart.put("mid",mid);
                cart.put("mid",mname);
                cart.put("items",items);
                cart.put("date",date);
                cart.put("cost", cost);
                cart.put("veg",veg);
                cart.put("time",time+"");
                cart.put("quan",quan);

                callback.onUploadClick(cart);

            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onDeleteClick(mid);
            }
        });
    }

    public static void setCallback(UploadAdapter.Callback callback1){
        callback = callback1;
    }

    public interface Callback {
        void onUploadClick(HashMap<String,String> cart);
        void onDeleteClick(String mid);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }
}