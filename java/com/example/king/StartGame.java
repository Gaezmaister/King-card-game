package com.example.king;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static android.graphics.Color.parseColor;


public class StartGame extends AppCompatActivity   implements SensorEventListener {
    private static final String LOG_TAG = StartGame.class.getSimpleName();
    private SensorManager sensorManager;
    private Sensor mLight;
    ImageView campo;
    ImageView player2;
    ImageView player3;
    ImageView player4;
    ImageView image;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    ImageView image5;
    ImageView image6;
    ImageView image7;
    ImageView image8;
    ImageView image9;
    ImageView image10;
    ImageView image11;
    ImageView image12;
    ImageView image13;
    ImageView[] images = new ImageView[13];
    String number;
    BufferedReader in;
    double cartaGiocata=0.0;
    BufferedReader reader = null;
    TextView edn;
    TextView edn2;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    static Intent intent;
    static String finalScore;
    static String finalScore1;
    static String finalScore2;
    static String finalScore3;
    String score1 = "";
    String score2 = "";
    String score3 = "";
    String score4 = "";
    boolean arrived = false;
    static String initialPlayer="";
    static String newOther="";
    static String newCards="";
    static String newPlayer="";
    static String newPlay="";
    int i = 0;
    double[] aux = new double[13];
    boolean[] carta = new boolean[53];
    double[] numbers=new double[13];
    String order = "";
    String actualHand="";
    Socket socket;
    Boolean played = false;
    String other = "";
    Boolean moved = false;
    String numberPlayer = "";
    String email="";
    String guest="";
    Boolean check = false; //control that a person order the cards


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        File path = getApplicationContext().getFilesDir();
        campo = (ImageView) findViewById(R.id.campo);
        player2 = (ImageView) findViewById(R.id.player2);
        player3 = (ImageView) findViewById(R.id.player3);
        player4 = (ImageView) findViewById(R.id.player4);
        String nickname = "";
        intent = getIntent();
        number = intent.getStringExtra("numbers");

        other = intent.getStringExtra("other");
        email = intent.getStringExtra("email");
        guest = intent.getStringExtra("guest");
        if(!guest.equals("guest"))
            loadImageFromStorage(path.toString());
        int i = 0;
        Log.d("ahah: ", number);
        String[] arrOfStr = number.split(",");
        for(int j=0; j<arrOfStr.length;j++)
            numbers[j]=Double.parseDouble(arrOfStr[j]);

        for(int k=0; k<numbers.length;k++){
            aux[k]=numbers[k];
            setCarta(k, numbers[k]);
            if((int)numbers[k]!=0)
                carta[(int)numbers[k]]=true;
        }

        edn = findViewById(R.id.nickname);
        edn2 = findViewById(R.id.title);
        nickname = intent.getStringExtra("nickname");
        order = intent.getStringExtra("order");

        actualHand = intent.getStringExtra("hand");

        Log.d("handsNow: ", actualHand);
        Log.d("handsNow: ", nickname);

        if (order != null && (order.equals("increase") || order.equals("decrease")))
            order();
        numberPlayer = intent.getStringExtra("numberPlayer");
        initialPlayer=numberPlayer;
        if (numberPlayer != null) {
            Log.d("player", numberPlayer);
        }
        arrived=true;
        edn.setText(nickname);
        if(actualHand.equals("0"))
            edn2.setText("No K e J");
        else if(actualHand.equals("1"))
            edn2.setText("No Q");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        new Thread(new StartGame.ClientThread()).start();
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

