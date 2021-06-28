package com.example.sprint_image_to_text_to_speech;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView ivImageId;
    private TextView tvTextFromImage;
    private Button btnChooseImage, btnReadTextFromImage, btnGoToDbActivity;

    private static final int STORAGE_PERMISSION_CODE = 113;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    private InputImage inputImage;
    private TextRecognizer textRecognizer;

    private TextToSpeech textToSpeech;

    private DBSetupHelper dbSetupHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImageId = findViewById(R.id.imageId);
        tvTextFromImage = findViewById(R.id.tvTextFromImage);
        btnChooseImage = findViewById(R.id.useNewImage);
        btnReadTextFromImage = findViewById(R.id.btnReadTextFromImage);
        btnGoToDbActivity = findViewById(R.id.btnGoToDb);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                // här hanteras bilden
                Intent data = result.getData();
                Uri imageUri = data.getData();

                convertImageToText(imageUri);

            }
        });

        btnChooseImage.setOnClickListener(this::useImage);
        btnReadTextFromImage.setOnClickListener(this::readTextFromImage);
        btnGoToDbActivity.setOnClickListener(this::GoToDbActivity);
    }

    private void convertImageToText(Uri imageUri) {

        try {
            inputImage = InputImage.fromFilePath(getApplicationContext(), imageUri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ivImageId.setImageURI(imageUri);
            }
            // hämta texten från inputImage
            Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(@NonNull Text text) {
                    tvTextFromImage.setText(text.getText());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    tvTextFromImage.setText("Error: " + e.getMessage());
                    Log.d("TAG", "Error: " + e.getMessage());
                }
            });


        } catch (Exception e) {
            Log.d("TAG", "convertImageToText: Error: " + e.getMessage());
        }

    }

    public void useImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        intentActivityResultLauncher.launch(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // take permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readTextFromImage(View view) {
        String toRead = tvTextFromImage.getText().toString();
        Log.d("toRead", "readTextFromImage: " + toRead);

        // kör ett simpelt sparande, känner att fler knappar kanske förstör mer än det gör nytta

        textToSpeech.speak(toRead, TextToSpeech.QUEUE_FLUSH, null);
        dbSetupHelper.addTextFromImages(toRead);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG", "onStop: ");
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbSetupHelper = new DBSetupHelper(MainActivity.this);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                try {
                    textToSpeech.setLanguage(Locale.GERMAN);
                    Log.d("trySpeak", "onInit: " + status);
                    //   textToSpeech.setLanguage(Locale.ENGLISH);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("speak", "onInit: TextToSpeech.ERROR" + TextToSpeech.ERROR);
                }
                Log.d("speak", "onInit: language is available: " + textToSpeech.isLanguageAvailable(Locale.getDefault()));
            }

        });
    }

    private void GoToDbActivity(View view) {
        Intent intent = new Intent(this, DbActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}