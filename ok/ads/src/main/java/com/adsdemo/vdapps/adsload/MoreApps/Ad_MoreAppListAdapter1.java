package com.adsdemo.vdapps.adsload.MoreApps;

import static com.adsdemo.vdapps.adsload.AdsManager.Headercountlist;
import static com.adsdemo.vdapps.adsload.AdsManager.finalAppCouner;
import static com.adsdemo.vdapps.adsload.AdsManager.totalAppCouner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import com.adsdemo.vdapps.adsload.Ad_Globals;
import com.adsdemo.vdapps.adsload.models.MoreAppModel;
import com.adsdemo.vdapps.R;


public class Ad_MoreAppListAdapter1 extends RecyclerView.Adapter<Ad_MoreAppListAdapter1.ViewHolder> {

    LayoutInflater layoutInflater;
    View view;
    Context context;
    ArrayList<MoreAppModel> appLists = new ArrayList<>();
    private SharedPreferences pref;


    public Ad_MoreAppListAdapter1(Context context, ArrayList<MoreAppModel> moreAppData) {
        this.context = context;
        this.appLists = moreAppData;
        this.layoutInflater = LayoutInflater.from(context);
        Headercountlist = new ArrayList<String>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.ad_more_list_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

//        if (position < 3) {
            if (Ad_Globals.getPrefBoolean(context, appLists.get(position).app_packageName) == true) {
                holder.iv_new.setVisibility(View.GONE);
            } else {
                holder.iv_new.setVisibility(View.VISIBLE);
            }
//        }
        if (Headercountlist.isEmpty()) {
            finalAppCouner = totalAppCouner;
        }

        if (Ad_Globals.getPrefBoolean(context, appLists.get(position).app_packageName) == true) {
            if (Headercountlist.contains(appLists.get(position).app_packageName)) {
            } else {
                Headercountlist.add(appLists.get(position).app_packageName);
                finalAppCouner = totalAppCouner - Headercountlist.size();
                pref = context.getSharedPreferences("counter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("lang_us", finalAppCouner);
                editor.commit();
            }

        }

        String action_str = appLists.get(position).app_packageName;
        if (action_str.contains("http")) {
            try {
                holder.txtAppName.setText(appLists.get(position).app_name.split("/")[0]);
            } catch (Exception e) {
                holder.txtAppName.setText(appLists.get(position).app_name);
            }
            String donwloadstr;
            try {
                donwloadstr = appLists.get(position).app_name.split("/")[1];
            } catch (Exception e) {
                donwloadstr = "Open";
            }
//            holder.download.setText(donwloadstr);
        } else {
            holder.txtAppName.setText(appLists.get(position).app_name.split(",")[0]);
//            holder.download.setText("Install");
        }


        Glide.with(context).load(appLists.get(position).app_logo)
                .apply(RequestOptions.placeholderOf(R.mipmap.ad_ic_launcher))
                .into(holder.ivApp);

       holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    if (getAdapterPosition() < 6) {
                Ad_Globals.setPrefBoolean(context, appLists.get(position).app_packageName, true);
//                    }
                try {
                    Uri uri = Uri.parse("market://details?id=" + appLists.get(position).app_packageName);
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(myAppLinkToMarket);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                notifyItemChanged(position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return appLists.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivApp;
        TextView txtAppName;
        RelativeLayout card_view;
        ImageView iv_new;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAppName = (TextView) itemView.findViewById(R.id.txt_explore_app_name);
            txtAppName.setSelected(true);
            ivApp = (ImageView) itemView.findViewById(R.id.img_explore_app_icon);
            card_view = (RelativeLayout) itemView.findViewById(R.id.rl_explore_app_single_main);
            iv_new = (ImageView) itemView.findViewById(R.id.iv_new);

        }
    }
}