    protected void setImageAsc(ImageView image, double randomNumero){
        if (randomNumero == 13.5 || randomNumero == 1)
            image.setImageResource(R.drawable.assocuori);
        else if (randomNumero == 26.5 || randomNumero == 14)
            image.setImageResource(R.drawable.assodenari);
        else if (randomNumero == 39.5 || randomNumero == 27)
            image.setImageResource(R.drawable.assofiori);
        else if (randomNumero == 52.5 || randomNumero == 40)
            image.setImageResource(R.drawable.assopicche);
        else if (randomNumero == 2)
            image.setImageResource(R.drawable.duecuori);
        else if (randomNumero == 15)
            image.setImageResource(R.drawable.duedenari);
        else if (randomNumero == 28)
            image.setImageResource(R.drawable.duefiori);
        else if (randomNumero == 41)
            image.setImageResource(R.drawable.duepicche);
        else if (randomNumero == 3)
            image.setImageResource(R.drawable.trecuori);
        else if (randomNumero == 16)
            image.setImageResource(R.drawable.tredenari);
        else if (randomNumero == 29)
            image.setImageResource(R.drawable.trefiori);
        else if (randomNumero == 42)
            image.setImageResource(R.drawable.trepicche);
        else if (randomNumero == 4)
            image.setImageResource(R.drawable.quattrocuori);
        else if (randomNumero == 17)
            image.setImageResource(R.drawable.quattrodenari);
        else if (randomNumero == 30)
            image.setImageResource(R.drawable.quattrofiori);
        else if (randomNumero == 43)
            image.setImageResource(R.drawable.quattropicche);
        else if (randomNumero == 5)
            image.setImageResource(R.drawable.cinquecuori);
        else if (randomNumero == 18)
            image.setImageResource(R.drawable.cinquedenari);
        else if (randomNumero == 31)
            image.setImageResource(R.drawable.cinquefiori);
        else if (randomNumero == 44)
            image.setImageResource(R.drawable.cinquepicche);
        else if (randomNumero == 6)
            image.setImageResource(R.drawable.seicuori);
        else if (randomNumero == 19)
            image.setImageResource(R.drawable.seidenari);
        else if (randomNumero == 32)
            image.setImageResource(R.drawable.seifiori);
        else if (randomNumero == 45)
            image.setImageResource(R.drawable.seipicche);
        else if (randomNumero == 7)
            image.setImageResource(R.drawable.settecuori);
        else if (randomNumero == 20)
            image.setImageResource(R.drawable.settedenari);
        else if (randomNumero == 33)
            image.setImageResource(R.drawable.settefiori);
        else if (randomNumero == 46)
            image.setImageResource(R.drawable.settepicche);
        else if (randomNumero == 8)
            image.setImageResource(R.drawable.ottocuori);
        else if (randomNumero == 21)
            image.setImageResource(R.drawable.ottodenari);
        else if (randomNumero == 34)
            image.setImageResource(R.drawable.ottofiori);
        else if (randomNumero == 47)
            image.setImageResource(R.drawable.ottopicche);
        else if (randomNumero == 9)
            image.setImageResource(R.drawable.novecuori);
        else if (randomNumero == 22)
            image.setImageResource(R.drawable.novedenari);
        else if (randomNumero == 35)
            image.setImageResource(R.drawable.novefiori);
        else if (randomNumero == 48)
            image.setImageResource(R.drawable.novepicche);
        else if (randomNumero == 10)
            image.setImageResource(R.drawable.diecicuori);
        else if (randomNumero == 23)
            image.setImageResource(R.drawable.diecidenari);
        else if (randomNumero == 36)
            image.setImageResource(R.drawable.diecifiori);
        else if (randomNumero == 49)
            image.setImageResource(R.drawable.diecipicche);
        else if (randomNumero == 11)
            image.setImageResource(R.drawable.jackcuori);
        else if (randomNumero == 24)
            image.setImageResource(R.drawable.jackdenari);
        else if (randomNumero == 37)
            image.setImageResource(R.drawable.jackfiori);
        else if (randomNumero == 50)
            image.setImageResource(R.drawable.jackpicche);
        else if (randomNumero == 12)
            image.setImageResource(R.drawable.donnacuori);
        else if (randomNumero == 25)
            image.setImageResource(R.drawable.donnadenari);
        else if (randomNumero == 38)
            image.setImageResource(R.drawable.donnafiori);
        else if (randomNumero == 51)
            image.setImageResource(R.drawable.donnapicche);
        else if (randomNumero == 13)
            image.setImageResource(R.drawable.kappacuori);
        else if (randomNumero == 26)
            image.setImageResource(R.drawable.kappadenari);
        else if (randomNumero == 39)
            image.setImageResource(R.drawable.kappafiori);
        else if (randomNumero == 52)
            image.setImageResource(R.drawable.kappapicche);
    }

