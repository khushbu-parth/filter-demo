package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityFullScreenView;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.util.List;


public class WallpaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity context;
    public String gaEventCategory;
    List<ThemeModel> list;

    public WallpaperAdapter(Activity activity, List<ThemeModel> list) {
        this.context = activity;
        this.list = list;
    }

    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView action_icon;
        ImageView add_icn;
        CircleImageView avtars;
        ImageView backgroundImg;
        RelativeLayout layout_constraint;
        TextView tcvUserNo;
        TextView theme_name;
        TextView tvUserName;
        View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            this.backgroundImg = (ImageView) view.findViewById(R.id.gifImageView);
            this.layout_constraint = (RelativeLayout) view.findViewById(R.id.flash_layout);
            this.add_icn = (ImageView) view.findViewById(R.id.add_icn);
            this.theme_name = (TextView) view.findViewById(R.id.theme_name);
            this.action_icon = (ImageView) view.findViewById(R.id.action_icon);
            this.avtars = (CircleImageView) view.findViewById(R.id.circleImageView);
            this.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            this.tcvUserNo = (TextView) view.findViewById(R.id.tcvUserNo);
        }
    }

    

    public void openCustom() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/jpg");
        this.context.startActivityForResult(Intent.createChooser(intent, "Choose Image"), 100);
    }

    public void openFullScreen(ThemeModel themeModel) {
        Intent intent = new Intent(this.context, ActivityFullScreenView.class);
        themeModel.setGaCategory(this.gaEventCategory);
        intent.putExtra("model", themeModel);
        this.context.startActivity(intent);
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_flash_screen, (ViewGroup) null, false));
        }
        return new MyViewHolder(LayoutInflater.from(this.context)
                .inflate(R.layout.row_flash_screen, (ViewGroup) null, false));
    }

    @Override 
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int resourceByName;
        if (getItemViewType(i) == 0) {
            return;
        }
        ThemeModel themeModel = this.list.get(i);
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        String color = themeModel.getColor();
        if (color != null && Helper.isColorCodeCorrect(color)) {
            myViewHolder.tvUserName.setTextColor(Color.parseColor("#" + color));
            myViewHolder.tcvUserNo.setTextColor(Color.parseColor("#" + color));
        } else {
            myViewHolder.tvUserName.setTextColor(Color.parseColor("#ffffff"));
            myViewHolder.tcvUserNo.setTextColor(Color.parseColor("#ffffff"));
        }
        YoYo.with(Techniques.FadeIn).duration(700L).playOn(myViewHolder.itemView);
        if (themeModel.getSource().equals("custom")) {
            myViewHolder.theme_name.setText(R.string.custom_theme);
            String appliedTheme = ActivityFullScreenView.getAppliedTheme();
            if (Helper.getAppliedThemeType().equals("custom")) {
                myViewHolder.layout_constraint.setVisibility(0);
                myViewHolder.add_icn.setVisibility(8);
                if (appliedTheme != null) {
                    Picasso.get().load(new File(appliedTheme)).resize(170, 300).centerInside().into(myViewHolder.backgroundImg);
                }
            } else {
                myViewHolder.layout_constraint.setVisibility(8);
                myViewHolder.add_icn.setVisibility(0);
                Picasso.get().load(R.drawable.add).into(myViewHolder.add_icn);
            }
        } else if (i == Constants.CUSTOM_POSITION) {
            myViewHolder.theme_name.setText(R.string.custom_theme);
            String string = PreferenceUtils.getInstance().getString(Constants.THEME_KEY);
            if (Helper.getAppliedThemeType().equals(ThemeModel.THEME_TYPE.CUSTOM.toString())) {
                myViewHolder.layout_constraint.setVisibility(0);
                myViewHolder.add_icn.setVisibility(8);
                Picasso.get().load(new File(string)).resize(170, 300).centerInside().into(myViewHolder.backgroundImg);
            } else {
                myViewHolder.layout_constraint.setVisibility(8);
                myViewHolder.add_icn.setVisibility(0);
                Picasso.get().load(R.drawable.add).into(myViewHolder.add_icn);
            }
        } else {
            myViewHolder.theme_name.setText(themeModel.getName());
            myViewHolder.tvUserName.setText(themeModel.getPersonName());
            myViewHolder.tcvUserNo.setText(themeModel.getPersonPhoneNumber());
            Picasso.get().load(Utility.getResourceByName(this.context, themeModel.getPersonImage(), "raw")).placeholder(R.drawable.user).into(myViewHolder.avtars);
            if (themeModel.getSource().equalsIgnoreCase(ThemeModel.Source.ONLINE.toString())) {
                Picasso.get().load(ThemeModel.BASE_IMAGE + themeModel.getThumbnail()).placeholder(R.drawable.placeholder).into(myViewHolder.backgroundImg);
            } else if (themeModel.getSource().equalsIgnoreCase(ThemeModel.Source.OFFLINE.toString()) && (resourceByName = Utility.getResourceByName(this.context, themeModel.getThumbnail(), "raw")) != 0) {
                Picasso.get().load(resourceByName).placeholder(R.drawable.placeholder).into(myViewHolder.backgroundImg);
            }
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.WallpaperAdapter.1
            @Override 
            public void onClick(View view) {
                if (themeModel.getSource().equals("custom")) {
                    WallpaperAdapter.this.openCustom();
                } else {
                    WallpaperAdapter.this.openFullScreen(themeModel);
                }
            }
        });
        setAppliedThemeIcon(myViewHolder, themeModel);
    }

    public void setAppliedThemeIcon(MyViewHolder myViewHolder, ThemeModel themeModel) {
        String string = PreferenceUtils.getInstance().getString(Constants.APPLIED_THEME_NAME);
        if (string != null) {
            if (string.equalsIgnoreCase(themeModel.getName())) {
                myViewHolder.action_icon.setImageResource(R.drawable.tick_theme);
                return;
            }
            myViewHolder.action_icon.setImageResource(R.drawable.set_theme_icn);
            myViewHolder.action_icon.setLayoutParams(new LinearLayout.LayoutParams(47, 47));
        }
    }

    public void updateDataList(List<ThemeModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        return this.list.get(i) == null ? 0 : 2;
    }

    @Override 
    public int getItemCount() {
        return this.list.size();
    }
}
