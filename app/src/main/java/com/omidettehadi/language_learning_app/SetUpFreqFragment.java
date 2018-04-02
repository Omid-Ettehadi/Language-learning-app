package com.omidettehadi.language_learning_app;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import me.anwarshahriar.calligrapher.Calligrapher;

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
import static com.omidettehadi.language_learning_app.MainActivity.a_1;
import static com.omidettehadi.language_learning_app.MainActivity.a_2;
import static com.omidettehadi.language_learning_app.MainActivity.e_1;
import static com.omidettehadi.language_learning_app.MainActivity.e_2;
import static com.omidettehadi.language_learning_app.MainActivity.i_1;
import static com.omidettehadi.language_learning_app.MainActivity.i_2;
import static com.omidettehadi.language_learning_app.MainActivity.o_1;
import static com.omidettehadi.language_learning_app.MainActivity.o_2;
import static com.omidettehadi.language_learning_app.MainActivity.sampleFreq;
import static com.omidettehadi.language_learning_app.MainActivity.u_1;
import static com.omidettehadi.language_learning_app.MainActivity.u_2;
import static com.omidettehadi.language_learning_app.MainActivity.æ_1;
import static com.omidettehadi.language_learning_app.MainActivity.æ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɑ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɑ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɒ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɒ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɔ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɔ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ə_1;
import static com.omidettehadi.language_learning_app.MainActivity.ə_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɛ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɛ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɜ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɜ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɪ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɪ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ʊ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ʊ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ʌ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ʌ_2;


public class SetUpFreqFragment extends Fragment {

    private Button btni,btnɪ, btne, btnɛ, btnæ, btna;
    private Button btnə, btnɜ;
    private Button btnu, btnʊ, btno, btnʌ, btnɔ, btnɒ, btnɑ;

    private Button btniR,btnɪR, btneR, btnɛR, btnæR, btnaR;
    private Button btnəR, btnɜR;
    private Button btnuR, btnʊR, btnoR, btnʌR, btnɔR, btnɒR, btnɑR;

    private TextView textView;

    private String text;
    private double ePer = 0.5;

