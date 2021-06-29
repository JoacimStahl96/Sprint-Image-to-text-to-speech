package com.example.sprint_image_to_text_to_speech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class DbActivity extends AppCompatActivity {

    private DBSetupHelper dbSetupHelper;
    private RecyclerView recyclerView;
    private ValueAdapter valueAdapter;
    private Button btnGoBackToMainActivity, btnDeleteData, btnPlaySpeechText;
    private ArrayList<TextFromImageEntity> dbValues = new ArrayList<TextFromImageEntity>();
    private TextToSpeech dbTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        Log.d("TTS", "onCreate: ");

        dbSetupHelper = new DBSetupHelper(DbActivity.this);

        btnGoBackToMainActivity = findViewById(R.id.btnbackToMainActivity);
        btnDeleteData = findViewById(R.id.btnDeleteDataDbActivity);
        btnPlaySpeechText = findViewById(R.id.dbActivityPlayButton);
        TextView tvSavedTextInDb = findViewById(R.id.savedtextInDb);

        btnGoBackToMainActivity.setOnClickListener(this::goBackToMainActivity);
        btnDeleteData.setOnClickListener(this::deleteDataFromDB);


        dbValues = dbSetupHelper.getValues(dbValues);

        recyclerView = findViewById(R.id.rvDbValuesDbActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        valueAdapter = new ValueAdapter(dbValues);
        recyclerView.setAdapter(valueAdapter);

        activateTTS();

        valueAdapter.setOnItemClickListener(new ValueAdapter.OnClickListener() {
            @Override
            public void playDbText(int position) {
                dbTextToSpeech.speak(dbValues.get(position).getTextFromImage(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void goBackToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void deleteDataFromDB(View view) {
        dbSetupHelper.deleteAllValuesInTable();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("TTS", "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbTextToSpeech.stop();
        dbTextToSpeech.shutdown();
    }

    private void activateTTS() {
        dbTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d("TTS ACTIVATED", "onInit: TTS ACTIVATEEEEEEED");

                try {
                    dbTextToSpeech.setLanguage(Locale.GERMAN);
                    Log.d("trySpeak", "onInit: DbActivity " + status);
                    //   dbTextToSpeech.setLanguage(Locale.ENGLISH);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("speak", "onInit: DbActivity: TextToSpeech.ERROR" + TextToSpeech.ERROR);
                }
                Log.d("speak", "onInit: DbActivity: language is available: " + dbTextToSpeech.isLanguageAvailable(Locale.getDefault()));
            }

        });
    }
}