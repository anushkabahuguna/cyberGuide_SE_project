package com.example.cyberguide2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.cyberguide2.R;
import com.example.cyberguide2.model.Article;
import com.example.cyberguide2.utils.ClickListener;
import com.example.cyberguide2.utils.OnRecyclerViewItemClickListener;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainArticleAdapter extends RecyclerView.Adapter<MainArticleAdapter.ViewHolder>  {
    // this class doesn't extend context or activity hence context should be passed in constructor
    private final Context mContext;
    private final ClickListener listener;
    private final List<Article> articleArrayList;
    public MainArticleAdapter(Context context, List<Article> articleArrayList,ClickListener listener){
        this.mContext = context;
        this.articleArrayList = articleArrayList;
        this.listener = listener;
    }


    private static OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


//    This method is called right when the adapter is created and is used to initialize the viewHolder.
    @NonNull
    @Override
    public MainArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main_article_adapter, viewGroup, false);
        return new ViewHolder(view,listener);

    }
//    This method is called for each ViewHolder to bind it to the adapter. This is where we will pass our data to our ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull MainArticleAdapter.ViewHolder viewHolder, int position) {
        final Article articleModel = articleArrayList.get(position);
//      TextUtils ---- set of utility functions to do operations on String objects
        if(!TextUtils.isEmpty(articleModel.getTitle())) {
            viewHolder.titleText.setText(articleModel.getTitle());
        }
        if(!TextUtils.isEmpty(articleModel.getDescription())) {
            viewHolder.descriptionText.setText(articleModel.getDescription());
        }
         if(!TextUtils.isEmpty(articleModel.getPublishedAt())) {

             String newDate;
             SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale("India"));
             try {
                 @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(articleModel.getPublishedAt());
                 newDate = dateFormat.format(date);
             } catch (ParseException e) {
                 e.printStackTrace();
                 newDate = articleModel.getPublishedAt();
             }
            viewHolder.publishedAt.setText(newDate);

        }
        if(!TextUtils.isEmpty(articleModel.getUrlToImage())) {

//Options can be shared across requests using the RequestOptions class for each request
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.color.black);
            requestOptions.error(R.color.white);
//            caches all versions of the image, return the copy instead of redownloading
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
//            requestOptions.centerCrop();
// Glide ---- Fast and efficient image loading for Android
            Glide.with(mContext)
                    .load(articleModel.getUrlToImage())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(viewHolder.image1);
        }
        if(!TextUtils.isEmpty(articleModel.getAuthor())) {
            viewHolder.authorText.setText(articleModel.getAuthor());
        }
// by setting tag , we can use one listener for all articles
        viewHolder.artilceAdapterParentLinear.setTag(articleModel);
    }
    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }


static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView titleText;
        private final TextView descriptionText;
        private final TextView authorText;
        private final TextView publishedAt;
        private final ImageView image1;
        private  final ToggleButton tgBtn;
        private final RelativeLayout artilceAdapterParentLinear;
    private final WeakReference<ClickListener> listenerRef;

        ViewHolder(View view, ClickListener listener) {
            super(view);

            TextView saveText = view.findViewById(R.id.Save);
            titleText = view.findViewById(R.id.article_adapter_tv_title);
            descriptionText = view.findViewById(R.id.article_adapter_tv_description);
            image1 = view.findViewById(R.id.article_adapter_tv_image1);
            authorText =view.findViewById(R.id.article_adapter_tv_author);
            publishedAt=view.findViewById(R.id.article_adapter_tv_publishedAt);
            tgBtn = view.findViewById(R.id.tgBtn);
            tgBtn.setText(null);
            tgBtn.setTextOn(null);
            tgBtn.setTextOff(null);
            listenerRef = new WeakReference<>(listener);
            itemView.setOnClickListener(this);
            tgBtn.setOnClickListener(this);

            artilceAdapterParentLinear = view.findViewById(R.id.article_adapter_ll_parent);

            artilceAdapterParentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);

                    }
                }
            });



        }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        if (v.getId() == tgBtn.getId()) {
            Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            View v2 = (View) v.getParent();
            TextView t1 = (TextView) v2.findViewById(R.id.Save);
            if (tgBtn.isChecked())
            t1.setText( "Saved");
            else
                t1.setText( "Save");
        } else {
            Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }

        listenerRef.get().onPositionClicked(getAdapterPosition());
    }
}
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

    }}
