package com.example.king;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Register extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = Register.class.getSimpleName();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private SensorManager sensorManager;
    private Sensor mLight;
    static Bitmap imageBitmap;
    String url = "";
    ImageView image;
    EditText t1;
    EditText t2;
    EditText t3;
    EditText t4;
    EditText t5;
    File file;
    File file2;
    File path;

    String email="";
    String[] listOfEmails;

    EditText edn;
    Intent myIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        image= (ImageView)findViewById(R.id.imageView);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("googlechrome://navigate?url=chrome-native://newtab/"));

        edn = (EditText) findViewById(R.id.url);
        myIntent = getIntent();
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

    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        image=(ImageView) findViewById(R.id.imageView);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void register(View v){
        Toast toast = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        t1 = (EditText) findViewById(R.id.editText2);
        t2 = (EditText) findViewById(R.id.editText6);
        t3 = (EditText) findViewById(R.id.editText7);
        t4 = (EditText) findViewById(R.id.editText5);
        t5 = (EditText) findViewById(R.id.editText3);
        if(!t1.getText().toString().equals("") && !t2.getText().toString().equals("") && !t3.getText().toString().equals("") && !t4.getText().toString().equals("")){
            save();
            setResult(RESULT_OK, myIntent);
        }
        else {
            toast =
                    Toast.makeText(context, "you must to complete all fields!", duration);
            toast.setGravity(Gravity.TOP|Gravity.LEFT, 200, 1425);

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

    public void parallel(View v){
        url=edn.getText().toString();
        Toast toast;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if(!url.equals("")) {
            for (int i = 0; i < 5; i++)
                new Register.DownloadImageTask(image).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
        else {

            toast =
                    Toast.makeText(context, "image url empty", duration);
            toast.show();
        }
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                imageBitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            Matrix matrix = new Matrix();
            Bitmap rotatedBitmap=imageBitmap;
            for(int i=0;i<20;i++) {
                matrix.postRotate(90);
                rotatedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
            }
            return rotatedBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    public void saveToInternalStorage(Bitmap bitmapImage){
        File path= getApplicationContext().getFilesDir();
        // path to /data/data/yourapp/app_data/imageDir
        //File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        email=t4.getText().toString();
        File mypath=new File(path,email+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void save(){
        try {
            saveTextPrivate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return true;
        else
            return false;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveTextPrivate() throws IOException {
        Toast toast = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        file = new File(path, "email.txt");
        file2 = new File(path, "password.txt");
        String password="";
        String emailTemp=t4.getText().toString();
        email=t4.getText().toString()+"\n";
        password=t5.getText().toString()+"\n";
        FileOutputStream stream = new FileOutputStream(file,true);
        FileOutputStream stream2 = new FileOutputStream(file2,true);
        if(file!=null){
            ReadTextPrivate();
            for(int i=0;i<listOfEmails.length;i++) {
                if (emailTemp.equals(listOfEmails[i])) {
                    toast =
                            Toast.makeText(context, "this email already exists!", duration);
                    toast.setGravity(Gravity.TOP | Gravity.LEFT, 200, 1425);

                    toast.show();
                }
                else if(listOfEmails[i].equals("")|| i==listOfEmails.length-1){
                    if(isExternalStorageReadable()) {
                        try {
                            stream.write(email.getBytes());
                            stream2.write(password.getBytes());
                        } finally {
                            stream.close();
                        }
                        saveToInternalStorage(imageBitmap);
                        finish();
                    }
                }

            }
        }

        else if(isExternalStorageReadable()) {
            try {
                stream.write(email.getBytes());
                stream2.write(password.getBytes());
            } finally {
                stream.close();
            }
            saveToInternalStorage(imageBitmap);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ReadTextPrivate() throws IOException {
        String contents = "";
        int length;
        byte[] bytes;
        FileInputStream in;
        TextView tv = (TextView) findViewById(R.id.question);
        path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        file = new File(path, "email.txt");
        file2 = new File(path, "password.txt");

        length = (int) file.length();
        bytes = new byte[length];
        in = new FileInputStream(file);
        try {
            in.read(bytes);
        } finally {
            in.close();
        }
        contents = new String(bytes);
        listOfEmails = contents.split("\n");

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
            //contents2 = new String(bytes);
            //emails = contents2.split("\n");
        }
    }
}
