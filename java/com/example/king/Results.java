package com.example.king;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Results extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = Results.class.getSimpleName();
    private SensorManager sensorManager;
    private Sensor mLight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String score1="";
        String score2="";
        String score3="";
        String score4="";
        String name="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();
        score1 = intent.getStringExtra("score1");
        score2 = intent.getStringExtra("score2");
        score3 = intent.getStringExtra("score3");
        score4 = intent.getStringExtra("score4");
        name = intent.getStringExtra("namePlayer");
        if(score1==null)
            score1="0";
        if(score2==null)
            score2="0";
        if(score3==null)
            score3="0";
        if(score4==null)
            score4="0";
        TextView edn = findViewById(R.id.textView4);
        edn.setText(name+" score= "+score1+"\nPlayer2 score= "+score2+"\nPlayer3 score= "+score3+"\nPlayer4 score= "+score4);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onCreate");
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux =  event.values[0];
        ConstraintLayout bgElement = findViewById(R.id.container);
        if(lux<20000)
            bgElement.setBackgroundColor(Color.LTGRAY);
        else
            bgElement.setBackgroundColor(Color.WHITE);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
