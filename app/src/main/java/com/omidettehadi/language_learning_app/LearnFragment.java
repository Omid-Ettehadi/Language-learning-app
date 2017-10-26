package com.omidettehadi.language_learning_app;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class LearnFragment extends Fragment {

    MediaRecorder audioCapture;
    String path;
    File tempFile;
    Button btnRecord, btnStop, btnPlay, btnStopPlay;
    MediaPlayer playback;

    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private String [] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public LearnFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        // Inflate the layout for this fragment

        requestPermissions(permissions, 200);


        audioCapture = new MediaRecorder();
        path = getContext().getCacheDir().getAbsolutePath() + File.separator + "tempAudio.3gp";
        tempFile = new File(path);

        audioCapture.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioCapture.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioCapture.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioCapture.setOutputFile(tempFile);

        btnRecord = (Button)view.findViewById(R.id.btnRecord);
        btnStop = (Button)view.findViewById(R.id.btnStop);
        btnPlay = (Button)view.findViewById(R.id.btnPlay);
        btnStopPlay = (Button)view.findViewById(R.id.btnStopPlay);

        btnStop.setEnabled(false);
        btnPlay.setEnabled(false);
        btnStopPlay.setEnabled(false);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    audioCapture.prepare();
                    audioCapture.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                btnRecord.setEnabled(false);
                btnStop.setEnabled(true);
                btnPlay.setEnabled(false);
                btnStopPlay.setEnabled(false);


            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
           public void onClick(View view) {
               audioCapture.stop();
               audioCapture.reset();
               audioCapture.setAudioSource(MediaRecorder.AudioSource.MIC);
               audioCapture.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
               audioCapture.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
               audioCapture.setOutputFile(tempFile);
               btnRecord.setEnabled(true);
               btnStop.setEnabled(false);
               btnPlay.setEnabled(true);
               btnStopPlay.setEnabled(false);
           }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnRecord.setEnabled(false);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(false);
                btnStopPlay.setEnabled(true);

                playback = new MediaPlayer();
                try {
                    playback.setDataSource(path);
                    playback.prepare();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                playback.start();
            }
        });
        btnStopPlay.setOnClickListener(new View.OnClickListener() {
           public void onClick(View view) {
               playback.stop();
               btnRecord.setEnabled(true);
               btnStop.setEnabled(false);
               btnPlay.setEnabled(true);
               btnStopPlay.setEnabled(false);
           }
        });

        return view;
    }

}
