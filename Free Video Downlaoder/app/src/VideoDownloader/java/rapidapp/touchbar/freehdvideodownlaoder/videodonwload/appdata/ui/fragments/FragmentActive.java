package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.WebApplication;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBDownloadManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.design.CustomRecyclerView;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.DownloadingItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.adapters.AdapterActive;


public class FragmentActive extends Fragment implements AdapterActive.ActiveListener {
    public static ActionMode actionMode;
    private final List<DownloadingItem> ActiveList = new ArrayList();
    private ActionModeCallback actionModeCallback;
    public AdapterActive mAdapter;
    private Context mContext;
    public CustomRecyclerView mRecyclerView;
    private View root;

    class ActionModeCallback implements ActionMode.Callback {
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        private ActionModeCallback() {
        }

        ActionModeCallback(FragmentActive fragmentActive, FragmentActive fragmentActive2) {
            this();
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() != R.id.action_delete) {
                return false;
            }
            FragmentActive.this.ShowDialog();
            return true;
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            FragmentActive.this.mAdapter.clearSelections();
            FragmentActive.actionMode = null;
            FragmentActive.this.mRecyclerView.post(new Runnable() {
                public void run() {
                    FragmentActive.this.mAdapter.resetAnimationIndex();
                }
            });
        }
    }

    public void Delete() {
        this.mAdapter.resetAnimationIndex();
        List<Integer> selectedItems = this.mAdapter.getSelectedItems();
        for (int size = selectedItems.size() - 1; size >= 0; size--) {
            DeleteActive(selectedItems.get(size).intValue());
            this.mAdapter.removeData(selectedItems.get(size).intValue());
        }
        ActionMode actionMode2 = actionMode;
        if (actionMode2 != null) {
            actionMode2.finish();
            this.mRecyclerView.post(new Runnable() {
                public void run() {
                    FragmentActive.this.mAdapter.resetAnimationIndex();
                }
            });
        }
    }

    private void DeleteActive(int i) {
        if (DBDownloadManager.getInstance(this.mContext).db_retrieve(this.ActiveList.get(i).fileId) != null) {
            try {
                FileDownloader.getImpl().clear(this.ActiveList.get(i).getDownloadId(), this.ActiveList.get(i).getLocalFilePath());
                if (new File(this.ActiveList.get(i).getLocalFilePath() + ".temp").exists()) {
                    Context context = this.mContext;
                    context.deleteFile(this.ActiveList.get(i).getLocalFilePath() + ".temp");
                }
                WebApplication.getInstance().downloadService.vnotificationManager.cancel(this.ActiveList.get(i).getDownloadId());
                DBDownloadManager.getInstance(this.mContext).deleteDownloadData(this.ActiveList.get(i).getDownloadId());
//                MainActivity.getInstance().show_badge();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void RefreshAdapter() {
        if (actionMode == null) {
            this.mAdapter.updateData(DBDownloadManager.getInstance(this.mContext).getDownloadingData());
        }
        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (FragmentActive.this.getActivity() != null) {
                        FragmentActive.this.RefreshAdapter();
                    }
                }
            }, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setMessage(getString(R.string.activedel));
        DialogInterface.OnClickListener onClickListener = null;
        builder.setPositiveButton(getString(R.string.delete), onClickListener);
        builder.setNegativeButton(getString(R.string.cancel), onClickListener);
        builder.setCancelable(true);
        final AlertDialog create = builder.create();
        create.setCancelable(false);
        create.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                Button button = create.getButton(-1);
                Button button2 = create.getButton(-2);
                button.setTextColor(FragmentActive.this.getResources().getColor(R.color.purple_500));
                button2.setTextColor(FragmentActive.this.getResources().getColor(R.color.material_cancel));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FragmentActive.this.Delete();
                        create.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        create.dismiss();
                    }
                });
            }
        });
        create.show();
    }

    private void enableActionMode(int i) {
        if (actionMode == null) {
            actionMode = MainActivity.getInstance().startSupportActionMode(this.actionModeCallback);
        }
        toggleSelection(i);
    }

    private void initAdapter() {
        this.ActiveList.addAll(DBDownloadManager.getInstance(this.mContext).getDownloadingData());
        AdapterActive adapterActive = new AdapterActive(this.mContext, this.ActiveList, this);
        this.mAdapter = adapterActive;
        this.mRecyclerView.setAdapter(adapterActive);
        this.mRecyclerView.setEmptyView(this.root.findViewById(R.id.empty_view));
        RefreshAdapter();
    }

    private void toggleSelection(int i) {
        this.mAdapter.toggleSelection(i);
        int selectedItemCount = this.mAdapter.getSelectedItemCount();
        if (selectedItemCount == 0) {
            actionMode.finish();
            return;
        }
        actionMode.setTitle((CharSequence) String.valueOf(selectedItemCount));
        actionMode.invalidate();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContext = layoutInflater.getContext();
        View inflate = layoutInflater.inflate(R.layout.fragmentactive, viewGroup, false);
        this.root = inflate;
        CustomRecyclerView customRecyclerView = (CustomRecyclerView) inflate.findViewById(R.id.recycler_active);
        this.mRecyclerView = customRecyclerView;
        customRecyclerView.setNestedScrollingEnabled(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.setItemAnimator((RecyclerView.ItemAnimator) null);
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(this.mContext, 1));
        this.actionModeCallback = new ActionModeCallback(this, this);
        initAdapter();
        this.mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int i, int i2) {
                super.onItemRangeInserted(i, i2);
                FragmentActive.this.mRecyclerView.smoothScrollToPosition(0);
            }
        });
        return this.root;
    }

    public void onIconClicked(int i) {
        if (actionMode == null) {
            actionMode = MainActivity.getInstance().startSupportActionMode(this.actionModeCallback);
        }
        toggleSelection(i);
    }

    public void onRowLongClicked(int i) {
        enableActionMode(i);
    }

    public void onSingleClicked(int i) {
        if (this.mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(i);
        }
    }
}
