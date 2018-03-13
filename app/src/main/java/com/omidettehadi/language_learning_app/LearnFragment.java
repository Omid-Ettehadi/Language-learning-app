package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.omidettehadi.language_learning_app.FFT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import static com.google.android.gms.internal.zzs.TAG;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_10_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_10_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_11_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_11_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_12_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_12_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_1_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_1_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_2_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_2_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_3_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_3_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_4_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_4_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_5_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_5_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_6_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_6_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_7_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_7_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_8_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_8_freq;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_9_character;
import static com.omidettehadi.language_learning_app.MainActivity.vowel_9_freq;
import static com.omidettehadi.language_learning_app.SigninActivity.wordoftheday;

public class LearnFragment extends Fragment implements TextToSpeech.OnInitListener{

    private EditText etInput;
    private Button btnSearch, btnMic, btnCapture, btnCam, btnRecord, btnStop, btnPlay;
    private TextView textView;
    private File file;
    private AudioRecord AudioRecorded;
    private AudioTrack AudioRecordedTrack;
    private FFT AudioRecordedFFT;

    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private final int RequestCameraPermissionID = 1001;

    private boolean recording;
    private int sampleFreq = 44100;

    private SpeechRecognizer speechrecognizer;
    private Intent speechrecognizerIntent;
    private boolean IsListening;

    private TextToSpeech tts;

    private String IPAString = "";
    private String word;
    private String [] IPAs;

    public LearnFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        etInput = view.findViewById(R.id.etInput);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnMic = view.findViewById(R.id.btnMic);
        btnCam = view.findViewById(R.id.btnCam);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnCapture.setVisibility(View.INVISIBLE);

        cameraView = view.findViewById(R.id.surface_view);
        cameraView.setVisibility(View.INVISIBLE);

        btnRecord = view.findViewById(R.id.btnRecord);
        btnStop = view.findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnPlay = view.findViewById(R.id.btnPlay);
        //btnPlay.setEnabled(false);

        textView = view.findViewById(R.id.textView);

        // Code for the Camera
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {

        }
        else {
            cameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(800, 200)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

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

        // Code for Voice Recorder
        speechrecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speechrecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechrecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechrecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,false);
        LearnFragment.SpeechRecognitionListener listener = new LearnFragment.SpeechRecognitionListener();
        speechrecognizer.setRecognitionListener(listener);

        // See if Search Button is pressed
        // Run Search for the word in the input EditText
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = etInput.getText().toString();
                //speakOut();
                IPAString = null;
                textView.setText(" Loading... ");
                new CallbackTask().execute(dictionaryEntries());




