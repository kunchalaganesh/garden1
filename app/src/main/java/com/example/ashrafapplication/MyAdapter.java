package com.example.ashrafapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;
    private String sdate;
    private String type;


    public MyAdapter(Context context, List<DataClass> dataList, String date, String type) {
        this.context = context;
        this.dataList = dataList;
        this.sdate = date;
        this.type  = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        Toast.makeText(context, "check1", Toast.LENGTH_SHORT).show();

        if (sdate.matches("")) {
//            if (dataList.get(position).getDataPeopleCapacity() != null) {
            Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);

//            } else {
//
//                holder.recImage.setImageDrawable(context.getResources().getDrawable(R.drawable.uploading));
//
//            }


            holder.recFullName.setText(dataList.get(position).getDataFullName());
            holder.recPhoneNumber.setText(dataList.get(position).getDataPhoneNumber());
            holder.recAcNonAc.setText(dataList.get(position).getDataAcNonAc());
            holder.recTotalAmount.setText(dataList.get(position).getDataTotalAmount());
            holder.createddate.setText(dataList.get(position).getDataFromDate() + "-" + dataList.get(position).getDataToDate());



        } else {

//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//            try {
//                Date date = dateFormat.parse(dataList.get(holder.getAdapterPosition()).getDataFromDate());
//                long timestamp = date.getTime();
//
//                long currentTimestamp = System.currentTimeMillis();
//                if (currentTimestamp > timestamp) {

//                    if (dataList.get(position).getDataPeopleCapacity() != null) {
            Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
//
//                    } else {

//                        holder.recImage.setImageDrawable(context.getResources().getDrawable(R.drawable.uploading));


            holder.recFullName.setText(dataList.get(position).getDataFullName());
            holder.recPhoneNumber.setText(dataList.get(position).getDataPhoneNumber());
            holder.recAcNonAc.setText(dataList.get(position).getDataAcNonAc());
            holder.recTotalAmount.setText(dataList.get(position).getDataTotalAmount());
            holder.createddate.setText(dataList.get(position).getDataFromDate() + "-" + dataList.get(position).getDataToDate());




//
//                }else{
//                    holder.recCard.setVisibility(View.GONE);
////                    notifyItemChanged(holder.getAdapterPosition());
//                }
//
//
//                // Do something with the timestamp...
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


        }


        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
            intent.putExtra("full_name", dataList.get(holder.getAdapterPosition()).getDataFullName());
            intent.putExtra("phone_number", dataList.get(holder.getAdapterPosition()).getDataPhoneNumber());
            intent.putExtra("acNonAc", dataList.get(holder.getAdapterPosition()).getDataAcNonAc());
            intent.putExtra("email", dataList.get(holder.getAdapterPosition()).getDataEmail());
            intent.putExtra("how_many_day", dataList.get(holder.getAdapterPosition()).getDataDays());
            intent.putExtra("book_from", dataList.get(holder.getAdapterPosition()).getDataFromDate());
            intent.putExtra("book_to", dataList.get(holder.getAdapterPosition()).getDataToDate());
            intent.putExtra("total_amount", dataList.get(holder.getAdapterPosition()).getDataTotalAmount());
            intent.putExtra("adv_paid", dataList.get(holder.getAdapterPosition()).getDataAdvPaid());
            intent.putExtra("remaining_bal", dataList.get(holder.getAdapterPosition()).getDataRemainingBal());
            intent.putExtra("adv_received_by", dataList.get(holder.getAdapterPosition()).getDataAdvReceivedBy());
            intent.putExtra("bal_received_by", dataList.get(holder.getAdapterPosition()).getDataBalReceivedBy());
            intent.putExtra("decoration_amount", dataList.get(holder.getAdapterPosition()).getDataDecorationAmount());
            intent.putExtra("electricity_bill", dataList.get(holder.getAdapterPosition()).getDataElectricityBill());
            intent.putExtra("cleaning_charges", dataList.get(holder.getAdapterPosition()).getDataCleaningCharges());
            intent.putExtra("waiter_amount", dataList.get(holder.getAdapterPosition()).getDataWaiterAmount());
            intent.putExtra("drinking_water", dataList.get(holder.getAdapterPosition()).getDataDrinkingWater());
            intent.putExtra("additional", dataList.get(holder.getAdapterPosition()).getDataDesc());
            intent.putExtra("key", dataList.get(holder.getAdapterPosition()).getKey());
            intent.putExtra("generator_charges", dataList.get(holder.getAdapterPosition()).getDataGeneratorCharges());
            intent.putExtra("damage_charges", dataList.get(holder.getAdapterPosition()).getDataDamageCharges());
            intent.putExtra("guest_house", dataList.get(holder.getAdapterPosition()).getDataGuestHouse());
            intent.putExtra("VIP_dine", dataList.get(holder.getAdapterPosition()).getDataVIPDine());
            intent.putExtra("function_type", dataList.get(holder.getAdapterPosition()).getDataFunctionType());
            intent.putExtra("hall_amount", dataList.get(holder.getAdapterPosition()).getHallamount());
            intent.putExtra("address", dataList.get(holder.getAdapterPosition()).getDataAddress());
            intent.putExtra("people_capacity", dataList.get(holder.getAdapterPosition()).getDataPeopleCapacity());
            intent.putExtra("createdby", dataList.get(holder.getAdapterPosition()).getCreatedby());
            intent.putExtra("createdtime", dataList.get(holder.getAdapterPosition()).getCreatedtime());
            intent.putExtra("updatedby", dataList.get(holder.getAdapterPosition()).getUpdatedby());
            intent.putExtra("updatedtime", dataList.get(holder.getAdapterPosition()).getUpdatedtime());
            intent.putExtra("type", type);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList, String date) {
        dataList = searchList;
        sdate = date;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView recImage;
    TextView recFullName, recAcNonAc, recPhoneNumber, recTotalAmount, createddate;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recPhoneNumber = itemView.findViewById(R.id.recPhoneNumber);
        recAcNonAc = itemView.findViewById(R.id.recAcNonAc);
        recFullName = itemView.findViewById(R.id.recFullName);
        recTotalAmount = itemView.findViewById(R.id.recTotalAmount);
        createddate = itemView.findViewById(R.id.createddate);
    }
}
