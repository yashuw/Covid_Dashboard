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

import java.util.ArrayList;
import java.util.List;

public class GroupAdpTestCenter extends RecyclerView.Adapter<GroupAdpTestCenter.ViewHolder> implements Filterable {
    private Activity activity;
    ArrayList<String>StateName;
    ArrayList<String>StateNameFull;

    GroupAdpTestCenter(View.OnClickListener activity, ArrayList<String>StateName)
    {
        this.activity= (Activity) activity;
        this.StateName=StateName;
        StateNameFull=new ArrayList<>(StateName);
    }

    public GroupAdpTestCenter(Activity activity, ArrayList<String> StateName) {
        this.activity=activity;
        this.StateName=StateName;
        StateNameFull=new ArrayList<>(StateName);

    }

    @NonNull
    @Override
    public GroupAdpTestCenter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group,parent,false);
        return new GroupAdpTestCenter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdpTestCenter.ViewHolder holder, int position)
    {
        holder.tvName.setText(StateName.get(position).toUpperCase());

        ArrayList<String>TestCenterList=new ArrayList<>();
        //Toast.makeText(activity, "StateName : "+StateName.get(position), Toast.LENGTH_SHORT).show();
        //Toast.makeText(activity, "Value : "+MainActivity.map.get(StateName.get(position)), Toast.LENGTH_SHORT).show();
        TestCenterList.addAll(MainActivity.map.get(StateName.get(position)));
        //TestCenterList.add("Hello");

        MemberAdpTestCenter adapterMember =new MemberAdpTestCenter(TestCenterList);

        //Initialize layout manager
        LinearLayoutManager layoutManagerMember=new LinearLayoutManager(activity);

        //set Layout Manager
        holder.rvMember.setLayoutManager(layoutManagerMember);

        //set Adapter
        holder.rvMember.setAdapter(adapterMember);


    }

    @Override
    public int getItemCount() {
        return StateName.size();
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
                filteredList.addAll(StateNameFull);
            }
            else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for(String item : StateNameFull)
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
            StateName.clear();
            StateName.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RecyclerView rvMember;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            rvMember=itemView.findViewById(R.id.rv_member);
        }
    }
}
