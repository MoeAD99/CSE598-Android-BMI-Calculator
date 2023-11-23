package com.bmi.bmicalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BMIModel model;
    private TextView bmiOutput;
    private TextView riskOutput;
    private EditText heightInput;
    private EditText weightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmiOutput = findViewById(R.id.bmiOutput);
        riskOutput = findViewById(R.id.riskOutput);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);

        Button BMIApiButton = findViewById(R.id.BMIApiButton);
        BMIApiButton.setOnClickListener(v -> {
            String height = heightInput.getText().toString();
            String weight = weightInput.getText().toString();
            model = new BMIModel(MainActivity.this, height, weight);

            model.setOnAPIFetchedListener(new BMIModel.OnAPIFetchedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                // automatically called when API data is fetched
                public void onAPIFetched(double bmi, String riskMessage, JSONArray moreInfoLinks) {
                    // Update the UI when API data is fetched
                    bmiOutput.setText(Double.toString(bmi));
                    riskOutput.setText(riskMessage);
                    changeColor(bmi);
                }
            });
        });
        Button learnMoreButton = findViewById(R.id.learnMoreButton);
        learnMoreButton.setOnClickListener(v -> {
            Random rand = new Random();
            int index = rand.nextInt(3);
            if (model != null && model.getLinks() != null && model.getLinks().length() > 0) {
                try {
                    String url = model.getLinks().getString(index);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changeColor(double bmi) {
        if (bmi < 18) {
            riskOutput.setTextColor(Color.BLUE);
        } else if (bmi >= 18 && bmi < 25) {
            riskOutput.setTextColor(Color.GREEN);
        } else if (bmi >= 25 && bmi < 30) {
            riskOutput.setTextColor(Color.parseColor("#800080"));
        } else {
            riskOutput.setTextColor(Color.RED);
        }
    }
}
