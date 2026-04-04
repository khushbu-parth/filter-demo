package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import java.util.List;


public class ConfereceCallAdapter extends RecyclerView.Adapter<ConfereceCallAdapter.ConferenceVH> {
    private List<CallModel> callModels;
    private Context context;

    
    public class ConferenceVH extends RecyclerView.ViewHolder {
        private Chronometer chronometer;
        private ImageView endCall;
        private TextView nameTxt;

        public ConferenceVH(View view) {
            super(view);
            this.endCall = (ImageView) view.findViewById(R.id.endCall);
            this.nameTxt = (TextView) view.findViewById(R.id.number_tv);
            this.chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        }
    }

    public ConfereceCallAdapter(Context context, List<CallModel> list) {
        this.context = context;
        this.callModels = list;
    }

    @Override 
    public int getItemCount() {
        return this.callModels.size();
    }

    @Override 
    public void onBindViewHolder(final ConferenceVH conferenceVH, int i) {
        final CallModel callModel = this.callModels.get(i);
        String nameFromCall = Utility.getNameFromCall(callModel, this.context);
        if (nameFromCall == null || nameFromCall.equals(this.context.getString(R.string.unknown))) {
            conferenceVH.nameTxt.setText(Utility.getPhoneNumberOfCall(callModel, this.context));
        } else {
            conferenceVH.nameTxt.setText(nameFromCall);
        }
        try {
            new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ConfereceCallAdapter.1
                @Override 
                public void run() {
                    try {
                        ConferenceVH conferenceVH2 = conferenceVH;
                        if (conferenceVH2 != null && conferenceVH2.nameTxt != null) {
                            conferenceVH.nameTxt.setSelected(true);
                        }
                    } catch (Exception unused) {
                    }
                }
            }, 1000L);
        } catch (Exception unused) {
        }
        conferenceVH.chronometer.setBase(callModel.getBaseTime());
        conferenceVH.chronometer.start();
        conferenceVH.endCall.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ConfereceCallAdapter.2
            @Override 
            public void onClick(View view) {
                callModel.getCall().disconnect();
                Utility.vibrate(ConfereceCallAdapter.this.context);
            }
        });
    }

    @Override 
    public ConferenceVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ConferenceVH(LayoutInflater.from(this.context).inflate(R.layout.confrence_item_new, viewGroup, false));
    }
}
