package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.design.FlipAnimator;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.listener.DiffCallbackOffline;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.DownloadingItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utility.DownloadFileUtility;

public class AdapterOffline extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int currentSelectedIndex = -1;
    private final List<DownloadingItem> OfflineList;
    private final SparseBooleanArray animationItemsIndex;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public final OfflineListener listener;
    private Context mContext;
    private boolean reverseAllAnimations = false;
    private final SparseBooleanArray selectedItems;

    public interface OfflineListener {
        void onIconClicked(int i);

        void onRowLongClicked(int i);

        void onSingleClicked(int i);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final RelativeLayout iconBack;
        public final RelativeLayout iconContainer;
        public final RelativeLayout iconFront;
        public final LinearLayout messageContainer;
        public final TextView name;
        public final ImageView prev;
        public final TextView size;
        public final TextView url;

        private ItemViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.name);
            this.url = (TextView) view.findViewById(R.id.url);
            this.size = (TextView) view.findViewById(R.id.size);
            this.iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            this.iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            this.prev = (ImageView) view.findViewById(R.id.icon_prev);
            this.iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            this.messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            view.setOnLongClickListener(this);
        }

        ItemViewHolder(AdapterOffline adapterOffline, AdapterOffline adapterOffline2, View view, byte b) {
            this(view);
        }

        public boolean onLongClick(View view) {
            AdapterOffline.this.listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(0);
            return true;
        }
    }

    public AdapterOffline(Context context, List<DownloadingItem> list, OfflineListener offlineListener) {
        this.mContext = context;
        this.OfflineList = list;
        this.listener = offlineListener;
        this.selectedItems = new SparseBooleanArray();
        this.animationItemsIndex = new SparseBooleanArray();
    }

    private String FileSize(Double d) {
        StringBuilder sb;
        try {
            if (d.doubleValue() <= 0.0d) {
                return "1";
            }
            if (d.doubleValue() < 1048576.0d) {
                sb = new StringBuilder();
                sb.append(Double.parseDouble(this.decimalFormat.format(d.doubleValue() / 1024.0d)));
                sb.append(" KB");
            } else if (d.doubleValue() > 1048576.0d && d.doubleValue() < 1.073741824E9d) {
                sb = new StringBuilder();
                sb.append(Double.parseDouble(this.decimalFormat.format((d.doubleValue() / 1024.0d) / 1024.0d)));
                sb.append(" MB");
            } else if (d.doubleValue() > 1.073741824E9d) {
                sb = new StringBuilder();
                sb.append(Double.parseDouble(this.decimalFormat.format(((d.doubleValue() / 1024.0d) / 1024.0d) / 1024.0d)));
                sb.append(" GB");
            } else {
                sb = null;
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    }

    private void applyClickEvents(ItemViewHolder itemViewHolder, final int i) {
        itemViewHolder.iconContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdapterOffline.this.listener.onSingleClicked(i);
            }
        });
        itemViewHolder.messageContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdapterOffline.this.listener.onSingleClicked(i);
            }
        });
        itemViewHolder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                AdapterOffline.this.listener.onRowLongClicked(i);
                view.performHapticFeedback(0);
                return true;
            }
        });
    }

    private void applyIconAnimation(ItemViewHolder itemViewHolder, int i) {
        if (this.selectedItems.get(i, false)) {
            itemViewHolder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(itemViewHolder.iconBack);
            itemViewHolder.iconBack.setVisibility(View.VISIBLE);
            itemViewHolder.iconBack.setAlpha(1.0f);
            if (currentSelectedIndex == i) {
                FlipAnimator.flipView(this.mContext, itemViewHolder.iconBack, itemViewHolder.iconFront, true);
                resetCurrentIndex();
                return;
            }
            return;
        }
        itemViewHolder.iconBack.setVisibility(View.GONE);
        resetIconYAxis(itemViewHolder.iconFront);
        itemViewHolder.iconFront.setVisibility(View.VISIBLE);
        itemViewHolder.iconFront.setAlpha(1.0f);
        if ((this.reverseAllAnimations && this.animationItemsIndex.get(i, false)) || currentSelectedIndex == i) {
            FlipAnimator.flipView(this.mContext, itemViewHolder.iconBack, itemViewHolder.iconFront, false);
            resetCurrentIndex();
        }
    }

    private void populateItemRows(ItemViewHolder itemViewHolder, int i) {
        DownloadingItem downloadingItem = this.OfflineList.get(i);
        itemViewHolder.name.setText(downloadingItem.getName());
        itemViewHolder.itemView.setActivated(this.selectedItems.get(i, false));
        itemViewHolder.size.setText(FileSize(Double.valueOf(downloadingItem.getTotalSize())));
        try {
            itemViewHolder.url.setText(new URL(downloadingItem.getUrl()).getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (DownloadFileUtility.check_video_extension(downloadingItem.getName())) {
            File file = new File(this.mContext.getFilesDir().toString() + "/.thumbs");
            if (!file.exists()) {
                file.mkdirs();
            }
            String str = downloadingItem.getFileId() + ".png";
            File file2 = new File(file, str);
            if (file2.exists()) {
                itemViewHolder.prev.setImageBitmap(BitmapFactory.decodeFile(file + "/" + str));
                applyIconAnimation(itemViewHolder, i);
                applyClickEvents(itemViewHolder, i);
            }
            Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(downloadingItem.getLocalFilePath(), 3);
            if (createVideoThumbnail != null) {
                Bitmap createScaledBitmap = Bitmap.createScaledBitmap(createVideoThumbnail, 90, 50, true);
                itemViewHolder.prev.setImageBitmap(createScaledBitmap);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    createScaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                applyIconAnimation(itemViewHolder, i);
                applyClickEvents(itemViewHolder, i);
            }
        } else {
            DownloadFileUtility.check_audio_extension(downloadingItem.getName());
        }
        itemViewHolder.prev.setImageBitmap(ThumbnailUtils.createVideoThumbnail(downloadingItem.getLocalFilePath(), 3));
        applyIconAnimation(itemViewHolder, i);
        applyClickEvents(itemViewHolder, i);
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0.0f) {
            view.setRotationY(0.0f);
        }
    }

    public void clearSelections() {
        this.reverseAllAnimations = true;
        this.selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (this.OfflineList.size() > 0) {
            return this.OfflineList.size();
        }
        return 0;
    }

    public long getItemId(int i) {
        return (long) this.OfflineList.get(i).getFileId();
    }

    public int getSelectedItemCount() {
        return this.selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        ArrayList arrayList = new ArrayList(this.selectedItems.size());
        for (int i = 0; i < this.selectedItems.size(); i++) {
            arrayList.add(Integer.valueOf(this.selectedItems.keyAt(i)));
        }
        return arrayList;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        populateItemRows((ItemViewHolder) viewHolder, i);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemViewHolder itemViewHolder = new ItemViewHolder(this, this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_offline, viewGroup, false), (byte) 0);
        this.mContext = viewGroup.getContext();
        return itemViewHolder;
    }

    public void removeData(int i) {
        this.OfflineList.remove(i);
        resetCurrentIndex();
    }

    public void resetAnimationIndex() {
        this.reverseAllAnimations = false;
        this.animationItemsIndex.clear();
    }

    public void toggleSelection(int i) {
        currentSelectedIndex = i;
        if (this.selectedItems.get(i, false)) {
            this.selectedItems.delete(i);
            this.animationItemsIndex.delete(i);
        } else {
            this.selectedItems.put(i, true);
            this.animationItemsIndex.put(i, true);
        }
        notifyItemChanged(i);
    }

    public void updateData(List<DownloadingItem> list) {
        DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new DiffCallbackOffline(this.OfflineList, list));
        this.OfflineList.clear();
        this.OfflineList.addAll(list);
        calculateDiff.dispatchUpdatesTo((RecyclerView.Adapter) this);
    }
}
