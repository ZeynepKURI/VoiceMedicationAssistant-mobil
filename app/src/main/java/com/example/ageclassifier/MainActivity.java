package com.example.ageclassifier;/*package com.example.ageclassifier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ageclassifier.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static Intent newIntent(Context context) { Log.d(TAG,"newIntent()");
        return new Intent(context.getApplicationContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
    private static final int PERMISSION_STATE = 0;
    private static final int CAMERA_REQUEST = 1;
    private Button imgCamera;
    private ImageView imgResult;
    private Button btnPredict;
    private TextView txtPrediction;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) { Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgCamera = (Button) findViewById(R.id.captureButton);
        imgResult = (ImageView) findViewById(R.id.resultImage);
        txtPrediction = (TextView) findViewById(R.id.textViewPrediction);
        btnPredict = (Button) findViewById(R.id.scanButton);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanButton:
                predict();
                break;
            case R.id.captureButton:
                launchCamera();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() { Log.d(TAG,"onResume()");
        super.onResume();
        btnPredict.setOnClickListener(this::onClick);
        imgCamera.setOnClickListener(this::onClick);
        checkPermissions();
    }

    @Override
    protected void onPause() { Log.d(TAG,"onPause()");
        super.onPause();
        btnPredict.setOnClickListener(null);
        imgCamera.setOnClickListener(null);
    }

    private void launchCamera() { Log.d(TAG,"launchCamera()");
        startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
    }



    private void predict() { Log.d(TAG,"predict()");
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        try { Log.d(TAG,"try");
            Model model = Model.newInstance(getApplicationContext());
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
            TensorImage tensorImage = new TensorImage(DataType.UINT8);
            tensorImage.load(bitmap);
            ByteBuffer byteBuffer = tensorImage.getBuffer();

            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            // Releases model resources if no longer used.
            model.close();
            txtPrediction.setText(getMax(outputFeature0.getFloatArray()));//txtPrediction.setText(outputFeature0.getFloatArray()[0] + "\n" + outputFeature0.getFloatArray()[1] + "\n" + outputFeature0.getFloatArray()[2]);
            getMax(outputFeature0.getFloatArray());
            Log.d("Result",Arrays.toString(outputFeature0.getFloatArray()));
        } catch (IOException e) {
            Log.e(TAG,"IOException " + e.getMessage());
        }
    }

    private String getMax(float [] outputs) { Log.d(TAG,"getMax( " + Arrays.toString(outputs) + ")");
        if (outputs.length != 0 & outputs[0] > outputs[1] & outputs[0] > outputs[2] & outputs[0] > outputs[3]) {
            return "ARVELES";
        } else if (outputs.length != 0 & outputs[1] > outputs[0] & outputs[1] > outputs[2] & outputs[1] > outputs[3]) {
            return "PAROL";
        } else if (outputs.length != 0 & outputs[2] > outputs[0] & outputs[2] > outputs[1] & outputs[2] > outputs[3]) {
            return "DOLOREX";
        }else if (outputs.length != 0 & outputs[3] > outputs[0] & outputs[3] > outputs[1]  & outputs[3] > outputs[2]) {
            return "MAJEZİK";
        } else {
            return "";
        }
    }

    private void checkPermissions() {
        String[] manifestPermissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            manifestPermissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        } else {
            manifestPermissions = new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
        for (String permission : manifestPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"Permission Granted " + permission);
            }
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG,"Permission Denied " + permission);
                requestPermissions();
            }
        }
    }

    private void requestPermissions() { Log.d(TAG, "requestPermissions()");
        String[] manifestPermissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            manifestPermissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        } else {
            manifestPermissions = new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        ActivityCompat.requestPermissions(
                this,
                manifestPermissions,
                PERMISSION_STATE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "PermissionsResult requestCode " + requestCode);
        Log.d(TAG, "PermissionsResult permissions " + Arrays.toString(permissions));
        Log.d(TAG, "PermissionsResult grantResults " + Arrays.toString(grantResults));
        if (requestCode == PERMISSION_STATE) {
            for (int grantResult : grantResults) {
                switch (grantResult) {
                    case PackageManager.PERMISSION_GRANTED:
                        Log.d(TAG, "PermissionsResult grantResult Allowed " + grantResult);
                        break;
                    case PackageManager.PERMISSION_DENIED:
                        Log.d(TAG, "PermissionsResult grantResult Denied " + grantResult);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode " + requestCode + " resultCode" + resultCode + "data " + data);
         if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgResult.setImageBitmap(bitmap);
        }
    }


}
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ageclassifier.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_STATE = 0;
    private static final int CAMERA_REQUEST = 1;
    private Button imgCamera;
    private ImageView imgResult;
    private Button btnPredict;
    private TextView txtPrediction;
    private Bitmap bitmap;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCamera = findViewById(R.id.captureButton);
        imgResult = findViewById(R.id.resultImage);
        txtPrediction = findViewById(R.id.textViewPrediction);
        btnPredict = findViewById(R.id.scanButton);

        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanButton:
                predictAndSpeak();
                break;
            case R.id.captureButton:
                launchCamera();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnPredict.setOnClickListener(this::onClick);
        imgCamera.setOnClickListener(this::onClick);
        checkPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        btnPredict.setOnClickListener(null);
        imgCamera.setOnClickListener(null);
    }

    private void launchCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
    }

    private void predictAndSpeak() {
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        try {
            Model model = Model.newInstance(getApplicationContext());
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
            TensorImage tensorImage = new TensorImage(DataType.UINT8);
            tensorImage.load(bitmap);
            ByteBuffer byteBuffer = tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            model.close();
            String prediction = getMax(outputFeature0.getFloatArray());
            txtPrediction.setText(prediction);
            speak(prediction);
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
        }
    }

    private String getMax(float[] outputs) {
        if (outputs.length != 0 && outputs[0] > outputs[1] && outputs[0] > outputs[2] && outputs[0] > outputs[3]) {
            return "ARVELES";
        } else if (outputs.length != 0 && outputs[1] > outputs[0] && outputs[1] > outputs[2] && outputs[1] > outputs[3]) {
            return "PAROL";
        } else if (outputs.length != 0 && outputs[2] > outputs[0] && outputs[2] > outputs[1] && outputs[2] > outputs[3]) {
            return "DOLOREX";
        } else if (outputs.length != 0 && outputs[3] > outputs[0] && outputs[3] > outputs[1] && outputs[3] > outputs[2]) {
            return "MAJEZİK";
        } else {
            return "";
        }
    }

    private void checkPermissions() {
        String[] manifestPermissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            manifestPermissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        } else {
            manifestPermissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
        for (String permission : manifestPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, manifestPermissions, PERMISSION_STATE);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_STATE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgResult.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(new Locale("tr", "TR"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Dil desteklenmiyor");
            }
        } else {
            Log.e(TAG, "Başlatma hatası");
        }
    }

    private void speak(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "speechId");
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
*/
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ageclassifier.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_STATE = 0;
    private static final int CAMERA_REQUEST = 1;
    private Button imgKamera;
    private ImageView imgSonuc;
    private Button btnTahmin;
    private TextView txtTahmin;
    private Bitmap bitmap;
    private TextToSpeech textToSpeech;
    private String[] ilacListesi = {"ARVELES", "PAROL", "DOLOREX", "MAJEZİK"};
    private String[] ilacTanımları = {
            "Arveles, ağrı kesici ve ateş düşürücü özelliklere sahip bir ilaçtır.",
            "Parol, ağrı kesici ve ateş düşürücü özelliklere sahip bir ilaçtır.",
            "Dolorex, ağrı kesici ve ateş düşürücü özelliklere sahip bir ilaçtır.",
            "Majezik, ağrı kesici ve ateş düşürücü özelliklere sahip bir ilaçtır."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgKamera = findViewById(R.id.captureButton);
        imgSonuc = findViewById(R.id.resultImage);
        txtTahmin = findViewById(R.id.textViewPrediction);
        btnTahmin = findViewById(R.id.scanButton);

        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanButton:
                tahminEtVeOku();
                break;
            case R.id.captureButton:
                kamerayiAc();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnTahmin.setOnClickListener(this::onClick);
        imgKamera.setOnClickListener(this::onClick);
        izinleriKontrolEt();
    }

    @Override
    protected void onPause() {
        super.onPause();
        btnTahmin.setOnClickListener(null);
        imgKamera.setOnClickListener(null);
    }

    private void kamerayiAc() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
    }

    private void tahminEtVeOku() {
        if (bitmap == null) {
            txtTahmin.setText("Önce bir fotoğraf çekin.");
            return;
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        try {
            Model model = Model.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
            TensorImage tensorImage = new TensorImage(DataType.UINT8);
            tensorImage.load(bitmap);
            ByteBuffer byteBuffer = tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);

            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            model.close();

            String tahmin = enBuyuguGetir(outputFeature0.getFloatArray());
            txtTahmin.setText(tahmin);
            speak(tahmin);
        } catch (IOException e) {
            Log.e(TAG, "Hata: " + e.getMessage());
        }
    }

    private String enBuyuguGetir(float[] ciktilar) {
        if (ciktilar.length != 0 && ciktilar[0] > ciktilar[1] && ciktilar[0] > ciktilar[2] && ciktilar[0] > ciktilar[3]) {
            return ilacTanımları[0];
        } else if (ciktilar.length != 0 && ciktilar[1] > ciktilar[0] && ciktilar[1] > ciktilar[2] && ciktilar[1] > ciktilar[3]) {
            return ilacTanımları[1];
        } else if (ciktilar.length != 0 && ciktilar[2] > ciktilar[0] && ciktilar[2] > ciktilar[1] && ciktilar[2] > ciktilar[3]) {
            return ilacTanımları[2];
        } else if (ciktilar.length != 0 && ciktilar[3] > ciktilar[0] && ciktilar[3] > ciktilar[1] && ciktilar[3] > ciktilar[2]) {
            return ilacTanımları[3];
        } else {
            return "";
        }
    }

    private void izinleriKontrolEt() {
        String[] manifestIzinleri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            manifestIzinleri = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        } else {
            manifestIzinleri = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
        for (String izin : manifestIzinleri) {
            if (ContextCompat.checkSelfPermission(this, izin) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, manifestIzinleri, PERMISSION_STATE);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_STATE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgSonuc.setImageBitmap(bitmap);
            txtTahmin.setText(""); // Yeni fotoğraf çekildiğinde eski tahmin bilgisini sil
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(new Locale("tr", "TR"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Dil desteklenmiyor");
            }
        } else {
            Log.e(TAG, "Başlatma hatası");
        }
    }

    private void speak(String metin) {
        textToSpeech.speak(metin, TextToSpeech.QUEUE_FLUSH, null, "metinId");
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
