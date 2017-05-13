//package com.example.idan.lungupfinal.Adapters;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.idan.lungupfinal.P_Exercise;
//import com.example.idan.lungupfinal.R;
//
//import java.util.ArrayList;
//
//public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {
//    private ArrayList<P_Exercise> values;
//    int selectedPosition=-1;
//    private Context mCtx;
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        // each data item is just a string in this case
//        public TextView txtHeader;
//        public TextView txtFooter;
//        public View layout;
//
//        public ViewHolder(View v) {
//            super(v);
//            layout = v;
//            txtHeader = (TextView) v.findViewById(R.id.firstLine);
//            txtFooter = (TextView) v.findViewById(R.id.secondLine);
//        }
//    }
//
//    public void add(int position, P_Exercise item) {
//        values.add(position, item);
//        notifyItemInserted(position);
//    }
//
//    public void remove(int position) {
//        values.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    //public ExercisesAdapter(ArrayList<String> myDataset) {
//    //	values = myDataset;
//    //}
//    public ExercisesAdapter(ArrayList<P_Exercise> exercisesList, Context mCtx){
//        values = exercisesList;
//        this.mCtx = mCtx;
//
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent,
//                                         int viewType) {
//        // create a new view
//        LayoutInflater inflater = LayoutInflater.from(
//                parent.getContext());
//        View v =
//                inflater.inflate(R.layout.ex_row_layout, parent, false);
//        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//        selectedPosition = holder.getAdapterPosition();
//        final String name = values.get(position).getExercise_name();
//        holder.txtHeader.setText(name);
//
//        holder.txtFooter.setText("" + values.get(position).getSchedule());
//
//        Log.d("clickcheck","selected position2:" + position);
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return values.size();
//    }
//
//}