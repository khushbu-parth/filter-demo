package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Model.SplashModel;

public class AppList_Adapter_Banner extends BaseAdapter {

    private Activity activity;
    SparseBooleanArray mSparseBooleanArray;
    ArrayList<SplashModel> exitAppList = new ArrayList<>();

    public AppList_Adapter_Banner(Activity dAct, ArrayList<SplashModel> exitAppList) {
        activity = dAct;
        this.exitAppList = exitAppList;
        mSparseBooleanArray = new SparseBooleanArray(exitAppList.size());
    }

    public int getCount() {
        return exitAppList.size();
    }

    public Object getItem(int position) {
        return exitAppList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_appstore1, parent, false);
            holder = new ViewHolder();
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.imglogo);
            holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
            holder.tv_install = convertView.findViewById(R.id.tv_install);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtname.setText(exitAppList.get(position).getAppName());
        holder.txtname.setSelected(true);
        Glide.with(activity)
                .load(exitAppList.get(position).getAppUrl())
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                .into(holder.imgIcon);

        holder.tv_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(exitAppList.get(position).getAppLink());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    activity.startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {

                    Toast.makeText(activity, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
        return convertView;

    }

    static class ViewHolder {
        ImageView imgIcon;
        TextView txtname, tv_install;
    }
}
