package com.example.king;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class End extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = End.class.getSimpleName();
    BufferedReader message;
    private static final int SERVERPORT = 8080;
    private static final String SERVER_IP = "10.0.2.2";
    public static Socket socket;
    static boolean answered=false;

    public static Socket getSocket() {
        return socket;
    }

    private SensorManager sensorManager;
    private Sensor mLight;
    File file;
    File file2;
    File path;
    String order;
    static boolean lose=false;
    boolean done=false;

    private View.OnLongClickListener b1OnLongClickListener = new View.OnLongClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onLongClick(View v) {
            lose=false;
            answered=false;
            showQuestion();
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String score1 = "";
        String score2 = "";
        String score3 = "";
        String score4 = "";
        String name = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Button b1 = (Button) findViewById(R.id.tryMe);
        b1.setOnLongClickListener(b1OnLongClickListener);
        Intent intent = getIntent();
        score1 = intent.getStringExtra("score1");
        score2 = intent.getStringExtra("score2");
        score3 = intent.getStringExtra("score3");
        score4 = intent.getStringExtra("score4");
        name = intent.getStringExtra("namePlayer");
        order = intent.getStringExtra("order");
        if (score1 == null)
            score1 = "0";
        if (score2 == null)
            score2 = "0";
        if (score3 == null)
            score3 = "0";
        if (score4 == null)
            score4 = "0";
        TextView edn = findViewById(R.id.textView4);
        edn.setText(name+" score= " + score1 + "\nPlayer2 score= " + score2 + "\nPlayer3 score= " + score3 + "\nPlayer4 score= " + score4);

        new Thread(new End.ClientThread()).start();
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
        float lux = event.values[0];
        ConstraintLayout bgElement = findViewById(R.id.container);
        if (lux < 20000)
            bgElement.setBackgroundColor(Color.LTGRAY);
        else
            bgElement.setBackgroundColor(Color.WHITE);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showQuestion() {
        Button b1 = (Button) findViewById(R.id.first);
        Button b2 = (Button) findViewById(R.id.second);
        Button b3 = (Button) findViewById(R.id.third);
        TextView tv = (TextView) findViewById(R.id.question);
        TextView tv2 = (TextView) findViewById(R.id.asyncStatus);
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        b3.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);
        startTask();
        try {
            ReadTextPrivate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ReadTextPrivate() throws IOException {
        int length;
        byte[] bytes;
        FileInputStream in;
        String contents;
        String contents2;
        Button b1 = (Button) findViewById(R.id.first);
        Button b2 = (Button) findViewById(R.id.second);
        Button b3 = (Button) findViewById(R.id.third);
        TextView tv = (TextView) findViewById(R.id.question);
        path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        file = new File(path, "kingQuestion.txt");
        file2 = new File(path, "kingAnswer.txt");


        if (file == null || !file.exists())
            tv.setText("file not exists");
        else {
            length = (int) file.length();
            bytes = new byte[length];
            in = new FileInputStream(file);
            try {
                in.read(bytes);
            } finally {
                in.close();
            }
            contents = new String(bytes);
            String[] array = contents.split("\n");
            tv.setText(array[0]);
        }

        if (file2 == null || !file2.exists())
            tv.setText("file not exists");
        else {
            length = (int) file2.length();
            bytes = new byte[length];
            in = new FileInputStream(file2);
            try {
                in.read(bytes);
            } finally {
                in.close();
            }
            contents2 = new String(bytes);
            String[] array = contents2.split("\n");
            b1.setText(array[0]);
            b2.setText(array[1]);
            b3.setText(array[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }






    public void startTask () {
        TextView tv= (TextView)findViewById(R.id.asyncStatus);
        tv.setText("10 seconds");
        new SimpleAsyncTask().execute(10);
    }

    private class SimpleAsyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... ints) {
            int duration = ints[0];
            while(duration!=0){
                duration=duration-1;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(duration);
            }
            return "time's over";

        }

        protected void onPostExecute(String result) {
            Button b1=(Button) findViewById(R.id.first);
            Button b2=(Button) findViewById(R.id.second);
            Button b3=(Button) findViewById(R.id.third);
            Intent finalIntent = new Intent(End.this, Buy.class);
            b1.setEnabled(false);
            b2.setEnabled(false);
            b3.setEnabled(false);
            lose=true;
            done=true;
            if(!answered) {
                startActivity(finalIntent);
                finish();
            }
        }

        @SuppressLint("WrongThread")
        protected void onProgressUpdate(Integer... progress) {
            TextView tv= (TextView)findViewById(R.id.asyncStatus);
            if(!progress[0].toString().equals("1"))
                tv.setText(progress[0].toString()+" seconds");
            else
                tv.setText(progress[0].toString()+" second");
        }
    }

    public void seeResult(View v){

        Button b2=(Button) findViewById(R.id.second);
        Intent myIntent = new Intent(End.this, MainActivity.class);
        Intent finalIntent = new Intent(End.this, Buy.class);
        if(v.getId()==b2.getId()) {
            startActivity(myIntent);
            done=true;
            answered=true;
            finish();
        }
        else{
            startActivity(finalIntent);
            lose=true;
            done=true;
            answered=true;
            finish();
        }




    }

    public void buy(View v){
        Intent finalIntent = new Intent(End.this, Buy.class);
        lose=true;
        done=true;
        startActivity(finalIntent);
        finish();
    }


    class ClientThread implements Runnable {

        @Override
        public void run() {

            socket = Game.getSocket();

            OutputStream output = null;
            try {
                output = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PrintWriter writer = null;
            if (output != null) {
                writer = new PrintWriter(output, true);
            }
            boolean z=false;
            while(!z) {
                if(done) {
                    if (lose) {
                        writer.println("lose");
                        done=false;
                        z=true;
                    }
                    else {
                        writer.println("win");
                        done=false;
                        lose=false;
                        z=true;
                    }
                }

            }


        }

    }

}
