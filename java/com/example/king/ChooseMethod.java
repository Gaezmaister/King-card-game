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
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.InputStream;

public class ChooseMethod extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = ChooseMethod.class.getSimpleName();
    private SensorManager sensorManager;
    private Sensor mLight;


    Intent myIntent;
    RadioButton rbVisa,rbMastercard, rbPaypal, rbPostePay, rbApplePay;
    ImageView[] images = new ImageView[5];
    String[] urls = new String[5];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_method);
        myIntent = new Intent();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        rbVisa = (RadioButton) findViewById(R.id.Visa);
        rbMastercard = (RadioButton) findViewById(R.id.Mastercard);
        rbPostePay = (RadioButton) findViewById(R.id.PostePay);
        rbApplePay = (RadioButton) findViewById(R.id.ApplePay);
        rbPaypal = (RadioButton) findViewById(R.id.Paypal);


        images[0] = (ImageView)findViewById(R.id.first);
        images[1] = (ImageView)findViewById(R.id.second);
        images[2] = (ImageView)findViewById(R.id.third);
        images[3] = (ImageView)findViewById(R.id.fourth);
        images[4] = (ImageView)findViewById(R.id.fifth);

        urls[0]="https://www.idmconsulting.it/wp-content/uploads/2017/02/visa-logo-png.png";
        urls[1]="https://banner2.cleanpng.com/20180802/xri/kisspng-logo-mastercard-vector-graphics-font-visa-mastercard-logo-png-photo-png-arts-5b634298cd58d5.9008352515332317688411.jpg";
        urls[2]="https://cdn3.iconfinder.com/data/icons/payment-method-1/64/_Postepay-512.png";
        urls[3]="https://cdn1.iconfinder.com/data/icons/logos-and-brands-3/512/23_Apple_Pay_Credit_Card_logo_logos-512.png";
        urls[4]="http://www.artandpole.it/wp-content/uploads/2016/08/Paypal-logo.png.png";

        parallel();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG, "onCreate");
    }

    public void SelectInt(View v) {
        Toast toast = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if(rbVisa.isChecked()) {
            myIntent.putExtra("method", "Visa"); //Optional parameters
            setResult(RESULT_OK, myIntent);
            finish();
        }
        else if(rbMastercard.isChecked()){
            myIntent.putExtra("method", "Mastercard"); //Optional parameters
            setResult(RESULT_OK, myIntent);
            finish();
        }

        else if(rbPaypal.isChecked()){
            myIntent.putExtra("method", "Paypal"); //Optional parameters
            setResult(RESULT_OK, myIntent);
            finish();
        }

        else if(rbPostePay.isChecked()){
            myIntent.putExtra("method", "PostePay"); //Optional parameters
            setResult(RESULT_OK, myIntent);
            finish();
        }

        else if(rbApplePay.isChecked()){
            myIntent.putExtra("method", "ApplePay"); //Optional parameters
            setResult(RESULT_OK, myIntent);
            finish();
        }

        else {
            toast =
                    Toast.makeText(context, "you must to choose one!", duration);

            toast.setGravity(Gravity.TOP|Gravity.LEFT, 200, 1250);
            toast.show();
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

    public void parallel(){
        for(int i=0; i<5;i++)
            new DownloadImageTask(images[i]).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urls[i]);
    }


    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
        }
    }




}
