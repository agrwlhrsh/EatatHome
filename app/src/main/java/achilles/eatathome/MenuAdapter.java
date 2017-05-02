package achilles.eatathome;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    ArrayList<HashMap<String, String>> myList=new ArrayList<>();

//    ArrayList<HashMap<String, String>> cartList = new ArrayList<>() ;
//    int totQuantity = 0;
//    float totCost = 0;
//    Context context;
    static Callback callback;
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCName, tvMName, tvIName, tvTime, tvDate, tvCost, tvBuy;
        ImageView ivUp, ivDown, ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;

        public ViewHolder(View rowView) {
            super(rowView);
            tvCName = (TextView) rowView.findViewById(R.id.tvCName);
            tvMName = (TextView) rowView.findViewById(R.id.tvMName);
            tvTime = (TextView) rowView.findViewById(R.id.tvTime);
            tvIName = (TextView) rowView.findViewById(R.id.tvIName);
            tvDate = (TextView) rowView.findViewById(R.id.tvDate);
            tvCost = (TextView) rowView.findViewById(R.id.tvCost);
            tvBuy = (TextView) rowView.findViewById(R.id.tvBuy);
            ivUp = (ImageView) rowView.findViewById(R.id.ivUp);
            ivDown = (ImageView) rowView.findViewById(R.id.ivDown);
            ivStar1 = (ImageView) rowView.findViewById(R.id.ivStar1);
            ivStar2 = (ImageView) rowView.findViewById(R.id.ivStar2);
            ivStar3 = (ImageView) rowView.findViewById(R.id.ivStar3);
            ivStar4 = (ImageView) rowView.findViewById(R.id.ivStar4);
            ivStar5 = (ImageView) rowView.findViewById(R.id.ivStar5);
        }

    }

    public MenuAdapter(ArrayList<HashMap<String, String>> myDataset, Callback callback) {
        super();
        myList = myDataset;
        this.callback = callback;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list, parent, false);
        MenuAdapter.ViewHolder vh = new MenuAdapter.ViewHolder(v);
//        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, final int position) {
        final String cookName = myList.get(position).get("name");
        final String menuName = myList.get(position).get("mname");
        final String itemsName = myList.get(position).get("items");
        final String date = myList.get(position).get("date");
        final String time = myList.get(position).get("time");
        final String mid = myList.get(position).get("mid");
        int quan = Integer.parseInt(myList.get(position).get("quan"));
        int sold = Integer.parseInt(myList.get(position).get("sold"));
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
                    callback.onImageClick(left, cart, select[0]);

                } else {
                    select[0] = 0;
                    callback.onImageClick(left, null, select[0]);
                }
            }
        });

//
//        final int[] quantity = {0};
//        holder.tvQuan.setText(quantity[0] + "");
//        holder.ivDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(quantity[0] > 0){
//                    totCost = totCost - cost;
//                    quantity[0] = quantity[0] - 1;
//                    totQuantity = totQuantity - 1;
//                    int pos = search_key(cartList, mid);
//                    cartList.remove(pos);
//                }
//                callback.onImageClick(totQuantity, totCost, cartList);
//                holder.tvQuan.setText(quantity[0] +"");
//            }
//       });
//        holder.ivUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(quantity[0] < left ){
//                    quantity[0] = quantity[0] + 1;
//                    totCost = totCost + cost;
//                    totQuantity = totQuantity + 1;
//                    if(quantity[0] == 1){
//                        HashMap<String, String> cart = new HashMap<String, String>();
//                        cart.put("mname", menuName);
//                        cart.put("cost", cost+ "");
//                        cart.put("date",date);
//                        cart.put("time",time);
//                        cart.put("mid",mid);
//                        cart.put("quan",quantity[0]+"");
//                        cartList.add(cart);
//                    }else{
//                        int pos = search_key(cartList, mid);
//                        cartList.get(pos).put("quan", quantity[0] + "");
//                    }
//
//                }
//                callback.onImageClick(totQuantity, totCost, cartList);
//                holder.tvQuan.setText(quantity[0] +"");
//            }
//        });

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
