package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

/* loaded from: classes4.dex */
public class video_player extends AppCompatDialogFragment {
    private TextView mBufferingTextView;
    Context mContext;
    private int mCurrentPosition = 0;
    private VideoView mVideoView;
    private downloadable_resource_model model;

    public video_player(downloadable_resource_model downloadable_resource_modelVar) {
        this.model = downloadable_resource_modelVar;
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.video_player, (ViewGroup) null);
        this.mContext = getContext();
        builder.setView(inflate).setPositiveButton(this.mContext.getString(R.string.Close), new DialogInterface.OnClickListener() { // from class: com.thntech.cast68.screen.tab.webcast.video_player.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        this.mVideoView = (VideoView) inflate.findViewById(R.id.videoview);
        this.mBufferingTextView = (TextView) inflate.findViewById(R.id.buffering_textview);
        if (bundle != null) {
            this.mCurrentPosition = bundle.getInt("play_time");
        }
        MediaController mediaController = new MediaController(inflate.getContext());
        mediaController.setMediaPlayer(this.mVideoView);
        this.mVideoView.setMediaController(mediaController);
        return builder.create();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < 24) {
            this.mVideoView.pause();
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("play_time", this.mVideoView.getCurrentPosition());
    }

    @SuppressLint("WrongConstant")
    private void initializePlayer() {
        this.mBufferingTextView.setVisibility(0);
        this.mVideoView.setVideoURI(getMedia(this.model.getURL()));
        this.mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.thntech.cast68.screen.tab.webcast.video_player.2
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                return false;
            }
        });
        this.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.thntech.cast68.screen.tab.webcast.video_player.3
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                video_player.this.mBufferingTextView.setVisibility(4);
                if (video_player.this.mCurrentPosition > 0) {
                    video_player.this.mVideoView.seekTo(video_player.this.mCurrentPosition);
                } else {
                    video_player.this.mVideoView.seekTo(1);
                }
                video_player.this.mVideoView.start();
            }
        });
        this.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.thntech.cast68.screen.tab.webcast.video_player.4
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                video_player.this.mVideoView.seekTo(0);
            }
        });
    }

    private void releasePlayer() {
        this.mVideoView.stopPlayback();
    }

    private Uri getMedia(String str) {
        if (URLUtil.isValidUrl(str)) {
            return Uri.parse(str);
        }
        return Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + str);
    }
}
