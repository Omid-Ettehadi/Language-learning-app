package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.BubbleFormatter;
import com.androidplot.xy.BubbleRenderer;
import com.androidplot.xy.BubbleSeries;
import com.androidplot.xy.XYPlot;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.lang.Math;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import static com.google.android.gms.internal.zzs.TAG;
import static com.omidettehadi.language_learning_app.MainActivity.e_1;
import static com.omidettehadi.language_learning_app.MainActivity.e_2;
import static com.omidettehadi.language_learning_app.MainActivity.word;
import static com.omidettehadi.language_learning_app.MainActivity.sampleFreq;
import static com.omidettehadi.language_learning_app.MainActivity.User_a_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_a_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_e_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_e_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_i_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_i_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_o_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_o_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_u_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_u_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_æ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_æ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɑ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɑ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɒ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɒ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɔ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɔ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ə_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ə_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɛ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɛ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɜ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɜ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɪ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɪ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ʊ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ʊ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ʌ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ʌ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɒ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɒ_2;

public class LearnFragment extends Fragment {

    // ----------------------------------------------------------------------------------Declaration
    // Items
    private EditText etInput;
    private Button btnSearch, btnMic, btnCapture, btnCam, btnRecord, btnStop, btnPlay;
    private TextView textView;
    private XYPlot plot;

    // Audio Recording
    private boolean recording;
    private File file;
    private AudioRecord AudioRecorded;
    private AudioTrack AudioRecordedTrack;
    private FFT AudioRecordedFFT;
    private double [][] user_recording_freq;
    IPA[] UsersRecordingIPA;

    // Camera
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private final int RequestCameraPermissionID = 1001;

    // Speech to Text
    private boolean IsListening;
    private SpeechRecognizer speechrecognizer;
    private Intent speechrecognizerIntent;

    // --------------------------------------------------------------------------------Global Values
    // Accuracy
    private double ePer = 0.5;
    private double hThresh = 1 + (ePer);
    private double lThresh = 1 - (ePer);

    // Searched Word
    private String IPAString = "";
    private String [] IPAs;
    private class IPA {
        String character;
        String[] vowel_character;
        double[] vowel_freq;
    }
    private IPA[] LookedUp;
    private int LookedUp_Length;
    private IPA[] mispronounced_vowel;
    private int[] mispronounced_vowel_index;

    // Pronunciation Status
    private boolean status = true;

    int resultcount = 0;
    String text = "";

    //-------------------------------------------------------------------------------------On Create
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        etInput = view.findViewById(R.id.etInput);
        etInput.setText(word);

        textView = view.findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        if (User_a_1 == 0 || User_a_2 == 0
                || User_e_1 == 0 || User_e_2 == 0
                || User_i_1 == 0 || User_i_2 == 0
                || User_o_1 == 0 || User_o_2 == 0
                || User_u_1 == 0 || User_u_2 == 0
                || User_æ_1 == 0 || User_æ_2 == 0
                || User_ɑ_1 == 0 || User_ɑ_2 == 0
                || User_ɒ_1 == 0 || User_ɒ_2 == 0
                || User_ɔ_1 == 0 || User_ɔ_2 == 0
                || User_ə_1 == 0 || User_ə_2 == 0
                || User_ɛ_1 == 0 || User_ɛ_2 == 0
                || User_ɜ_1 == 0 || User_ɜ_2 == 0
                || User_ɪ_1 == 0 || User_ɪ_2 == 0
                || User_ʊ_1 == 0 || User_ʊ_2 == 0
                || User_ʌ_1 == 0 || User_ʌ_2 == 0){
            textView.setText("You have to first set up your own set of frequencies!");
        } else {
            textView.setText("First you have to search for the word that you want to pronounce!");
        }

        cameraView = view.findViewById(R.id.surface_view);
        cameraView.setVisibility(View.INVISIBLE);

        btnSearch = view.findViewById(R.id.btnSearch);
        btnMic = view.findViewById(R.id.btnMic);
        btnCam = view.findViewById(R.id.btnCam);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnCapture.setVisibility(View.INVISIBLE);
        btnRecord = view.findViewById(R.id.btnRecord);
        btnRecord.setEnabled(false);
        btnStop = view.findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnPlay.setEnabled(false);

        plot = view.findViewById(R.id.xyPlot);
        plot.setDomainBoundaries(0,1000, BoundaryMode.AUTO);
        plot.setRangeBoundaries(0,3000,BoundaryMode.AUTO);
        plot.setVisibility(View.INVISIBLE);

        // Call the camera and the voice recording for Speech to Text
        camera();
        speech_to_text();

        //-----------------------------------------------------------------------------------Buttons
        // See if Search Button is pressed
        // Run Search for the word in the input EditText
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text = "";
                plot.clear();
                plot.setVisibility(View.INVISIBLE);

