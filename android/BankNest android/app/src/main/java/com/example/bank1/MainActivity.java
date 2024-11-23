package com.example.bank1;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.bank1.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        String welcomeMessage = "Welcome to BankNest";
        SpannableString spannableString = new SpannableString(welcomeMessage);

        int start = welcomeMessage.indexOf("BankNest");
        int end = start + "BankNest".length();

        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#50609F")),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannableString.setSpan(
                new RelativeSizeSpan(1.3f),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        welcomeTextView.setText(spannableString);
    }

    public void addAccount(View view) {
        Intent intent = new Intent(MainActivity.this, form.class);
        startActivity(intent);
    }

    public void myAccount(View view) {
        Intent intent = new Intent(MainActivity.this, ShowAccounts.class);
        startActivity(intent);
    }
}
