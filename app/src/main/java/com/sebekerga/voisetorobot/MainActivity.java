package com.sebekerga.voisetorobot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    public OutputStream Outstream;
    public BluetoothDevice device;
    public BluetoothAdapter bluetooth;
    BluetoothSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> DevList = bluetooth.getBondedDevices();

        for (BluetoothDevice i : DevList) {
            Log.e("Name", i.getName());
            if (i.getName().equals("Bob")) device = i;
        }
        if (device == null) {
            finish();
        }

        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            socket.connect();
            Outstream = socket.getOutputStream();
            byte ke1[] = {0x08, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA6, 0x00, 0x02,
                    0x08, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA6, 0x00, 0x04};
            Outstream.write(ke1);
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }


        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String command = result.get(0).split(" ", 2)[0];
                    switch (command) {
                        case "вперед":
                            try {
                                byte ke1[] = {0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x02, (byte) 0x81,(byte) 30,
                                        0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x04, (byte) 0x81,(byte) 30};
                                Outstream.write(ke1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "назад":
                            try {
                                byte ke1[] = {0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x02, (byte) 0x81,(byte) -30,
                                        0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x04, (byte) 0x81,(byte) -30};
                                Outstream.write(ke1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "направо":
                            try {
                                byte ke1[] = {0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x02, (byte) 0x81,(byte) 0,
                                        0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x04, (byte) 0x81,(byte) 30};
                                Outstream.write(ke1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "налево":
                            try {
                                byte ke1[] = {0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x02, (byte) 0x81,(byte) 30,
                                        0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x04, (byte) 0x81,(byte) 0};
                                Outstream.write(ke1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            try {
                                byte ke1[] = {0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x02, (byte) 0x81,(byte) 0,
                                        0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x04, (byte) 0x81,(byte) 0};
                                Outstream.write(ke1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                    }

                }
                break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            onDestroy();
        }
    }
}
