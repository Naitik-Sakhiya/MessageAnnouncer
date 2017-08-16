package com.naitiks.messageannouncer.Activities;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naitiks.messageannouncer.R;

public class ReadingService extends Service {
    private WindowManager windowManager;
    static private View msgScreen;
    Context context;
    private String sender, msg;
    TextToSpeech tts;
    LayoutInflater mInflater;
    static TextView lblSender, lblMsg;
    static Button cncl, reply;
    Intent intentThis;
    SharedPreferences sharedprefence;
    int notificationVolume;
    int mediaVolume, status = 0;
    AudioManager audioManager;
    String newMsg, oldSender;
    int lang = 0, speed = 2, pitcs = 2, read = 0, silent = 1, flag = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedprefence = getApplicationContext().getSharedPreferences("msgNotifier", 0);
        intentThis = new Intent(this, ReadingService.class);
        mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        msgScreen = mInflater.inflate(R.layout.msglayout, null);
        lblSender = (TextView) msgScreen.findViewById(R.id.textView_sender);
        lblMsg = (TextView) msgScreen.findViewById(R.id.textView_msg);
        this.sender = intent.getStringExtra("sender");
        this.msg = intent.getStringExtra("msg");
        oldSender = intent.getStringExtra("sender");
        lblSender.setText("Message From: " + sender);
        lblMsg.setText("Message is: " + msg);
        if (this.sender.length() > 10) {
            try {
                this.sender = this.sender.substring(13);
            } catch (Exception e) {
            }
        }
        cncl = (Button) msgScreen.findViewById(R.id.btn_cancel);
        cncl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    windowManager.removeView(msgScreen);
                    tts.stop();
                    stopSelf();
                } catch (Exception e) {
                    stopSelf();
                }
            }
        });
        reply = (Button) msgScreen.findViewById(R.id.btn_reply);
        reply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    tts.stop();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            final Intent i = new Intent(ReadingService.this, MessageSendingActivity.class);
                            i.putExtra("sender", oldSender);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(i);
                            try {
                                if (tts.isSpeaking())
                                    tts.stop();
                            } catch (Exception e) {
                            }
                        }
                    }, 1000);
                } catch (Exception e) {
                    stopSelf();
                }

            }
        });
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        windowManager.addView(msgScreen, params);
        readMsg();
        return START_NOT_STICKY;
    }

    private void readMsg() {
        if (sharedprefence.contains("language"))
            lang = sharedprefence.getInt("language", 0);
        if (sharedprefence.contains("speed"))
            speed = sharedprefence.getInt("speed", 0);
        if (sharedprefence.contains("pitcs"))
            speed = sharedprefence.getInt("pitcs", 0);
        if (sharedprefence.contains("read"))
            read = sharedprefence.getInt("read", 0);
        if (sharedprefence.contains("silent"))
            silent = sharedprefence.getInt("silent", 0);
        tts = new TextToSpeech(getApplicationContext(), new OnInitListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onInit(int status) {
                int result = tts.setLanguage(Locale.US);
                if (status == TextToSpeech.SUCCESS) {

                    switch (pitcs) {
                        case 0:
                            tts.setPitch((float) 0.5);
                            break;
                        case 1:
                            tts.setPitch((float) 0.75);
                            break;
                        case 2:
                            tts.setPitch((float) 1.0);
                            break;
                        case 3:
                            tts.setPitch((float) 1.5);
                            break;
                        case 4:
                            tts.setPitch((float) 2.0);
                            break;
                        default:
                            break;
                    }
                    switch (speed) {
                        case 0:
                            tts.setSpeechRate((float) 0.5);
                            break;
                        case 1:
                            tts.setSpeechRate((float) 0.75);
                            break;
                        case 2:
                            tts.setSpeechRate((float) 1.0);
                            break;
                        case 3:
                            tts.setSpeechRate((float) 1.5);
                            break;
                        case 4:
                            tts.setSpeechRate((float) 2.0);
                            break;
                        default:
                            break;
                    }
                    switch (lang) {
                        case 0:
                            result = tts.setLanguage(Locale.US);
                            break;
                        case 1:
                            result = tts.setLanguage(Locale.UK);
                            break;
                        case 2:
                            result = tts.setLanguage(Locale.GERMAN);
                            break;
                        case 3:
                            result = tts.setLanguage(Locale.ITALIAN);
                            break;
                        case 4:
                            result = tts.setLanguage(Locale.KOREAN);
                            break;
                        default:
                            break;
                    }
                    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL)
                        flag = 1;
                    else if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL && silent == 1)
                        flag = 0;

                    notificationVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                    mediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
                    final int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVol / 1.25), 0);

                    if (read == 0)
                        newMsg = "Message from" + sender + "Message is" + msg;
                    else if (read == 1)
                        newMsg = "Message from" + sender;
                    else if (read == 2)
                        newMsg = "Message is" + msg;
                    else if (read == 3)
                        newMsg = "";
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "Language is not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        if (flag == 1) {
                            tts.speak(newMsg, TextToSpeech.QUEUE_FLUSH, null);
                            manageDisplay();
                        }

                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        try {
            windowManager.removeView(msgScreen);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    final Handler newhandler = new Handler();

    private void manageDisplay() {
        handler.postDelayed(new Runnable() {
            public void run() {
                if (tts.isSpeaking()) {
                    manageHandler();
                } else if (!tts.isSpeaking()) {
                    manageDisplay();
                }
            }
        }, 1000);
    }

    final Handler handler = new Handler();

    private void manageHandler() {
        handler.postDelayed(new Runnable() {
            public void run() {
                if (tts.isSpeaking()) {
                    manageHandler();
                } else if (!tts.isSpeaking()) {
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notificationVolume, 0);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediaVolume, 0);
                    try {
                        windowManager.removeView(msgScreen);
                        stopSelf();
                    } catch (Exception e) {
                        stopSelf();
                    }
                }
            }
        }, 1000);
    }
}
