package com.example.sprint_image_to_text_to_speech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DbActivity extends AppCompatActivity {

    private DBSetupHelper dbSetupHelper;
    private RecyclerView recyclerView;
    private ValueAdapter valueAdapter;
    private Button goBackToMainActivity, btnDeleteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        dbSetupHelper = new DBSetupHelper(DbActivity.this);

        goBackToMainActivity = findViewById(R.id.btnbackToMainActivity);
        btnDeleteData = findViewById(R.id.btnDeleteDataDbActivity);

        goBackToMainActivity.setOnClickListener(this::goBackToMainActivity);
        btnDeleteData.setOnClickListener(this::deleteDataFromDB);

        ArrayList<TextFromImageEntity> dbValues = new ArrayList<TextFromImageEntity>();
        dbValues = dbSetupHelper.getValues(dbValues);

        recyclerView = findViewById(R.id.rvDbValuesDbActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        valueAdapter = new ValueAdapter(dbValues);
        recyclerView.setAdapter(valueAdapter);
    }



    private void goBackToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void deleteDataFromDB(View view) {
        dbSetupHelper.deleteAllValuesInTable();
    }
}