package com.example.king;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;


public class Buy extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = Buy.class.getSimpleName();
    public static final int chooseMethod = 1;
    private SensorManager sensorManager;
    private Sensor mLight;
    String[] urls = new String[5];
    ImageView image;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    Boolean setImage=false;

    Intent myIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        image= (ImageView)findViewById(R.id.type);
        myIntent = new Intent();
        urls[0]="https://www.idmconsulting.it/wp-content/uploads/2017/02/visa-logo-png.png";
        urls[1]="https://banner2.cleanpng.com/20180802/xri/kisspng-logo-mastercard-vector-graphics-font-visa-mastercard-logo-png-photo-png-arts-5b634298cd58d5.9008352515332317688411.jpg";
        urls[2]="https://cdn3.iconfinder.com/data/icons/payment-method-1/64/_Postepay-512.png";
        urls[3]="https://cdn1.iconfinder.com/data/icons/logos-and-brands-3/512/23_Apple_Pay_Credit_Card_logo_logos-512.png";
        urls[4]="http://www.artandpole.it/wp-content/uploads/2016/08/Paypal-logo.png.png";
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

    public void choose(View v){
        Intent ChooseIntent = new Intent(Buy.this, ChooseMethod.class);
        startActivityForResult(ChooseIntent, chooseMethod);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String method="";
        if (requestCode == chooseMethod) { // Identify activity
            if (resultCode == RESULT_OK) { // Activity succeeded
                method = data.getStringExtra("method");
                if (method != null) {
                    switch (method) {
                        case "Visa":
                            parallel(0);
                            break;
                        case "Mastercard":
                            parallel(1);
                            break;
                        case "PostePay":
                            parallel(2);
                            break;
                        case "ApplePay":
                            parallel(3);
                            break;
                        case "Paypal":
                            parallel(4);
                            break;
                    }
                }

            }
        }
    }

    public void parallel(int i){
        new Buy.DownloadImageTask(image).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urls[i]);
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            Matrix matrix = new Matrix();
            Bitmap rotatedBitmap=mIcon;
            for(int i=0;i<20;i++) {
                matrix.postRotate(90);
                rotatedBitmap = Bitmap.createBitmap(mIcon, 0, 0, mIcon.getWidth(), mIcon.getHeight(), matrix, true);
            }
            return rotatedBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            setImage=true;
        }
    }

    public void closeApp(View v){
        Toast toast = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        t1= findViewById(R.id.editText2);
        t2= findViewById(R.id.editText3);
        t3= findViewById(R.id.editText4);
        t4= findViewById(R.id.editText);
        if(!t1.getText().toString().equals("") && !t2.getText().toString().equals("") && !t3.getText().toString().equals("") && !t4.getText().toString().equals("") && setImage)
            finish();
        else {
            toast =
                    Toast.makeText(context, "you must to complete all fields!", duration);
            toast.setGravity(Gravity.TOP|Gravity.LEFT, 200, 1600);

            toast.show();
        }
    }



}
