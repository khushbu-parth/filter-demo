package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBBookmarkHelper;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.design.FlipAnimator;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.listener.DiffCallbackBookmarks;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.BookmarkItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.ColorGenerator;

public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int currentSelectedIndex = -1;
    private final SparseBooleanArray animationItemsIndex;
    private final List<BookmarkItem> bookmarkItem;
    private final ColorGenerator generator = ColorGenerator.MATERIAL;
    public final BMAdapterListener listener;
    private Context mContext;
    private boolean reverseAllAnimations = false;
    private final SparseBooleanArray selectedItems;

    public interface BMAdapterListener {
        void onIconClicked(int i);

        void onRowLongClicked(int i);

        void onSingleClicked(int i);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final RelativeLayout iconBack;
        public final RelativeLayout iconContainer;
        public final RelativeLayout iconFront;
        public final ImageView imgProfile;
        public final LinearLayout main_lin;
        public final LinearLayout messageContainer;
        public final TextView name;
        public final TextView url;

        private ItemViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.name);
            this.url = (TextView) view.findViewById(R.id.url);
            this.iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            this.iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            this.imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            this.iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            this.messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            this.main_lin = (LinearLayout) view.findViewById(R.id.main_lin);
            view.setOnLongClickListener(this);
        }

        ItemViewHolder(BookmarkAdapter bookmarkAdapter, BookmarkAdapter bookmarkAdapter2, View view, byte b) {
            this(view);
        }

        public boolean onLongClick(View view) {
            BookmarkAdapter.this.listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(0);
            return true;
        }
    }

    public BookmarkAdapter(Context context, List<BookmarkItem> list, BMAdapterListener bMAdapterListener) {
        this.mContext = context;
        this.bookmarkItem = list;
        this.listener = bMAdapterListener;
        this.selectedItems = new SparseBooleanArray();
        this.animationItemsIndex = new SparseBooleanArray();
    }

    private void applyClickEvents(ItemViewHolder itemViewHolder, final int i) {
        itemViewHolder.main_lin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BookmarkAdapter.this.listener.onSingleClicked(i);
            }
        });
        itemViewHolder.main_lin.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                BookmarkAdapter.this.listener.onIconClicked(i);
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
        BookmarkItem bookmarkItem2 = this.bookmarkItem.get(i);
        String name = bookmarkItem2.getName();
        String favicon = bookmarkItem2.getFavicon();
        itemViewHolder.itemView.setTag(Long.valueOf((long) bookmarkItem2.getId()));
        itemViewHolder.name.setText(name);
        itemViewHolder.itemView.setActivated(this.selectedItems.get(i, false));
        if (!favicon.isEmpty()) {
            String file = this.mContext.getFilesDir().toString();
            File file2 = new File(file + "/.thumbs");
            if (new File(file2, favicon).exists()) {
                itemViewHolder.imgProfile.setImageBitmap(BitmapFactory.decodeFile(file2 + "/" + favicon));
            } else if (favicon.contains("drawable/") || favicon.contains("mipmap/")) {
                itemViewHolder.imgProfile.setImageResource(this.mContext.getResources().getIdentifier(favicon, (String) null, this.mContext.getPackageName()));
            }
            applyIconAnimation(itemViewHolder, i);
            applyClickEvents(itemViewHolder, i);
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
        return this.bookmarkItem.size();
    }

    public long getItemId(int i) {
        return (long) this.bookmarkItem.get(i).getId();
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
        ItemViewHolder itemViewHolder = new ItemViewHolder(this, this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_bookmark, viewGroup, false), (byte) 0);
        this.mContext = viewGroup.getContext();
        return itemViewHolder;
    }

    public void removeData(int i) {
        int i2 = this.bookmarkItem.get(i).getdelete();
        String url = this.bookmarkItem.get(i).getUrl();
        if (i2 == 1) {
            this.bookmarkItem.remove(i);
            try {
                DBBookmarkHelper.getInstance(this.mContext).DeleteBookmark(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void updateData(List<BookmarkItem> list) {
        DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new DiffCallbackBookmarks(this.bookmarkItem, list));
        this.bookmarkItem.clear();
        this.bookmarkItem.addAll(list);
        calculateDiff.dispatchUpdatesTo((RecyclerView.Adapter) this);
    }
}
