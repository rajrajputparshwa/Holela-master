package com.holela.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.holela.Fragment.VideoRecorder;
import com.holela.R;

import java.io.File;

public class VideoPreview extends AppCompatActivity {


    TextView cancel,postvideo;
    VideoView videoView ;
    String myvideo;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),CameraViewpager.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);

        cancel = (TextView) findViewById(R.id.cancel);
        postvideo = (TextView) findViewById(R.id.postee);
        videoView = (VideoView) findViewById(R.id.postvideo);



        File file = new File(VideoRecorder.path);
        myvideo = file.getAbsolutePath();


        videoView.setVideoPath(VideoRecorder.path);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.e("Video","Error");
                return true;
            }
        });










    }
}
