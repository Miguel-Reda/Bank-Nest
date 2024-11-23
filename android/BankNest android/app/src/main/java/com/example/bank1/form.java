package com.example.bank1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class form extends AppCompatActivity {

    EditText bankName;
    EditText amount;
    EditText year;
    EditText precenatage;
    EditText startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);

        bankName = findViewById(R.id.bankName);
        amount = findViewById(R.id.amount);
        year = findViewById(R.id.year);
        precenatage = findViewById(R.id.percentage);
      startDate = findViewById(R.id.startDate);

    }


    public void addAccount(View view) {
        String bankNameInput = bankName.getText().toString();
        String amountInput = amount.getText().toString();
        String yearInput = year.getText().toString();
        String percentageInput = precenatage.getText().toString();
        String startDateInput = startDate.getText().toString();
        // Check if all strings are not empty and shake them
        if (bankNameInput.isEmpty() || amountInput.isEmpty() || yearInput.isEmpty() || percentageInput.isEmpty() ) {
            if (bankNameInput.isEmpty()) {
                YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(findViewById(R.id.bankName));
            }
            if (amountInput.isEmpty()) {
                YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(findViewById(R.id.amount));
            }
            if (yearInput.isEmpty()) {
                YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(findViewById(R.id.year));
            }
            if (percentageInput.isEmpty()) {
                YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(findViewById(R.id.percentage));
            }}
        // Check if the start date is in the correct format
        if (!isValidDate(startDateInput)) {
            YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(findViewById(R.id.startDate));
            Toast.makeText(this, "Please enter a valid date in dd/MM/yyyy format", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(form.this, ShowAccounts.class);
        intent.putExtra("BankName", bankNameInput);
        intent.putExtra("Amount", amountInput);
        intent.putExtra("Year", yearInput);
        intent.putExtra("Precentage", percentageInput);
        intent.putExtra("StartDate", startDateInput);
        startActivity(intent);
    }

    // Method to check if the date is valid
    private boolean isValidDate(String date) {
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.parse(date, formatter);
            }
            return true;
        } catch (Exception e) {
            return false; // Invalid date format
        }
    }
}
