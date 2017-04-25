package achilles.eatathome;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> myList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView label1;

        public ViewHolder(View rowView) {
            super(rowView);
            label1 = (TextView) rowView.findViewById(R.id.label1);
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
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.label1.setText("\u20B9 " + myList.get(position).get("amount"));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }

}