                if(IPAString == null){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           if(IPAString == null){
                               final Handler handler = new Handler();
                               handler.postDelayed(new Runnable() {
                                   @Override
                                   public void run() {
                                       double[][] test = {{760, 1320, 2500}, {360, 2220, 2960}};
                                       comparator(test);
                                   }
                               }, 5000);
                           }
                           else {
                               double[][] test = {{760, 1320, 2500}, {360, 2220, 2960}};
                               comparator(test);
                           }
                        }
                    }, 3000);
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

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
            else {
                //btnSpeak.setEnabled(true);
            }
        }
        else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    protected class SpeechRecognitionListener implements RecognitionListener {
        @Override
        public void onBeginningOfSpeech() { Toast.makeText(getActivity(), "Recording", Toast.LENGTH_SHORT).show();}

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

    private void speakOut() {
        tts.speak(word , TextToSpeech.QUEUE_FLUSH, null);
    }

    private String dictionaryEntries() {
        final String language = "en";
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }

    // In android calling network requests on the main thread forbidden by default
    // Create class to do async job
    class CallbackTask extends AsyncTask<String, Integer, String> {

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
            for(i = 0; i < 4096; i++) {
                powerSpectrum[i] = (dataRec[4096+i] * dataRec[4096+i]) + (zeros[4096+i] * zeros[4096+i]);
            }

            //these loops start at 1 because we look at i-1 so don't want an error. They end at 4096/4 because we don't care about frequencies higher than ~5000Hz
            for(i = 1; i < 1024; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1])) {
                    max = powerSpectrum[i];
                    maxIndex[0] = i;
                }
            }
            max = 0.0;
            for(i = 1; i < 1024; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1]) && (Math.abs(i - maxIndex[0]) > 2)) {
                    max = powerSpectrum[i];
                    maxIndex[1] = i;
                }
            }
            max = 0.0;
            for(i = 1; i < 1024; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1]) && (Math.abs(i - maxIndex[0]) > 2) && (Math.abs(i - maxIndex[1]) > 2)) {
                    max = powerSpectrum[i];
                    maxIndex[2] = i;
                }
            }
            maxFreq[0] = (((double) maxIndex[0])/4096.0) * (sampleFreq / 2.0);
            maxFreq[1] = (((double) maxIndex[1])/4096.0) * (sampleFreq / 2.0);
            maxFreq[2] = (((double) maxIndex[2])/4096.0) * (sampleFreq / 2.0);

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
    }

    // Given the right input, an array of array of frequencies, It will see if the frequencies match
    // the required frequencies.
    private void comparator(double[][] InputFreq){
        double ePerD = 0.1;
        IPA[] LookedUp = new IPA[IPAs.length];
        boolean status = true;

        int j = 0;
        for ( int i = 0; i<IPAs.length; i++) {
            word += IPAs[i];
            if("i".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "i:";
                temp.vowel_character = new String[]{"iː", "Close", "Front", "Long"};
                temp.vowel_freq = new int[]{280, 2620, 3380};
                LookedUp[j] = temp;
                j++;
            }
            else if ("ɪ".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "ɪ";
                temp.vowel_character = new String[]{"ɪ", "Close", "Front", "Short"};
                temp.vowel_freq = new int[]{360, 2220, 2960};
                LookedUp[j] = temp;
                j++;
            }
            else if ("e".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "e";
                temp.vowel_character = new String[]{"e", "Half-open", "Front", "Short"};
                temp.vowel_freq = new int[]{600, 2060, 2840};
                LookedUp[j] = temp;
                j++;
            }
            else if ("æ".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "æ";
                temp.vowel_character = new String[]{"æ", "Open", "Front", "Short"};
                temp.vowel_freq = new int[]{800, 1760, 2500};
                LookedUp[j] = temp;
                j++;
            }
            else if ("ʌ".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "ʌ";
                temp.vowel_character = new String[]{"ʌ", "Open", "Central", "Short"};
                temp.vowel_freq = new int[]{760, 1320, 2500};
                LookedUp[j] = temp;
                j++;
            }
            else if ("ɑ".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "ɑː";
                temp.vowel_character = new String[]{"ɑː", "Open", "Back", "Long"};
                temp.vowel_freq = new int[]{740, 1180, 2640};
                LookedUp[j] = temp;
                j++;
            }
            else if ("ɒ".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "ɒ";
                temp.vowel_character = new String[]{"ɒ", "Open", "Back", "Short"};
                temp.vowel_freq = new int[]{560, 920, 2560};
                LookedUp[j] = temp;
                j++;
            }
            else if ("ɔ".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "ɔː";
                temp.vowel_character = new String[]{"ɔː", "Half-open", "Back", "Long"};
                temp.vowel_freq = new int[]{480, 760, 2620};
                LookedUp[j] = temp;
                j++;
            }
            else if ("ʊ".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "ʊ";
                temp.vowel_character = new String[]{"ʊ", "Close", "Back", "Short"};
                temp.vowel_freq = new int[]{380, 940, 2300};
                LookedUp[j] = temp;
                j++;
            }
            else if ("u".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "uː";
                temp.vowel_character = new String[]{"uː", "Close", "Back", "Long"};
                temp.vowel_freq = new int[]{320, 920, 2200};
                LookedUp[j] = temp;
                j++;
            }
            else if ("ɜ".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "ɜː";
                temp.vowel_character = new String[]{"ɜː", "Half-open", "Central", "Long"};
                temp.vowel_freq = new int[]{560, 1480, 2520};
                LookedUp[j] = temp;
                j++;
            }
            else if ("ə".equals(IPAs[i])) {
                IPA temp = new IPA();
                LookedUp[j] = new IPA();
                temp.character = "ə";
                temp.vowel_character = new String[]{"ə", "Half-open", "Central", "Short"};
                temp.vowel_freq = new int[]{500, 1500, 3000};
                LookedUp[j] = temp;
                j++;
            }
            else {
                word += " Not a vowel ";
            }
        }

        //textView.setText(word);

        boolean[][] flag = new boolean[InputFreq.length][3];

        double hThresh = 1 + (ePerD);
        double lThresh = 1 - (ePerD);

        for ( int i = 0; i<InputFreq.length ; i++){
            if (InputFreq.length != j){
                status = false;
            }
            else {
                double Lower, Higher, freq;
                for (int k = 0; k < InputFreq[i].length; k++) {
                    Lower = lThresh * LookedUp[i].vowel_freq[k];
                    Higher = hThresh * LookedUp[i].vowel_freq[k];
                    freq = InputFreq[i][k];
                    if (freq < Lower) {
                        flag[i][k] = false;
                        //word += " " + i + "-" + k +" false is found (lower) " + LookedUp[i].vowel_freq[k] + " ";
                    } else if (freq > Higher) {
                        flag[i][k] = false;
                        //word += " " + i + "-" + k +" false is found (higher) " + LookedUp[i].vowel_freq[k] + " ";
                    } else {
                        flag[i][k] = true;
                        //word += " " + i + "-" + k +" Correct " + LookedUp[i].vowel_freq[k] + " ";
                    }
                }
            }
        }

        int z = -1;
        if(status == true){
            for ( int i = 0; i< flag.length ; i++){
                for ( int k = 0; k<flag.length ; k++) {
                    if (flag[i][k] == false) {
                        //textView.setText( " False Pronunciation of " + LookedUp[k].vowel_character );
                        status = false;
                        z = i;
                    } else {
                        //textView.setText(" Perfect ");
                    }
                }
            }
        }

        if( status == true){
            textView.setText(" Perfect ");
        } else if (status == false && z != -1) {
            textView.setText(" False at " + (z+1));
        } else{
            textView.setText(" Lengths don't match! ");
        }
    }

    private void feedback(double[][] InputFreq){

    }

    private IPA IPAfromFreq(double []InputFreq){

        IPA youripa = new IPA();

        double ePer = 0.1;

            if(InputFreq[0] < 280*(1+ePer) && InputFreq[0] > 280*(1-ePer) &&
                    InputFreq[1] < 2620*(1+ePer) && InputFreq[1] > 2620*(1-ePer) &&
                    InputFreq[2] < 3380*(1+ePer) && InputFreq[2] > 3380*(1-ePer)){

                IPA temp = new IPA();
                temp.character = "i";
                temp.vowel_character = new String[]{"i", "Close", "Front", "Long"};
                temp.vowel_freq = new int[]{280, 2620, 3380};
                youripa = temp;
            }
            else if (InputFreq[0] < 360*(1+ePer) && InputFreq[0] > 360*(1-ePer) &&
                    InputFreq[1] < 2220*(1+ePer) && InputFreq[1] > 2220*(1-ePer) &&
                    InputFreq[2] < 2960*(1+ePer) && InputFreq[2] > 2960*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "ɪ";
                temp.vowel_character = new String[]{"ɪ", "Close", "Front", "Short"};
                temp.vowel_freq = new int[]{360, 2220, 2960};
                youripa = temp;
            }
            else if (InputFreq[0] < 600*(1+ePer) && InputFreq[0] > 600*(1-ePer) &&
                    InputFreq[1] < 2060*(1+ePer) && InputFreq[1] > 2060*(1-ePer) &&
                    InputFreq[2] < 2840*(1+ePer) && InputFreq[2] > 2840*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "e";
                temp.vowel_character = new String[]{"e", "Half-open", "Front", "Short"};
                temp.vowel_freq = new int[]{600, 2060, 2840};
                youripa = temp;
            }
            else if (InputFreq[0] < 800*(1+ePer) && InputFreq[0] > 800*(1-ePer) &&
                    InputFreq[1] < 1760*(1+ePer) && InputFreq[1] > 1760*(1-ePer) &&
                    InputFreq[2] < 2500*(1+ePer) && InputFreq[2] > 2500*(1-ePer)){

                IPA temp = new IPA();
                temp.character = "æ";
                temp.vowel_character = new String[]{"æ", "Open", "Front", "Short"};
                temp.vowel_freq = new int[]{800, 1760, 2500};
                youripa = temp;
            }
            else if (InputFreq[0] < 760*(1+ePer) && InputFreq[0] > 760*(1-ePer) &&
                    InputFreq[1] < 1320*(1+ePer) && InputFreq[1] > 1320*(1-ePer) &&
                    InputFreq[2] < 2500*(1+ePer) && InputFreq[2] > 2500*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "ʌ";
                temp.vowel_character = new String[]{"ʌ", "Open", "Central", "Short"};
                temp.vowel_freq = new int[]{760, 1320, 2500};
                youripa = temp;
            }
            else if (InputFreq[0] < 740*(1+ePer) && InputFreq[0] > 740*(1-ePer) &&
                    InputFreq[1] < 1180*(1+ePer) && InputFreq[1] > 1180*(1-ePer) &&
                    InputFreq[2] < 2640*(1+ePer) && InputFreq[2] > 2640*(1-ePer)){

                IPA temp = new IPA();
                temp.character = "ɑ";
                temp.vowel_character = new String[]{"ɑ", "Open", "Back", "Long"};
                temp.vowel_freq = new int[]{740, 1180, 2640};
                youripa = temp;
            }
            else if (InputFreq[0] < 560*(1+ePer) && InputFreq[0] > 560*(1-ePer) &&
                    InputFreq[1] < 920*(1+ePer) && InputFreq[1] > 920*(1-ePer) &&
                    InputFreq[2] < 2560*(1+ePer) && InputFreq[2] > 2560*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "ɒ";
                temp.vowel_character = new String[]{"ɒ", "Open", "Back", "Short"};
                temp.vowel_freq = new int[]{560, 920, 2560};
                youripa = temp;
            }
            else if (InputFreq[0] < 480*(1+ePer) && InputFreq[0] > 480*(1-ePer) &&
                    InputFreq[1] < 760*(1+ePer) && InputFreq[1] > 760*(1-ePer) &&
                    InputFreq[2] < 2620*(1+ePer) && InputFreq[2] > 2620*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "ɔ";
                temp.vowel_character = new String[]{"ɔ", "Half-open", "Back", "Long"};
                temp.vowel_freq = new int[]{480, 760, 2620};
                youripa = temp;
            }
            else if (InputFreq[0] < 380*(1+ePer) && InputFreq[0] > 380*(1-ePer) &&
                    InputFreq[1] < 940*(1+ePer) && InputFreq[1] > 940*(1-ePer) &&
                    InputFreq[2] < 2300*(1+ePer) && InputFreq[2] > 2300*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "ʊ";
                temp.vowel_character = new String[]{"ʊ", "Close", "Back", "Short"};
                temp.vowel_freq = new int[]{380, 940, 2300};
                youripa = temp;
            }
            else if (InputFreq[0] < 320*(1+ePer) && InputFreq[0] > 320*(1-ePer) &&
                    InputFreq[1] < 920*(1+ePer) && InputFreq[1] > 920*(1-ePer) &&
                    InputFreq[2] < 2200*(1+ePer) && InputFreq[2] > 2200*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "u";
                temp.vowel_character = new String[]{"u", "Close", "Back", "Long"};
                temp.vowel_freq = new int[]{320, 920, 2200};
                youripa = temp;
            }
            else if (InputFreq[0] < 560*(1+ePer) && InputFreq[0] > 560*(1-ePer) &&
                    InputFreq[1] < 1480*(1+ePer) && InputFreq[1] > 1480*(1-ePer) &&
                    InputFreq[2] < 2520*(1+ePer) && InputFreq[2] > 2520*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "ɜ";
                temp.vowel_character = new String[]{"ɜ", "Half-open", "Central", "Long"};
                temp.vowel_freq = new int[]{560, 1480, 2520};
                youripa = temp;
            }
            else if (InputFreq[0] < 500*(1+ePer) && InputFreq[0] > 500*(1-ePer) &&
                    InputFreq[1] < 1500*(1+ePer) && InputFreq[1] > 1500*(1-ePer) &&
                    InputFreq[2] < 3000*(1+ePer) && InputFreq[2] > 3000*(1-ePer)) {

                IPA temp = new IPA();
                temp.character = "ə";
                temp.vowel_character = new String[]{"ə", "Half-open", "Central", "Short"};
                temp.vowel_freq = new int[]{500, 1500, 3000};
                youripa = temp;
            }
            else {
                textView.setText( " Not a vowel ");
            }
        textView.setText(youripa.character);
    return youripa;
    }

}