    protected void setImageDesc(){
        int countA=0;
        int countB=0;
        int countC=0;
        int countD=0;
        int count;
        double[] a=new double[13];
        double[] b=new double[13];
        double[] c=new double[13];
        double[] d=new double[13];
        for(int i=0;i<aux.length;i++){
            if(aux[i]<14){
                a[countA]=aux[i];
                countA++;
            } else if(aux[i]>13 && aux[i]<27){
                b[countB]=aux[i];
                countB++;
            } else if(aux[i]>26 && aux[i]<40){
                c[countC]=aux[i];
                countC++;
            } else
                d[countD]=aux[i];
            countD++;
        }
        bubbleSort(a);
        bubbleSort(b);
        bubbleSort(c);
        bubbleSort(d);
        reverse(a);
        reverse(b);
        reverse(c);
        reverse(d);
        int i;
        count=0;
        for (i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                aux[i] = a[i];
                count++;
            }
        }

        for (i = 0; i < b.length; i++) {
            if (b[i] != 0) {
                aux[count] = b[i];
                count++;
            }
        }

        for (i = 0; i < c.length; i++) {
            if (c[i] != 0) {
                aux[count] = c[i];
                count++;
            }
        }

        for (i = 0; i < c.length; i++) {
            if (d[i] != 0) {
                aux[count] = d[i];
                count++;
            }
        }
    }


    protected void setCarta(int immagine, double randomNumero) {

        image = (ImageView) findViewById(R.id.firstcard);
        image2 = (ImageView) findViewById(R.id.secondcard);
        image3 = (ImageView) findViewById(R.id.thirdcard);
        image4 = (ImageView) findViewById(R.id.fourthcard);
        image5 = (ImageView) findViewById(R.id.fifthcard);
        image6 = (ImageView) findViewById(R.id.sixthcard);
        image7 = (ImageView) findViewById(R.id.seventhcard);
        image8 = (ImageView) findViewById(R.id.eighthcard);
        image9 = (ImageView) findViewById(R.id.ninthcard);
        image10 = (ImageView) findViewById(R.id.tenthcard);
        image11 = (ImageView) findViewById(R.id.eleventhcard);
        image12 = (ImageView) findViewById(R.id.twelthcard);
        image13 = (ImageView) findViewById(R.id.thirteenthcard);
        images[0]=image;
        images[1]=image2;
        images[2]=image3;
        images[3]=image4;
        images[4]=image5;
        images[5]=image6;
        images[6]=image7;
        images[7]=image8;
        images[8]=image9;
        images[9]=image10;
        images[10]=image11;
        images[11]=image12;
        images[12]=image13;
        if (immagine == 0) {
            setImageAsc(image,randomNumero);
        }
        else if (immagine == 1) {
            setImageAsc(image2,randomNumero);
        }
        else if (immagine == 2) {
            setImageAsc(image3,randomNumero);
        }
        else if (immagine == 3) {
            setImageAsc(image4,randomNumero);
        }
        else if (immagine == 4) {
            setImageAsc(image5,randomNumero);
        }
        else if (immagine == 5) {
            setImageAsc(image6,randomNumero);
        }
        else if (immagine == 6) {
            setImageAsc(image7,randomNumero);
        }
        else if (immagine == 7) {
            setImageAsc(image8,randomNumero);
        }
        else if (immagine == 8) {
            setImageAsc(image9,randomNumero);
        }
        else if (immagine == 9) {
            setImageAsc(image10,randomNumero);
        }
        else if (immagine == 10) {
            setImageAsc(image11,randomNumero);
        }
        else if (immagine == 11) {
            setImageAsc(image12,randomNumero);
        }
        else if (immagine == 12) {
            setImageAsc(image13,randomNumero);
        }

    }

    public static void bubbleSort(double[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n; i++) {
            swapped = false;
            for (int j = 1; j < (n - i); j++)
                if (arr[j - 1] > arr[j]) {
                    double temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                    swapped = true;
                }
            if (!swapped)
                break;
        }
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

    public static void reverse(double[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            double temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    public void order() {
        for(i=0;i<numbers.length;i++) {
            if (numbers[i] == 1.0)
                numbers[i] = 13.5;
            else if (numbers[i] == 14.0)
                numbers[i] = 26.5;
            else if (numbers[i] == 27.0)
                numbers[i] = 39.5;
            else if (numbers[i] == 40.0)
                numbers[i] = 52.5;
        }
        bubbleSort(numbers);
        if(order.equals("decrease"))
            setImageDesc();

        check=true;
        aux = new double[13];

        int k = 0;
        for (int i = 0; i < carta.length; i++) {
            if (carta[i]) {
                aux[k] = i;
                k++;
            }
        }

        for(i=0;i<aux.length;i++) {
            if (aux[i] == 1.0)
                aux[i] = 13.5;
            else if (aux[i] == 14.0)
                aux[i] = 26.5;
            else if (aux[i] == 27.0)
                aux[i] = 39.5;
            else if (aux[i] == 40.0)
                aux[i] = 52.5;
        }
        bubbleSort(aux);
        int j = 0;

        if (order.equals("decrease"))
            setImageDesc();
        while (j < 13) {
            setCarta(j, aux[j]);
            j++;
        }
    }



    private void loadImageFromStorage(String path)
    {
        Log.d("emailStart",email);
        try {
            Log.d("emailStart",email);
            File f=new File(path, email+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imgPicker);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public void move(View v) {
        image = (ImageView) findViewById(R.id.firstcard);
        image2 = (ImageView) findViewById(R.id.secondcard);
        image3 = (ImageView) findViewById(R.id.thirdcard);
        image4 = (ImageView) findViewById(R.id.fourthcard);
        image5 = (ImageView) findViewById(R.id.fifthcard);
        image6 = (ImageView) findViewById(R.id.sixthcard);
        image7 = (ImageView) findViewById(R.id.seventhcard);
        image8 = (ImageView) findViewById(R.id.eighthcard);
        image9 = (ImageView) findViewById(R.id.ninthcard);
        image10 = (ImageView) findViewById(R.id.tenthcard);
        image11 = (ImageView) findViewById(R.id.eleventhcard);
        image12 = (ImageView) findViewById(R.id.twelthcard);
        image13 = (ImageView) findViewById(R.id.thirteenthcard);
        ImageView campo = (ImageView) findViewById(R.id.campo);
        campo.setVisibility(View.VISIBLE);
        v.setVisibility(View.INVISIBLE);


        if(v.getId()==image.getId()) {
            campo.setImageDrawable(image.getDrawable());
            cartaGiocata=aux[0];
            numbers[0]=0;
            played = true;
        }
        else if(v.getId()==image2.getId()){
            campo.setImageDrawable(image2.getDrawable());
            cartaGiocata=aux[1];
            numbers[1]=0;
            played = true;
        }
        else if(v.getId()==image3.getId()){
            campo.setImageDrawable(image3.getDrawable());
            cartaGiocata=aux[2];
            numbers[2]=0;
            played = true;
        }
        else if(v.getId()==image4.getId()){
            campo.setImageDrawable(image4.getDrawable());
            cartaGiocata=aux[3];
            numbers[3]=0;
            played = true;
        }
        else if(v.getId()==image5.getId()){
            campo.setImageDrawable(image5.getDrawable());
            cartaGiocata=aux[4];
            numbers[4]=0;
            played = true;
        }
        else if(v.getId()==image6.getId()){
            campo.setImageDrawable(image6.getDrawable());
            cartaGiocata=aux[5];
            numbers[5]=0;
            played = true;
        }
        else if(v.getId()==image7.getId()){
            campo.setImageDrawable(image7.getDrawable());
            cartaGiocata=aux[6];
            numbers[6]=0;
            played = true;
        }
        else if(v.getId()==image8.getId()){
            campo.setImageDrawable(image8.getDrawable());
            cartaGiocata=aux[7];
            numbers[7]=0;
            played = true;
        }
        else if(v.getId()==image9.getId()){
            campo.setImageDrawable(image9.getDrawable());
            cartaGiocata=aux[8];
            numbers[8]=0;
            played = true;
        }
        else if(v.getId()==image10.getId()){
            campo.setImageDrawable(image10.getDrawable());
            cartaGiocata=aux[9];
            numbers[9]=0;
            played = true;
        }
        else if(v.getId()==image11.getId()){
            campo.setImageDrawable(image11.getDrawable());
            cartaGiocata=aux[10];
            numbers[10]=0;
            played = true;
        }
        else if(v.getId()==image12.getId()){
            campo.setImageDrawable(image12.getDrawable());
            cartaGiocata=aux[11];
            numbers[11]=0;
            played = true;
        }
        else if(v.getId()==image13.getId()){
            campo.setImageDrawable(image13.getDrawable());
            cartaGiocata=aux[12];
            numbers[12]=0;
            played = true;
        }

        moved=true;
    }




    class ClientThread implements Runnable {

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {

            tv1 = findViewById(R.id.score1);
            tv2 = findViewById(R.id.score2);
            tv3 = findViewById(R.id.score3);
            tv4 = findViewById(R.id.score4);
            if(numberPlayer.equals("1"))
                tv1.setTextColor(parseColor("RED"));
            else if(numberPlayer.equals("2"))
                tv2.setTextColor(parseColor("RED"));
            else if(numberPlayer.equals("3"))
                tv3.setTextColor(parseColor("RED"));
            else
                tv4.setTextColor(parseColor("RED"));
            String score1="";
            String score2="";
            String score3="";
            String score4="";
            ImageView imageview = (ImageView) findViewById(R.id.player2);
            ImageView imageview2 = (ImageView) findViewById(R.id.player3);
            ImageView imageview3 = (ImageView) findViewById(R.id.player4);
            socket = Game.getSocket();
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

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


            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
                double[] internal=new double[3];
                String[] arrOfStr = other.split(",");
                if(!numberPlayer.equals("1")){
                    for(int i=0; i<arrOfStr.length;i++)
                        internal[i]=Double.parseDouble(arrOfStr[i]);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    setImages(internal[0]);

                    if(arrived) {
                        if (numberPlayer.equals("2")) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    setImageAsc(imageview, internal[0]);
                                    setImageAsc(imageview2, internal[1]);
                                    setImageAsc(imageview3, internal[2]);

                                }
                            });
                        } else if (numberPlayer.equals("3")) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    setImageAsc(imageview2, internal[0]);
                                    setImageAsc(imageview3, internal[2]);

                                }
                            });
                        } else if (numberPlayer.equals("4")) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    setImageAsc(imageview3, internal[0]);

                                }
                            });
                        }
                    }

                    while(true){
                        if(moved) {
                            Log.d("ciao","ciaociao");
                            if (numberPlayer.equals("4")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        setImageAsc(imageview, internal[1]);
                                        setImageAsc(imageview2, internal[2]);
                                    }
                                });
                                if (writer != null) {
                                    writer.println(cartaGiocata);
                                }
                                break;
                            } else if (numberPlayer.equals("3")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        setImageAsc(imageview, internal[1]);
                                    }
                                });
                                if (writer != null) {
                                    writer.println(cartaGiocata);
                                }
                                break;
                            } else if (numberPlayer.equals("2")) {
                                if (writer != null) {
                                    writer.println(cartaGiocata);
                                }
                                break;
                            }
                        }

                            }
                            /**/

                }
                else {
                    while(true) {
                        if (moved) {
                            Log.d("b", "bbb");
                            Log.d("ciao", String.valueOf(cartaGiocata));
                            if(cartaGiocata==13.5)
                                cartaGiocata=1.0;
                            else if(cartaGiocata==26.5)
                                cartaGiocata=14.0;
                            else if(cartaGiocata==39.5)
                                cartaGiocata=27.0;
                            else if(cartaGiocata==52.5)
                                cartaGiocata=40.0;
                            if (writer != null) {
                                writer.println(cartaGiocata);
                            }
                            String otherPlayersIf1 = "";
                            if (numberPlayer.equals("1")) {
                                try {
                                    otherPlayersIf1 = in.readLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("otheee", otherPlayersIf1);
                                double[] othersPlay = new double[3];
                                String[] str = otherPlayersIf1.split(",");
                                for (int i = 0; i < str.length; i++)
                                    othersPlay[i] = Double.parseDouble(str[i]);

                                for (int i = 0; i < othersPlay.length; i++)
                                    Log.d("other",Double.toString(othersPlay[i]));
                                Log.d("a","asdsafsaf");
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        setImageAsc(imageview, othersPlay[0]);
                                        setImageAsc(imageview2, othersPlay[1]);
                                        setImageAsc(imageview3, othersPlay[2]);
                                    }
                                });
                            }
                            break;
                        }

                        else
                            Log.d("kjergkrek","");
                    }
                }
            try {
                score1= in.readLine();
                score2= in.readLine();
                score3= in.readLine();
                score4= in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                numberPlayer= in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("numberPlayerAfter",numberPlayer);
            finalScore = score1;
            finalScore1 = score2;
            finalScore2 = score3;
            finalScore3 = score4;
            Log.d("score1",score1);
            Log.d("score2",score2);
            Log.d("score3",score3);
            Log.d("score4",score4);

            runOnUiThread(new Runnable() {
                public void run() {
                    tv1.setText("your score= " + finalScore);
                    tv2.setText("player2 score= " + finalScore1);
                    tv3.setText("player3 score= " + finalScore2);
                    tv4.setText("player4 score= " + finalScore3);
                    tv1.setTextColor(parseColor("BLACK"));
                    tv2.setTextColor(parseColor("BLACK"));
                    tv3.setTextColor(parseColor("BLACK"));
                    tv4.setTextColor(parseColor("BLACK"));

                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    campo.setImageDrawable(null);
                    imageview.setImageDrawable(null);
                    imageview2.setImageDrawable(null);
                    imageview3.setImageDrawable(null);
                }
            });
            moved=false;
            Log.d("asaa",numberPlayer);
            Log.d("moved",Boolean.toString(moved));
            int numberPlay=2;

            while(numberPlay<14){
                runOnUiThread(new Runnable() {
                    public void run() {
                if(numberPlayer.equals("1"))
                    tv1.setTextColor(parseColor("RED"));
                else if(numberPlayer.equals("2"))
                    tv2.setTextColor(parseColor("RED"));
                else if(numberPlayer.equals("3"))
                    tv3.setTextColor(parseColor("RED"));
                else
                    tv4.setTextColor(parseColor("RED"));
                    }
                });
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(order.equals("decrease")) {
                            for (int k = 0; k < aux.length; k++) {
                                if (aux[k] != 0) {
                                    setImageDesc();
                                    setCarta(k, aux[k]);
                                }
                            }
                        }
                        else{
                            for (int k = 0; k < numbers.length; k++) {
                                if (numbers[k] != 0) {
                                    setCarta(k, numbers[k]);
                                }
                            }
                        }
                        for(int i=0;i<numbers.length;i++) {
                            images[i].setEnabled(true);
                            images[i].setAlpha(255);
                        }
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!numberPlayer.equals("1")) {
                    try {
                        newOther = in.readLine();
                        newOther = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                sendOtherPlays(numberPlayer);
                Log.d("ciao",numberPlayer);
                newOther="";
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        campo.setImageDrawable(null);
                        imageview.setImageDrawable(null);
                        imageview2.setImageDrawable(null);
                        imageview3.setImageDrawable(null);
                        tv1.setTextColor(parseColor("BLACK"));
                        tv2.setTextColor(parseColor("BLACK"));
                        tv3.setTextColor(parseColor("BLACK"));
                        tv4.setTextColor(parseColor("BLACK"));
                    }
                });
                try {
                    numberPlayer= in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                moved=false;
                numberPlay++;
            }
            if(actualHand.equals("0")) {
                try {
                    newPlayer= in.readLine();
                    newCards= in.readLine();
                    newPlay= in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d("score1After",finalScore);
            Log.d("score2After",finalScore1);
            Log.d("score3After",finalScore2);
            Log.d("score4After",finalScore3);
            if(actualHand.equals("0")) {
                Log.d("newPlayer", newPlayer);
                Log.d("newCards", newCards);
                Log.d("newPlay", newPlay);
                intent.putExtra("cards", newCards); //Optional parameters
                intent.putExtra("player", newPlayer); //Optional parameters
                intent.putExtra("newPlay", newPlay); //Optional parameters
            }
            setResult(RESULT_OK, intent);
            finish();
            }




        protected void sendOtherPlays(String numberPlayer) {


            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            tv1 = findViewById(R.id.score1);
            tv2 = findViewById(R.id.score2);
            tv3 = findViewById(R.id.score3);
            tv4 = findViewById(R.id.score4);
            ImageView imageview = (ImageView) findViewById(R.id.player2);
            ImageView imageview2 = (ImageView) findViewById(R.id.player3);
            ImageView imageview3 = (ImageView) findViewById(R.id.player4);
            OutputStream output = null;

            try {
                output = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter writer = new PrintWriter(output, true);

            double[] internal = new double[3];
            boolean x=false;

                if (!numberPlayer.equals("1")) {
                    Log.d("qui","qui");
                    String[] arrOfStr = newOther.split(",");
                    for (int i = 0; i < arrOfStr.length; i++)
                        internal[i] = Double.parseDouble(arrOfStr[i]);
                    setImages(internal[0]);
                    if (numberPlayer.equals("2")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                setImageAsc(imageview, internal[0]);
                                setImageAsc(imageview2, internal[1]);
                                setImageAsc(imageview3, internal[2]);

                            }
                        });
                    } else if (numberPlayer.equals("3")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                setImageAsc(imageview2, internal[0]);
                                setImageAsc(imageview3, internal[2]);

                            }
                        });
                    } else if (numberPlayer.equals("4")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                setImageAsc(imageview3, internal[0]);

                            }
                        });
                    }
                    boolean y=false;
                    while (!y) {
                        if (moved) {
                            if (numberPlayer.equals("4")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        setImageAsc(imageview, internal[1]);
                                        setImageAsc(imageview2, internal[2]);
                                    }
                                });
                                writer.println(cartaGiocata);
                                y=true;
                            } else if (numberPlayer.equals("3")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        setImageAsc(imageview, internal[1]);
                                    }
                                });
                                writer.println(cartaGiocata);
                                y=true;
                            } else if (numberPlayer.equals("2")) {
                                writer.println(cartaGiocata);
                                y=true;
                            }
                        }

                    }
                }else {
                    while (!x) {

                        if (moved) {
                            Log.d("moved",Boolean.toString(moved));
                            Log.d("b", "bbb");
                            Log.d("ciao", String.valueOf(cartaGiocata));
                            writer.println(cartaGiocata);
                            String otherPlayersIf1 = "";
                                try {
                                    otherPlayersIf1 = in.readLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("otheee", otherPlayersIf1);
                                double[] othersPlay = new double[3];
                                String[] str = otherPlayersIf1.split(",");
                                for (int i = 0; i < str.length; i++)
                                    othersPlay[i] = Double.parseDouble(str[i]);

                                for (int i = 0; i < othersPlay.length; i++)
                                    Log.d("other", Double.toString(othersPlay[i]));
                                Log.d("a", "asdsafsaf");
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        setImageAsc(imageview, othersPlay[0]);
                                        setImageAsc(imageview2, othersPlay[1]);
                                        setImageAsc(imageview3, othersPlay[2]);
                                    }
                                });
                            x=true;
                        }


                    }
                }

                try {
                    score1 = in.readLine();
                    score2 = in.readLine();
                    score3 = in.readLine();
                    score4 = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String finalScore = score1;
                String finalScore1 = score2;
                String finalScore2 = score3;
                String finalScore3 = score4;

            Log.d("score1",score1);
            Log.d("score2",score2);
            Log.d("score3",score3);
            Log.d("score4",score4);
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    public void run() {
                        tv1.setText("your score= " + finalScore);
                        tv2.setText("player2 score= " + finalScore1);
                        tv3.setText("player3 score= " + finalScore2);
                        tv4.setText("player4 score= " + finalScore3);
                        intent.putExtra("score1", finalScore); //Optional parameters
                        intent.putExtra("score2", finalScore1); //Optional parameters
                        intent.putExtra("score3", finalScore2); //Optional parameters
                        intent.putExtra("score4", finalScore3); //Optional parameters

                    }
                });

            }


            protected void setImages(double number){
                if(number<14)
                    setImagesAux(1,13.5);
                else if(number>13.5 && number<27)
                    setImagesAux(14,26.5);
                else if(number>26.5 && number<40)
                    setImagesAux(27,39.5);
                else if(number>39.5)
                    setImagesAux(40,52.5);
            }

            protected void setImagesAux(int thresholdMin, double thresholdMax){
                boolean youCan=false;
                Log.d("thresholdMin",Double.toString(thresholdMin));
                Log.d("thresholdMax",Double.toString(thresholdMax));
                for(int k=0; k<numbers.length;k++) {
                    Log.d("for","ciao");
                    if (numbers[k]>=thresholdMin && numbers[k]<=thresholdMax)
                        youCan=true;
                }
                Log.d("youCan",Boolean.toString(youCan));
                if(youCan) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            for (int i = 0; i < numbers.length; i++) {
                                if (numbers[i] < thresholdMin || numbers[i] > thresholdMax) {
                                    images[i].setEnabled(false);
                                    images[i].setImageResource(R.drawable.retro);
                                }

                            }
                        }
                    });
                }
            }
    }
}



