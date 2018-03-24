package com.example.root.akuvo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.BassBoost;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


public class MicToSpeakerActivity extends AppCompatActivity {

    //Audio
    private Button mOn;
    private boolean isOn;
    private boolean isRecording;
    private AudioRecord record;
    private AudioTrack player;
    private AudioManager manager;
    private int recordState, playerState;
    private int minBuffer;

    //Audio Settings
    private final int source = MediaRecorder.AudioSource.CAMCORDER;
    private final int channel_in = AudioFormat.CHANNEL_IN_MONO;
    private final int channel_out = AudioFormat.CHANNEL_OUT_MONO;
    private final int format = AudioFormat.ENCODING_PCM_16BIT;

    private final static int REQUEST_ENABLE_BT = 1;
    private boolean IS_HEADPHONE_AVAILBLE=false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic_to_speaker);

        //Reduce latancy
        setVolumeControlStream(AudioManager.MODE_IN_COMMUNICATION);


        mOn = (Button) findViewById(R.id.button);
        isOn = false;
        isRecording = false;

        manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        manager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        /*

        //Check for headset availability
        AudioDeviceInfo[] audioDevices = manager.getDevices(AudioManager.GET_DEVICES_ALL);
        for(AudioDeviceInfo deviceInfo : audioDevices) {
            if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET || deviceInfo.getType() == AudioDeviceInfo.TYPE_USB_HEADSET) {
                IS_HEADPHONE_AVAILBLE = true;
            }
        }
        if (!IS_HEADPHONE_AVAILBLE){
            // get delete_audio_dialog.xml view

            LayoutInflater layoutInflater = LayoutInflater.from(MicToSpeakerActivity.this);
            View promptView = layoutInflater.inflate(R.layout.insert_headphone_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MicToSpeakerActivity.this);
            alertDialogBuilder.setView(promptView);

            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(getIntent()));
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(MicToSpeakerActivity.this,MainActivity.class));
                                    dialog.cancel();
                                }
                            });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
*/
        initAudio();

        mOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mOn.getBackground().setColorFilter(getResources().getColor(!isOn ? R.color.colorOn : R.color.colorOff), PorterDuff.Mode.SRC_ATOP);
                isOn = !isOn;
                if(isOn) {
                    (new Thread() {
                        @Override
                        public void run()
                        {
                            startAudio();
                        }
                    }).start();
                } else {
                    endAudio();
                }
            }
        });
    }
    public void initAudio() {
        //Tests all sample rates before selecting one that works
        int sample_rate = getSampleRate();
        minBuffer = AudioRecord.getMinBufferSize(sample_rate, channel_in, format);

        record = new AudioRecord(source, sample_rate, channel_in, format, minBuffer);
        recordState = record.getState();
        int id = record.getAudioSessionId();
        Log.d("Record", "ID: " + id);
        playerState = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            player = new AudioTrack(
                    new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),
                    new AudioFormat.Builder().setEncoding(format).setSampleRate(sample_rate).setChannelMask(channel_out).build(),
                    minBuffer,
                    AudioTrack.MODE_STREAM,
                    AudioManager.AUDIO_SESSION_ID_GENERATE);
            playerState = player.getState();
            // Formatting Audio
            if(AcousticEchoCanceler.isAvailable()) {
                AcousticEchoCanceler echo = AcousticEchoCanceler.create(id);
                echo.setEnabled(true);
                Log.d("Echo", "Off");
            }
            if(NoiseSuppressor.isAvailable()) {
                NoiseSuppressor noise = NoiseSuppressor.create(id);
                noise.setEnabled(true);
                Log.d("Noise", "Off");
            }
            if(AutomaticGainControl.isAvailable()) {
                AutomaticGainControl gain = AutomaticGainControl.create(id);
                gain.setEnabled(false);
                Log.d("Gain", "Off");
            }
            BassBoost base = new BassBoost(1, player.getAudioSessionId());
            base.setStrength((short) 1000);
        }
    }

    public void startAudio() {
        int read = 0, write = 0;
        if(recordState == AudioRecord.STATE_INITIALIZED && playerState == AudioTrack.STATE_INITIALIZED) {
            record.startRecording();
            player.play();
            isRecording = true;
            Log.d("Record", "Recording...");
        }
        while(isRecording) {
            short[] audioData = new short[minBuffer];
            if(record != null)
                read = record.read(audioData, 0, minBuffer);
            else
                break;
            Log.d("Record", "Read: " + read);
            if(player != null)
                write = player.write(audioData, 0, read);
            else
                break;
            Log.d("Record", "Write: " + write);
        }
    }

    public void endAudio() {
        if(record != null) {
            if(record.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING)
                record.stop();
            isRecording = false;
            Log.d("Record", "Stopping...");
        }
        if(player != null) {
            if(player.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
                player.stop();
            isRecording = false;
            Log.d("Player", "Stopping...");
        }
    }

    public int getSampleRate() {
        //Find a sample rate that works with the device
        for (int rate : new int[] {8000, 11025, 16000,  22050, 44100, 48000}) {
            int buffer = AudioRecord.getMinBufferSize(rate, channel_in, format);
            if (buffer > 0)
                return rate;
        }
        return -1;
    }

}