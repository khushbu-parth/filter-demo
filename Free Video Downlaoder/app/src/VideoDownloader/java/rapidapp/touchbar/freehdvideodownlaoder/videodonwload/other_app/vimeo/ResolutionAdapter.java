package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.vimeo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;

public class ResolutionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemResolutionModel> irem_arrayList;

    public ResolutionAdapter(Context context, ArrayList<ItemResolutionModel> arrayList) {
        this.context = context;
        this.irem_arrayList = arrayList;
    }

    public int getCount() {
        if (this.irem_arrayList != null) {
            return this.irem_arrayList.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        if (this.irem_arrayList != null) {
            return this.irem_arrayList.get(position);
        }
        return Integer.valueOf(0);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    @SuppressLint("WrongConstant")
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.item_resolution, null);
        }
        TextView txtName = (TextView) convertView.findViewById(R.id.text1);
        TextView resolution = (TextView) convertView.findViewById(R.id.text2);
        String name = "";
        String resolution_text = "";
        if (((ItemResolutionModel) this.irem_arrayList.get(position)).getNameVideo() != null) {
            name = ((ItemResolutionModel) this.irem_arrayList.get(position)).getNameVideo();
        }
        if (((ItemResolutionModel) this.irem_arrayList.get(position)).getResolution() != null) {
            resolution_text = ((ItemResolutionModel) this.irem_arrayList.get(position)).getResolution();
        }
        if (name.length() > 20) {
            name = name.substring(0, 20) + "...";
        }
        txtName.setText(name);
        resolution.setText("" + resolution_text);
        return convertView;
    }
}