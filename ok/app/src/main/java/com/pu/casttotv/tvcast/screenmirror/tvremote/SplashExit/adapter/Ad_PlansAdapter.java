package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.models.Packages;

import java.util.ArrayList;

public class Ad_PlansAdapter extends RecyclerView.Adapter<Ad_PlansAdapter.ViewHolder> {
    Context context;
    ArrayList<Packages> packages;
    onPlans plans;
    private int selectedPos = -1;

    public Ad_PlansAdapter(Context context, ArrayList<Packages> packages, onPlans plans) {
        this.context = context;
        this.packages = packages;
        this.plans = plans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ad_item_plans, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Packages data = packages.get(position);
        Log.e("parth", "onBindViewHolder: " + data.getDays());
        holder.tvPremiumTitle.setText(data.getTitle());
        holder.tvPremiumDesc.setText(data.getDescription());
        holder.rlMain.setBackground(context.getResources().getDrawable(selectedPos == position ? R.drawable.ad_bg_button_short : R.drawable.ad_bg_item_device));
        holder.ivPremium.setImageResource(selectedPos == position ? R.drawable.ad_ic_premium_selected : R.drawable.ad_ic_premium_unselected);
        holder.ivShine.setVisibility(selectedPos == position ? View.VISIBLE : View.GONE);
        Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.ad_left_right);
        if (selectedPos == position) {
            holder.ivShine.startAnimation(loadAnimation);
            loadAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    holder.ivShine.startAnimation(loadAnimation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        holder.itemView.setOnClickListener(v -> {
            plans.onSelectedPlan(data);
            int pos = position;
            if (pos != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedPos);
                selectedPos = position;
                notifyItemChanged(selectedPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    public interface onPlans {
        public void onSelectedPlan(Packages packages);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlMain;
        ImageView ivPremium, ivShine;
        TextView tvPremiumTitle, tvPremiumDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rlMain);
            ivPremium = itemView.findViewById(R.id.ivPremium);
            ivShine = itemView.findViewById(R.id.ivShine);
            tvPremiumTitle = itemView.findViewById(R.id.tvPremiumTitle);
            tvPremiumDesc = itemView.findViewById(R.id.tvPremiumDesc);

        }

    }
}


