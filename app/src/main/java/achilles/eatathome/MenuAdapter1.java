package achilles.eatathome;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class MenuAdapter1 extends RecyclerView.Adapter<MenuAdapter1.ViewHolder>{

    public ArrayList<HashMap<String, String>> myList=new ArrayList<>();

    static Callback callback;
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCName, tvMName, tvIName, tvTime, tvDate, tvCost, tvBuy;
        ImageView ivUp, ivDown, ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;

        public ViewHolder(View rowView) {
            super(rowView);
            Log.w(TAG, "ViewHolder: asdfgh" );
            tvCName = (TextView) rowView.findViewById(R.id.tvCName);
            tvMName = (TextView) rowView.findViewById(R.id.tvMName);
            tvTime = (TextView) rowView.findViewById(R.id.tvTime);
            tvIName = (TextView) rowView.findViewById(R.id.tvIName);
            tvDate = (TextView) rowView.findViewById(R.id.tvDate);
            tvCost = (TextView) rowView.findViewById(R.id.tvCost);
            tvBuy = (TextView) rowView.findViewById(R.id.tvBuy);
            ivUp = (ImageView) rowView.findViewById(R.id.ivUp);
            ivDown = (ImageView) rowView.findViewById(R.id.ivDown);
//            ivStar1 = (ImageView) rowView.findViewById(R.id.ivStar1);
//            ivStar2 = (ImageView) rowView.findViewById(R.id.ivStar2);
//            ivStar3 = (ImageView) rowView.findViewById(R.id.ivStar3);
//            ivStar4 = (ImageView) rowView.findViewById(R.id.ivStar4);
//            ivStar5 = (ImageView) rowView.findViewById(R.id.ivStar5);
        }
    }

    public MenuAdapter1(ArrayList<HashMap<String, String>> myDataset, Callback callback) {
        super();
        Log.w(TAG, "MenuAdapter1: called" );
        this.callback = callback;
        myList = myDataset;
        Log.w(TAG, "MenuAdapter1: after mylist" + myList.toString() );
    }

    @Override
    public MenuAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        Log.w(TAG, "onCreateViewHolder: kuch kuch horaha" );
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list, parent, false);
        MenuAdapter1.ViewHolder vh = new MenuAdapter1.ViewHolder(v);
        return vh;
    }

//    public View getView(ViewGroup parent){
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list, parent, false);
//        MenuAdapter1.ViewHolder vh = new MenuAdapter1.ViewHolder(v);
//        return v;
//    }

    @Override
    public void onBindViewHolder(final MenuAdapter1.ViewHolder holder, final int position) {
        Log.w(TAG, "onBindViewHolder: happpeningggg" );
        final String sid = myList.get(position).get("sid");
        final String s_balance = myList.get(position).get("s_balance");
        final String sup_address = myList.get(position).get("sup_address");
        final String cookName = myList.get(position).get("name");
        final String menuName = myList.get(position).get("mname");
        final String itemsName = myList.get(position).get("items");
        final String date = myList.get(position).get("date");
        final String time = myList.get(position).get("time");
        final String mid = myList.get(position).get("mid");
        int quan = Integer.parseInt(myList.get(position).get("quan"));
        final int sold = Integer.parseInt(myList.get(position).get("sold"));
        final float cost = Float.parseFloat(myList.get(position).get("cost"));
        final int left = quan - sold;

        holder.tvCName.setText(cookName);
        holder.tvMName.setText(menuName);
        holder.tvCost.setText("\u20B9 " + cost);
        holder.tvIName.setText(itemsName);
        holder.tvDate.setText(date);
        if (time.equalsIgnoreCase("1")) {
            holder.tvTime.setText("LUNCH");
        } else {
            holder.tvTime.setText("DINNER");
        }
        final int[] select = {0};
        holder.tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((select[0] == 0)) {
                    select[0] = 1;
                    HashMap<String, String> cart = new HashMap<String, String>();
                    cart.put("mname", menuName);
                    cart.put("cost", cost + "");
                    cart.put("date", date);
                    cart.put("time", time);
                    cart.put("mid", mid);
                    cart.put("left", left + "");
                    cart.put("sold", sold+"");
                    cart.put("s_balance", s_balance);
                    cart.put("sid",sid);
                    cart.put("sup_address",sup_address);
                    callback.onImageClick(left, cart, select[0]);
                } else {
                    select[0] = 0;
                    callback.onImageClick(left, null, select[0]);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static void setCallback(Callback callback1){
        callback = callback1;
    }

    public interface Callback {
        void onImageClick(int left, HashMap<String,String> cart, int select);
    }
    public static int search_key(ArrayList<HashMap<String,String>> listing, String search_key) {
        for (int temp = 0; temp < listing.size(); temp++) {
            String id = listing.get(temp).get("mid");
            System.out.println(id+" : "+search_key);
            if (id != null && id.equals(search_key)) {
                return temp;
            }
        }
        return -1;
    }
}
