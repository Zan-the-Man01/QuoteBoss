package com.example.quoteboss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Represents the quote type selection menu of the app.
 */
public final class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Button cat1 = findViewById(R.id.catButton1);
        Button cat2 = findViewById(R.id.catButton2);
        Button cat3 = findViewById(R.id.catButton3);

        cat1.setOnClickListener(unused -> handleClick(1));
        cat2.setOnClickListener(unused -> handleClick(2));
        cat3.setOnClickListener(unused -> handleClick(3));
    }

    private void handleClick(int cat) {
        Intent intent = new Intent(this, QuoteActivity.class);
        intent.putExtra("category", cat);
        startActivity(intent);
    }
}
