package com.example.idan.lungupfinal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.idan.lungupfinal.CageGiverActivities.ExercisesPlanActivity;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.R;

import java.util.ArrayList;

/**
 * Created by Belal on 29/09/16.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Patient> list;
    private Context mCtx;

    public CustomAdapter(ArrayList<Patient> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.ViewHolder holder, final int position) {
        Patient myList = list.get(position);
        holder.textViewHead.setText(myList.getName());
        holder.textViewDesc.setText(myList.getEmail());
        
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                Log.d("check",""+position+"  - "+ list.get(position).getName());
                                Intent i = new Intent(view.getContext(),ExercisesPlanActivity.class);
                                String usrUid = list.get(position).getUid();
                                i.putExtra("USER_UID", usrUid);
                                mCtx.startActivity(i);

                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView buttonViewOption;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }
}
