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
import java.lang.Math;

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

                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                DataInputStream datainputStream = new DataInputStream(inputStream);
                double[] answer = SampleFFT(datainputStream);
                String text = answer[0]+ " - " +answer[1]+ " - " + answer[2];
                textView.setText(text);
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

    //should read 1024 samples at a time?
    private double[] SampleFFT(DataInputStream dataInputStream){
        try {
            double[] dataRec = new double[8192];
            double[] zeros = new double[8192];
            double[] powerSpectrum = new double[8192];
            double max = 0.0;
            int[] maxIndex = {0, 0, 0};
            double[] maxFreq = {0.0, 0.0, 0.0};
            int i = 0;
            while (i < 8192) {
                if (dataInputStream.available() > 0) {
                    dataRec[i] = (double)dataInputStream.readShort();
                } else {
                    dataRec[i] = (short) 0;
                }
                zeros[i] = 0;
                i++;
            }

            AudioRecordedFFT = new FFT(8192);
            AudioRecordedFFT.fft(dataRec, zeros);

            //these loops start at 1 and end at 4094 because we don't check the first and last frequencies
            for(i = 1; i < 4095; i++) {
                powerSpectrum[i] = (dataRec[4096+i] * dataRec[4096+i]) + (zeros[4096+i] * zeros[4096+i]);
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1])) {
                    max = powerSpectrum[i];
                    maxIndex[0] = i;
                }
            }
            max = 0.0;
            for(i = 1; i < 4095; i++) {
                powerSpectrum[i] = (dataRec[4096+i] * dataRec[4096+i]) + (zeros[4096+i] * zeros[4096+i]);
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1]) && (Math.abs(i - maxIndex[0]) > 2)) {
                    max = powerSpectrum[i];
                    maxIndex[1] = i;
                }
            }
            max = 0.0;
            for(i = 1; i < 4095; i++) {
                powerSpectrum[i] = (dataRec[4096+i] * dataRec[4096+i]) + (zeros[4096+i] * zeros[4096+i]);
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1]) && (Math.abs(i - maxIndex[0]) > 2) && (Math.abs(i - maxIndex[1]) > 2)) {
                    max = powerSpectrum[i];
                    maxIndex[2] = i;
                }
            }
            maxFreq[0] = (maxIndex[0] * sampleFreq)/(2 * 4096);
            maxFreq[1] = (maxIndex[1] * sampleFreq)/(2 * 4096);
            maxFreq[2] = (maxIndex[2] * sampleFreq)/(2 * 4096);

            return maxFreq;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    class IPA {
        String character;
        String[] vowel_character;
        int[] vowel_freq;
    };

    private void feedback(double[][] InputFreq){
        String word = "Hi";
        String [] IPAs = {"h","ʌ","ɪ"};

        IPA[] LookedUp = new IPA[10];

        int j = 0;
        for ( int i = 0; i<IPAs.length; i++) {
            if(IPAs[i] == "iː") {
                LookedUp[j].character = "i:";
                LookedUp[j].vowel_character = new String[]{"iː", "Close", "Front", "Long"};
                LookedUp[j].vowel_freq = new int[]{280, 2620, 3380};
                j++;
            }
            if (IPAs[i] == "ɪ") {
                LookedUp[j].character = "ɪ";
                LookedUp[j].vowel_character = new String[]{"ɪ", "Close", "Front", "Short"};
                LookedUp[j].vowel_freq = new int[]{360, 2220, 2960};
                j++;
            }
            if (IPAs[i] == "e") {
                LookedUp[j].character = "e";
                LookedUp[j].vowel_character = new String[]{"e", "Half-open", "Front", "Short"};
                LookedUp[j].vowel_freq = new int[]{600, 2060, 2840};
                j++;
            }
            if (IPAs[i] == "æ") {
                LookedUp[j].character = "æ";
                LookedUp[j].vowel_character = new String[]{"æ", "Open", "Front", "Short"};
                LookedUp[j].vowel_freq = new int[]{800, 1760, 2500};
                j++;
            }
            if (IPAs[i] == "ʌ") {
                LookedUp[j].character = "ʌ";
                LookedUp[j].vowel_character = new String[]{"ʌ", "Open", "Central", "Short"};
                LookedUp[j].vowel_freq = new int[]{760, 1320, 2500};
                j++;
            }
            if (IPAs[i] == "ɑː") {
                LookedUp[j].character = "ɑː";
                LookedUp[j].vowel_character = new String[]{"ɑː", "Open", "Back", "Long"};
                LookedUp[j].vowel_freq = new int[]{740, 1180, 2640};
                j++;
            }
            if (IPAs[i] == "ɒ") {
                LookedUp[j].character = "ɒ";
                LookedUp[j].vowel_character = new String[]{"ɒ", "Open", "Back", "Short"};
                LookedUp[j].vowel_freq = new int[]{560, 920, 2560};
                j++;
            }
            if (IPAs[i] == "ɔː") {
                LookedUp[j].character = "ɔː";
                LookedUp[j].vowel_character = new String[]{"ɔː", "Half-open", "Back", "Long"};
                LookedUp[j].vowel_freq = new int[]{480, 760, 2620};
                j++;
            }
            if (IPAs[i] == "ʊ") {
                LookedUp[j].character = "ʊ";
                LookedUp[j].vowel_character = new String[]{"ʊ", "Close", "Back", "Short"};
                LookedUp[j].vowel_freq = new int[]{380, 940, 2300};
                j++;
            }
            if (IPAs[i] == "uː") {
                LookedUp[j].character = "uː";
                LookedUp[j].vowel_character = new String[]{"uː", "Close", "Back", "Long"};
                LookedUp[j].vowel_freq = new int[]{320, 920, 2200};
                j++;
            }
            if (IPAs[i] == "ɜː") {
                LookedUp[j].character = "ɜː";
                LookedUp[j].vowel_character = new String[]{"ɜː", "Half-open", "Central", "Long"};
                LookedUp[j].vowel_freq = new int[]{560, 1480, 2520};
                j++;
            }
            if (IPAs[i] == "ə") {
                LookedUp[j].character = "ə";
                LookedUp[j].vowel_character = new String[]{"ə", "Half-open", "Central", "Short"};
                LookedUp[j].vowel_freq = new int[]{500, 1500, 3000};
                j++;
            }
            else {

            }
        }

        /*boolean[] flag = new boolean[10];

        double ePerD = 0.1;
        double hThresh = 1 + (ePerD);
        double lThresh = 1 - (ePerD);
        for ( int i = 0; i<InputFreq.length ; i++){
            if( (lThresh * LookedUp[i].vowel_freq[0])  <= InputFreq[i][0] <= (hThresh * LookedUp[i].vowel_freq[0]) )

        }*/
    }
}
