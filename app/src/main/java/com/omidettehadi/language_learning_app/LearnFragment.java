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
import android.widget.TextView;

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
    TextView textView;
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

        btnRecord = view.findViewById(R.id.btnRecord);
        btnStop = view.findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnPlay.setEnabled(false);
        textView = view.findViewById(R.id.textView);


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

    private void StartRecording() {

        file = new File(getContext().getCacheDir().getAbsolutePath() + File.separator, "Recording.pcm");

        try {
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioDataRec = new short[minBufferSize];

            AudioRecorded = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            // Filters for audio
            AcousticEchoCanceler.create(AudioRecorded.getAudioSessionId());
            NoiseSuppressor.create(AudioRecorded.getAudioSessionId());
            AutomaticGainControl.create(AudioRecorded.getAudioSessionId());

            AudioRecorded.startRecording();

            while (recording) {
                int numberOfShort = AudioRecorded.read(audioDataRec, 0, minBufferSize);
                for (int i = 0; i < numberOfShort; i++) {
                    dataOutputStream.writeShort(audioDataRec[i]);
                }
            }

            AudioRecorded.stop();
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Play() {
        file = new File(getContext().getCacheDir().getAbsolutePath() + File.separator, "Recording.pcm");

        int shortSizeInBytes = Short.SIZE / Byte.SIZE;
        int bufferSizeInBytes = (int) (file.length() / shortSizeInBytes);
        short[] audioDataRec = new short[bufferSizeInBytes];
        double[] audioDataFFT = new double[bufferSizeInBytes];
        double[] zeros = new double[bufferSizeInBytes];
        double[] magnitudeFFT = new double[bufferSizeInBytes];

        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

/*            int i = 0;
            while (dataInputStream.available() > 0) {
                audioDataRec[i] = dataInputStream.readShort();
                i++;
            }*/
            while(dataInputStream.available() > 0) {
                SampleFFT(dataInputStream);
            }

            dataInputStream.close();

            AudioRecordedTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM);

            AudioRecordedTrack.play();
            AudioRecordedTrack.write(audioDataRec, 0, bufferSizeInBytes);


            /*
            for (int j = 0; j < bufferSizeInBytes - 1; j++) {
                audioDataFFT[j] = 100.0;
                zeros[j] = 100.0;
            }
            AudioRecordedFFT = new FFT(bufferSizeInBytes);
            AudioRecordedFFT.fft(audioDataFFT, zeros);

            // find magnitudes
            for(int j =0; j < audioDataRec.length ; j++){
                magnitudeFFT[j] = Math.sqrt(audioDataFFT[j] * audioDataFFT[j] + zeros[j] * zeros[j]);
            }

            // find largest peak in power spectrum
            double max_magnitude = magnitudeFFT[0];
            int max_index = 0;
            for (int j = 0; j < magnitudeFFT.length; ++j) {
                if (magnitudeFFT[j] > max_magnitude) {
                    max_magnitude = (int) magnitudeFFT[j];
                    max_index = j;
                }
            }
            double freq = max_index * 44100 / bufferSizeInBytes;
            textView.setText(Double.toString(freq));
            */

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //should read 1024 samples at a time?
    private double SampleFFT(DataInputStream dataInputStream){
        try {
            double[] dataRec = new double[1024];
            double[] zeros = new double[1024];
            double[] powerSpectrum = new double[512];
            double max = 0.0;
            int maxIndex = 0;
            double maxFreq = 0.0;
            int i = 0;
            while (i < 1024) {
                if (dataInputStream.available() > 0) {
                    dataRec[i] = (double)dataInputStream.readShort();
                } else {
                    dataRec[i] = (short) 0;
                }
                zeros[i] = 0;
                i++;
            }

            AudioRecordedFFT = new FFT(1024);
            AudioRecordedFFT.fft(dataRec, zeros);

            for(i = 0; i < 512; i++) {
                powerSpectrum[i] = (dataRec[512+i] * dataRec[512+i]) + (zeros[512+i] * zeros[512+i]);
                if(powerSpectrum[i] > max) {
                    max = powerSpectrum[i];
                    maxIndex = i;
                }
            }
            maxFreq = (maxIndex * sampleFreq)/2;

            return maxFreq;






        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0.0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0.0;
        }

    }
}
