package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class RecordingViewHolder extends RecyclerView.ViewHolder {
    public ImageView callType;
    public CardView cardView;
    public int colorCode;
    public LinearLayout deleteView;
    public LinearLayout detailView;
    public LinearLayout editView;
    public ImageView favBtn;
    public LinearLayout favView;
    public ImageView icon;
    public AppCompatTextView letter;
    public LinearLayout moreToggleIcon;
    public EditText recordingNameEdit;
    public TextView recording_date;
    public TextView recording_name;
    public TextView recording_size;
    public LinearLayout shareView;
    public CircleImageView userImg;

    public RecordingViewHolder(View view) {
        super(view);
        this.colorCode = -1;
        this.recording_date = (TextView) view.findViewById(R.id.recording_date);
        this.recording_name = (TextView) view.findViewById(R.id.recording_name);
        this.recording_size = (TextView) view.findViewById(R.id.recording_duration);
        this.favBtn = (ImageView) view.findViewById(R.id.favIcon);
        this.moreToggleIcon = (LinearLayout) view.findViewById(R.id.toggle_icon);
        this.detailView = (LinearLayout) view.findViewById(R.id.option_pane);
        this.shareView = (LinearLayout) view.findViewById(R.id.shareRecording);
        this.deleteView = (LinearLayout) view.findViewById(R.id.deleteRecording);
        this.editView = (LinearLayout) view.findViewById(R.id.editRecording);
        this.recordingNameEdit = (EditText) view.findViewById(R.id.recording_name_edit);
        this.callType = (ImageView) view.findViewById(R.id.call_type);
        this.userImg = (CircleImageView) view.findViewById(R.id.user_img);
        this.cardView = (CardView) view.findViewById(R.id.card);
        this.letter = (AppCompatTextView) view.findViewById(R.id.letter);
        this.favView = (LinearLayout) view.findViewById(R.id.favView);
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public void setColorCode(int i) {
        this.colorCode = i;
    }
}
