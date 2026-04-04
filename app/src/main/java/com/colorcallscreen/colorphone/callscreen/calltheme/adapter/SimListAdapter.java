package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.os.Build;
import android.os.CountDownTimer;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.SimChooseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.SimChooserHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import java.util.List;


public class SimListAdapter extends RecyclerView.Adapter<SimListAdapter.MyViewHolder> {
    private CallModel callModel;
    private SimChooseActivity context;
    private List<PhoneAccountHandle> phoneAccountHandles;
    private TelecomManager telecomManager;
    public CountDownTimer timer;

    
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView sim_icn;
        private final TextView sim_name;
        private final TextView sim_no;
        private final TextView timer;

        public MyViewHolder(View view) {
            super(view);
            this.sim_name = (TextView) view.findViewById(R.id.sim_name);
            this.sim_no = (TextView) view.findViewById(R.id.sim_no);
            this.timer = (TextView) view.findViewById(R.id.timer);
            this.sim_icn = (ImageView) view.findViewById(R.id.sim_icn);
        }
    }

    public SimListAdapter(SimChooseActivity simChooseActivity, List<PhoneAccountHandle> list) {
        this.context = simChooseActivity;
        this.phoneAccountHandles = list;
        this.telecomManager = (TelecomManager) simChooseActivity.getSystemService("telecom");
    }

    @Override 
    public int getItemCount() {
        return this.phoneAccountHandles.size();
    }

    public void setCallModel(CallModel callModel) {
        this.callModel = callModel;
    }

    /* JADX WARN: Type inference failed for: r12v6, types: [com.colorcallscreen.colorphone.callscreen.calltheme.adapter.SimListAdapter$1] */
    @Override 
    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            final PhoneAccount phoneAccount = this.telecomManager.getPhoneAccount(this.phoneAccountHandles.get(i));
            myViewHolder.sim_name.setText(phoneAccount.getLabel());
            myViewHolder.sim_no.setText("" + (i + 1));
            if (i == 0) {
                myViewHolder.sim_icn.setColorFilter(this.context.getColor(R.color.sim_one));
            }
            if (i == 1) {
                myViewHolder.sim_icn.setColorFilter(this.context.getColor(R.color.sim_two));
            }
            String selectedSim = SimChooserHandler.getSelectedSim();
            if (selectedSim != null) {
                if (selectedSim.equalsIgnoreCase(phoneAccount.getLabel().toString())) {
                    myViewHolder.timer.setVisibility(0);
                    this.timer = new CountDownTimer(6000L, 1000L) { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.SimListAdapter.1
                        @Override
                        public void onFinish() {
                            CallHandler callHandler = CallHandler.sharedInstance;
                            if (callHandler != null) {
                                callHandler.onSimSelected(phoneAccount.getAccountHandle(), SimListAdapter.this.callModel);
                            }
                        }

                        @Override
                        public void onTick(long j) {
                            myViewHolder.timer.setText("(" + (j / 1000) + " s)");
                        }
                    }.start();
                } else {
                    myViewHolder.timer.setVisibility(8);
                }
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.SimListAdapter.2
                @Override 
                public void onClick(View view) {
                    CallHandler callHandler = CallHandler.sharedInstance;
                    if (callHandler != null) {
                        callHandler.onSimSelected(phoneAccount.getAccountHandle(), SimListAdapter.this.callModel);
                        if (SimListAdapter.this.context != null) {
                            SimListAdapter.this.context.finishAndRemoveTask();
                        }
                    }
                }
            });
        }
    }

    @Override 
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_sim_view, viewGroup, false));
    }
}
