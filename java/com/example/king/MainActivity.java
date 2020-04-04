package com.example.king;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final int Result = 1;
    private SensorManager sensorManager;
    private Sensor mLight;
    String[] urls = new String[5];
    ImageView image;
    File file;
    File file2;
    File path;
    Button b1;
    Button b2;
    EditText e1;
    EditText e2;
    TextView tv1;
    TextView tv2;
    String[] emails;
    String[] passwords;
    String contents = "";
    String contents2 = "";

    Intent myIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1= (Button) findViewById(R.id.button3);
        b2 = (Button) findViewById(R.id.button4);
        e1= findViewById(R.id.editText2);
        e2= findViewById(R.id.editText3);
        tv1= findViewById(R.id.textView10);
        tv2= findViewById(R.id.textView11);
        myIntent = new Intent();sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux =  event.values[0];
        ConstraintLayout bgElement = findViewById(R.id.container);
        if(lux<20000)
            bgElement.setBackgroundColor(Color.LTGRAY);
        else
            bgElement.setBackgroundColor(Color.WHITE);

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


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void register(View v){
        Intent Intent = new Intent(MainActivity.this, Register.class);
        startActivityForResult(Intent, Result);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Play(View v){
        Toast toast = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        e1= findViewById(R.id.editText2);
        e2= findViewById(R.id.editText3);

        Button b3= (Button) findViewById(R.id.button8);
        Intent finalIntent = new Intent(MainActivity.this, Login.class);
        if(v.getId()==b3.getId()) {
            finalIntent.putExtra("guest", "guest"); //Optional parameterss
            finalIntent.putExtra("email", "nothing"); //Optional parameterss
            startActivity(finalIntent);
            finish();
        }
        else {
            finalIntent.putExtra("guest", "nothing"); //Optional parameterss
            try {
                ReadTextPrivate();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String email = e1.getText().toString();
            String password = e2.getText().toString();
            int i;
            tv1 = findViewById(R.id.textView10);
            if(emails!=null) {
                for (i = 0; i < emails.length; i++) {
                    if (email.equals(emails[i]) && password.equals(passwords[i])) {
                        finalIntent.putExtra("email", email); //Optional parameterss
                        startActivity(finalIntent);
                        finish();
                        break;
                    }
                }
                if (i == emails.length) {
                    toast =
                            Toast.makeText(context, "wrong email or password!", duration);
                    toast.setGravity(Gravity.TOP | Gravity.LEFT, 200, 1425);

                    toast.show();
                }
            }

            else{
                toast =
                        Toast.makeText(context, "wrong email or password!", duration);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 200, 1425);

                toast.show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ReadTextPrivate() throws IOException {
        Toast toast = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        int length;
        byte[] bytes;
        FileInputStream in;
        TextView tv = (TextView) findViewById(R.id.question);
        path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        file = new File(path, "email.txt");
        file2 = new File(path, "password.txt");

            length = (int) file.length();
            bytes = new byte[length];
            in = new FileInputStream(file2);
            try {
                in.read(bytes);
            } finally {
                in.close();
            }
            contents = new String(bytes);
            passwords = contents.split("\n");

        if (file == null || !file.exists()){

            toast =
                    Toast.makeText(context, "wrong email or password!", duration);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 200, 1425);

            toast.show();
        }
        else {
            length = (int) file2.length();
            bytes = new byte[length];
            in = new FileInputStream(file);
            try {
                in.read(bytes);
            } finally {
                in.close();
            }
            contents2 = new String(bytes);
            emails = contents2.split("\n");
        }
    }


}
