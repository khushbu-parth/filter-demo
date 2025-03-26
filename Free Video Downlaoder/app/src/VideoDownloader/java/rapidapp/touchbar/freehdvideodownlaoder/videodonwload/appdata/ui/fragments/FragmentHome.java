package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBBookmarkHelper;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.listener.CustomListener;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.BookmarkItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.HelpActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.adapters.BookmarkAdapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.AndroidUtils;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.Constants;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.StoreUserData;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.whatsappdata.WhatsappActivity;


public class FragmentHome extends Fragment implements BookmarkAdapter.BMAdapterListener, MaterialSearchBar.OnSearchActionListener {
    public static ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private final List<BookmarkItem> bookmarkItems = new ArrayList();
    private DBBookmarkHelper dbBookmarkHelper;
    int i1;
    private final List<BookmarkItem> items = new ArrayList();
    public BookmarkAdapter mAdapter;
    public Context mContext;
    private CustomListener mListener;
    public RecyclerView mRecyclerView;
    public MaterialSearchBar searchBar;
    private StoreUserData storeUserData;

    public void onSearchStateChanged(boolean z) {
    }

    class ActionModeCallback implements ActionMode.Callback {
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        private ActionModeCallback() {
        }

        ActionModeCallback(FragmentHome fragmentHome, FragmentHome fragmentHome2) {
            this();
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() != R.id.action_delete) {
                return false;
            }
            FragmentHome.this.ShowDialog();
            return true;
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            FragmentHome.this.mAdapter.clearSelections();
            FragmentHome.actionMode = null;
            FragmentHome.this.mRecyclerView.post(new Runnable() {
                public void run() {
                    FragmentHome.this.mAdapter.resetAnimationIndex();
                }
            });
        }
    }

    private void AddBookmarks() {
        if (this.dbBookmarkHelper.getTotalBookmarks() == 0) {
            try {
                if (this.dbBookmarkHelper.getTotalBookmarks() == 0) {
                    Add_Items();
                    for (int i = 0; i < this.items.size(); i++) {
                        this.dbBookmarkHelper.AddBookmark(this.items.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initAdapter();
    }

    private void Add_Items() {
        this.items.add(new BookmarkItem(0, "Facebook", "https://facebook.com", "drawable/f", 0, 0));
        this.items.add(new BookmarkItem(0, "WhatsApp", "", "drawable/w", 1, 0));
        this.items.add(new BookmarkItem(0, "Instagram", "https://instagram.com", "drawable/i", 0, 0));
        this.items.add(new BookmarkItem(0, "Twitter", "https://twitter.com", "drawable/t", 0, 0));
        this.items.add(new BookmarkItem(0, "Dailymotion", "https://dailymotion.com", "drawable/d", 0, 0));
        this.items.add(new BookmarkItem(0, "Pinterest", "https://pinterest.com/", "drawable/p", 0, 0));
        this.items.add(new BookmarkItem(0, "Veoh", "https://veoh.com/", "drawable/v", 0, 0));
        this.items.add(new BookmarkItem(0, "Flickr", "https://flickr.com/", "drawable/fl", 0, 0));
    }

    public void Delete() {
        this.mAdapter.resetAnimationIndex();
        List<Integer> selectedItems = this.mAdapter.getSelectedItems();
        for (int size = selectedItems.size() - 1; size >= 0; size--) {
            if (this.bookmarkItems.get(selectedItems.get(size).intValue()).getdelete() == 1) {
                DeleteData(selectedItems.get(size).intValue());
                this.mAdapter.removeData(selectedItems.get(size).intValue());
            }
        }
        ActionMode actionMode2 = actionMode;
        if (actionMode2 != null) {
            actionMode2.finish();
            this.mRecyclerView.post(new Runnable() {
                public void run() {
                    FragmentHome.this.mAdapter.resetAnimationIndex();
                }
            });
        }
    }

    private void DeleteData(int i) {
        if (this.dbBookmarkHelper.bm_retrieve(this.bookmarkItems.get(i).getId()) != null) {
            try {
                this.dbBookmarkHelper.DeleteBookmarkById(this.bookmarkItems.get(i).getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void RefreshAdapter() {
        if (actionMode == null) {
            this.mAdapter.updateData(this.dbBookmarkHelper.getBookmarks());
        }
        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (FragmentHome.this.getActivity() != null) {
                        FragmentHome.this.RefreshAdapter();
                    }
                }
            }, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setMessage(getString(R.string.delbm));
        builder.setPositiveButton(getString(R.string.delete), (DialogInterface.OnClickListener) null);
        builder.setNegativeButton(getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        builder.setCancelable(true);
        final AlertDialog create = builder.create();
        create.setCancelable(false);
        create.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                Button button = create.getButton(-1);
                Button button2 = create.getButton(-2);
                button.setTextColor(FragmentHome.this.getResources().getColor(R.color.purple_500));
                button2.setTextColor(FragmentHome.this.getResources().getColor(R.color.material_cancel));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FragmentHome.this.Delete();
                        create.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (FragmentHome.actionMode != null) {
                            FragmentHome.actionMode.finish();
                        }
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
        this.bookmarkItems.addAll(DBBookmarkHelper.getInstance(this.mContext).getBookmarks());
        BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(this.mContext, this.bookmarkItems, this);
        this.mAdapter = bookmarkAdapter;
        this.mRecyclerView.setAdapter(bookmarkAdapter);
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

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (CustomListener) context;
        } catch (ClassCastException unused) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onButtonClicked(int i) {
        if (i != 1 && i == 3) {
            this.searchBar.disableSearch();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContext = layoutInflater.getContext();
        this.storeUserData = new StoreUserData(this.mContext);
        View inflate = layoutInflater.inflate(R.layout.fragmenthome, viewGroup, false);
        MaterialSearchBar materialSearchBar = (MaterialSearchBar) inflate.findViewById(R.id.searchBar);
        this.searchBar = materialSearchBar;
        materialSearchBar.setOnSearchActionListener(this);
        this.dbBookmarkHelper = new DBBookmarkHelper(this.mContext);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_home);
        this.mRecyclerView = recyclerView;
        recyclerView.setNestedScrollingEnabled(false);
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        this.actionModeCallback = new ActionModeCallback(this, this);
        AddBookmarks();

        ((MaterialButton) inflate.findViewById(R.id.help)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(FragmentHome.this.mContext, HelpActivity.class);
                intent.putExtra("key", 0);
                FragmentHome.this.startActivity(intent);
            }
        });

        this.searchBar.addTextChangeListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + FragmentHome.this.searchBar.getText());
            }
        });

        return inflate;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
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

    public void onSearchConfirmed(CharSequence charSequence) {
        this.searchBar.disableSearch();
        if (charSequence.length() > 0) {
            onclick(charSequence.toString());
        }
    }

    public void onSingleClicked(int i) {
        this.i1 = i;
        if (this.mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(i);
            return;
        }
        BookmarkItem bookmarkItem = this.bookmarkItems.get(i);
        int type = bookmarkItem.getType();
        String url = bookmarkItem.getUrl();
        if (type == 0) {
            onclick(url);
            return;
        }
        this.mContext.startActivity(new Intent(this.mContext, WhatsappActivity.class));
    }

    private void onclick(String str) {
        String lowerCase = str.toLowerCase();
        if (!AndroidUtils.isValidUrl(str)) {
            lowerCase = Constants.SearchQueries(this.storeUserData.getSettings().getEngine()) + lowerCase;
        } else if (!str.toLowerCase(Locale.ENGLISH).contains("http")) {
            lowerCase = "http://".concat(str);
        }
        this.mListener.onFragmentInteraction(lowerCase);
    }

    public void onResume() {
        super.onResume();
    }

}
