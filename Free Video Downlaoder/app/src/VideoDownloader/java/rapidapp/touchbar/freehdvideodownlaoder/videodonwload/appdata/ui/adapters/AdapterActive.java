package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.WebApplication;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBDownloadManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.design.FlipAnimator;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.listener.DiffCallback;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.DownloadingItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.NetworkUtil;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.StoreUserData;

public class AdapterActive extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int currentSelectedIndex = -1;
    private List<DownloadingItem> ActiveList = new ArrayList();
    private final SparseBooleanArray animationItemsIndex;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public final ActiveListener listener;
    public Context mContext;
    private boolean reverseAllAnimations = false;
    private final SparseBooleanArray selectedItems;
    public final StoreUserData storeUserData;

    public interface ActiveListener {
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
        public final ImageView pause;
        public final TextView percent;
        private final ImageView prev;
        private final ProgressBar progress_bar;
        public final ImageView resume;
        public final TextView size;
        public final TextView speed;

        private ItemViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.name);
            this.size = (TextView) view.findViewById(R.id.size);
            this.speed = (TextView) view.findViewById(R.id.speed);
            this.percent = (TextView) view.findViewById(R.id.percent);
            this.pause = (ImageView) view.findViewById(R.id.video_pause);
            this.resume = (ImageView) view.findViewById(R.id.video_resume);
            this.iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            this.iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            this.prev = (ImageView) view.findViewById(R.id.icon_prev);
            this.progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
            this.iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            this.messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            view.setOnLongClickListener(this);
        }

        ItemViewHolder(AdapterActive adapterActive, AdapterActive adapterActive2, View view, byte b) {
            this(view);
        }

        public ProgressBar getProgressBar() {
            return this.progress_bar;
        }

        public boolean onLongClick(View view) {
            AdapterActive.this.listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(0);
            return true;
        }
    }

    public AdapterActive(Context context, List<DownloadingItem> list, ActiveListener activeListener) {
        this.ActiveList.addAll(list);
        this.mContext = context;
        this.ActiveList = list;
        this.listener = activeListener;
        this.selectedItems = new SparseBooleanArray();
        this.animationItemsIndex = new SparseBooleanArray();
        this.storeUserData = new StoreUserData(context);
    }

    private String ConvertToString(int i) {
        try {
            return i + "%";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String FileSize(double d, double d2) {
        String str;
        StringBuilder sb;
        StringBuilder sb2;
        double d3 = 0.0d;
        String str2 = "1";
        if (d > 0.0d) {
            if (d < 1048576.0d) {
                try {
                    sb2 = new StringBuilder();
                    sb2.append(Double.parseDouble(this.decimalFormat.format(d / 1024.0d)));
                    sb2.append(" KB");
                } catch (Exception e) {
                    e.printStackTrace();
                    return str2 + " / " + str2;
                }
            } else if (d <= 1048576.0d || d >= 1.073741824E9d) {
                sb2 = new StringBuilder();
                sb2.append(Double.parseDouble(this.decimalFormat.format(((d / 1024.0d) / 1024.0d) / 1024.0d)));
                sb2.append(" GB");
            } else {
                sb2 = new StringBuilder();
                sb2.append(Double.parseDouble(this.decimalFormat.format((d / 1024.0d) / 1024.0d)));
                sb2.append(" MB");
            }
            str = sb2.toString();
            d3 = 0.0d;
        } else {
            str = str2;
        }
        if (d2 > d3) {
            if (d2 < 1048576.0d) {
                sb = new StringBuilder();
                sb.append(Double.parseDouble(this.decimalFormat.format(d2 / 1024.0d)));
                sb.append(" KB");
            } else if (d2 <= 1048576.0d || d2 >= 1.073741824E9d) {
                sb = new StringBuilder();
                sb.append(Double.parseDouble(this.decimalFormat.format(((d2 / 1024.0d) / 1024.0d) / 1024.0d)));
                sb.append(" GB");
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(Double.parseDouble(this.decimalFormat.format((d2 / 1024.0d) / 1024.0d)));
                sb3.append(" MB");
                sb = sb3;
            }
            str2 = sb.toString();
        }
        return str + " / " + str2;
    }

    private void applyClickEvents(final ItemViewHolder itemViewHolder, final int i) {
        final DownloadingItem downloadingItem = this.ActiveList.get(i);
        itemViewHolder.pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                itemViewHolder.pause.setVisibility(View.GONE);
                itemViewHolder.resume.setVisibility(View.VISIBLE);
                itemViewHolder.speed.setText(AdapterActive.this.mContext.getString(R.string.paused));
                downloadingItem.setIsInPause(1);
                WebApplication.getInstance().downloadService.pauseDownload(downloadingItem);
            }
        });
        itemViewHolder.resume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (DBDownloadManager.getInstance(AdapterActive.this.mContext).getCurrentDownloadingCount() >= AdapterActive.this.storeUserData.getSettings().getMaximumDownload()) {
                    MainActivity.getInstance().show_snack(AdapterActive.this.mContext.getString(R.string.queuelimit));
                } else if (!NetworkUtil.isConnected()) {
                    MainActivity.getInstance().show_snack(AdapterActive.this.mContext.getString(R.string.nointernet));
                } else if (!NetworkUtil.getMobileConnectivityStatus(AdapterActive.this.mContext)) {
                    downloadingItem.setIsInPause(0);
                    WebApplication.getInstance().downloadService.resumeDownload(downloadingItem);
                    itemViewHolder.speed.setText(AdapterActive.this.download_speed((double) downloadingItem.getSpeed()));
                    itemViewHolder.resume.setVisibility(View.GONE);
                    itemViewHolder.pause.setVisibility(View.VISIBLE);
                } else if (AdapterActive.this.storeUserData.getSettings().isMobile()) {
                    downloadingItem.setIsInPause(0);
                    WebApplication.getInstance().downloadService.resumeDownload(downloadingItem);
                    itemViewHolder.speed.setText(AdapterActive.this.download_speed((double) downloadingItem.getSpeed()));
                    itemViewHolder.resume.setVisibility(View.GONE);
                    itemViewHolder.pause.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(AdapterActive.this.mContext, AdapterActive.this.mContext.getString(R.string.disabled_mobile), 1).show();
                }
            }
        });
        itemViewHolder.iconContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdapterActive.this.listener.onIconClicked(i);
            }
        });
        itemViewHolder.messageContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdapterActive.this.listener.onSingleClicked(i);
            }
        });
        itemViewHolder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                AdapterActive.this.listener.onRowLongClicked(i);
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

    public String download_speed(double d) {
        StringBuilder sb;
        if (d < 1024.0d) {
            try {
                sb = new StringBuilder();
                sb.append(Double.parseDouble(this.decimalFormat.format(d)));
                sb.append(" KB/s");
            } catch (Exception e) {
                e.printStackTrace();
                return "0 KB/s";
            }
        } else {
            sb = new StringBuilder();
            sb.append(Double.parseDouble(this.decimalFormat.format(d * 0.001d)));
            sb.append(" MB/s");
        }
        return sb.toString();
    }

    private void populateItemRows(ItemViewHolder itemViewHolder, int i) {
        DownloadingItem downloadingItem = this.ActiveList.get(i);
        itemViewHolder.name.setText(downloadingItem.getName());
        itemViewHolder.itemView.setActivated(this.selectedItems.get(i, false));
        itemViewHolder.size.setText(FileSize(downloadingItem.getCurrentSize(), downloadingItem.getTotalSize()));
        int progress = downloadingItem.getProgress();
        itemViewHolder.speed.setText(download_speed((double) downloadingItem.getSpeed()));
        if (progress > 0) {
            itemViewHolder.percent.setText(ConvertToString(downloadingItem.getProgress()));
        }
        if (progress > 0) {
            ProgressBar progressBar = itemViewHolder.getProgressBar();
            if (progressBar.isIndeterminate()) {
                progressBar.setIndeterminate(false);
            }
            progressBar.setProgress(progress);
        }
        if (downloadingItem.getIsInPause() == 0) {
            itemViewHolder.resume.setVisibility(View.GONE);
            itemViewHolder.pause.setVisibility(View.VISIBLE);
            itemViewHolder.speed.setText(download_speed((double) downloadingItem.getSpeed()));
        } else {
            itemViewHolder.resume.setVisibility(View.VISIBLE);
            itemViewHolder.pause.setVisibility(View.GONE);
            itemViewHolder.speed.setText(this.mContext.getString(R.string.paused));
        }
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
        if (this.ActiveList.size() > 0) {
            return this.ActiveList.size();
        }
        return 0;
    }

    public long getItemId(int i) {
        return (long) this.ActiveList.get(i).getFileId();
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
        ItemViewHolder itemViewHolder = new ItemViewHolder(this, this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_active, viewGroup, false), (byte) 0);
        this.mContext = viewGroup.getContext();
        return itemViewHolder;
    }

    public void removeData(int i) {
        this.ActiveList.remove(i);
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
        DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new DiffCallback(this.ActiveList, list));
        this.ActiveList.clear();
        this.ActiveList.addAll(list);
        calculateDiff.dispatchUpdatesTo((RecyclerView.Adapter) this);
    }
}
