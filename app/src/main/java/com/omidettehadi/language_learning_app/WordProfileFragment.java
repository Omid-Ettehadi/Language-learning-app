package com.omidettehadi.language_learning_app;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.omidettehadi.language_learning_app.MainActivity.word;
import static com.omidettehadi.language_learning_app.MainActivity.wordoftheday;
import static com.omidettehadi.language_learning_app.MainActivity.WordHistory;
import static com.omidettehadi.language_learning_app.MainActivity.historystatus;
import static com.omidettehadi.language_learning_app.MainActivity.email;


public class WordProfileFragment extends Fragment implements TextToSpeech.OnInitListener {

    // ----------------------------------------------------------------------------------Declaration
    // Items
    Button btnBack, btnSpeak;
    TextView tvWord, tvProfile;

    // Global Variables
    TextToSpeech tts;


    // ------------------------------------------------------------------------------------On Create
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word_profile, container, false);

        // Set default font
        Calligrapher calligrapher = new Calligrapher(getContext());
        calligrapher.setFont(getActivity(),"tradegothicltstdlight.otf",true);

        // Definitions
        btnBack = view.findViewById(R.id.btnBack);
        btnSpeak = view.findViewById(R.id.btnSpeak);

        tvWord = view.findViewById(R.id.tvWord);
        tvWord.setText(word);
        tvProfile = view.findViewById(R.id.tvProfile);

        tts = new TextToSpeech(getActivity(),WordProfileFragment.this);

        // Call Oxford Dictionary
        new CallbackTask().execute(dictionaryEntries());

        // ----------------------------------------------------------------------------------Buttons
        // See if Back Button is pressed
        // Go back to Dictionary Fragment.
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.main, new DictionaryFragment()).commit();
            }
        });

        // See if Speak Button is pressed
        // Speak out the word.
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakOut();

            }
        });

        return view;
    }

    // -------------------------------------------------------------------------------Text-to-Speech
    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
            else {
                btnSpeak.setEnabled(true);
            }
        }
        else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    private void speakOut() {
        tts.speak(word , TextToSpeech.QUEUE_FLUSH, null);
    }

    // ----------------------------------------------------------------------------Oxford Dictionary
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

                // Read the output from the server
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

                        String IPAString = IPA.toString().substring(IPA.toString().
                                lastIndexOf(":")+2,IPA.toString().lastIndexOf('"'));

                        number += 1 ;
                        definition += System.lineSeparator();
                        definition += number + ") " + IPAString + "\n" + "\n";

                        for(int k = 0 ; k < three.length() ; k++){
                            JSONObject senses = three.getJSONObject(k);
                            JSONArray four = senses.getJSONArray("senses");
                            for(int l = 0 ; l < four.length() ; l++) {
                                JSONObject definitions = four.getJSONObject(l);
                                JSONArray five = definitions.getJSONArray("definitions");
                                definition += "- ";
                                definition += five.getString(0) + "\n";
                            }
                        }
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            // Check if the word was in the dictionary or not
            if(definition.length() == 0){
                tvProfile.setText("This word does not exist! Please try again.");
            } else{
                tvProfile.setText(definition);
            }
        }
    }
}