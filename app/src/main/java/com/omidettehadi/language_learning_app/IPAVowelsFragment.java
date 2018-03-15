package com.omidettehadi.language_learning_app;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;

import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static com.omidettehadi.language_learning_app.SigninActivity.word;

public class IPAVowelsFragment extends Fragment{

    private Button btni,btnɪ, btne, btnɛ, btnæ, btna;
    private Button btnə, btnɜ;
    private Button btnu, btnʊ, btno, btnʌ, btnɔ, btnɒ, btnɑ;

    public IPAVowelsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ipavowels, container, false);

        btni = view.findViewById(R.id.btni);
        btnɪ = view.findViewById(R.id.btnɪ);
        btne = view.findViewById(R.id.btne);
        btnɛ = view.findViewById(R.id.btnɛ);
        btnæ = view.findViewById(R.id.btnæ);
        btna = view.findViewById(R.id.btna);

        btnə = view.findViewById(R.id.btnə);
        btnɜ = view.findViewById(R.id.btnɜ);

        btnu = view.findViewById(R.id.btnu);
        btnʊ = view.findViewById(R.id.btnʊ);
        btno = view.findViewById(R.id.btno);
        btnʌ = view.findViewById(R.id.btnʌ);
        btnɔ = view.findViewById(R.id.btnɔ);
        btnɒ = view.findViewById(R.id.btnɒ);
        btnɑ = view.findViewById(R.id.btnɑ);


        btni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.close_front_unrounded_vowel);
                vowel.start();
            }
        });

        btnɪ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.near_close_near_front_unrounded_vowel);
                vowel.start();
            }
        });

        btne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.close_mid_front_unrounded_vowel);
                vowel.start();
            }
        });

        btnɛ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_mid_front_unrounded_vowel);
                vowel.start();
            }
        });

        btnæ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(), R.raw.near_open_front_unrounded_vowel);
                vowel.start();
            }
        });

        btna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(), R.raw.open_front_unrounded_vowel);
                vowel.start();
            }
        });





        btnə.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.mid_central_vowel);
                vowel.start();
            }
        });

        btnɜ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_mid_central_unrounded_vowel);
                vowel.start();
            }
        });





        btnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.close_back_rounded_vowel);
                vowel.start();
            }
        });

        btnʊ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.near_close_near_back_rounded_vowel);
                vowel.start();
            }
        });

        btno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.close_mid_back_rounded_vowel);
                vowel.start();
            }
        });

        btnɔ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_mid_back_rounded_vowel);
                vowel.start();
            }
        });

        btnʌ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_mid_back_unrounded_vowel);
                vowel.start();
            }
        });

        btnɒ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_back_rounded_vowel);
                vowel.start();
            }
        });

        btnɑ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_back_unrounded_vowel);
                vowel.start();
            }
        });


        return view;
    }
}
