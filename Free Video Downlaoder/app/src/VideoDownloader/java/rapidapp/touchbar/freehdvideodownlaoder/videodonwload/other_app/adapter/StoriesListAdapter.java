package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ItemsWhatsappViewBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.story.ItemModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class StoriesListAdapter extends RecyclerView.Adapter<StoriesListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemModel> storyItemModelList;

    public StoriesListAdapter(Context context2, ArrayList<ItemModel> arrayList) {
        this.context = context2;
        this.storyItemModelList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemsWhatsappViewBinding) DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.items_whatsapp_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final ItemModel itemModel = this.storyItemModelList.get(i);
        try {
            if (itemModel.getMedia_type() == 2) {
                viewHolder.binding.ivPlay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.ivPlay.setVisibility(View.GONE);
            }
            Glide.with(this.context).load(itemModel.getImage_versions2().getCandidates().get(0).getUrl()).into(viewHolder.binding.pcw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.binding.tvDownload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (itemModel.getMedia_type() == 2) {
                    String url = itemModel.getVideo_versions().get(0).getUrl();
                    String str = Utils.RootDirectoryInsta;
                    Context context = StoriesListAdapter.this.context;
                    Utils.startDownload(url, str, context, "story_" + itemModel.getId() + ".mp4");
                    return;
                }
                String url2 = itemModel.getImage_versions2().getCandidates().get(0).getUrl();
                String str2 = Utils.RootDirectoryInsta;
                Context context2 = StoriesListAdapter.this.context;
                Utils.startDownload(url2, str2, context2, "story_" + itemModel.getId() + ".png");
            }
        });
    }

    @Override
    public int getItemCount() {
        ArrayList<ItemModel> arrayList = this.storyItemModelList;
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