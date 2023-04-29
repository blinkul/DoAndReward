package com.example.doandreward;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.doandreward.database.AppDatabase;
import com.example.doandreward.database.AppExecutors;
import com.example.doandreward.entities.Prize;

public class AddPrizeActivity extends AppCompatActivity {

    private TextView prizeDescription;
    private TextView prizePrice;

    private final AppDatabase database = AppDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prize);
        prizeDescription = findViewById(R.id.text_view_prize_description);
        prizePrice = findViewById(R.id.text_view_prize_price);
    }

    public void addPrize(View v) {
        Prize prize = new Prize(
                String.valueOf(prizeDescription.getText()),
                Integer.parseInt(String.valueOf(prizePrice.getText()))
        );

        Runnable runnable = () -> database.prizeDao().addPrize(prize);

        //TODO: S-ar putea face intentul inaintea obiectului Prize (same in AddObjective)
        AppExecutors.getInstance().getSingleThreadRunnable().execute(runnable);

        Intent toMainActivity = new Intent(this, PrizesActivity.class);
        startActivity(toMainActivity);

    }

}