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

public class CookAdapter extends RecyclerView.Adapter<CookAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> myList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMName, tvIName, tvTime, tvDate, tvCost, tvSold;

        public ViewHolder(View rowView) {
            super(rowView);
            tvMName = (TextView) rowView.findViewById(R.id.tvMName);
            tvIName= (TextView) rowView.findViewById(R.id.tvIName);
            tvTime= (TextView) rowView.findViewById(R.id.tvTime);
            tvDate= (TextView) rowView.findViewById(R.id.tvDate);
            tvCost= (TextView) rowView.findViewById(R.id.tvCost);
            tvSold = (TextView)rowView.findViewById(R.id.tvSold);
        }
    }

    public CookAdapter(ArrayList<HashMap<String, String>> myDataset) {
        myList = myDataset;
    }

    @Override
    public CookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cook_list, parent, false);
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
        final String sold = myList.get(position).get("sold");
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
        holder.tvSold.setText("TO COOK : " + sold);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }
}