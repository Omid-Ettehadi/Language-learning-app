package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DictionaryFragment extends Fragment implements TextToSpeech.OnInitListener {

    private TextView tvSpeakNow;
    private EditText etInput;
    private Button btnSearch,btnSpeakNow;
    private SpeechRecognizer speechrecognizer;
    private Intent speechrecognizerIntent;
    private boolean IsListening, packageName;
    private String GoogleScore;


    public static String word;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        tvSpeakNow = (TextView) view.findViewById(R.id.tvSpeakNow);
        etInput = (EditText) view.findViewById(R.id.etInput);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnSpeakNow = (Button) view.findViewById(R.id.btnSpeakNow);

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
        btnSpeakNow.setOnClickListener(new View.OnClickListener() {
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

            for (int i = 0 ; i < matches.size() ; i++){
                if(matches.get(i).toString().equals(etInput.getText().toString())){
                    GoogleScore = String.valueOf(score[i]*100);
                    tvSpeakNow.setText( System.lineSeparator() + "Your pronunciation is at " + GoogleScore + " % ");
                    break;
                }
                else{
                    tvSpeakNow.setText( System.lineSeparator() + "Sorry, What was that?");
                }
            }
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
