package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ItemsFileViewBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.interfaces.FileListClickInterface;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<File> fileArrayList;
    private FileListClickInterface fileListClickInterface;
    private LayoutInflater layoutInflater;

    public FileListAdapter(Context context2, ArrayList<File> arrayList, FileListClickInterface fileListClickInterface2) {
        this.context = context2;
        this.fileArrayList = arrayList;
        this.fileListClickInterface = fileListClickInterface2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.layoutInflater == null) {
            this.layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new ViewHolder((ItemsFileViewBinding) DataBindingUtil.inflate(this.layoutInflater, R.layout.items_file_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final File file = this.fileArrayList.get(i);
        try {
            if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".mp4")) {
                viewHolder.mbinding.ivPlay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mbinding.ivPlay.setVisibility(View.GONE);
            }
            Glide.with(this.context).load(file.getPath()).into(viewHolder.mbinding.pc);
        } catch (Exception unused) {
        }
        viewHolder.mbinding.rlMain.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                FileListAdapter.this.fileListClickInterface.getPosition(i, file);
            }
        });
    }

    @Override
    public int getItemCount() {
        ArrayList<File> arrayList = this.fileArrayList;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemsFileViewBinding mbinding;

        public ViewHolder(ItemsFileViewBinding itemsFileViewBinding) {
            super(itemsFileViewBinding.getRoot());
            this.mbinding = itemsFileViewBinding;
        }
    }
}