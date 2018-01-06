package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;

public class DictionaryFragment extends Fragment implements TextToSpeech.OnInitListener {

    private EditText etInput;
    private Button btnSearch, btnMic, btnCapture, btnCam;
    private SpeechRecognizer speechrecognizer;
    private Intent speechrecognizerIntent;
    private boolean IsListening, packageName;
    private String GoogleScore;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private final int RequestCameraPermissionID = 1001;

    public static String word;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        etInput = (EditText) view.findViewById(R.id.etInput);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnMic = (Button) view.findViewById(R.id.btnMic);
        btnCapture = (Button) view.findViewById(R.id.btnCapture);
        btnCam = (Button) view.findViewById(R.id.btnCam);
        cameraView = (SurfaceView) view.findViewById(R.id.surface_view);
        cameraView.setVisibility(View.INVISIBLE);

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

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {

        } else {
            cameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
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
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

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
                                etInput.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }

        speechrecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speechrecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechrecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechrecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        speechrecognizer.setRecognitionListener(listener);

        // See if Search Button is pressed
        // Run Search for the word in the input EditText
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = etInput.getText().toString();
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
                if (!IsListening)
                {
                    speechrecognizer.startListening(speechrecognizerIntent);
                }
            }
        });
        return view;
    }

    public boolean getPackageName() {
        return packageName;
    }

    protected class SpeechRecognitionListener implements RecognitionListener {
        @Override
        public void onBeginningOfSpeech()
        {
            Toast.makeText(getActivity(), "Recording", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
            Toast.makeText(getActivity(), "Done!", Toast.LENGTH_SHORT).show();
            IsListening = false;

        }

        @Override
        public void onError(int error)
        {
            speechrecognizer.startListening(speechrecognizerIntent);
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onResults(Bundle results)
        {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            float[] score = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
            etInput.setText(matches.get(0).toString());
            /*
            for (int i = 0 ; i < matches.size() ; i++){
                if(matches.get(i).toString().equals(etInput.getText().toString())){
                    GoogleScore = String.valueOf(score[i]*100);
                    tvSpeakNow.setText( System.lineSeparator() + "Your pronunciation is at " + GoogleScore + " % ");
                    break;
                }
                else{
                    tvSpeakNow.setText( System.lineSeparator() + "Sorry, What was that?");
                }
            }*/
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {
        }
    }

    @Override
    public void onInit(int status) {

    }
    @Override
    public void onDestroy() {
        if (speechrecognizer != null)
        {
            speechrecognizer.destroy();
        }
        super.onDestroy();
    }
}
