package achilles.eatathome;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    ArrayList<HashMap<String,String>> myList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView label,label2, label3, label4, label5, label6;

        public ViewHolder(View rowView) {
            super(rowView);
            label=(TextView) rowView.findViewById(R.id.label);
            label2=(TextView) rowView.findViewById(R.id.label2);
            label3=(TextView) rowView.findViewById(R.id.label3);
            label4=(TextView) rowView.findViewById(R.id.label4);
            label5=(TextView) rowView.findViewById(R.id.label5);
            label6=(TextView) rowView.findViewById(R.id.label6);
        }
    }

    public CustomAdapter(ArrayList<HashMap<String,String>> myDataset) {
        myList = myDataset;
    }
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label2.setText("\u20B9 "+myList.get(position).get("amount"));
        holder.label3.setText("Transaction # " + myList.get(position).get("tid"));
        int ss = Integer.parseInt(myList.get(position).get("tstatus"));
        if(ss == 0){
            holder.label4.setText("FAILED");
            holder.label4.setTextColor(Color.parseColor("#E74A29"));
        }
        holder.label5.setText("Order # " +myList.get(position).get("oid"));
        holder.label6.setText("Date " + myList.get(position).get("date"));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }

}






















/*
class CustomAdapter extends BaseAdapter {

    private static final String TAG = "CA";
    ArrayList<HashMap<String,String>> myList = new ArrayList<>();
    Context context;

    private static LayoutInflater inflater=null;

    public CustomAdapter(MainActivity mainActivity, ArrayList<HashMap<String,String>> transList) {
        // TODO Auto-generated constructor stub
        myList = transList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        Log.w(TAG, "getCount: " + myList.size());
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView label,label2, label3, label4, label5, label6;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);
        label=(TextView) rowView.findViewById(R.id.label);
        label2=(TextView) rowView.findViewById(R.id.label2);
        label3=(TextView) rowView.findViewById(R.id.label3);
        label4=(TextView) rowView.findViewById(R.id.label4);
        label5=(TextView) rowView.findViewById(R.id.label5);
        label6=(TextView) rowView.findViewById(R.id.label6);
        label2.setText("\u20B9 "+myList.get(position).get("amount"));
        label3.setText(myList.get(position).get("tid"));
        label4.setText(myList.get(position).get("tstatus"));
        label5.setText(myList.get(position).get("oid"));
        label6.setText(myList.get(position).get("date"));
        Log.w(TAG, "getView: working fast getview" );
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        return rowView;
    }
}*/
