package com.example.isdmap;

import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codesgood.views.JustifiedTextView;

public class AboutUsActivity extends AppCompatActivity {

    TextView textView;
    WebView webView;
    JustifiedTextView justifiedTextView;
    String  dummytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        textView = findViewById(R.id.abuttext);
        webView = findViewById(R.id.web_view);
        justifiedTextView = findViewById(R.id.justify_text);

        //get string from resources
        dummytext = getResources().getString(R.string.navigation_about_us_text);

        //for justifytextview
        justifiedTextView.setText(Html.fromHtml(dummytext));
    }
}