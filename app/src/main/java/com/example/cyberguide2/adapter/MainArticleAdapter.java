package com.example.cyberguide2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.example.cyberguide2.cache_java.FileCache;
import com.example.cyberguide2.cache_java.MemoryCache;
import com.example.cyberguide2.cache_java.Utils;
import com.example.cyberguide2.model.Article;
import com.example.cyberguide2.utils.OnRecyclerViewItemClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//
//class ImageLoader {
//
//    MemoryCache memoryCache=new MemoryCache();
//    FileCache fileCache;
//    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
//    ExecutorService executorService;
//
//    public ImageLoader(Context context){
//        fileCache=new FileCache(context);
//        executorService=Executors.newFixedThreadPool(5);
//    }
//
//    int stub_id = R.drawable.ic_launcher_background;
//    public void DisplayImage(String url, int loader, ImageView imageView)
//    {
//        stub_id = loader;
//        imageViews.put(imageView, url);
//        Bitmap bitmap=memoryCache.get(url);
//        if(bitmap!=null)
//            imageView.setImageBitmap(bitmap);
//        else
//        {
//            queuePhoto(url, imageView);
//            imageView.setImageResource(loader);
//        }
//    }
//
//    private void queuePhoto(String url, ImageView imageView)
//    {
//        PhotoToLoad p=new PhotoToLoad(url, imageView);
//        executorService.submit(new PhotosLoader(p));
//    }
//
//    private Bitmap getBitmap(String url)
//    {
//        File f=fileCache.getFile(url);
//
//        //from SD cache
//        Bitmap b = decodeFile(f);
//        if(b!=null)
//            return b;
//
//        //from web
//        try {
//            Bitmap bitmap=null;
//            URL imageUrl = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
//            conn.setConnectTimeout(30000);
//            conn.setReadTimeout(30000);
//            conn.setInstanceFollowRedirects(true);
//            InputStream is=conn.getInputStream();
//            OutputStream os = new FileOutputStream(f);
//            Utils.CopyStream(is, os);
//            os.close();
//            bitmap = decodeFile(f);
//            return bitmap;
//        } catch (Exception ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }
//
//    //decodes image and scales it to reduce memory consumption
//    private Bitmap decodeFile(File f){
//        try {
//            //decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
//
//            //Find the correct scale value. It should be the power of 2.
//            final int REQUIRED_SIZE=70;
//            int width_tmp=o.outWidth, height_tmp=o.outHeight;
//            int scale=1;
//            while(true){
//                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
//                    break;
//                width_tmp/=2;
//                height_tmp/=2;
//                scale*=2;
//            }
//
//            //decode with inSampleSize
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize=scale;
//            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
//        } catch (FileNotFoundException e) {}
//        return null;
//    }
//
//    //Task for the queue
//    private class PhotoToLoad
//    {
//        public String url;
//        public ImageView imageView;
//        public PhotoToLoad(String u, ImageView i){
//            url=u;
//            imageView=i;
//        }
//    }
//
//    class PhotosLoader implements Runnable {
//        PhotoToLoad photoToLoad;
//        PhotosLoader(PhotoToLoad photoToLoad){
//            this.photoToLoad=photoToLoad;
//        }
//
//        @Override
//        public void run() {
//            if(imageViewReused(photoToLoad))
//                return;
//            Bitmap bmp=getBitmap(photoToLoad.url);
//            memoryCache.put(photoToLoad.url, bmp);
//            if(imageViewReused(photoToLoad))
//                return;
//            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
//            Activity a=(Activity)photoToLoad.imageView.getContext();
//            a.runOnUiThread(bd);
//        }
//    }
//
//    boolean imageViewReused(PhotoToLoad photoToLoad){
//        String tag=imageViews.get(photoToLoad.imageView);
//        if(tag==null || !tag.equals(photoToLoad.url))
//            return true;
//        return false;
//    }
//
//    //Used to display bitmap in the UI thread
//    class BitmapDisplayer implements Runnable
//    {
//        Bitmap bitmap;
//        PhotoToLoad photoToLoad;
//        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
//        public void run()
//        {
//            if(imageViewReused(photoToLoad))
//                return;
//            if(bitmap!=null)
//                photoToLoad.imageView.setImageBitmap(bitmap);
//            else
//                photoToLoad.imageView.setImageResource(stub_id);
//        }
//    }
//
//    public void clearCache() {
//        memoryCache.clear();
//        fileCache.clear();
//    }
//
//}
//
//class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//    @SuppressLint("StaticFieldLeak")
//    ImageView bmImage;
//
//
//    public DownloadImageTask(ImageView bmImage) {
//        this.bmImage = bmImage;
//    }
//
//    protected Bitmap doInBackground(String... urls) {
//        String urldisplay = urls[0];
//        Bitmap mIcon11 = null;
//        try {
//            InputStream in = new java.net.URL(urldisplay).openStream();
//            mIcon11 = BitmapFactory.decodeStream(in);
//        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
//        return mIcon11;
//    }
//
//    protected void onPostExecute(Bitmap result) {
//        bmImage.setImageBitmap(result);
//    }
//}
//The adapter is the piece that will connect the data to the RecyclerView and determine the ViewHolder(s)
// which will need to be used to display that data.
public class MainArticleAdapter extends RecyclerView.Adapter<MainArticleAdapter.ViewHolder>  {
    // this class doesn't extend context or activity hence context should be passed in constructor
    private Context mContext;
    public MainArticleAdapter(Context context, List<Article> articleArrayList){
        this.mContext = context;
        this.articleArrayList = articleArrayList;
    }
    private final List<Article> articleArrayList;

    private static OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
//    public MainArticleAdapter(List<Article> articleArrayList) {
//        this.articleArrayList = articleArrayList;
//    }

//    This method is called right when the adapter is created and is used to initialize the viewHolder.
    @NonNull
    @Override
    public MainArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main_article_adapter, viewGroup, false);
        return new ViewHolder(view);

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
        if(!TextUtils.isEmpty(articleModel.getUrlToImage())) {
//            new DownloadImageTask(viewHolder.image1).execute(articleModel.getUrlToImage());
//            viewHolder.image1.setImageURI(Uri.parse(articleModel.getUrlToImage()));

//            ImageLoader imgLoader = new ImageLoader(mContext);
//imgLoader.DisplayImage(articleModel.getUrlToImage(), R.drawable.ic_launcher_background,viewHolder.image1);
//Options can be shared across requests using the RequestOptions class for each request
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.color.black);
            requestOptions.error(R.color.white);
//            caches all versions of the image, return the copy instead of redownloading
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.centerCrop();
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
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }


static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView titleText;
        private final TextView descriptionText;
        private final TextView authorText;
        private final ImageView image1;

        private final LinearLayout artilceAdapterParentLinear;

        ViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.article_adapter_tv_title);
            descriptionText = view.findViewById(R.id.article_adapter_tv_description);
            image1 = view.findViewById(R.id.article_adapter_tv_image1);
            authorText =view.findViewById(R.id.article_adapter_tv_author);

            artilceAdapterParentLinear = view.findViewById(R.id.article_adapter_ll_parent);

            artilceAdapterParentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;
                    if (onRecyclerViewItemClickListener != null) {
//                        Log.d("sjjikrc","me");
                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);

                    }
                }
            });



        }
    }
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

    }}
