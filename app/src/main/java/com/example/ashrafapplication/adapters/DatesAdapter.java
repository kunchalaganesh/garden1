package com.example.ashrafapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ashrafapplication.R;
import com.example.ashrafapplication.models.Bookmodel;

import java.util.List;

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.ViewHolder> {

    private List<Bookmodel> dates;

    public DatesAdapter(List<Bookmodel> dates) {
        this.dates = dates;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textPhone;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textPhone = itemView.findViewById(R.id.text_phone);
        }

        public void bind(Bookmodel model) {
            textName.setText(model.getDataFullName());
            textPhone.setText(model.getDataPhoneNumber());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bookmodel model = dates.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}