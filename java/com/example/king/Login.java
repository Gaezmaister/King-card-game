package com.example.king;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Login extends AppCompatActivity  implements SensorEventListener {
    private static final String LOG_TAG = Login.class.getSimpleName();
    private SensorManager sensorManager;
    private Sensor mLight;
    File file;
    File file2;
    File path;
    String email="";
    String guest="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        email=intent.getStringExtra("email");
        guest=intent.getStringExtra("guest");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Intent in= getIntent();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux =  event.values[0];
        ConstraintLayout bgElement = findViewById(R.id.container);
        String mytext;
        mytext=Float.toString(lux);
        if(lux<20000)
            bgElement.setBackgroundColor(Color.LTGRAY);
        else
            bgElement.setBackgroundColor(Color.WHITE);

    }

    public Boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return true;
        else
            return false;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Play(View v){
        RadioButton increasing = (RadioButton) findViewById(R.id.increasing);
        RadioButton decreasing = (RadioButton) findViewById(R.id.decreasing);
        Intent myIntent = new Intent(Login.this, Game.class);
        myIntent.putExtra("email", email); //Optional parameterss
        myIntent.putExtra("guest", guest); //Optional parameterss
        if(increasing.isChecked())
            myIntent.putExtra("order", "increase"); //Optional parameterss
        else if(decreasing.isChecked())
            myIntent.putExtra("order", "decrease"); //Optional parameterss
        else
            myIntent.putExtra("order", "random"); //Optional parameterss
        startActivity(myIntent);
        try {
            saveTextPrivate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }

    public void openUrlInChrome(View v) {
        String url="https://it.wikipedia.org/wiki/King_(gioco)";
        try {
            try {
                Uri uri = Uri.parse("googlechrome://navigate?url="+ url);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse(url);
                // Chrome is probably not installed
                // OR not selected as default browser OR if no Browser is selected as default browser
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        } catch (Exception ex) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveTextPrivate() throws IOException {
        path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        file = new File(path, "kingQuestion.txt");
        file2 = new File(path, "kingAnswer.txt");


        FileOutputStream stream = new FileOutputStream(file);
        FileOutputStream stream2 = new FileOutputStream(file2);
        if(isExternalStorageReadable()) {
            try {
                stream.write("Which of the following objects is NOT comparable to a database?\n".getBytes());
                stream.write("who is?".getBytes());
                stream2.write("Phonebook\n".getBytes());
                stream2.write("Fiction book\n".getBytes());
                stream2.write("Class register".getBytes());
            } finally {
                stream.close();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mLight, sensorManager.SENSOR_DELAY_NORMAL);
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onStart");
    }
}