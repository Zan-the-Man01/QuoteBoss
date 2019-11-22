package com.example.quoteboss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public final class QuoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        Intent intent = getIntent();
        int category = intent.getIntExtra("category", 0);
        TextView quote = findViewById(R.id.quote);
        String newQuote = quote.getText().toString();

        if (category == 1) {
            newQuote += " You picked category 1.";
            quote.setText(newQuote);
        } else if (category == 2) {
            newQuote += " You picked category 2.";
            quote.setText(newQuote);
        } else if (category == 3) {
            newQuote += " You picked category 3.";
            quote.setText(newQuote);
        } else if (category == 4) {
            newQuote += " You picked category 4.";
            quote.setText(newQuote);
        } else {
            newQuote += " There was a problem..";
            quote.setText(newQuote);
        }
    }
}
