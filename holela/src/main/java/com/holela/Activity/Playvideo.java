package com.holela.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.holela.R;

public class Playvideo extends AppCompatActivity {


    VideoView videoView;
    String value;
    private MediaController mediaControls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            value = extras.getString("video");
        }

        if (mediaControls == null) {
            mediaControls = new MediaController(Playvideo.this);
        }

        videoView= (VideoView) findViewById(R.id.myplay);
        videoView.setMediaController(mediaControls);

        videoView.setVideoURI(Uri.parse(value));

        videoView.start();
    }
}
