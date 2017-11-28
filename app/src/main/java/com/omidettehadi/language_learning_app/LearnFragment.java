package com.omidettehadi.language_learning_app;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
<<<<<<< HEAD
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.Equalizer;
=======
>>>>>>> OmidBranch
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.Toast;
=======
>>>>>>> OmidBranch

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
<<<<<<< HEAD
import java.nio.ShortBuffer;
=======

>>>>>>> OmidBranch

import static android.media.AudioFormat.CHANNEL_IN_MONO;

<<<<<<< HEAD
public class LearnFragment extends Fragment {

    Button btnRecord, btnStop, btnPlay;
    boolean recording;
    AudioRecord AudioRecorded;
    AudioTrack AudioRecordedTrack;
=======
>>>>>>> OmidBranch

    public LearnFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

<<<<<<< HEAD
        btnRecord = (Button)view.findViewById(R.id.btnRecord);
        btnStop = (Button)view.findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnPlay = (Button)view.findViewById(R.id.btnPlay);
        btnPlay.setEnabled(false);
=======
>>>>>>> OmidBranch

        btnRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnRecord.setEnabled(false);
                btnStop.setEnabled(true);
                btnPlay.setEnabled(false);

                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
<<<<<<< HEAD
           public void onClick(View view) {
               btnRecord.setEnabled(true);
               btnStop.setEnabled(false);
               btnPlay.setEnabled(true);
               recording = false;
           }
=======
>>>>>>> OmidBranch
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnRecord.setEnabled(true);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);
                Play();
            }
        });

<<<<<<< HEAD

        return view;
    }

    private void StartRecording(){

        File file = new File(getContext().getCacheDir().getAbsolutePath() + File.separator , "Recording.pcm");

        int sampleFreq = 44100;
=======
>>>>>>> OmidBranch

        try {
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
<<<<<<< HEAD
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioData = new short[minBufferSize];
            AudioRecorded = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
=======
>>>>>>> OmidBranch
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            // Filters for audio
            AcousticEchoCanceler.create(AudioRecorded.getAudioSessionId());
            NoiseSuppressor.create(AudioRecorded.getAudioSessionId());
            AutomaticGainControl.create(AudioRecorded.getAudioSessionId());

            AudioRecorded.startRecording();

<<<<<<< HEAD
            while(recording){
                int numberOfShort = AudioRecorded.read(audioData, 0, minBufferSize);
                for(int i = 0; i < numberOfShort; i++){
                    dataOutputStream.writeShort(audioData[i]);
=======
>>>>>>> OmidBranch
                }
            }

            AudioRecorded.stop();
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
<<<<<<< HEAD

    }

    private void Play() {
        File file = new File(getContext().getCacheDir().getAbsolutePath() + File.separator , "Recording.pcm");

        int shortSizeInBytes = Short.SIZE/Byte.SIZE;

        int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);
        short[] audioData = new short[bufferSizeInBytes];
=======
>>>>>>> OmidBranch

        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
<<<<<<< HEAD
            while(dataInputStream.available() > 0){
                audioData[i] = dataInputStream.readShort();
=======
>>>>>>> OmidBranch
                i++;
            }

            dataInputStream.close();

<<<<<<< HEAD
            int sampleFreq = 44100;

            AudioRecordedTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
=======
>>>>>>> OmidBranch
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM);

            AudioRecordedTrack.play();
<<<<<<< HEAD
            AudioRecordedTrack.write(audioData, 0, bufferSizeInBytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}