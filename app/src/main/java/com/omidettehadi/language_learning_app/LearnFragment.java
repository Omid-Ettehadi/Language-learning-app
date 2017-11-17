package com.omidettehadi.language_learning_app;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import com.omidettehadi.language_learning_app.FFT;

public class LearnFragment extends Fragment {

    Button btnRecord, btnStop, btnPlay;
    File file;
    AudioRecord AudioRecorded;
    AudioTrack AudioRecordedTrack;
    FFT AudioRecordedFFT;

    boolean recording;
    int sampleFreq = 44100;

    public LearnFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        btnRecord = (Button)view.findViewById(R.id.btnRecord);
        btnStop = (Button)view.findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnPlay = (Button)view.findViewById(R.id.btnPlay);
        btnPlay.setEnabled(false);


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
           public void onClick(View view) {
               btnRecord.setEnabled(true);
               btnStop.setEnabled(false);
               btnPlay.setEnabled(true);
               recording = false;
           }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnRecord.setEnabled(true);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);
                Play();
            }
        });


        return view;
    }

    private void StartRecording(){

        file = new File(getContext().getCacheDir().getAbsolutePath() + File.separator , "Recording.pcm");

        try {
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioData = new short[minBufferSize];
            AudioRecorded = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            // Filters for audio
            AcousticEchoCanceler.create(AudioRecorded.getAudioSessionId());
            NoiseSuppressor.create(AudioRecorded.getAudioSessionId());
            AutomaticGainControl.create(AudioRecorded.getAudioSessionId());

            AudioRecorded.startRecording();

            while(recording){
                int numberOfShort = AudioRecorded.read(audioData, 0, minBufferSize);
                for(int i = 0; i < numberOfShort; i++){
                    dataOutputStream.writeShort(audioData[i]);
                }
            }

            AudioRecorded.stop();
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void Play() {
        file = new File(getContext().getCacheDir().getAbsolutePath() + File.separator , "Recording.pcm");

        int shortSizeInBytes = Short.SIZE/Byte.SIZE;
        int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);
        short[] audioData = new short[bufferSizeInBytes];

        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
            while(dataInputStream.available() > 0){
                audioData[i] = dataInputStream.readShort();
                i++;
            }

            dataInputStream.close();

            AudioRecordedTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM);

            AudioRecordedTrack.play();
            AudioRecordedTrack.write(audioData, 0, bufferSizeInBytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FFT getFFT(){
        file = new File(getContext().getCacheDir().getAbsolutePath() + File.separator , "Recording.pcm");

        int shortSizeInBytes = Short.SIZE/Byte.SIZE;
        int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);
        short[] audioData = new short[bufferSizeInBytes];
        short[] zeros = new short[bufferSizeInBytes];

        for (int i = 0 ; i < bufferSizeInBytes ; i++){
            audioData[i] = AudioRecorded.[i];
            zeros[i] = 0;
        }

        AudioRecordedFFT.fft(audioData,zeros);

        return AudioRecordedFFT;
    }
}