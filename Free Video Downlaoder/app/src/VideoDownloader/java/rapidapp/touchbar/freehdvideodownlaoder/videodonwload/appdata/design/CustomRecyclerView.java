package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.design;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerView extends RecyclerView {
    private View mEmptyView;
    private final AdapterDataObserver observer = new AdapterDataObserver() {
        public void onChanged() {
            super.onChanged();
            CustomRecyclerView.this.initEmptyView();
        }

        public void onItemRangeInserted(int i, int i2) {
            super.onItemRangeInserted(i, i2);
            CustomRecyclerView.this.initEmptyView();
        }

        public void onItemRangeRemoved(int i, int i2) {
            super.onItemRangeRemoved(i, i2);
            CustomRecyclerView.this.initEmptyView();
        }
    };

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void initEmptyView() {
        View view = this.mEmptyView;
        if (view != null) {
            int i = 0;
            view.setVisibility((getAdapter() == null || getAdapter().getItemCount() == 0) ? View.VISIBLE : View.GONE);
            if (getAdapter() == null || getAdapter().getItemCount() == 0) {
                i = 8;
            }
            setVisibility(i);
        }
    }

    public void setAdapter(Adapter adapter) {
        Adapter adapter2 = getAdapter();
        super.setAdapter(adapter);
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(this.observer);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.observer);
        }
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
        initEmptyView();
    }
}
