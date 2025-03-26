package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.view.ActionMode;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.BuildConfig;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBDownloadManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.design.CustomRecyclerView;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.DownloadingItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.VideoPlayer;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.adapters.AdapterOffline;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utility.DownloadFileUtility;


public class FragmentOffline extends Fragment implements AdapterOffline.OfflineListener {
    private static final String ARGUMENT_NAME = "VIDEO_NAME";
    private static final String ARGUMENT_PATH = "VIDEO_PATH";
    public static ActionMode actionMode;
    private final List<DownloadingItem> OfflineList = new ArrayList();
    private final ArrayList<Uri> ShareItems = new ArrayList<>();
    private ActionModeCallback actionModeCallback;
    private DBDownloadManager dbDownloadManager;
    public AdapterOffline mAdapter;
    private Context mContext;
    public CustomRecyclerView mRecyclerView;
    private View root;

    class ActionModeCallback implements ActionMode.Callback {
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        private ActionModeCallback() {
        }

        ActionModeCallback(FragmentOffline fragmentOffline, FragmentOffline fragmentOffline2) {
            this();
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_delete) {
                FragmentOffline.this.ShowDialog();
                return true;
            } else if (itemId != R.id.action_share) {
                return false;
            } else {
                FragmentOffline.this.Share();
                return true;
            }
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menuoffline, menu);
            return true;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            FragmentOffline.this.mAdapter.clearSelections();
            FragmentOffline.actionMode = null;
            FragmentOffline.this.mRecyclerView.post(new Runnable() {
                public void run() {
                    FragmentOffline.this.mAdapter.resetAnimationIndex();
                }
            });
        }
    }

    public void Delete() {
        this.mAdapter.resetAnimationIndex();
        List<Integer> selectedItems = this.mAdapter.getSelectedItems();
        for (int size = selectedItems.size() - 1; size >= 0; size--) {
            DeleteData(selectedItems.get(size).intValue());
            this.mAdapter.removeData(selectedItems.get(size).intValue());
        }
        ActionMode actionMode2 = actionMode;
        if (actionMode2 != null) {
            actionMode2.finish();
            this.mRecyclerView.post(new Runnable() {
                public void run() {
                    FragmentOffline.this.mAdapter.resetAnimationIndex();
                }
            });
        }
    }

    private void DeleteData(int i) {
        if (DBDownloadManager.getInstance(this.mContext).db_retrieve(this.OfflineList.get(i).fileId) != null) {
            try {
                DBDownloadManager.getInstance(this.mContext).delete_query(this.OfflineList.get(i).fileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void RefreshAdapter() {
        if (actionMode == null) {
            this.mAdapter.updateData(this.dbDownloadManager.getDownloadedData());
        }
        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (FragmentOffline.this.getActivity() != null) {
                        FragmentOffline.this.RefreshAdapter();
                    }
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Share() {
        this.ShareItems.clear();
        List<Integer> selectedItems = this.mAdapter.getSelectedItems();
        File file = new File(Environment.getExternalStorageDirectory(), "Video Downloader");
        try {
            for (int size = selectedItems.size() - 1; size >= 0; size--) {
                DownloadingItem db_retrieve = DBDownloadManager.getInstance(this.mContext).db_retrieve(this.OfflineList.get(selectedItems.get(size).intValue()).fileId);
                if (db_retrieve != null) {
                    File file2 = new File(file, db_retrieve.getName());
                    if (file2.exists()) {
                        this.ShareItems.add(FileProvider.getUriForFile(this.mContext, "com.mt.player.provider", file2));
                    }
                }
            }
            Intent intent = new Intent();
            intent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE, BuildConfig.APPLICATION_ID);
            intent.setAction("android.intent.action.SEND_MULTIPLE");
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.ShareItems);
            intent.setType("video/*|audio/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(this.mContext.getPackageManager()) != null) {
                intent = Intent.createChooser(intent, "Share to");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mAdapter.resetAnimationIndex();
        ActionMode actionMode2 = actionMode;
        if (actionMode2 != null) {
            actionMode2.finish();
        }
    }

    public void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setMessage(getString(R.string.deloff));
        builder.setPositiveButton(getString(R.string.delete), (DialogInterface.OnClickListener) null);
        builder.setNegativeButton(getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        builder.setCancelable(true);
        final AlertDialog create = builder.create();
        create.setCancelable(false);
        create.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                Button button = create.getButton(-1);
                Button button2 = create.getButton(-2);
                button.setTextColor(FragmentOffline.this.getResources().getColor(R.color.purple_500));
                button2.setTextColor(FragmentOffline.this.getResources().getColor(R.color.material_cancel));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FragmentOffline.this.Delete();
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
        this.OfflineList.addAll(this.dbDownloadManager.getDownloadedData());
        AdapterOffline adapterOffline = new AdapterOffline(this.mContext, this.OfflineList, this);
        this.mAdapter = adapterOffline;
        this.mRecyclerView.setAdapter(adapterOffline);
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
        View inflate = layoutInflater.inflate(R.layout.fragmentoffline, viewGroup, false);
        this.root = inflate;
        CustomRecyclerView customRecyclerView = (CustomRecyclerView) inflate.findViewById(R.id.recycler_offline);
        this.mRecyclerView = customRecyclerView;
        customRecyclerView.setNestedScrollingEnabled(false);
        this.dbDownloadManager = new DBDownloadManager(this.mContext);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(this.mContext, 1));
        this.actionModeCallback = new ActionModeCallback(this, this);
        initAdapter();
        this.mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int i, int i2) {
                super.onItemRangeInserted(i, i2);
                FragmentOffline.this.mRecyclerView.smoothScrollToPosition(0);
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
            return;
        }
        String name = this.OfflineList.get(i).getName();
        if (!new File(this.OfflineList.get(i).getLocalFilePath()).exists()) {
            MainActivity.getInstance().show_snack(getString(R.string.flnot));
        } else if (DownloadFileUtility.check_video_extension(name)) {
            Intent intent = new Intent(getActivity(), VideoPlayer.class);
            intent.putExtra(ARGUMENT_NAME, this.OfflineList.get(i).getName());
            intent.putExtra(ARGUMENT_PATH, this.OfflineList.get(i).getLocalFilePath());
            startActivity(intent);
        } else {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setDataAndType(Uri.parse(this.OfflineList.get(i).getLocalFilePath()), "audio/mpeg3");
            intent2.setType("audio/mp3g3");
            intent2.putExtra("android.intent.extra.STREAM", Uri.parse(this.OfflineList.get(i).getLocalFilePath()));
            this.mContext.startActivity(Intent.createChooser(intent2, "Play Using"));
        }
    }
}
