package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Model.SplashModel;

public class AppList_Adapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    SparseBooleanArray mSparseBooleanArray;
    ArrayList<SplashModel> exitList = new ArrayList<>();

    public AppList_Adapter(Activity dAct, ArrayList<SplashModel> exitList) {
        activity = dAct;
        this.exitList = exitList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSparseBooleanArray = new SparseBooleanArray(exitList.size());
    }

    public int getCount() {
        return exitList.size();
    }

    public Object getItem(int position) {
        return exitList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_appstore, parent, false);
            holder = new ViewHolder();
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.imglogo);
            holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
            holder.tv_install = convertView.findViewById(R.id.tv_install);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtname.setText(exitList.get(position).getAppName());
        holder.txtname.setSelected(true);
        Glide.with(activity)
                .load(exitList.get(position).getAppUrl())
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                .into(holder.imgIcon);

        return convertView;
    }

    public class ViewHolder {
        ImageView imgIcon;
        TextView txtname, tv_install;
    }
}
