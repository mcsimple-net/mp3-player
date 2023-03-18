package com.improve.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.os.Handler;

import java.util.logging.LogRecord;

public class MusicActivity extends AppCompatActivity {

    private Button buttonPlayPause, buttonNext, buttonPrevious;
    private TextView textViewProgress, textViewTotalTime, textViewFileNameMusic;
    private SeekBar musicSeekBar, volumeSeekBar;
    String title, filePath;
    int position;
    ArrayList<String> list;
    private MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;
    int totalTime;
    private Animation animation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        buttonPlayPause = findViewById(R.id.buttonPlayPause);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        textViewProgress = findViewById(R.id.textViewProgress);
        textViewTotalTime = findViewById(R.id.textViewTotalTime);
        textViewFileNameMusic = findViewById(R.id.textViewFileNameMusic);
        musicSeekBar = findViewById(R.id.musicSeekBar);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_animation);
        textViewFileNameMusic.setAnimation(animation);

        Intent intent = getIntent();

        title = getIntent().getStringExtra("title");
        filePath = getIntent().getStringExtra("filePath");
        position = getIntent().getIntExtra("position", 0);
        list = getIntent().getStringArrayListExtra("list");

        textViewFileNameMusic.setText(title);

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                buttonPlayPause.setBackgroundResource(R.drawable.play);
            }
        });

        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        buttonPrevious.setOnClickListener(view -> {
            mediaPlayer.reset();

            if (position == 0)
            {
                position = list.size() - 1;
            }
            else
            {
                position--;
            }

            String newFilePath = list.get(position);

            try {
                mediaPlayer.setDataSource(newFilePath);
                mediaPlayer.prepare();
                mediaPlayer.start();

                buttonPlayPause.setBackgroundResource(R.drawable.pause);
                String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/") + 1);
                textViewFileNameMusic.setText(newTitle);

                textViewFileNameMusic.clearAnimation();
                textViewFileNameMusic.startAnimation(animation);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        buttonNext.setOnClickListener(view -> {

            mediaPlayer.reset();

            if (position == list.size() - 1)
            {
                position = 0;
            }
            else
            {
                position++;
            }

            String newFilePath = list.get(position);

            try {
                mediaPlayer.setDataSource(newFilePath);
                mediaPlayer.prepare();
                mediaPlayer.start();

                buttonPlayPause.setBackgroundResource(R.drawable.pause);
                String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/") + 1);
                textViewFileNameMusic.setText(newTitle);

                textViewFileNameMusic.clearAnimation();
                textViewFileNameMusic.startAnimation(animation);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        buttonPlayPause.setOnClickListener(view -> {
           if (mediaPlayer.isPlaying())
           {
               mediaPlayer.pause();
               buttonPlayPause.setBackgroundResource(R.drawable.play);
           }
           else
           {
               mediaPlayer.start();
               buttonPlayPause.setBackgroundResource(R.drawable.pause);
           }
        });

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    volumeSeekBar.setProgress(progress);
                    float volumeLevel = progress / 100.0f;
                    mediaPlayer.setVolume(volumeLevel, volumeLevel);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        musicSeekBar.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    musicSeekBar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                totalTime = mediaPlayer.getDuration();
                musicSeekBar.setMax(totalTime);

                int currentPosition = mediaPlayer.getCurrentPosition();
                musicSeekBar.setProgress(currentPosition);
                handler.postDelayed(runnable, 1000);

                String elapsedTime = createTimeLable(currentPosition);
                String lastTime = createTimeLable(totalTime);

                textViewProgress.setText(elapsedTime);
                textViewTotalTime.setText(lastTime);

                if (elapsedTime.equals(lastTime))
                {
                    mediaPlayer.reset();

                    if (position == list.size() - 1)
                    {
                        position = 0;
                    }
                    else
                    {
                        position++;
                    }

                    String newFilePath = list.get(position);

                    try {
                        mediaPlayer.setDataSource(newFilePath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        buttonPlayPause.setBackgroundResource(R.drawable.pause);
                        String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/") + 1);
                        textViewFileNameMusic.setText(newTitle);

                        textViewFileNameMusic.clearAnimation();
                        textViewFileNameMusic.startAnimation(animation);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        };

        handler.post(runnable);

    }

    public String createTimeLable(int currentPosition)
    {
        String timeLablel;
        int minute, second;

        minute = currentPosition / 1000 / 60;
        second = currentPosition / 1000 % 60;

        if (second < 10)
        {
            timeLablel = minute + ":0" + second;
        }
        else
        {
            timeLablel = minute + ":" + second;
        }

        return timeLablel;
    }
}