package com.example.quoteboss;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * The portion of the app where the quote is actually retrieved and shown.
 */
public final class QuoteActivity extends AppCompatActivity {

    /** The Gson parser used to parse response JSON. */
    private static JsonParser jsonParser = new JsonParser();

    /** The Wikiquote API URL */
    private final String URL = "https://en.wikiquote.org/w/api.php";

    /** The code for standard organized pages */
    private final int STANDARD = 0;

    /** The code for bold organized pages */
    private final int BOLD = 1;

    /** The list of quotes */
    private List<String> quoteList;

    /** The list of people corresponding to their quotes */
    private List<String> peopleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        Intent intent = getIntent();
        int category = intent.getIntExtra("category", 0);

        TextView quote = findViewById(R.id.quote);
        TextView person = findViewById(R.id.person);
        quote.setMovementMethod(new ScrollingMovementMethod());


        if (category == 1) {
            // proverbs
            getQuotes("English_proverbs", BOLD);
        } else if (category == 2) {
            // knowledge
            getQuotes("Knowledge", STANDARD);
        } else if (category == 3) {
            // technology
            getQuotes("Technology", STANDARD);
        } else {
            // something went wrong
            quote.setText("There was a problem.");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Random random = new Random();
                int current = Math.abs(random.nextInt()) % quoteList.size();
                while (quoteList.get(current).indexOf('<') != -1 || quoteList.get(current).length() < 10
                    || (quoteList.get(current).charAt(0) < 65 || quoteList.get(current).charAt(0) > 90)) {
                    current = Math.abs(random.nextInt()) % quoteList.size();
                }
                quoteList.set(current, quoteList.get(current).replaceAll("<br />", " "));
                quote.setText("\"" + quoteList.get(current) + "\"");
                if (!peopleList.isEmpty()) {
                    peopleList.set(current, peopleList.get(current).replaceAll("_", " "));
                    person.setText(peopleList.get(current));
                    person.setVisibility(View.VISIBLE);
                }
            }
        }, 1500);   //5 seconds
    }

    private void getQuotes(String page, int type) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL + "?action=parse&page=" + page + "&prop=text&format=json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        JsonObject page = jsonParser.parse(response).getAsJsonObject();
                        String text = page.get("parse").getAsJsonObject()
                                .get("text").getAsJsonObject()
                                .get("*").getAsString();

                        final List<String> quotes = new ArrayList<>();
                        final List<String> people = new ArrayList<>();

                        if (type == STANDARD) {
                            String currentText = text;
                            int index = text.indexOf("<ul><li>") + 8;
                            while (index != -1) {
                                currentText = currentText.substring(index);
                                if (currentText.charAt(0) != '<') {
                                    quotes.add(currentText.substring(0, currentText.indexOf('\n')));

                                    int index2 = currentText.indexOf("/wiki/");
                                    if (index2 != -1) {
                                        currentText = currentText.substring(index2 + 6);

                                        people.add(currentText.substring(0, currentText.indexOf('\"')));
                                    } else {
                                        people.add("Unknown");
                                    }

                                    index = currentText.indexOf("<ul><li>");
                                    if (index != -1) {
                                        index += 8;
                                    }
                                }
                            }
                        }

                        if (type == BOLD) {
                            final Document document = Jsoup.parse(text);
                            final Elements elements = document.select("b");
                            for (final Element element : elements) {
                                quotes.add(element.text());
                            }
                        }

                        quoteList = quotes;
                        peopleList = people;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("There was an error");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}


