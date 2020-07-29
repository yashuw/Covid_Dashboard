package com.example.covid_dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemberAdpTestCenter extends RecyclerView.Adapter<MemberAdpTestCenter.ViewHolder> {

    //Initialize Arraylist
    ArrayList<String >arrayListMember;


    public MemberAdpTestCenter(ArrayList<String> arrayListMember) {

        this.arrayListMember=arrayListMember;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Initialize view
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_members,parent,false);

        return new MemberAdpTestCenter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvName.setText(arrayListMember.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayListMember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tv_name);



        }
    }
}
