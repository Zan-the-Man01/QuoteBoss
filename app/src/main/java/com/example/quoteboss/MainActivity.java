package com.example.quoteboss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * Represents the title screen of the app.
 */
public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.begin);

        button.setOnClickListener(unused -> buttonClicked());
    }

    private void buttonClicked() {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }
}
