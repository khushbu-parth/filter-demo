package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes4.dex */
public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.SavedHolder> {
    file_type File_type;
    private Context context;
    private List<File> list;

    public DownloadsAdapter(List<File> list, Context context, file_type file_typeVar) {
        this.list = list;
        this.context = context;
        this.File_type = file_typeVar;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public SavedHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SavedHolder(LayoutInflater.from(this.context).inflate(R.layout.download_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SavedHolder savedHolder, final int i) {
        savedHolder.name.setText(this.list.get(i).getName());
        Uri fromFile = Uri.fromFile(this.list.get(i).getAbsoluteFile());
        if (this.File_type == file_type.AUDIO) {
            fromFile = Uri.parse("android.resource://" + this.context.getApplicationContext().getPackageName() + "/drawable/thumbnil_no_preview");
        }
        Glide.with(this.context).asBitmap().load(fromFile).into(savedHolder.thumbnail);
        savedHolder.thumbnail.setOnClickListener(new View.OnClickListener() { // from class: com.thntech.cast68.screen.tab.webcast.DownloadsAdapter.1
            @SuppressLint("WrongConstant")
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                Context context = DownloadsAdapter.this.context;
                Uri uriForFile = FileProvider.getUriForFile(context, DownloadsAdapter.this.context.getApplicationContext().getPackageName() + ".provider", (File) DownloadsAdapter.this.list.get(i));
                file_type file_typeVar = DownloadsAdapter.this.File_type;
                if (file_typeVar == file_type.VIDEO) {
                    intent.setDataAndType(uriForFile, "video/*");
                } else if (file_typeVar == file_type.IMAGE) {
                    intent.setDataAndType(uriForFile, "image/*");
                } else if (file_typeVar == file_type.AUDIO) {
                    intent.setDataAndType(uriForFile, "audio/*");
                }
                intent.addFlags(1);
                DownloadsAdapter.this.context.startActivity(intent);
            }
        });
        savedHolder.menu.setOnClickListener(new View.OnClickListener() { // from class: com.thntech.cast68.screen.tab.webcast.DownloadsAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DownloadsAdapter downloadsAdapter = DownloadsAdapter.this;
                downloadsAdapter.popUp(view, (File) downloadsAdapter.list.get(i));
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    /* loaded from: classes4.dex */
    public static class SavedHolder extends RecyclerView.ViewHolder {
        private ImageView menu;
        private TextView name;
        private ImageView thumbnail;

        public SavedHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.downloadCompletedName);
            this.thumbnail = (ImageView) view.findViewById(R.id.downloadThumnail);
            this.menu = (ImageView) view.findViewById(R.id.download_menu);
        }
    }

    public void popUp(View view, final File file) {
        PopupMenu popupMenu = new PopupMenu(this.context.getApplicationContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.download_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.thntech.cast68.screen.tab.webcast.DownloadsAdapter.3
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.download_delete) {
                    new AlertDialog.Builder(DownloadsAdapter.this.context).setMessage(DownloadsAdapter.this.context.getString(R.string.suretodelete)).setPositiveButton(DownloadsAdapter.this.context.getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.thntech.cast68.screen.tab.webcast.DownloadsAdapter.3.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (file.delete()) {
                                DownloadsAdapter.this.updateList();
                            }
                        }
                    }).setNegativeButton(DownloadsAdapter.this.context.getString(R.string.no), new DialogInterface.OnClickListener() { // from class: com.thntech.cast68.screen.tab.webcast.DownloadsAdapter.3.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                    return true;
                } else if (itemId != R.id.download_share) {
                    return onMenuItemClick(menuItem);
                } else {
                    File file2 = file;
                    Uri uriForFile = FileProvider.getUriForFile(DownloadsAdapter.this.context, "com.secure.fast.video.browser.downloader.provider", file2);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType(URLConnection.guessContentTypeFromName(file2.getName()));
                    intent.putExtra("android.intent.extra.STREAM", uriForFile);
                    DownloadsAdapter.this.context.startActivity(Intent.createChooser(intent, DownloadsAdapter.this.context.getString(R.string.share_app)));
                    return true;
                }
            }
        });
        popupMenu.show();
    }

    public void updateList() {
        File file = new File(SettingsManager.DOWNLOAD_FOLDER_VIDEO);
        if (file.exists()) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(file.listFiles()));
            this.list = arrayList;
            notifyDataSetChanged();
        }
    }
}
