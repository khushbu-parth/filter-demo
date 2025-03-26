package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ItemsWhatsappViewBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.WhatsappStatusModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class WhatsappStatusAdapter extends RecyclerView.Adapter<WhatsappStatusAdapter.ViewHolder> {
    public String SaveFilePath = (Utils.RootDirectoryWhatsappShow + "/");
    private Context context;
    private ArrayList<WhatsappStatusModel> fileArrayList;
    private LayoutInflater layoutInflater;

    public WhatsappStatusAdapter(Context context2, ArrayList<WhatsappStatusModel> arrayList) {
        this.context = context2;
        this.fileArrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.layoutInflater == null) {
            this.layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new ViewHolder((ItemsWhatsappViewBinding) DataBindingUtil.inflate(this.layoutInflater, R.layout.items_whatsapp_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final WhatsappStatusModel whatsappStatusModel = this.fileArrayList.get(i);
        if (whatsappStatusModel.getUri().toString().endsWith(".mp4")) {
            viewHolder.binding.ivPlay.setVisibility(View.VISIBLE);
        } else {
            viewHolder.binding.ivPlay.setVisibility(View.GONE);
        }
        Glide.with(this.context).load(whatsappStatusModel.getPath()).into(viewHolder.binding.pcw);
        viewHolder.binding.tvDownload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Utils.createFileFolder();
                String path = whatsappStatusModel.getPath();
                String substring = path.substring(path.lastIndexOf("/") + 1);
                try {
                    FileUtils.copyFileToDirectory(new File(path), new File(WhatsappStatusAdapter.this.SaveFilePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String substring2 = substring.substring(12);
                MediaScannerConnection.scanFile(WhatsappStatusAdapter.this.context, new String[]{new File(WhatsappStatusAdapter.this.SaveFilePath + substring2).getAbsolutePath()}, new String[]{whatsappStatusModel.getUri().toString().endsWith(".mp4") ? "video/*" : "image/*"}, new MediaScannerConnection.MediaScannerConnectionClient() {

                    public void onMediaScannerConnected() {
                    }

                    public void onScanCompleted(String str, Uri uri) {
                    }
                });
                new File(WhatsappStatusAdapter.this.SaveFilePath, substring).renameTo(new File(WhatsappStatusAdapter.this.SaveFilePath, substring2));
                Context context = WhatsappStatusAdapter.this.context;
                Toast.makeText(context, WhatsappStatusAdapter.this.context.getResources().getString(R.string.saved_to) + WhatsappStatusAdapter.this.SaveFilePath + substring2, 1).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        ArrayList<WhatsappStatusModel> arrayList = this.fileArrayList;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemsWhatsappViewBinding binding;

        public ViewHolder(ItemsWhatsappViewBinding itemsWhatsappViewBinding) {
            super(itemsWhatsappViewBinding.getRoot());
            this.binding = itemsWhatsappViewBinding;
        }
    }
}