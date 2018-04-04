package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import java.io.IOException;
import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.omidettehadi.language_learning_app.MainActivity.WordHistory;
import static com.omidettehadi.language_learning_app.MainActivity.historystatus;
import static com.omidettehadi.language_learning_app.MainActivity.word;
import static com.omidettehadi.language_learning_app.MainActivity.wordoftheday;

public class DictionaryFragment extends Fragment {

    // ----------------------------------------------------------------------------------Declaration
    // Items
    private EditText etInput;
    private Button btnSearch, btnMic, btnCapture, btnCam, btnYES, btnNO;
    private TextView tvWordoftheDay;
    private ListView listview;
    private SurfaceView cameraView;

    // --------------------------------------------------------------------------------Global Values
    // Speech to text
    private SpeechRecognizer speechrecognizer;
    private Intent speechrecognizerIntent;
    private boolean IsListening;

    // Optical Character Recognition
    private CameraSource cameraSource;
    private final int RequestCameraPermissionID = 1001;


    // ------------------------------------------------------------------------------------On Create
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        // Set default font
        Calligrapher calligrapher = new Calligrapher(getContext());
        calligrapher.setFont(getActivity(),"tradegothicltstdlight.otf",true);

        // Definitions
        etInput = view.findViewById(R.id.etInput);

        tvWordoftheDay = view.findViewById(R.id.tvWordoftheDay);

        btnSearch = view.findViewById(R.id.btnSearch);
        btnMic = view.findViewById(R.id.btnMic);
        btnCam = view.findViewById(R.id.btnCam);
        btnYES = view.findViewById(R.id.btnYES);
        btnNO = view.findViewById(R.id.btnNO);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnCapture.setVisibility(View.INVISIBLE);
        cameraView = view.findViewById(R.id.surface_view);
        cameraView.setVisibility(View.INVISIBLE);

        listview = view.findViewById(R.id.lvHistory);


        // Call the camera and the voice recording for Speech to Text
        camera();
        speech_to_text();


        // Word of teh Day Hard Coded
        wordoftheday = "Opportunity";
        tvWordoftheDay.setText(wordoftheday);
        /*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvWordoftheDay.setText(wordoftheday);
            }
        }, 500);
        */

        // Word History List Initiation
        if (historystatus == true){
            WordHistory = new String[]{};
            historystatus = false;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1, WordHistory);
        listview.setAdapter(adapter);


        // ----------------------------------------------------------------------------------Buttons
        // See if Yes Button is pressed
        // Go to Word of the Day Fragment
        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.main, new WordProfileFragment()).commit();
            }
        });

        // See if No Button is pressed
        // Go to Learn Fragment with  Word of the Day set as the Word
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = wordoftheday;
                getFragmentManager().beginTransaction()
                        .replace(R.id.main, new LearnFragment()).commit();
            }
        });

        // See if Search Button is pressed
        // Run Search for the word in the input EditText
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = etInput.getText().toString();
                // If there is a word in the word, add it to the history
                // and go to Word profile fragment.
                // If no word is insert don't do anything.
                if (etInput.getText().toString().length() != 0) {
                    // Add word to the history
                    int currentSize = WordHistory.length;
                    int newSize = currentSize + 1;
                    String[] tempArray = new String[newSize];
                    for (int i = 0; i < currentSize; i++) {
                        tempArray[i] = WordHistory[i];
                    }
                    tempArray[newSize - 1] = word;
                    WordHistory = tempArray;
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1, WordHistory);
                    listview.setAdapter(adapter);
                    listview.setOnItemClickListener(new ListClickHandler());
                    // Go to Word Profile Fragment
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main, new WordProfileFragment()).commit();
                } else{
                    // Nothing
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

        // See if Word of the Day is pressed
        // Run Word Profile Fragment on the Word of the Day
        tvWordoftheDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add word to the history
                int currentSize = WordHistory.length;
                int newSize = currentSize + 1;
                String[] tempArray = new String[ newSize ];
                for (int i=0; i < currentSize; i++)
                {
                    tempArray[i] = WordHistory[i];
                }
                tempArray[newSize- 1] = wordoftheday;
                WordHistory = tempArray;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_1, WordHistory);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new ListClickHandler());

                // Run Word of the Day Fragment
                word = wordoftheday;
                getFragmentManager().beginTransaction()
                        .replace(R.id.main, new WordProfileFragment()).commit();
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
                            .setRequestedPreviewSize(400, 200)
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
        public void onBeginningOfSpeech() {
            Toast.makeText(getActivity(), "Recording", Toast.LENGTH_SHORT).show();
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
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
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
            /*
            WordProfileFragment fragment = new WordProfileFragment();
            FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"Word Profile");
            fragmenttransaction.commit();
            */
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
