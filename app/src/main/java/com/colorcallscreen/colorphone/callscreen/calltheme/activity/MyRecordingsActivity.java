package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallFetchHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.helpers.MediaPlayerController;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MyRecordingsActivity extends BaseActivity implements MediaPlayerController.MediaPlayerCallback, RecordingAdapter.AudioClickListener {
    ImageView allRecording;
    private LinearLayout audioPlayerLayout;
    private long chronometeTime_new;
    private TextView emptyListText;
    ImageView favRecording;
    private int mediaMax_new;
    private MediaPlayerController mediaPlayerController;
    private int mediaPos_new;
    private Chronometer mediaTimerReverse;
    private RecyclerView my_recording_recycler_view;
    private String name;
    private SeekBar playerSeekBar;
    private ProgressBar progressBar;
    private RecordingAdapter recordingAdapter;
    private TextView timePlayer;
    private String filePath = null;
    private TextView boldTextView = null;
    private ImageButton btPlayAudio = null;
    private MediaPlayer mediaPlayer = null;
    private Handler handler = new Handler();
    public boolean isContextMenuOpen = false;
    private boolean isFavTabOpened = false;
    private Runnable moveSeekBarThread = new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.MyRecordingsActivity.1
        @Override 
        public void run() {
            if (MyRecordingsActivity.this.mediaPlayer == null || !MyRecordingsActivity.this.mediaPlayer.isPlaying()) {
                return;
            }
            MyRecordingsActivity myRecordingsActivity = MyRecordingsActivity.this;
            myRecordingsActivity.mediaPos_new = myRecordingsActivity.mediaPlayer.getCurrentPosition();
            MyRecordingsActivity myRecordingsActivity2 = MyRecordingsActivity.this;
            myRecordingsActivity2.mediaMax_new = myRecordingsActivity2.mediaPlayer.getDuration();
            MyRecordingsActivity.this.playerSeekBar.setMax(MyRecordingsActivity.this.mediaMax_new);
            MyRecordingsActivity.this.playerSeekBar.setProgress(MyRecordingsActivity.this.mediaPos_new);
            MyRecordingsActivity.this.handler.postDelayed(this, 100L);
        }
    };
    private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.MyRecordingsActivity.2
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (MyRecordingsActivity.this.mediaPlayer == null || !z) {
                return;
            }
            MyRecordingsActivity.this.mediaPlayer.seekTo(i);
            MyRecordingsActivity myRecordingsActivity = MyRecordingsActivity.this;
            myRecordingsActivity.chronometeTime_new = myRecordingsActivity.mediaPlayer.getCurrentPosition();
            MyRecordingsActivity.this.mediaTimerReverse.setBase(MyRecordingsActivity.this.chronometeTime_new + SystemClock.elapsedRealtime());
        }
    };

    private void audioPlayerId() {
        this.boldTextView = (TextView) findViewById(R.id.audioName);
        this.audioPlayerLayout = (LinearLayout) findViewById(R.id.audioPlayerLayout);
        this.btPlayAudio = (ImageButton) findViewById(R.id.btPlayAudio);
        SeekBar seekBar = (SeekBar) findViewById(R.id.playerSeekBar);
        this.playerSeekBar = seekBar;
        seekBar.setOnSeekBarChangeListener(this.seekBarChanged);
        this.mediaTimerReverse = (Chronometer) findViewById(R.id.mediaTimerReverse);
        this.audioPlayerLayout.setVisibility(8);
        this.timePlayer = (TextView) findViewById(R.id.time);
    }

    private void cleanSelectedAudioIfExist() {
        this.toolbar.getMenu().clear();
        this.isContextMenuOpen = false;
        this.recordingAdapter.booleanArray.clear();
        this.recordingAdapter.notifyDataSetChanged();
        this.toolbar.setTitle(R.string.my_recordings);
        this.isContextMenuOpen = false;
    }

    private void cleanUpMediaPlayer() {
        MediaPlayerController mediaPlayerController = this.mediaPlayerController;
        if (mediaPlayerController == null || !mediaPlayerController.isPlaying()) {
            return;
        }
        this.mediaPlayerController.forceStop();
    }

    public static List<String> getPath() {
        File[] listFiles;
        ArrayList arrayList = new ArrayList();
        File file = new File(Constants.THEME_DIRECTORY + "/BoloVoiceRecorder/Audios");
        if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (File file2 : listFiles) {
                arrayList.add(file2.getAbsolutePath());
            }
        }
        return arrayList;
    }

    private void inItComponent() {
        this.my_recording_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        setRecyclerData(false);
        this.btPlayAudio.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.MyRecordingsActivity.3
            @Override 
            public void onClick(View view) {
                if (MyRecordingsActivity.this.mediaPlayerController == null || MyRecordingsActivity.this.mediaPlayerController.isCompleted) {
                    return;
                }
                MyRecordingsActivity.this.mediaPlayerController.playPause();
            }
        });
    }

    public void setRecyclerData(final boolean z) {
        this.progressBar.setVisibility(0);
        this.emptyListText.setVisibility(8);
        RecordedCallFetchHandler.getSharedInstance().getRecordedCallsModel(z, new RecordedCallFetchHandler.OnRecordedCallFetched() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.MyRecordingsActivity.4
            @Override // com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallFetchHandler.OnRecordedCallFetched
            public void onRecordedCallFetched(List<RecordedCallModel> list) {
                MyRecordingsActivity.this.recordingAdapter = new RecordingAdapter(MyRecordingsActivity.this, list);
                MyRecordingsActivity.this.recordingAdapter.setAudioClickListener(MyRecordingsActivity.this);
                MyRecordingsActivity.this.recordingAdapter.setFavList(z);
                MyRecordingsActivity.this.my_recording_recycler_view.setAdapter(MyRecordingsActivity.this.recordingAdapter);
                MyRecordingsActivity.this.progressBar.setVisibility(8);
                if (list == null || list.isEmpty()) {
                    MyRecordingsActivity.this.emptyListText.setVisibility(0);
                }
            }
        });
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            View currentFocus = getCurrentFocus();
            if (currentFocus instanceof EditText) {
                Rect rect = new Rect();
                currentFocus.getGlobalVisibleRect(rect);
                if (!rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                    currentFocus.clearFocus();
                    ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    RecordingAdapter recordingAdapter = this.recordingAdapter;
                    if (recordingAdapter != null) {
                        recordingAdapter.onEditDone();
                    }
                }
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.adapter.RecordingAdapter.AudioClickListener
    public void onAudioFileClicked(RecordedCallModel recordedCallModel) {
        String str;
        String str2;
        this.audioPlayerLayout.setVisibility(0);
        MediaPlayerController mediaPlayerController = this.mediaPlayerController;
        if (mediaPlayerController != null) {
            mediaPlayerController.forceStop();
        }
        MediaPlayerController callback = MediaPlayerController.getPlayer().setAudioPath(recordedCallModel.getFile().getPath()).setChronometer(this.mediaTimerReverse, true).setSeekBarProgress(this.playerSeekBar).setCallback(this);
        this.mediaPlayerController = callback;
        callback.startPlayer();
        String name = recordedCallModel.getName();
        this.name = name;
        this.boldTextView.setText(name);
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        long minutes = timeUnit.toMinutes(recordedCallModel.getDuration());
        long seconds = timeUnit.toSeconds(recordedCallModel.getDuration() - ((60 * minutes) * 1000));
        if (minutes < 10) {
            str = "0" + minutes + ":";
        } else {
            str = minutes + ":";
        }
        if (seconds < 10) {
            str2 = "/" + str + "0" + seconds;
        } else {
            str2 = "/" + str + seconds;
        }
        this.timePlayer.setText(str2);
    }

    @Override 
    public void onBackPressed() {
        if (this.isContextMenuOpen) {
            cleanSelectedAudioIfExist();
        } else {
            super.onBackPressed();
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_recordings);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle("My Recordings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.allRecording = (ImageView) findViewById(R.id.allRecording);
        this.favRecording = (ImageView) findViewById(R.id.favRecording);
        this.my_recording_recycler_view = (RecyclerView) findViewById(R.id.my_recordings_recycler_view);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.emptyListText = (TextView) findViewById(R.id.empty_list_txt);
        this.filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.bolo/BoloVoiceRecorder/Audios/";
        audioPlayerId();
        inItComponent();
        findViewById(R.id.fl_allRecording).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.MyRecordingsActivity.5
            @Override 
            public void onClick(View view) {
                MyRecordingsActivity.this.isFavTabOpened = false;
                MyRecordingsActivity.this.setRecyclerData(false);
                MyRecordingsActivity.this.allRecording.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MyRecordingsActivity.this.getApplicationContext(), R.color.colorAccent)));
                MyRecordingsActivity.this.favRecording.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MyRecordingsActivity.this.getApplicationContext(), R.color.black)));
            }
        });
        findViewById(R.id.fl_favRecording).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.MyRecordingsActivity.6
            @Override 
            public void onClick(View view) {
                MyRecordingsActivity.this.isFavTabOpened = true;
                MyRecordingsActivity.this.setRecyclerData(true);
                MyRecordingsActivity.this.allRecording.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MyRecordingsActivity.this.getApplicationContext(), R.color.black)));
                MyRecordingsActivity.this.favRecording.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MyRecordingsActivity.this.getApplicationContext(), R.color.colorAccent)));
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        try {
            cleanUpMediaPlayer();
        } catch (Exception e) {
            Log.e("Media Player", "" + e);
        }
    }

    public void onFileDeleted() {
        setRecyclerData(this.isFavTabOpened);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.helpers.MediaPlayerController.MediaPlayerCallback
    public void onMediaPause() {
        this.btPlayAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.helpers.MediaPlayerController.MediaPlayerCallback
    public void onMediaPlaying(boolean z) {
        this.btPlayAudio.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.helpers.MediaPlayerController.MediaPlayerCallback
    public void onMediaStop(boolean z) {
        this.btPlayAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        try {
            cleanUpMediaPlayer();
        } catch (Exception e) {
            Log.e("Media Player", "" + e);
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        try {
            MediaPlayer mediaPlayer = this.mediaPlayer;
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                this.mediaPlayer.start();
            }
            this.mediaTimerReverse.start();
            this.btPlayAudio.setImageResource(R.drawable.ic_pause_black_24dp);
            this.mediaPos_new = this.mediaPlayer.getCurrentPosition();
            int duration = this.mediaPlayer.getDuration();
            this.mediaMax_new = duration;
            this.playerSeekBar.setMax(duration);
            this.playerSeekBar.setProgress(this.mediaPos_new);
        } catch (Exception e) {
            Log.e("Media Player", "" + e);
        }
    }

    public void shareAudio(String str, String str2) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setFlags(268435456);
            intent.setData(Uri.parse(str));
            intent.putExtra("android.intent.extra.TEXT", str2);
            intent.putExtra("android.intent.extra.STREAM", str);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "" + e, 0).show();
        }
    }
}
