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

    ArrayList<HashMap<String, String>>myList=new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCName, tvMName, tvIName, tvTime, tvDate, tvCost, tvQuan;
        ImageView ivUp, ivDown, ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;

        public ViewHolder(View rowView) {
            super(rowView);
            tvCName = (TextView) rowView.findViewById(R.id.tvCName);
            tvMName = (TextView) rowView.findViewById(R.id.tvMName);
            tvTime = (TextView) rowView.findViewById(R.id.tvTime);
            tvIName = (TextView) rowView.findViewById(R.id.tvIName);
            tvDate = (TextView) rowView.findViewById(R.id.tvDate);
            tvCost = (TextView) rowView.findViewById(R.id.tvCost);
            tvQuan = (TextView) rowView.findViewById(R.id.tvQuan);
            ivUp = (ImageView) rowView.findViewById(R.id.ivUp);
            ivDown = (ImageView) rowView.findViewById(R.id.ivDown);
            ivStar1 = (ImageView) rowView.findViewById(R.id.ivStar1);
            ivStar2 = (ImageView) rowView.findViewById(R.id.ivStar2);
            ivStar3 = (ImageView) rowView.findViewById(R.id.ivStar3);
            ivStar4 = (ImageView) rowView.findViewById(R.id.ivStar4);
            ivStar5 = (ImageView) rowView.findViewById(R.id.ivStar5);
        }

    }

    public MenuAdapter(ArrayList<HashMap<String, String>> myDataset) {
        myList = myDataset;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list, parent, false);
        MenuAdapter.ViewHolder vh = new MenuAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, int position) {

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }
}
