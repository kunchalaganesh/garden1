package com.example.ashrafapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

private Context mContext;
private List<String> mImageUrls;

public ImageAdapter(Context context, List<String> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
        }

@NonNull
@Override
public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = mImageUrls.get(position);

        // Load the image from the URL using an image loading library like Picasso or Glide
//        Picasso.get().load(imageUrl).into(holder.imageView);

    Glide.with(holder.itemView.getContext())
            .load(imageUrl)
            .fitCenter()
            .into(holder.imageView);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String imageUrl = mImageUrls.get(holder.getAdapterPosition());

            // Launch the FullScreenActivity with the image URL as an extra
            Intent intent = new Intent(v.getContext(), FullScreenActivity.class);
            intent.putExtra("imageUrl", imageUrl);
            v.getContext().startActivity(intent);
        }
    });





}

@Override
public int getItemCount() {
        return mImageUrls.size();
        }

public static class ImageViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_view);




    }
}
}