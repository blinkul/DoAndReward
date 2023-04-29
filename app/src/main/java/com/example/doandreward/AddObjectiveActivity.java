package com.example.doandreward;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.doandreward.database.AppDatabase;
import com.example.doandreward.database.AppExecutors;
import com.example.doandreward.entities.Objective;

public class AddObjectiveActivity extends AppCompatActivity {

    private TextView objectiveDescription;
    private TextView objectivePrize;

    private final AppDatabase database = AppDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_objective);
        objectiveDescription = findViewById(R.id.txt_objective_description);
        objectivePrize = findViewById(R.id.txt_objective_prize);
    }

    public void addObjective(View v) {
        Objective objective = new Objective(
                String.valueOf(objectiveDescription.getText()),
                Integer.parseInt(String.valueOf(objectivePrize.getText()))
        );

        Runnable runnable = () -> database.objectiveDao().addNewObjective(objective);

        //TODO: S-ar putea face intentul inaintea obiectului Prize (Same in AddPrize)
        AppExecutors.getInstance().getSingleThreadRunnable().execute(runnable);

        Intent toMainActivity = new Intent(this, MainActivity.class);
        startActivity(toMainActivity);
    }
}