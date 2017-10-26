package com.omidettehadi.language_learning_app;


import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class LearnFragment extends Fragment {

    MediaRecorder audioCapture = new MediaRecorder();
    String path = getContext().getCacheDir().getAbsolutePath() + File.separator + "tempAudio";
    File tempFile = new File(path);
    Button btnRecord, btnStop, btnPlay;
    public LearnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        // Inflate the layout for this fragment

        audioCapture.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioCapture.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioCapture.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioCapture.setOutputFile(tempFile);

        btnRecord = (Button)view.findViewById(R.id.btnRecord);
        btnStop = (Button)view.findViewById(R.id.btnStop);
        btnPlay = (Button)view.findViewById(R.id.btnPlay);

        return view;
    }

}
