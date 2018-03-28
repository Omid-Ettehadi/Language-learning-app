package com.omidettehadi.language_learning_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import static com.omidettehadi.language_learning_app.SigninActivity.word;
import static com.omidettehadi.language_learning_app.SigninActivity.wordoftheday;
import static com.omidettehadi.language_learning_app.SigninActivity.WordHistory;
import static com.omidettehadi.language_learning_app.SigninActivity.historystatus;

public class DictionaryFragment extends Fragment {

    private EditText etInput;
    private Button btnSearch, btnMic, btnCapture, btnCam, btnYES, btnNO;
    private TextView tvWordoftheDay, tvWordoftheDayAns;
    private SpeechRecognizer speechrecognizer;
    private Intent speechrecognizerIntent;
    private boolean IsListening;

    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private final int RequestCameraPermissionID = 1001;

    private ListView listview;


    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        etInput = view.findViewById(R.id.etInput);
        tvWordoftheDay = view.findViewById(R.id.tvWordoftheDay);
        tvWordoftheDayAns = view.findViewById(R.id.tvWordoftheDayAns);
        tvWordoftheDayAns.setText(wordoftheday);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnMic = view.findViewById(R.id.btnMic);
        btnCam = view.findViewById(R.id.btnCam);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnCapture.setVisibility(View.INVISIBLE);
        cameraView = view.findViewById(R.id.surface_view);
        cameraView.setVisibility(View.INVISIBLE);
        btnYES = view.findViewById(R.id.btnYES);
        btnNO = view.findViewById(R.id.btnNO);

        listview = view.findViewById(R.id.lvHistory);

        camera();
        speech_to_text();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvWordoftheDayAns.setText(wordoftheday);
            }
        }, 500);

        if (historystatus == true){
            WordHistory = new String[]{};
            historystatus = false;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, WordHistory);
        listview.setAdapter(adapter);

        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordoftheDayFragment fragment = new WordoftheDayFragment();
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentttransaction = fragmentmanager.beginTransaction();
                fragmentttransaction.replace(R.id.main, fragment);
                fragmentttransaction.addToBackStack(null);
                fragmentttransaction.commit();
            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = wordoftheday;
                LearnFragment fragment = new LearnFragment();
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentttransaction = fragmentmanager.beginTransaction();
                fragmentttransaction.replace(R.id.main, fragment);
                fragmentttransaction.addToBackStack(null);
                fragmentttransaction.commit();
            }
        });

        // See if Search Button is pressed
        // Run Search for the word in the input EditText
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = etInput.getText().toString();

                int currentSize = WordHistory.length;
                int newSize = currentSize + 1;
                String[] tempArray = new String[ newSize ];
                for (int i=0; i < currentSize; i++)
                {
                    tempArray[i] = WordHistory[i];
                }
                tempArray[newSize- 1] = word;
                WordHistory = tempArray;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, WordHistory);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new ListClickHandler());

                getFragmentManager().beginTransaction()
                        .replace(R.id.main, new WordProfileFragment()).commit();
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

        tvWordoftheDayAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the wordoftheday action
                int currentSize = WordHistory.length;
                int newSize = currentSize + 1;
                String[] tempArray = new String[ newSize ];
                for (int i=0; i < currentSize; i++)
                {
                    tempArray[i] = WordHistory[i];
                }
                tempArray[newSize- 1] = wordoftheday;
                WordHistory = tempArray;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, WordHistory);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new ListClickHandler());

                WordoftheDayFragment fragment = new WordoftheDayFragment();
                FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
                fragmenttransaction.replace(R.id.main,fragment,"Word of The Day");
                fragmenttransaction.commit();
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
                tvWordoftheDay.setVisibility(View.INVISIBLE);
                tvWordoftheDayAns.setVisibility(View.INVISIBLE);
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
                            .setRequestedPreviewSize(800, 200)
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
    private void speech_to_text() {
        // Code for Voice Recorder
        speechrecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speechrecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechrecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechrecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, false);
        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        speechrecognizer.setRecognitionListener(listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    class SpeechRecognitionListener implements RecognitionListener {
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

    // -----------------------------------------------------------------------------------Item Click
    public class ListClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            TextView listText = (TextView) view.findViewById(R.id.lvHistory);
            String text = listText.getText().toString();
            word = text;
            WordProfileFragment fragment = new WordProfileFragment();
            FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"Word Profile");
            fragmenttransaction.commit();
        }
    }

    // ---------------------------------------------------------------------------Permission-Request
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            getContext(),android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}
