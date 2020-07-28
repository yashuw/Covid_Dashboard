package com.example.covid_dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemberAdp extends RecyclerView.Adapter<MemberAdp.ViewHolder> {

    //Initialize Arraylist
    ArrayList<TwoValueStorageForMemberAdp >arrayListMember;


    public MemberAdp(ArrayList<TwoValueStorageForMemberAdp> arrayListMember) {

        this.arrayListMember=arrayListMember;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Initialize view
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_member,parent,false);

        return new MemberAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TwoValueStorageForMemberAdp Data=arrayListMember.get(position);
        holder.tvName1.setText(Data.getFirst());
        holder.tvName2.setText(Data.getSecond());

    }

    @Override
    public int getItemCount() {
        return arrayListMember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName1;
        TextView tvName2;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName1=itemView.findViewById(R.id.tv_name1);
            tvName2=itemView.findViewById(R.id.tv_name2);
        }
    }
}
