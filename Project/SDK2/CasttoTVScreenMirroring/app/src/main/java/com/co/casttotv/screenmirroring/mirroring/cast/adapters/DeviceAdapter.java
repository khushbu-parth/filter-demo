package com.co.casttotv.screenmirroring.mirroring.cast.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ItemDevicesBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.interfaces.AdapterListener;
import com.co.casttotv.screenmirroring.mirroring.cast.models.ConnectModel;
import com.connectsdk.device.ConnectableDevice;

import java.util.ArrayList;
import java.util.Iterator;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    Context mContext;
    ArrayList<ConnectableDevice> arrayList = new ArrayList<>();
    ArrayList<ConnectModel> modelList;

    private ConnectAdapterCallback mCallback;
    private int cur_selected = -1;


    public interface ConnectAdapterCallback extends AdapterListener<ArrayList<ConnectModel>, ConnectModel> {
    }

    public DeviceAdapter(Context context, ArrayList<ConnectModel> arrayList) {
        this.mContext = context;
        this.modelList = new ArrayList<>(arrayList);
    }

    public void updateData(ArrayList<ConnectModel> arrayList) {
        this.modelList = new ArrayList<>(arrayList);
        notifyDataSetChanged();
    }

    public void RemoveData(ConnectableDevice device) {
        arrayList.remove(device);
        notifyDataSetChanged();
    }

    public void setAdapterListener(ConnectAdapterCallback connectAdapterCallback) {
        mCallback = connectAdapterCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDevicesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_devices, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<ConnectableDevice> devices = modelList.get(position).getDevices();

        if (devices != null) {
            ConnectableDevice device = devices.get(0);
            holder.binding.textTitle.setText(device.getFriendlyName());

            if (devices.size() != 1) {
                StringBuilder sb = new StringBuilder();

                Iterator<ConnectableDevice> it = devices.iterator();
                while (it.hasNext()) {
                    sb.append(it.next().getServiceId()).append(", ");
                }
                holder.binding.textTag.setText(sb.substring(0, sb.lastIndexOf(", ")));
            } else {
                holder.binding.textTag.setText(device.getServiceId());
            }
        }

        if (modelList.get(position).isConnected()) {
            holder.binding.textConnected.setVisibility(View.VISIBLE);
            holder.binding.textConnected.setText("Connected");
        } else {
            holder.binding.textConnected.setVisibility(View.GONE);
            if (modelList.get(position).isSelected()) {
                holder.binding.textConnected.setVisibility(View.VISIBLE);
                holder.binding.textConnected.setText("Connecting..");
            }
        }

        holder.itemView.setOnClickListener(view -> {
            int i2 = cur_selected;
            if (i2 != -1) {
                modelList.get(position).setSelected(false);
            }
            notifyItemChanged(cur_selected);

            cur_selected = position;
            modelList.get(position).setSelected(true);

            notifyItemChanged(cur_selected);
            if (mCallback != null) {
                ArrayList<ConnectModel> arrayList = modelList;
                mCallback.onClickItem(arrayList, arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (modelList != null) return modelList.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemDevicesBinding binding;

        public ViewHolder(@NonNull ItemDevicesBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
