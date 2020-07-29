package com.example.covid_dashboard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupAdpDistWise extends RecyclerView.Adapter<GroupAdpDistWise.ViewHolder> implements Filterable {
    Activity activity;
    ArrayList<String>DistNames;
    ArrayList<String>DistNameFull;
    JSONObject State;

    GroupAdpDistWise(Activity activity, ArrayList<String>names, JSONObject state)
    {
        this.activity=activity;
        this.DistNames=names;
        this.State=state;
        this.DistNameFull=new ArrayList<String>(names);
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group,parent,false);

        return new GroupAdpDistWise.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(DistNames.get(position).toUpperCase());

        //Intialize member arraylist
        ArrayList<TwoValueStorageForMemberAdp>arrayListMember=new ArrayList<>();
        try {
            JSONObject District=State.getJSONObject(DistNames.get(position));
            arrayListMember.add(new TwoValueStorageForMemberAdp("Active   :  "+District.getString("active"),
                    "Confirmed :  "+District.getString("confirmed")));
//            arrayListMember.add("Active   :  "+District.getString("active"));
//            arrayListMember.add("Confirmed :  "+District.getString("confirmed"));
            arrayListMember.add(new TwoValueStorageForMemberAdp("Recovered :  "+District.getString("recovered"),
                    "Deceased :  "+District.getString("deceased")));
//            arrayListMember.add("Recovered :  "+District.getString("recovered"));
//            arrayListMember.add("Deceased :  "+District.getString("deceased"));

            //Initialize member adapter
            MemberAdp adapterMember =new MemberAdp(arrayListMember);

            //Initialize layout manager
            LinearLayoutManager layoutManagerMember=new LinearLayoutManager(activity);

            //set Layout Manager
            holder.rvMember.setLayoutManager(layoutManagerMember);

            //set Adapter
            holder.rvMember.setAdapter(adapterMember);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return DistNames.size();
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String>filteredList=new ArrayList<>();
            if(constraint==null || constraint.length()==0)
            {
                filteredList.addAll(DistNameFull);
            }
            else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for(String item : DistNameFull)
                {
                    if(item.toLowerCase().contains(filterpattern))
                    {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            DistNames.clear();
            DistNames.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Initialize variables
        TextView tvName;
        RecyclerView rvMember;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            rvMember=itemView.findViewById(R.id.rv_member);
        }
    }
}
