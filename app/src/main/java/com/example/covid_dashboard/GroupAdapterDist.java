package com.example.covid_dashboard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GroupAdapterDist extends RecyclerView.Adapter< GroupAdapterDist.ViewHolder> implements Filterable {
    ArrayList<String>States;
    ArrayList<String>StatesFull;
    Activity activity;
    OnCardClickListener mListener;

    public  interface OnCardClickListener{
        void OnCardClick(int position);
    }
    public  void setOnCardClickListener(OnCardClickListener Listener){
        mListener=Listener;
    }

    GroupAdapterDist(Activity activity , ArrayList<String>StateNames)
    {
        this.activity=activity;
        this.States=StateNames;
        StatesFull=new ArrayList<>(StateNames);
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_grp_state,parent,false);

        return new GroupAdapterDist.ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvName.setText(States.get(position));

    }


    @Override
    public int getItemCount() {
        return States.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(StatesFull);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (String item : StatesFull) {
                    if (item.toLowerCase().contains(filterpattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            States.clear();
            States.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(@NonNull View itemView, final OnCardClickListener listener) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            listener.OnCardClick(pos);
                        }
                    }

                }
            });
        }
    }
}
