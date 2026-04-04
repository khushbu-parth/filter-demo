package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MyRecordingsActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallFetchHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.RecordingViewHolder;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class RecordingAdapter extends RecyclerView.Adapter<RecordingViewHolder> {
    private static SparseIntArray colorAvatarArray;
    private AudioClickListener audioClickListener;
    private boolean isFavList;
    private MyRecordingsActivity myRecordings;
    private List<RecordedCallModel> recordingModelArrayList;
    public RecordedCallModel selectedModel;
    public RecordingViewHolder selectedRecordingViewHolder;
    public SparseBooleanArray booleanArray = new SparseBooleanArray();
    private int actionPos = -1;

    
    public interface AudioClickListener {
        void onAudioFileClicked(RecordedCallModel recordedCallModel);
    }

    public RecordingAdapter(MyRecordingsActivity myRecordingsActivity, List<RecordedCallModel> list) {
        this.myRecordings = myRecordingsActivity;
        this.recordingModelArrayList = list;
        if (colorAvatarArray == null) {
            colorAvatarArray = new SparseIntArray();
        }
    }

    @Override 
    public int getItemCount() {
        return this.recordingModelArrayList.size();
    }

    public void onEditDone() {
        RecordedCallModel recordedCallModel;
        if (this.selectedRecordingViewHolder != null && (recordedCallModel = this.selectedModel) != null) {
            File file = recordedCallModel.getFile();
            File file2 = new File(file.getPath().replace(this.selectedModel.getFileName(), this.selectedModel.getFileName().replace(this.selectedRecordingViewHolder.recording_name.getText().toString(), this.selectedRecordingViewHolder.recordingNameEdit.getText().toString())));
            file.renameTo(file2);
            this.selectedModel.setFile(file2);
            this.selectedModel.setFileName(file2.getName());
            this.selectedRecordingViewHolder.recording_name.setText(this.selectedRecordingViewHolder.recordingNameEdit.getText());
            this.selectedRecordingViewHolder.recording_name.setVisibility(0);
            this.selectedRecordingViewHolder.recordingNameEdit.setVisibility(8);
            this.selectedModel.setName(this.selectedRecordingViewHolder.recording_name.getText().toString());
        }
        this.selectedModel = null;
        this.selectedRecordingViewHolder = null;
    }

    public void setAudioClickListener(AudioClickListener audioClickListener) {
        this.audioClickListener = audioClickListener;
    }

    public void setFavList(boolean z) {
        this.isFavList = z;
    }

    @Override 
    public void onBindViewHolder(final RecordingViewHolder recordingViewHolder, final int i) {
        recordingViewHolder.detailView.setVisibility(8);
        final RecordedCallModel recordedCallModel = this.recordingModelArrayList.get(i);
        recordingViewHolder.recording_name.setText(recordedCallModel.getName());
        recordingViewHolder.recording_date.setText(CallLogUtils.calculateTiming(new Date(Long.valueOf(new Date(recordedCallModel.getCreationTime() * 1000).getTime()).longValue())));
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        long minutes = timeUnit.toMinutes(recordedCallModel.getDuration());
        recordingViewHolder.recording_size.setText((minutes > 0 ? minutes + " min " : "") + timeUnit.toSeconds(recordedCallModel.getDuration() - ((60 * minutes) * 1000)) + " sec");
        if (this.booleanArray.get(i)) {
            recordingViewHolder.itemView.setBackgroundColor(BoloApplication.getApplication().getResources().getColor(R.color.colorAccent));
            recordingViewHolder.recording_name.setTextColor(-1);
            recordingViewHolder.recording_date.setTextColor(-1);
            recordingViewHolder.recording_size.setTextColor(-1);
        } else {
            recordingViewHolder.itemView.setBackgroundColor(BoloApplication.getApplication().getResources().getColor(R.color.colorPrimary));
            recordingViewHolder.recording_name.setTextColor(BoloApplication.getApplication().getResources().getColor(R.color.textColor));
            recordingViewHolder.recording_date.setTextColor(BoloApplication.getApplication().getResources().getColor(R.color.textColor));
            recordingViewHolder.recordingNameEdit.setTextColor(BoloApplication.getApplication().getResources().getColor(R.color.textColor));
            recordingViewHolder.recording_size.setTextColor(BoloApplication.getApplication().getResources().getColor(R.color.textColor));
        }
        if (recordedCallModel.getCallType().equals(Constants.CallTypeIncoming)) {
            recordingViewHolder.callType.setImageResource(CallLogUtils.getCallTypeIcon(1));
        } else {
            recordingViewHolder.callType.setImageResource(CallLogUtils.getCallTypeIcon(2));
        }
        final Context context = recordingViewHolder.favView.getContext();
        if (recordedCallModel.isFav()) {
            recordingViewHolder.favBtn.setColorFilter(context.getColor(R.color.colorAccent));
        } else {
            recordingViewHolder.favBtn.setColorFilter(context.getColor(R.color.gray1));
        }
        if (colorAvatarArray.get(i, -1) == -1) {
            colorAvatarArray.append(i, new Random().nextInt(CallLogUtils.colors.length));
        }
        if (recordingViewHolder.getColorCode() == -1) {
            recordingViewHolder.setColorCode(CallLogUtils.colors[colorAvatarArray.get(i)]);
        }
        recordingViewHolder.userImg.setImageResource(recordingViewHolder.getColorCode());
        recordingViewHolder.letter.setText(recordedCallModel.getFirstLetter());
        if (this.actionPos == i) {
            recordingViewHolder.moreToggleIcon.performClick();
        }
        recordingViewHolder.moreToggleIcon.setRotation(0.0f);
        recordingViewHolder.detailView.setVisibility(8);
        recordingViewHolder.favView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.1
            @Override 
            public void onClick(View view) {
                String replace;
                RecordingAdapter.this.actionPos = i;
                RecordedCallModel recordedCallModel2 = recordedCallModel;
                recordedCallModel2.setFav(!recordedCallModel2.isFav());
                if (recordedCallModel.isFav()) {
                    recordingViewHolder.favBtn.setColorFilter(context.getColor(R.color.colorAccent));
                    Toast.makeText(context, "Added to favorite", 0).show();
                } else {
                    recordingViewHolder.favBtn.setColorFilter(context.getColor(R.color.gray1));
                }
                File file = recordedCallModel.getFile();
                String path = file.getPath();
                if (recordedCallModel.isFav()) {
                    replace = path.replace("_favbol0", "_favbol1");
                } else {
                    replace = path.replace("_favbol1", "_favbol0");
                }
                File file2 = new File(replace);
                file.renameTo(file2);
                recordedCallModel.setFile(file2);
                recordedCallModel.setFileName(file2.getName());
                if (RecordingAdapter.this.isFavList && !recordedCallModel.isFav()) {
                    RecordingAdapter.this.recordingModelArrayList.remove(recordedCallModel);
                }
                RecordingAdapter.this.notifyDataSetChanged();
            }
        });
        recordingViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.2
            @Override 
            public void onClick(View view) {
                if (RecordingAdapter.this.audioClickListener != null) {
                    if (!RecordingAdapter.this.myRecordings.isContextMenuOpen) {
                        RecordingAdapter.this.audioClickListener.onAudioFileClicked((RecordedCallModel) RecordingAdapter.this.recordingModelArrayList.get(i));
                        return;
                    }
                    if (RecordingAdapter.this.booleanArray.get(i)) {
                        RecordingAdapter.this.booleanArray.append(i, false);
                    } else {
                        RecordingAdapter.this.booleanArray.append(i, true);
                    }
                    RecordingAdapter.this.notifyDataSetChanged();
                }
            }
        });
        recordingViewHolder.moreToggleIcon.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.3
            @Override 
            public void onClick(View view) {
                if (recordingViewHolder.detailView.getVisibility() == 8) {
                    recordingViewHolder.moreToggleIcon.animate().rotation(180.0f).start();
                    recordingViewHolder.detailView.setVisibility(0);
                } else {
                    recordingViewHolder.moreToggleIcon.animate().rotation(0.0f).start();
                    recordingViewHolder.detailView.setVisibility(8);
                }
                recordingViewHolder.cardView.setCardElevation(8.0f);
            }
        });
        recordingViewHolder.shareView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.4
            @Override 
            public void onClick(View view) {
                Uri parse = Uri.parse(recordedCallModel.getFile().getPath());
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("audio/*");
                intent.putExtra("android.intent.extra.STREAM", parse);
                intent.setFlags(268435456);
                RecordingAdapter.this.myRecordings.startActivity(Intent.createChooser(intent, "Share Sound File"));
            }
        });
        recordingViewHolder.deleteView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.5
            @Override 
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordingAdapter.this.myRecordings);
                builder.setMessage(R.string.delete).setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.5.2
                    @Override 
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        recordedCallModel.getFile().delete();
                        RecordedCallFetchHandler.getSharedInstance().onRecordingDeleted(recordedCallModel);
                        if (RecordingAdapter.this.myRecordings != null) {
                            RecordingAdapter.this.myRecordings.onFileDeleted();
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.5.1
                    @Override 
                    public void onClick(DialogInterface dialogInterface, int i2) {
                    }
                });
                builder.create().show();
            }
        });
        recordingViewHolder.editView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.6
            @Override 
            public void onClick(View view) {
                RecordingAdapter.this.selectedRecordingViewHolder = recordingViewHolder;
                RecordingAdapter.this.selectedModel = recordedCallModel;
                recordingViewHolder.recordingNameEdit.setVisibility(0);
                recordingViewHolder.recordingNameEdit.requestFocus();
                recordingViewHolder.recordingNameEdit.setText(recordingViewHolder.recording_name.getText());
                EditText editText = recordingViewHolder.recordingNameEdit;
                editText.setSelection(editText.getText().length());
                recordingViewHolder.recording_name.setVisibility(8);
            }
        });
    }

    @Override 
    public RecordingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecordingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_recordings_view, viewGroup, false));
    }
}
