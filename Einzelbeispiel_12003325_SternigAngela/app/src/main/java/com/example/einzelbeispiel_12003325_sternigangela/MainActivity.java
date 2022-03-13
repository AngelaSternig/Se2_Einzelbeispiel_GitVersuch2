package com.example.einzelbeispiel_12003325_sternigangela;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public EditText editTextMatrikelnummer;
    public static TextView textViewResult;
    public Button buttonSend;

    public static TextView textViewCalculate;
    public Button buttonCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMatrikelnummer = findViewById(R.id.Matrikelnumber);
        buttonSend = findViewById(R.id.Senden);
        textViewResult = findViewById(R.id.Ausgabe);
        buttonCalculate = (Button) findViewById(R.id.Berechnen);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerButtonSend();
            }
        });
    }

    private void listenerButtonSend(){
        new Thread() {
            public void run() {
                String sentence = editTextMatrikelnummer.getText().toString();
                try {
                    Socket clientSocket = new Socket("se2-isys.aau.at", 53212);

                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                    BufferedReader inFromServer = new BufferedReader
                            (new InputStreamReader(clientSocket.getInputStream()));

                    outToServer.writeBytes(sentence + '\n');

                    String modifiedSentence = inFromServer.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewResult.setText(modifiedSentence);
                        }
                    });

                    clientSocket.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}