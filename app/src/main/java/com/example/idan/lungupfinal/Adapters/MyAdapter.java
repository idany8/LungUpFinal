package com.example.idan.lungupfinal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.idan.lungupfinal.PatientSumActivity;
import com.example.idan.lungupfinal.R;
import com.example.idan.lungupfinal.User;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
	private ArrayList<User> values;
	int selectedPosition=-1;
	private Context mCtx;

	public class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView txtHeader;
		public TextView txtFooter;

		public View layout;

		public ViewHolder(View v) {
			super(v);
			layout = v;
			txtHeader = (TextView) v.findViewById(R.id.firstLine);
			txtFooter = (TextView) v.findViewById(R.id.secondLine);

		}
	}

	public void add(int position, User item) {
		values.add(position, item);
		notifyItemInserted(position);
	}

	public void remove(int position) {
		values.remove(position);
		notifyItemRemoved(position);
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	//public MyAdapter(ArrayList<String> myDataset) {
	//	values = myDataset;
	//}
	public MyAdapter(ArrayList<User> usersList, Context mCtx){
        values = usersList;
		this.mCtx = mCtx;

    }

	// Create new views (invoked by the layout manager)
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		// create a new view
		LayoutInflater inflater = LayoutInflater.from(
				parent.getContext());
		View v = 
				inflater.inflate(R.layout.rowlayout, parent, false);
		// set the view's size, margins, paddings and layout parameters
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
		selectedPosition = holder.getAdapterPosition();
		final String name = values.get(position).getName();
		holder.txtHeader.setText(name);
		holder.txtHeader.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	//remove(position);
					Intent i = new Intent(mCtx, PatientSumActivity.class);
					String strUid = values.get(position).getUid();
					i.putExtra("PATIENT_UID", strUid);
					mCtx.startActivity(i);

                    Log.d("clickcheck","email: " + values.get(position).getEmail()+ "..name: "+ values.get(position).getName()+"  id:"+ values.get(position).getUid());
					Log.d("clickcheck","selected position:" + position);

		        }
		    });
		
		holder.txtFooter.setText("" + values.get(position).getEmail());

		Log.d("clickcheck","selected position2:" + position);
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return values.size();
	}

}