                // Check if something is entered
                if (etInput.getText().toString().length() != 0) {
                    textView.setText(" Loading... ");
                    word = etInput.getText().toString();
                    IPAString = null;

                    new CallbackTask().execute(dictionaryEntries());

                    if (IPAString == null) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (IPAString == null) {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            textView.setText("Now try pronouncing the word!");
                                            btnRecord.setEnabled(true);
                                        }
                                    }, 1000);
                                } else {
                                    textView.setText("Now try pronouncing the word!");
                                    btnRecord.setEnabled(true);
                                }
                            }
                        }, 2000);
                    }
                }
            }
        });

        // See if Listen Button is pressed
        // Run Speech Recognition
        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pronounce the word
                if (!IsListening) {
                    speechrecognizer.startListening(speechrecognizerIntent);
                }
            }
        });

        // See if Camera Button is pressed
        // Run Camera
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setVisibility(View.INVISIBLE);
                btnSearch.setVisibility(View.INVISIBLE);
                btnMic.setVisibility(View.INVISIBLE);
                btnCapture.setVisibility(View.VISIBLE);
                btnCam.setVisibility(View.INVISIBLE);
                cameraView.setVisibility(View.VISIBLE);
            }
        });

        // See if Capture Button is pressed
        // Run Text Recognition from Camera
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                btnMic.setVisibility(View.VISIBLE);
                btnCapture.setVisibility(View.INVISIBLE);
                btnCam.setVisibility(View.VISIBLE);
                cameraView.setVisibility(View.INVISIBLE);
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                text = "";
                plot.clear();
                plot.setVisibility(View.INVISIBLE);
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

                LookedUp = new IPA[IPAs.length];
                status = true;

                for (int i = 0; i < IPAs.length; i++){
                    text += IPAs[i].toString();
                }
                text += "\n" + "\n";

                // Retrieve data for the vowels in the searched word
                LookedUp_Length = 0;
                for ( int k = 0; k<IPAs.length; k++) {
                    LookedUp[LookedUp_Length] = vowel_characteristics_lookup(IPAs[k], null, 'v');
                    if (LookedUp[LookedUp_Length] != null){
                        LookedUp_Length++;
                    }
                }

                text += "Vowels you need to pronounce:" + "\n";
                for (int i = 0; i<LookedUp_Length ; i++){
                    if(i == LookedUp_Length-1){
                        text += LookedUp[i].character + "\n";
                    } else {
                        text += LookedUp[i].character + " - ";
                    }
                }

                int i = 0;
                user_recording_freq = new double[i][3];

                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                DataInputStream datainputStream = new DataInputStream(inputStream);
                try {
                    while (datainputStream.available() > 0){

                        double[][] temp = user_recording_freq;
                        user_recording_freq = new double[i+1][3];
                        for ( int j = 0; j<i; j++){
                            user_recording_freq[j] = temp[j];
                        }
                        user_recording_freq [i] = SampleFFT(datainputStream);
                        if(user_recording_freq[i][0] == 0.0 && user_recording_freq[i][1] == 0.0) {
                            i--; //cancel out this iteration so the next go through of the loop overwrites it.
                            //still print the text below just as a record.
                        } else {
                            i++;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*for (int j = 0; j < user_recording_freq.length ; j++){
                    text += "{" + user_recording_freq[j][0] + " - " + user_recording_freq[j][1] + "}" + "\n";
                } textView.setText(text);

                // Filtering anything above 1000 and below 175
                int n = 0;
                double[][] answer = new double[1][3];
                for (int j = 0; j < user_recording_freq.length; j++) {
                    if (user_recording_freq[j][0] > 175 && user_recording_freq[j][0] < 1000) {
                        double[][] temp = answer;
                        answer = new double[n + 1][3];
                        for (int m = 0; m < temp.length; m++) {
                            answer[m] = temp[m];
                        }
                        answer[n] = user_recording_freq[j];
                        n++;
                    }
                }
                user_recording_freq = answer;*/

                // Find vowels from User recordings
                i = 0;
                UsersRecordingIPA = new IPA[i];
                for(int m = 0; m < user_recording_freq.length; m++) {
                    IPA temp = vowel_characteristics_lookup(null, user_recording_freq[i], 'f');
                    if(temp != null){
                        IPA [] recover = UsersRecordingIPA;
                        UsersRecordingIPA = new IPA[i+1];
                        for ( int j = 0; j < recover.length; j++){
                            UsersRecordingIPA[j] = recover[j];
                        }
                        UsersRecordingIPA[i] = temp;
                        i++;
                    }
                }
                if ( UsersRecordingIPA.length == 0){
                    text += " We were not able to identify any vowels!";
                }

                /*for (int j = 0; j<UsersRecordingIPA.length ; j++){
                    text += UsersRecordingIPA[j].character + " - ";
                } textView.setText(text);*/


                i = 1;
                mispronounced_vowel_index = new int[0];
                mispronounced_vowel = new IPA[0];
                int y = 0;
                status = false;
                while( y < LookedUp_Length){
                    for(int x = 0; x < UsersRecordingIPA.length; x++ ){
                        Log.d(TAG, "onClick: here 1");
                        if(UsersRecordingIPA[x].character.equals(LookedUp[y].character)){
                            y++;
                            status = true;
                            Log.d(TAG, "onClick: here 2");
                        }
                    }
                    if(status == false) {
                        int[] temp = mispronounced_vowel_index;
                        IPA[] temp1 = mispronounced_vowel;
                        mispronounced_vowel_index = new int[i];
                        mispronounced_vowel = new IPA[i];
                        for (int f = 0; f < temp.length; f++) {
                            mispronounced_vowel_index[f] = temp[f];
                            mispronounced_vowel[f] = temp1[f];
                        }
                        mispronounced_vowel_index[i-1] = y;
                        mispronounced_vowel[i-1] = LookedUp[y];
                        i++;
                        y++;
                    }
                    y++;
                }

                if( mispronounced_vowel.length == 0 ){
                    textView.setText("Perfect!");
                } else {
                    text += "false pronunciation of ";
                    for ( int f = 0; f <mispronounced_vowel.length; f++ ){
                        if( f == mispronounced_vowel.length-1){
                            text += mispronounced_vowel[f].character + "\n";
                        } else {
                            text += mispronounced_vowel[f].character + " & ";
                        }
                    }

                    textView.setText(text);
                    feedback();
                }

            }
        });
        return view;
    }

    // ----------------------------------------------------------------------------------Input Tools
    // Camera
    private void camera(){
        // Code for the Camera
        TextRecognizer textRecognizer =
                new TextRecognizer.Builder(getActivity().getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {

        }
        else {
            cameraSource =
                    new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1600, 400)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getContext(),
                                android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {}

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0)
                    {
                        etInput.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i =0;i<items.size();++i)
                                {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                String out [] = stringBuilder.toString().split(" ");
                                etInput.setText(out[0]);
                            }
                        });
                    }
                }
            });
        }
    }

    // Speech to Text
    private void speech_to_text(){
        // Code for Voice Recorder
        speechrecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speechrecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechrecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechrecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,false);
        LearnFragment.SpeechRecognitionListener listener =
                new LearnFragment.SpeechRecognitionListener();
        speechrecognizer.setRecognitionListener(listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private class SpeechRecognitionListener implements RecognitionListener {
        @Override
        public void onBeginningOfSpeech() {
            Toast.makeText(getActivity(), "Recording", Toast.LENGTH_SHORT).show();
            IsListening = true;
        }

        @Override
        public void onBufferReceived(byte[] buffer) { }

        @Override
        public void onEndOfSpeech(){
            Toast.makeText(getActivity(), "Done!", Toast.LENGTH_SHORT).show();
            IsListening = false;
        }

        @Override
        public void onError(int error)
        {
            speechrecognizer.startListening(speechrecognizerIntent);
        }

        @Override
        public void onEvent(int eventType, Bundle params){ }

        @Override
        public void onPartialResults(Bundle partialResults){ }

        @Override
        public void onReadyForSpeech(Bundle params){ }

        @Override
        public void onResults(Bundle results){
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            etInput.setText(matches.get(0).toString());
        }

        @Override
        public void onRmsChanged(float rmsdB){ }
    }

    // ------------------------------------------------------------------------Oxford Dictionary API
    private String dictionaryEntries() {
        final String language = "en";
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }

    // In android calling network requests on the main thread forbidden by default
    // Create class to do async job
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            final String app_id = "091e7ddf";
            final String app_key = "af40991963d253e055aaa9cd04a3731d";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            try {
                JSONObject results = new JSONObject(result);
                JSONArray one = results.getJSONArray("results");

                for(int i = 0 ; i < one.length() ; i++){
                    JSONObject lexicalEntries = one.getJSONObject(i);
                    JSONArray two = lexicalEntries.getJSONArray("lexicalEntries");

                    for(int j = 0 ; j < two.length() ; j++){
                        JSONObject pronunciations = two.getJSONObject(j);
                        JSONObject entries = two.getJSONObject(j);
                        JSONArray three = entries.getJSONArray("entries");
                        JSONArray IPA = pronunciations.getJSONArray("pronunciations");

                        IPAString = IPA.toString().substring(IPA.toString().lastIndexOf(":")+2,IPA.toString().lastIndexOf('"'));

                        for(int k = 0 ; k < three.length() ; k++){
                            JSONObject senses = three.getJSONObject(k);
                            JSONArray four = senses.getJSONArray("senses");
                            for(int l = 0 ; l < four.length() ; l++) {
                                JSONObject definitions = four.getJSONObject(l);
                                JSONArray five = definitions.getJSONArray("definitions");
                            }
                        }
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            IPAs = new String[IPAString.length()];
            for ( int i = 0; i<IPAString.length();i++){
                IPAs[i] = String.valueOf(IPAString.charAt(i)).toString();
            }
        }
    }

    private void StartRecording() {

        file = new File(getContext().getCacheDir().getAbsolutePath() + File.separator, "Recording.pcm");

        try {
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioDataRec = new short[minBufferSize];

            AudioRecorded = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION,
                    sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
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

    // ------------------------------------------------------------------------Vowel Characteristics
    // Returns the vowel's characteristic as an IPA class
    // From vowel if given (String of the vowel, null , 'v')
    // From Frequency if given (null, array of frequencies, 'f')
    private IPA vowel_characteristics_lookup( String vowel, double [] InputFreq, char s){
        IPA result;

        if( s == 'v' ){
            if("i".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "i";
                temp.vowel_character = new String[]{"i", "Close", "Unrounded", "Front"};
                temp.vowel_freq = new double[]{User_i_1, User_i_2};
                result = temp;
            }
            else if ("ɪ".equals(vowel)) { //this is the tone i put in tonalhell.wav
                IPA temp = new IPA();
                temp.character = "ɪ";
                temp.vowel_character = new String[]{"ɪ", "Near-close", "Unrounded", "Near-front"};
                temp.vowel_freq = new double[]{User_ɪ_1, User_ɪ_2};
                result = temp;
            }
            else if ("e".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "e";
                temp.vowel_character = new String[]{"e", "Close-mid","Unrounded","Front"};
                temp.vowel_freq = new double[]{User_e_1, User_e_2};
                result = temp;
            }
            else if ("ɛ".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "ɛ";
                temp.vowel_character = new String[]{"ɛ", "Open-mid","Unrounded","Front"};
                temp.vowel_freq = new double[]{User_ɛ_1, User_ɛ_2};
                result = temp;
            }
            else if ("æ".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "æ";
                temp.vowel_character = new String[]{"æ", "Near-open", "Unrounded", "Front"};
                temp.vowel_freq = new double[]{User_æ_1, User_æ_2};
                result = temp;
            }
            else if ("a".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "a";
                temp.vowel_character = new String[]{"a", "Open", "Unrounded", "Front"};
                temp.vowel_freq = new double[]{User_a_1, User_a_2};
                result = temp;
            }



            else if ("ə".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "ə";
                temp.vowel_character = new String[]{"ə", "Mid", "Rounded", "Central"};
                temp.vowel_freq = new double[]{User_ə_1, User_ə_2};
                result = temp;
            }
            else if ("ɜ".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "ɜ";
                temp.vowel_character = new String[]{"ɜ", "Open-mid", "Unrounded", "Central"};
                temp.vowel_freq = new double[]{User_ɜ_1, User_ɜ_2};
                result = temp;
            }



            else if ("u".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "u";
                temp.vowel_character = new String[]{"u", "Close", "Rounded", "Back"};
                temp.vowel_freq = new double[]{User_u_1, User_u_2};
                result = temp;
            }
            else if ("ʊ".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "ʊ";
                temp.vowel_character = new String[]{"ʊ", "Near-close", "Rounded", "Near-back"};
                temp.vowel_freq = new double[]{User_ʊ_1, User_ʊ_2};
                result = temp;
            }
            else if ("o".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "o";
                temp.vowel_character = new String[]{"o", "Close-mid", "Rounded", "Back"};
                temp.vowel_freq = new double[]{User_o_1, User_o_2};
                result = temp;
            }
            else if ("ʌ".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "ʌ";
                temp.vowel_character = new String[]{"ʌ", "Open-mid", "Unrounded", "Back"};
                temp.vowel_freq = new double[]{User_ʌ_1, User_ʌ_2};
                result = temp;
            }
            else if ("ɔ".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "ɔ";
                temp.vowel_character = new String[]{"ɔ", "Open-mid", "Rounded", "Back"};
                temp.vowel_freq = new double[]{User_ɔ_1, User_ɔ_2};
                result = temp;
            }
            else if ("ɒ".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "ɒ";
                temp.vowel_character = new String[]{"ɒ", "Open", "Rounded", "Back"};
                temp.vowel_freq = new double[]{User_ɒ_1, User_ɒ_2};
                result = temp;
            }
            else if ("ɑ".equals(vowel)) {
                IPA temp = new IPA();
                temp.character = "ɑ";
                temp.vowel_character = new String[]{"ɑ", "Open", "Unrounded", "Back"};
                temp.vowel_freq = new double[]{User_ɑ_1, User_ɑ_2};
                result = temp;
            }

            else {
                Log.d(TAG, "Vowel_characteristis_Lookup: Not a vowel");
                return null;
            }
        }
        else if ( s == 'f'){
            if(InputFreq[0] < User_i_1*(1+ePer) && InputFreq[0] > User_i_1*(1-ePer) &&
                    InputFreq[1] < User_i_2*(1+ePer) && InputFreq[1] > User_i_2*(1-ePer)) {

                result =  vowel_characteristics_lookup("i",null,'v');
            }
            else if (InputFreq[0] < User_ɪ_1*(1+ePer) && InputFreq[0] > User_ɪ_1*(1-ePer) &&
                    InputFreq[1] < User_ɪ_2*(1+ePer) && InputFreq[1] > User_ɪ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("ɪ",null,'v');
            }
            else if (InputFreq[0] < User_e_1*(1+ePer) && InputFreq[0] > User_e_1*(1-ePer) &&
                    InputFreq[1] < User_e_2*(1+ePer) && InputFreq[1] > User_e_2*(1-ePer)) {

                result = vowel_characteristics_lookup("e",null,'v');
            }
            else if (InputFreq[0] < User_ɛ_1*(1+ePer) && InputFreq[0] > User_ɛ_1*(1-ePer) &&
                    InputFreq[1] < User_ɛ_2*(1+ePer) && InputFreq[1] > User_ɛ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("ɛ",null,'v');
            }
            else if (InputFreq[0] < User_æ_1*(1+ePer) && InputFreq[0] > User_æ_1*(1-ePer) &&
                    InputFreq[1] < User_æ_2*(1+ePer) && InputFreq[1] > User_æ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("æ",null,'v');
            }
            else if (InputFreq[0] < User_a_1*(1+ePer) && InputFreq[0] > User_a_1*(1-ePer) &&
                    InputFreq[1] < User_a_2*(1+ePer) && InputFreq[1] > User_a_2*(1-ePer)) {

                result = vowel_characteristics_lookup("a",null,'v');
            }
            else if (InputFreq[0] < User_ə_1*(1+ePer) && InputFreq[0] > User_ə_1*(1-ePer) &&
                    InputFreq[1] < User_ə_2*(1+ePer) && InputFreq[1] > User_ə_2*(1-ePer)) {

                result = vowel_characteristics_lookup("ə",null,'v');
            }
            else if (InputFreq[0] < 560*(1+ePer) && InputFreq[0] > 560*(1-ePer) &&
                    InputFreq[1] < 1480*(1+ePer) && InputFreq[1] > 1480*(1-ePer) &&
                    InputFreq[2] < 2520*(1+ePer) && InputFreq[2] > 2520*(1-ePer)) {

                result = vowel_characteristics_lookup("ɜ",null,'v');
            }
            else if (InputFreq[0] < User_ɜ_1*(1+ePer) && InputFreq[0] > User_ɜ_1*(1-ePer) &&
                    InputFreq[1] < User_ɜ_2*(1+ePer) && InputFreq[1] > User_ɜ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("u",null,'v');
            }
            else if (InputFreq[0] < User_ʊ_1*(1+ePer) && InputFreq[0] > User_ʊ_1*(1-ePer) &&
                    InputFreq[1] < User_ʊ_2*(1+ePer) && InputFreq[1] > User_ʊ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("ʊ",null,'v');
            }
            else if (InputFreq[0] < User_o_1*(1+ePer) && InputFreq[0] > User_o_1*(1-ePer) &&
                    InputFreq[1] < User_o_2*(1+ePer) && InputFreq[1] > User_o_2*(1-ePer)) {

                result = vowel_characteristics_lookup("o",null,'v');
            }
            else if (InputFreq[0] < User_ʌ_1*(1+ePer) && InputFreq[0] > User_ʌ_1*(1-ePer) &&
                    InputFreq[1] < User_ʌ_2*(1+ePer) && InputFreq[1] > User_ʌ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("ʌ",null,'v');
            }
            else if (InputFreq[0] < User_ɔ_1*(1+ePer) && InputFreq[0] > User_ɔ_1*(1-ePer) &&
                    InputFreq[1] < User_ɔ_2*(1+ePer) && InputFreq[1] > User_ɔ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("ɔ",null,'v');
            }
            else if (InputFreq[0] < User_ɒ_1*(1+ePer) && InputFreq[0] > User_ɒ_1*(1-ePer) &&
                    InputFreq[1] < User_ɒ_2*(1+ePer) && InputFreq[1] > User_ɒ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("ɒ",null,'v');
            }
            else if (InputFreq[0] < User_ɑ_1*(1+ePer) && InputFreq[0] > User_ɑ_1*(1-ePer) &&
                    InputFreq[1] < User_ɑ_2*(1+ePer) && InputFreq[1] > User_ɑ_2*(1-ePer)) {

                result = vowel_characteristics_lookup("ɑ",null,'v');
            }
            else {
                Log.d(TAG, "vowel_characteristics_lookup: Not a vowel");
                return null;
            }
        }
        else {
            Log.d(TAG, "vowel_characteristics_lookup: Wrong character input");
            return null;
        }
        return result;
    }

    // ---------------------------------------------------------------------------Frequency Analysis
    // FFT
    // we want to have a resolution of ~20Hz in our FFT
    // output buffer is half the size of the input buffer, and the max freq is sampleFreq/2 so
    // sampleFreq/(inputBufferSizeFloor) < 20
    private double[] SampleFFT(DataInputStream dataInputStream){
        try {
            //Log.d("TEST", "SampleFFT Instance");
            double inputBufferSizeFloor = (double)sampleFreq / 25.0;
            int inputBufferSize, i;
            i = 0;
            while(true) {
                inputBufferSize = (int)Math.pow(2.0, (double)i);
                if(inputBufferSize > inputBufferSizeFloor) break;
                else i++;
            }
            int outputBufferSize = inputBufferSize/2;

            //Log.d("TEST", "SampleFFT input buffer size: " + inputBufferSize + " output buffer size: " + outputBufferSize);
            short[] testData = new short[inputBufferSize];
            double[] dataRec = new double[inputBufferSize];
            double[] zeros = new double[inputBufferSize];
            double[] powerSpectrum = new double[outputBufferSize];


            double max = 0.0;
            int[] maxIndex = {0, 0, 0};
            double[] maxFreq = {0.0, 0.0, 0.0};
            i = 0;
            //the bytebuffer is here to convert dataInputStream endianness. dataInputStream only outputs big endian stuff
            while (i < inputBufferSize) {
                if (dataInputStream.available() > 0) {
                    Byte temp = dataInputStream.readByte();
                    ByteBuffer bb = ByteBuffer.allocate(2);
                    bb.order(ByteOrder.BIG_ENDIAN); //set to BIG_ENDIAN or LITTLE_ENDIAN as appropriate
                    bb.put(temp);
                    if(dataInputStream.available() > 0)
                        temp = dataInputStream.readByte();
                    else
                        temp = (byte)0;
                    bb.put(temp);
                    testData[i] = bb.getShort(0);

                    dataRec[i] = testData[i];
                } else {
                    testData[i] = 0;
                }
                zeros[i] = 0.0;
                i++;
            }

            AudioRecordedFFT = new FFT(inputBufferSize);
            AudioRecordedFFT.fft(dataRec, zeros);
            for(i = 0; i < outputBufferSize; i++) {
                powerSpectrum[i] = Math.pow(dataRec[i + outputBufferSize], 2.0) + Math.pow(zeros[i + outputBufferSize], 2.0);
            }

            max = 0.0;
            double sum = 0.0;
            for(i = 0; i < outputBufferSize; i++)
                sum += powerSpectrum[i];
            double average;
            average = sum / (double)outputBufferSize;
            for(i = 980; i < 1010; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1])) {
                    max = powerSpectrum[i];
                    maxIndex[0] = i;
                }
            }
            if(powerSpectrum[maxIndex[0]] < (average * 1.3)) {
                maxFreq[0] = 0.0;
                maxFreq[1] = 0.0;
                maxFreq[2] = 0.0;
                return maxFreq;
            }
            //Log.d("TEST", "maxIndex[0] is" + maxIndex[0]);
            max = 0.0;
            for(i = 885; i < 977; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1]) && (Math.abs(i - maxIndex[0]) > 3)) {
                    max = powerSpectrum[i];
                    maxIndex[1] = i;
                }
            }
            //Log.d("TEST", "maxIndex[1] is" + maxIndex[1]);
            max = 0.0;
            for(i = 885; i < 977; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1]) && (Math.abs(i - maxIndex[0]) > 3) && (Math.abs(i - maxIndex[1]) > 3)) {
                    max = powerSpectrum[i];
                    maxIndex[2] = i;
                }
            }

            Log.d("TEST", "outputBufferSize" + outputBufferSize);

            maxFreq[0] = (sampleFreq/2.0) - ((((double) maxIndex[0])/(double)(outputBufferSize)) * (sampleFreq / 2.0));
            maxFreq[1] = (sampleFreq/2.0) - ((((double) maxIndex[1])/(double)(outputBufferSize)) * (sampleFreq / 2.0));
            maxFreq[2] = (sampleFreq/2.0) - ((((double) maxIndex[2])/(double)(outputBufferSize)) * (sampleFreq / 2.0));
            double temp;

            //the bubble sort shuffle (sorts lowest freq to high)
            if(maxFreq[0] > maxFreq[1]) {
                temp = maxFreq[0];
                maxFreq[0] = maxFreq[1];
                maxFreq[1] = temp;
            }
            if(maxFreq[1] > maxFreq[2]) {
                temp = maxFreq[1];
                maxFreq[1] = maxFreq[2];
                maxFreq[2] = temp;
            }
            if(maxFreq[0] > maxFreq[1]) {
                temp = maxFreq[0];
                maxFreq[0] = maxFreq[1];
                maxFreq[1] = temp;
            }

            return maxFreq;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Feedback
    private void feedback(){

        String user_text = "";
        String data_text = "";
        BubbleSeries data;

        for ( int i = 0; i <mispronounced_vowel.length  ; i++){

            textView.setText(textView.getText() + "\n" + "\n" + "You were supposed to pronounce " + LookedUp[i].character + "\n");
            // " but you pronounced " + UsersRecordingIPA[i].character + " !" + "\n" + "\n");

            plot.setVisibility(View.VISIBLE);
            plot.setDomainBoundaries(200/1000,1000/1000, BoundaryMode.FIXED);
            plot.setRangeBoundaries(1000/1000,3000/1000,BoundaryMode.FIXED);
            plot.setDomainLabel("F1");
            plot.setRangeLabel("F2");
            data_text = "Ideal " + LookedUp[i].character.toString();
            data = new BubbleSeries(
                    Arrays.asList(new Number[]{(LookedUp[i].vowel_freq[0])/1000}),
                    Arrays.asList(new Number[]{(LookedUp[i].vowel_freq[1])/1000}),
                    Arrays.asList(new Number[]{1.5}),
                    data_text
            );
            plot.addSeries(data,new BubbleFormatter (Color.WHITE, Color.WHITE));

            String char1, char2, char3;
            char1 = LookedUp[i].vowel_character[1];
            char2 = LookedUp[i].vowel_character[2];
            char3 = LookedUp[i].vowel_character[3];


            if(char1 == "Close" ){
                textView.setText(textView.getText() + "Try closing your mouth." + "\n");

            } else if (char1 == "Near-close"){
                textView.setText(textView.getText() + "Try nearly closing your mouth." + "\n");

            } else if (char1 == "Close-mid"){
                textView.setText(textView.getText() + "Try opening your mouth nearly half way." + "\n");

            } else if (char1 == "Mid"){
                textView.setText(textView.getText() + "Try opening your mouth half way." + "\n");

            } else if (char1 == "Open-mid"){
                textView.setText(textView.getText() + "Try opening your mouth nearly half way." + "\n");

            } else if (char1 == "Near-open"){
                textView.setText(textView.getText() + "Try nearly opening your mouth." + "\n");

            } else if (char1 == "Open"){
                textView.setText(textView.getText() + "Try opening your mouth." + "\n");

            } else{
                // Something is wrong
            }

            if(char2 == "Rounded"){
                textView.setText(textView.getText() + "Try making your lips rounder" + "\n");

            } else if (char2 == "Unrounded"){
                textView.setText(textView.getText() + "Try making your lips less round" + "\n");
            } else {
                // Something is wrong
            }

            if(char3 == "Front"){
                textView.setText(textView.getText() + "Try bringing your tongue to the front of your mouth" + "\n");
            } else if (char3 == "Near-front" ){
                textView.setText(textView.getText() + "Try bringing your tongue close to the front of your mouth" + "\n");
            } else if (char3 == "Central" ){
                textView.setText(textView.getText() + "Try bringing your tongue to the center of your mouth" + "\n");
            } else if (char3 == "Near-back" ){
                textView.setText(textView.getText() + "Try bringing your tongue close to the back of your mouth" + "\n");
            } else if (char3 == "Back" ){
                textView.setText(textView.getText() + "Try bringing your tongue to the back of your mouth" + "\n");
            } else{
                // Something is wrong
            }
        }

        /*
        Arrays.asList(new Number[]{(UsersRecordingIPA[i].vowel_freq[0])/1000}),
                Arrays.asList(new Number[]{(UsersRecordingIPA[i].vowel_freq[1])/1000}),
                Arrays.asList(new Number[]{1}),
                user_text
         */
        ArrayList<Number> f1 = new ArrayList<Number>();
        ArrayList<Number> f2 = new ArrayList<Number>();
        ArrayList<Number> r = new ArrayList<Number>();

        for (int i = 0; i<UsersRecordingIPA.length ; i++ ){

            f1.add(UsersRecordingIPA[i].vowel_freq[0]/1000);
            f2.add(UsersRecordingIPA[i].vowel_freq[1]/1000);
            r.add(0.5);
        }
        user_text = "Users";
        data = new BubbleSeries(
                f1,
                f2,
                r,
                user_text
        );
        plot.addSeries(data,new BubbleFormatter (Color.RED, Color.RED));

    }

    // ---------------------------------------------------------------------------------------------
    private double[][] vowel_from_recording (double[][] Input){
        double[][] result = new double[0][3];
        double thresh = 100;
        double value = 0;
        double next_value = 0;
        double next_next_value = 0;
        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;
        int count = 0;

        text += "\n";
        for (int i = 0; i < Input.length ; i++){

            if ( i < Input.length-2) {
                value = Input[i][0];
                next_value = Input[i + 1][0];
                next_next_value = Input[i + 2][0];
                if (value < next_value + thresh && value > next_value - thresh) {
                    //text += "1";
                    count++;
                    sum1 += Input[i][0];
                    sum2 += Input[i][1];
                    sum3 += Input[i][2];
                } else {
                    if (value < next_next_value + thresh && value > next_next_value - thresh) {
                        //text += "2";
                        count++;
                        sum1 += Input[i][0];
                        sum2 += Input[i][1];
                        sum3 += Input[i][2];
                        i++;
                    } else if (count > 0) {
                        //text += "3";
                        count++;
                        sum1 += value;
                        sum2 += Input[i][1];
                        sum3 += Input[i][2];
                        double [][] temp = result;
                        result = new double[resultcount+1][3];
                        for(int k = 0 ; k<temp.length ; k++){
                            result[k] = temp[k];
                        }
                        result[resultcount][0] = (sum1 / count);
                        result[resultcount][1] = (sum2 / count);
                        result[resultcount][2] = (sum3 / count);
                        resultcount++;
                        //text += "result:" + (sum1 / count) + " - ";
                        count = 0;
                        sum1 = 0;
                        sum2 = 0;
                        sum3 = 0;
                    }
                    //text += "4" + "COUNT:" + count;
                }
            }
            else if ( i < Input.length-1 ) {

            }
        }

        double [][] temp = result;
        result = new double[resultcount+1][3];
        for(int k = 0 ; k<temp.length ; k++){
            result[k] = temp[k];
        }
        result[resultcount][0] = (sum1 / count);
        result[resultcount][1] = (sum2 / count);
        result[resultcount][2] = (sum3 / count);
        resultcount++;
        textView.setText(text);

        return result;
    }
    // --------------------------------Analysis
    // Given the right input, an array of array of frequencies, It will see if the frequencies match
    // the required frequencies.
    private void comparator(double[][] InputFreq){
        LookedUp = new IPA[IPAs.length];
        status = true;

        // Retrieve data for the vowels in the searched word
        LookedUp_Length = 0;
        for ( int i = 0; i<IPAs.length; i++) {
            LookedUp[LookedUp_Length] = vowel_characteristics_lookup(IPAs[i], null, 'v');
            if (LookedUp[LookedUp_Length] != null){
                LookedUp_Length++;
            }
        }

        text += LookedUp_Length + " " + InputFreq.length + "\n";
        // Check if the recording's frequencies match the desired frequencies
        // if yes -> flag [][] = true
        // if no ->  flag [][] = false
        boolean[][] flag = new boolean[InputFreq.length][3];
        boolean lenghtmismatch = false;
        for ( int i = 0; i<InputFreq.length ; i++){
            // Check if the lengths of from the recording and the searched word mismatch
            // if there is a mismatch set:  status = false
            if (InputFreq.length != LookedUp_Length){
                status = false;
                lenghtmismatch = true;
            }
            else {
                double Lower, Higher, freq;
                for (int k = 0; k < InputFreq[i].length; k++) {
                    Lower = lThresh * LookedUp[i].vowel_freq[k];
                    Higher = hThresh * LookedUp[i].vowel_freq[k];
                    freq = InputFreq[i][k];
                    if (freq < Lower) {
                        flag[i][k] = false;
                    } else flag[i][k] = !(freq > Higher);
                }
            }
        }

        // Check if any flag is false
        // If a flag is is false set: status = false
        //                            mispronounced_vowel_index[i]= the index of vowel with flag
        mispronounced_vowel_index = new int[10];
        Arrays.fill(mispronounced_vowel_index,0);
        if(status == true){
            for ( int i = 0; i< flag.length ; i++){
                for ( int k = 0; k<flag.length ; k++) {
                    if (flag[i][k] == false) {
                        status = false;
                        mispronounced_vowel_index[i] = 1;
                    }
                }
            }
        }

        // If status is true, the word is rightly pronounced
        // output: Perfect
        // If status is false, check if there is a mismatch in length
        //                           or comment on which vowel is mispronounced
        boolean firstFault = true;
        if( status == true){
            textView.setText( text + " Perfect ");
        } else if (status == false && lenghtmismatch == false) {
            text += " False pronunciation of  ";
            for(int i = 0; i < LookedUp_Length; i++){
                if(mispronounced_vowel_index[i] == 1){
                    if(firstFault){
                        text += LookedUp[i].character.toString();
                        firstFault = false;
                    }
                    else{
                        text += " & " + LookedUp[i].character.toString();
                    }
                }
            }
            textView.setText(text  + "!");
            feedback();
        } else{
            textView.setText( text + " Lengths don't match! ");
        }
    }

}
