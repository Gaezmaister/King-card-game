package com.example.king;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Game extends AppCompatActivity   implements SensorEventListener {
    private static final String LOG_TAG = Game.class.getSimpleName();
    private SensorManager sensorManager;
    private Sensor mLight;
    public static final int chooseInt = 1;
    static String reply;
    static String reply2;
    static String reply3;
    static int score1=0;
    static int score2=0;
    static int score3=0;
    static int score4=0;
    BufferedReader message;
    String numbers = "";
    String other="";
    String order;
    String email="";
    String guest="";
    static String name="";
    static int hands=0;
    String numberPlayer = "";
    Boolean valueUpdated = false;
    private BufferedReader in = null;

    private static final int SERVERPORT = 8080;
    private final String SERVER_IP = "10.0.2.2";
    public static final int Result = 1;
    EditText edn;
    TextView edn2;

    public static Socket socket;

    public static Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        Game.socket = socket;
    }
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        order = intent.getStringExtra("order");
        email = intent.getStringExtra("email");
        guest = intent.getStringExtra("guest");
        edn = (EditText) findViewById(R.id.nick);
        if(guest.equals("guest"))
            edn.setVisibility(View.INVISIBLE);
        edn2= (TextView) findViewById(R.id.description);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        new Thread(new Game.ClientThread()).start();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onCreate");
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

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mLight, sensorManager.SENSOR_DELAY_NORMAL);
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onPause");
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

class ClientThread implements Runnable {

    @Override
    public void run() {

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            socket = new Socket(serverAddr, SERVERPORT);


        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

            try {
                message = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        if(hands==0) {

            try {
                numbers = message.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                numberPlayer = message.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                other = message.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        valueUpdated = true;
        }

    }




    public void start(View v) {
        Toast toast;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Intent myIntent;

            myIntent= new Intent(Game.this, StartGame.class);
        String name=edn.getText().toString();
        if(name.equals(""))
            name="Player1";
        myIntent.putExtra("nickname", name); //Optional parameterss
        myIntent.putExtra("email", email); //Optional parameterss
        myIntent.putExtra("guest", guest); //Optional parameterss
        if(hands==0) {
            myIntent.putExtra("hand", "0"); //Optional parameterss
            myIntent.putExtra("numberPlayer", numberPlayer); //Optional parameterss
            myIntent.putExtra("numbers", numbers); //Optional parameterss
            myIntent.putExtra("other", other); //Optional parameterss
        }

        myIntent.putExtra("order", order); //Optional parameterss
        if(valueUpdated) {
            if(hands>0) {
                other="";
                myIntent.putExtra("numbers", reply); //Optional parameterss
                myIntent.putExtra("numberPlayer", reply2); //Optional parameterss
                myIntent.putExtra("other", reply3); //Optional parameterss
                myIntent.putExtra("hand", String.valueOf(hands)); //Optional parameterss
                startActivityForResult(myIntent, Result);
            }
            else
                startActivityForResult(myIntent, Result);
        }
            else {
                toast =
                        Toast.makeText(context, "server down", duration);
                toast.show();
            }

    }

    public void openUrlInChrome(View v) {
        String url="https://www.google.it/";
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == chooseInt) { // Identify activity
            if (resultCode == RESULT_OK) { // Activity succeeded
                hands++;
                reply = data.getStringExtra("cards");
                reply2 = data.getStringExtra("player");
                reply3 = data.getStringExtra("newPlay");
                score1 += Integer.parseInt(data.getStringExtra("score1"));
                score2 += Integer.parseInt(data.getStringExtra("score2"));
                score3 += Integer.parseInt(data.getStringExtra("score3"));
                score4 += Integer.parseInt(data.getStringExtra("score4"));

                edn.setVisibility(View.INVISIBLE);
                edn2.setText("No women. The purpose of this hand is to not take any woman of any suit. Otherwise, 3 negative points are awarded for each card taken between them");
                if(hands==2){
                    end();
                    score1=0;
                    hands=0;
                    score2=0;
                    score3=0;
                    score4=0;
                }

            }
        }

    }

    public void showResults(View v){
        Intent myIntent = new Intent(Game.this, Results.class);
        name= edn.getText().toString();
        if(name.equals(""))
            name="Player1";
        myIntent.putExtra("score1", String.valueOf(score1)); //Optional parameterss
        myIntent.putExtra("score2", String.valueOf(score2)); //Optional parameterss
        myIntent.putExtra("score3", String.valueOf(score3)); //Optional parameterss
        myIntent.putExtra("score4", String.valueOf(score4)); //Optional parameterss
        myIntent.putExtra("namePlayer", name); //Optional parameterss
        startActivity(myIntent);
    }

    public void end(){
        Intent myIntent = new Intent(Game.this, End.class);
        myIntent.putExtra("order", order); //Optional parameterss
        if(name.equals(""))
            name="Player1";
        myIntent.putExtra("score1", String.valueOf(score1)); //Optional parameterss
        myIntent.putExtra("score2", String.valueOf(score2)); //Optional parameterss
        myIntent.putExtra("score3", String.valueOf(score3)); //Optional parameterss
        myIntent.putExtra("score4", String.valueOf(score4)); //Optional parameterss
        myIntent.putExtra("namePlayer", name); //Optional parameterss
        startActivity(myIntent);
        finish();
    }
}
