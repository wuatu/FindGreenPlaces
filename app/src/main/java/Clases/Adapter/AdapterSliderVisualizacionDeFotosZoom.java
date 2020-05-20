package Clases.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.cristian.findgreenplaces.R;
import com.example.cristian.findgreenplaces.VisualizarAtractivoTuristico;

import Fragment.FotosATFragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.cristian.findgreenplaces.R;
import com.example.cristian.findgreenplaces.VisualizacionDeImagen;
import com.example.cristian.findgreenplaces.VisualizarAtractivoTuristico;
import Fragment.FotosATFragment;

import java.util.ArrayList;


public class AdapterSliderVisualizacionDeFotosZoom extends PagerAdapter {
    private Context context;
    private String[] imageUrls;
    VisualizarAtractivoTuristico activity;
    private int positionImage=0;
    ImageView imageViewImage=null;

    public AdapterSliderVisualizacionDeFotosZoom(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }


    public ImageView getImageViewImage() {
        return imageViewImage;
    }

    public void setImageViewImage(ImageView imageViewImage) {
        this.imageViewImage = imageViewImage;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        imageViewImage = new ImageView(context);
        imageViewImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(context)
                .load(imageUrls[position])
                .centerCrop()
                .into(imageViewImage);
        container.addView(imageViewImage);
        return imageViewImage;
    }

    public int getPositionImage() {
        return positionImage;
    }

    public void setPositionImage(int positionImage) {
        this.positionImage = positionImage;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}