    // Audio Recording
    private boolean recording;
    private File file;
    private AudioRecord AudioRecorded;
    private FFT AudioRecordedFFT;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_up_freq, container, false);

        // Set default font
        Calligrapher calligrapher = new Calligrapher(getContext());
        calligrapher.setFont(getActivity(),"tradegothicltstdlight.otf",true);


        // Definitions
        btni = view.findViewById(R.id.btni);
        btni.setEnabled(true);
        btnɪ = view.findViewById(R.id.btnɪ);
        btnɪ.setEnabled(false);
        btne = view.findViewById(R.id.btne);
        btne.setEnabled(false);
        btnɛ = view.findViewById(R.id.btnɛ);
        btnɛ.setEnabled(false);
        btnæ = view.findViewById(R.id.btnæ);
        btnæ.setEnabled(false);
        btna = view.findViewById(R.id.btna);
        btna.setEnabled(false);

        btnə = view.findViewById(R.id.btnə);
        btnə.setEnabled(false);
        btnɜ = view.findViewById(R.id.btnɜ);
        btnɜ.setEnabled(false);

        btnu = view.findViewById(R.id.btnu);
        btnu.setEnabled(false);
        btnʊ = view.findViewById(R.id.btnʊ);
        btnʊ.setEnabled(false);
        btno = view.findViewById(R.id.btno);
        btno.setEnabled(false);
        btnʌ = view.findViewById(R.id.btnʌ);
        btnʌ.setEnabled(false);
        btnɔ = view.findViewById(R.id.btnɔ);
        btnɔ.setEnabled(false);
        btnɒ = view.findViewById(R.id.btnɒ);
        btnɒ.setEnabled(false);
        btnɑ = view.findViewById(R.id.btnɑ);
        btnɑ.setEnabled(false);

        btniR = view.findViewById(R.id.btniR);
        btnɪR = view.findViewById(R.id.btnɪR);
        btneR = view.findViewById(R.id.btneR);
        btnɛR = view.findViewById(R.id.btnɛR);
        btnæR = view.findViewById(R.id.btnæR);
        btnaR = view.findViewById(R.id.btnaR);

        btnəR = view.findViewById(R.id.btnəR);
        btnɜR = view.findViewById(R.id.btnɜR);

        btnuR = view.findViewById(R.id.btnuR);
        btnʊR = view.findViewById(R.id.btnʊR);
        btnoR = view.findViewById(R.id.btnoR);
        btnʌR = view.findViewById(R.id.btnʌR);
        btnɔR = view.findViewById(R.id.btnɔR);
        btnɒR = view.findViewById(R.id.btnɒR);
        btnɑR = view.findViewById(R.id.btnɑR);

        textView = view.findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());


        btni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.close_front_unrounded_vowel);
                vowel.start();
            }
        });
        btniR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < i_1*(1+ePer) && answer[0] > i_1*(1-ePer)){
                            if(answer[1] < i_2*(1+ePer) && answer[1] > i_2*(1-ePer)){
                                User_i_1 = answer[0];
                                User_i_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btni.setEnabled(false);
                                btnɪ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                + "expected: " + i_1 + " - " + i_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + i_1 + " - " + i_2);
                        }
                    }
                }, 500);
            }
        });

        btnɪ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.near_close_near_front_unrounded_vowel);
                vowel.start();
            }
        });
        btnɪR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ɪ_1*(1+ePer) && answer[0] > ɪ_1*(1-ePer)){
                            if(answer[1] < ɪ_2*(1+ePer) && answer[1] > ɪ_2*(1-ePer)){
                                User_ɪ_1 = answer[0];
                                User_ɪ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnɪ.setEnabled(false);
                                btne.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ɪ_1 + " - " + ɪ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ɪ_1 + " - " + ɪ_2);
                        }
                    }
                }, 500);
            }
        });

        btne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.close_mid_front_unrounded_vowel);
                vowel.start();
            }
        });
        btneR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < e_1*(1+ePer) && answer[0] > e_1*(1-ePer)){
                            if(answer[1] < e_2*(1+ePer) && answer[1] > e_2*(1-ePer)){
                                User_e_1 = answer[0];
                                User_e_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btne.setEnabled(false);
                                btnɛ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + e_1 + " - " + e_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + e_1 + " - " + e_2);
                        }
                    }
                }, 500);
            }
        });

        btnɛ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_mid_front_unrounded_vowel);
                vowel.start();
            }
        });
        btnɛR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ɛ_1*(1+ePer) && answer[0] > ɛ_1*(1-ePer)){
                            if(answer[1] < ɛ_2*(1+ePer) && answer[1] > ɛ_2*(1-ePer)){
                                User_ɛ_1 = answer[0];
                                User_ɛ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnɛ.setEnabled(false);
                                btnæ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ɛ_1 + " - " + ɛ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ɛ_1 + " - " + ɛ_2);
                        }
                    }
                }, 500);
            }
        });

        btnæ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(), R.raw.near_open_front_unrounded_vowel);
                vowel.start();
            }
        });
        btnæR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < æ_1*(1+ePer) && answer[0] > æ_1*(1-ePer)){
                            if(answer[1] < æ_2*(1+ePer) && answer[1] > æ_2*(1-ePer)){
                                User_æ_1 = answer[0];
                                User_æ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnæ.setEnabled(false);
                                btna.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + æ_1 + " - " + æ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + æ_1 + " - " + æ_2);
                        }
                    }
                }, 500);
            }
        });

        btna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(), R.raw.open_front_unrounded_vowel);
                vowel.start();
            }
        });
        btnaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < a_1*(1+ePer) && answer[0] > a_1*(1-ePer)){
                            if(answer[1] < a_2*(1+ePer) && answer[1] > a_2*(1-ePer)){
                                User_a_1 = answer[0];
                                User_a_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btna.setEnabled(false);
                                btnə.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + a_1 + " - " + a_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + a_1 + " - " + a_2);
                        }
                    }
                }, 500);
            }
        });




        btnə.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.mid_central_vowel);
                vowel.start();
            }
        });
        btnəR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ə_1*(1+ePer) && answer[0] > ə_1*(1-ePer)){
                            if(answer[1] < ə_2*(1+ePer) && answer[1] > ə_2*(1-ePer)){
                                User_ə_1 = answer[0];
                                User_ə_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnə.setEnabled(false);
                                btnɜ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ə_1 + " - " + ə_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ə_1 + " - " + ə_2);
                        }
                    }
                }, 500);
            }
        });

        btnɜ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_mid_central_unrounded_vowel);
                vowel.start();
            }
        });
        btnɜR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ɜ_1*(1+ePer) && answer[0] > ɜ_1*(1-ePer)){
                            if(answer[1] < ɜ_2*(1+ePer) && answer[1] > ɜ_2*(1-ePer)){
                                User_ɜ_1 = answer[0];
                                User_ɜ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnɜ.setEnabled(false);
                                btnu.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ɜ_1 + " - " + ɜ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ɜ_1 + " - " + ɜ_2);
                        }
                    }
                }, 500);
            }
        });





        btnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.close_back_rounded_vowel);
                vowel.start();
            }
        });
        btnuR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < u_1*(1+ePer) && answer[0] > u_1*(1-ePer)){
                            if(answer[1] < u_2*(1+ePer) && answer[1] > u_2*(1-ePer)){
                                User_u_1 = answer[0];
                                User_u_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnu.setEnabled(false);
                                btnʊ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + u_1 + " - " + u_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + u_1 + " - " + u_2);
                        }
                    }
                }, 500);
            }
        });

        btnʊ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.near_close_near_back_rounded_vowel);
                vowel.start();
            }
        });
        btnʊR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ʊ_1*(1+ePer) && answer[0] > ʊ_1*(1-ePer)){
                            if(answer[1] < ʊ_2*(1+ePer) && answer[1] > ʊ_2*(1-ePer)){
                                User_ʊ_1 = answer[0];
                                User_ʊ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnʊ.setEnabled(false);
                                btno.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ʊ_1 + " - " + ʊ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ʊ_1 + " - " + ʊ_2);
                        }
                    }
                }, 500);
            }
        });

        btno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.close_mid_back_rounded_vowel);
                vowel.start();
            }
        });
        btnoR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < o_1*(1+ePer) && answer[0] > o_1*(1-ePer)){
                            if(answer[1] < o_2*(1+ePer) && answer[1] > o_2*(1-ePer)){
                                User_o_1 = answer[0];
                                User_o_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btno.setEnabled(false);
                                btnʌ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + o_1 + " - " + o_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + o_1 + " - " + o_2);
                        }
                    }
                }, 500);
            }
        });

        btnɔ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_mid_back_rounded_vowel);
                vowel.start();
            }
        });
        btnɔR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ɔ_1*(1+ePer) && answer[0] > ɔ_1*(1-ePer)){
                            if(answer[1] < ɔ_2*(1+ePer) && answer[1] > ɔ_2*(1-ePer)){
                                User_ɔ_1 = answer[0];
                                User_ɔ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnʌ.setEnabled(false);
                                btnɔ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ɔ_1 + " - " + ɔ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ɔ_1 + " - " + ɔ_2);
                        }
                    }
                }, 500);
            }
        });

        btnʌ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_mid_back_unrounded_vowel);
                vowel.start();
            }
        });
        btnʌR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ʌ_1*(1+ePer) && answer[0] > ʌ_1*(1-ePer)){
                            if(answer[1] < ʌ_2*(1+ePer) && answer[1] > ʌ_2*(1-ePer)){
                                User_ʌ_1 = answer[0];
                                User_ʌ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnɔ.setEnabled(false);
                                btnɒ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ʌ_1 + " - " + ʌ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ʌ_1 + " - " + ʌ_2);
                        }
                    }
                }, 500);
            }
        });

        btnɒ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_back_rounded_vowel);
                vowel.start();
            }
        });
        btnɒR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ɒ_1*(1+ePer) && answer[0] > ɒ_1*(1-ePer)){
                            if(answer[1] < ɒ_2*(1+ePer) && answer[1] > ɒ_2*(1-ePer)){
                                User_ɒ_1 = answer[0];
                                User_ɒ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1]);
                                btnɒ.setEnabled(false);
                                btnɑ.setEnabled(true);
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ɒ_1 + " - " + ɒ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ɒ_1 + " - " + ɒ_2);
                        }
                    }
                }, 500);
            }
        });

        btnɑ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                MediaPlayer vowel = MediaPlayer.create(getContext(),R.raw.open_back_unrounded_vowel);
                vowel.start();
            }
        });
        btnɑR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Thread recordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        StartRecording();
                    }
                });
                recordThread.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recording = false;
                        double[] answer = FreqAnalysis();
                        if(answer[0] < ɑ_1*(1+ePer) && answer[0] > ɑ_1*(1-ePer)){
                            if(answer[1] < ɑ_2*(1+ePer) && answer[1] > ɑ_2*(1-ePer)){
                                User_ɑ_1 = answer[0];
                                User_ɑ_2 = answer[1];
                                textView.setText("The values are set at " + answer[0] + " & " + answer[1] + "\n" + "You are now done!");
                            } else{
                                textView.setText("Try again!" + "\n"
                                        + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                        + "expected: " + ɑ_1 + " - " + ɑ_2);
                            }
                        }else{
                            textView.setText("Try again!" + "\n"
                                    + "your freq: " + answer[0] + " - " + answer[1] + "\n"
                                    + "expected: " + ɑ_1 + " - " + ɑ_2);
                        }
                    }
                }, 500);
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

    private double [] FreqAnalysis() {
        double [] result = new double[2];
        int i = 0;
        double[][] answer = new double[i][3];

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataInputStream datainputStream = new DataInputStream(inputStream);
        try {
            while (datainputStream.available() > 0) {

                double[][] temp = answer;
                answer = new double[i + 1][3];
                for (int j = 0; j < i; j++) {
                    answer[j] = temp[j];
                }
                answer[i] = SampleFFT(datainputStream);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Filtering anything above 1000 and below 175
        int n = 0;
        double[][] answer2 = new double[1][3];
        for (int j = 0; j < answer.length; j++) {
            if (answer[j][0] > 175 && answer[j][0] < 1000) {
                double[][] temp = answer2;
                answer2 = new double[n + 1][3];
                for (int m = 0; m < temp.length; m++) {
                    answer2[m] = temp[m];
                }
                answer2[n] = answer[j];
                n++;
            }
        }

        double sum1 = 0;
        double sum2 = 0;
        int count = 0;

        for (int m = 1; m < answer2.length-1 ; m++){
            sum1 += answer2[m][0];
            sum2 += answer2[m][1];
            count++;
        }

        result[0] = (sum1 / count);
        result[1] = (sum2 / count);

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

            for(i = 0; i < 100; i++) {
            }

            //loops from
            max = 0.0;
            for(i = 1; i < outputBufferSize-1; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1])) {
                    max = powerSpectrum[i];
                    maxIndex[0] = i;
                }
            }
            //Log.d("TEST", "maxIndex[0] is" + maxIndex[0]);
            max = 0.0;
            for(i = 1; i < outputBufferSize-1; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1]) && (Math.abs(i - maxIndex[0]) > 3)) {
                    max = powerSpectrum[i];
                    maxIndex[1] = i;
                }
            }
            //Log.d("TEST", "maxIndex[1] is" + maxIndex[1]);
            max = 0.0;
            for(i = 1; i < outputBufferSize-1; i++) {
                if((powerSpectrum[i] > max) && (powerSpectrum[i] > powerSpectrum[i-1]) && (powerSpectrum[i] > powerSpectrum[i+1]) && (Math.abs(i - maxIndex[0]) > 3) && (Math.abs(i - maxIndex[1]) > 3)) {
                    max = powerSpectrum[i];
                    maxIndex[2] = i;
                }
            }
            //Log.d("TEST", "maxIndex[2] is" + maxIndex[2]);

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
}
