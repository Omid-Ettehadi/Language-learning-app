package com.omidettehadi.language_learning_app;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;


public class DictionaryFragment extends Fragment implements TextToSpeech.OnInitListener {

    private TextView tvResult, tvSpeakNow;
    private EditText etInput;
    private Button btnSearch, btnVoice, btnSpeakNow;
    private TextToSpeech tts;
    private SpeechRecognizer speechrecognizer;
    private Intent speechrecognizerIntent;
    private boolean IsListening, packageName;
    private String GoogleScore;


    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        // Inflate the layout for this fragment
        tvResult = (TextView) view.findViewById(R.id.tvResult);
        tvSpeakNow = (TextView) view.findViewById(R.id.tvSpeakNow);
        etInput = (EditText) view.findViewById(R.id.etInput);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnVoice = (Button) view.findViewById(R.id.btnVoice);
        btnSpeakNow = (Button) view.findViewById(R.id.btnSpeakNow);
        tts = new TextToSpeech(getActivity(), DictionaryFragment.this);


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
                // Running lookup from Oxford API dictionary
                new CallbackTask().execute(dictionaryEntries());
            }
        });

        // See if Voice Button is pressed
        // Run SpeakOut
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pronounce the word
                speakOut();
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

    protected class SpeechRecognitionListener implements RecognitionListener
    {

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

    private String dictionaryEntries() {

        final String language = "en";
        final String word = etInput.getText().toString();
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }

    //in android calling network requests on the main thread forbidden by default
    //create class to do async job
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
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
            // Definition for local variables
            String definition = "";
            int number = 0;

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

                        number += 1 ;
                        definition += System.lineSeparator();
                        definition += number + ") " + IPA.toString().substring(IPA.toString().lastIndexOf(":")+2,IPA.toString().lastIndexOf('"'));

                        for(int k = 0 ; k < three.length() ; k++){
                            JSONObject senses = three.getJSONObject(k);
                            JSONArray four = senses.getJSONArray("senses");
                            for(int l = 0 ; l < four.length() ; l++) {
                                JSONObject definitions = four.getJSONObject(l);
                                JSONArray five = definitions.getJSONArray("definitions");
                                definition += System.lineSeparator() + " - ";
                                definition += five.getString(0);
                            }
                        }
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            tvResult.setText(definition);

        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)
        {
            int result = tts.setLanguage(Locale.US);
            tts.setPitch(1);
            tts.setSpeechRate(1);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            } else {
                speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (speechrecognizer != null)
        {
            speechrecognizer.destroy();
        }
        super.onDestroy();
    }
    private void speakOut() {
        tts.speak(etInput.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        Log.e("root ", etInput.getText().toString());
    }
}
