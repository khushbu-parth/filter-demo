package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.whatsappdata;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemDecorations extends RecyclerView.ItemDecoration {
    private int mGridSize;
    private boolean mNeedLeftSpacing = false;
    private int mSizeGridSpacingPx;

    public ItemDecorations(int i, int i2) {
        this.mSizeGridSpacingPx = i;
        this.mGridSize = i2;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int i = this.mGridSize;
        int width = (recyclerView.getWidth() / this.mGridSize) - ((int) ((((float) recyclerView.getWidth()) - (((float) this.mSizeGridSpacingPx) * ((float) (i - 1)))) / ((float) i)));
        int viewAdapterPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (viewAdapterPosition < this.mGridSize) {
            rect.top = 0;
        } else {
            rect.top = this.mSizeGridSpacingPx;
        }
        int i2 = this.mGridSize;
        if (viewAdapterPosition % i2 == 0) {
            rect.left = 0;
            rect.right = width;
            this.mNeedLeftSpacing = true;
        } else if ((viewAdapterPosition + 1) % i2 == 0) {
            this.mNeedLeftSpacing = false;
            rect.right = 0;
            rect.left = width;
        } else if (this.mNeedLeftSpacing) {
            this.mNeedLeftSpacing = false;
            rect.left = this.mSizeGridSpacingPx - width;
            if ((viewAdapterPosition + 2) % this.mGridSize == 0) {
                rect.right = this.mSizeGridSpacingPx - width;
            } else {
                rect.right = this.mSizeGridSpacingPx / 2;
            }
        } else if ((viewAdapterPosition + 2) % i2 == 0) {
            this.mNeedLeftSpacing = false;
            rect.left = this.mSizeGridSpacingPx / 2;
            rect.right = this.mSizeGridSpacingPx - width;
        } else {
            this.mNeedLeftSpacing = false;
            rect.left = this.mSizeGridSpacingPx / 2;
            rect.right = this.mSizeGridSpacingPx / 2;
        }
        rect.bottom = 0;
    }
}
