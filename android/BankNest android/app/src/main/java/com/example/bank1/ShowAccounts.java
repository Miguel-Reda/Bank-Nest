package com.example.bank1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ShowAccounts extends AppCompatActivity {
    private ListView userListView;
    private UserAccountAdapter adapter;
    private ArrayList<userAccount> userAccounts;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_accounts);

        initializeViews();
        setupAdapter();
        setupFooter();

        Intent intent = getIntent();
        String bankName = intent.getStringExtra("BankName");
        String amount = intent.getStringExtra("Amount");
        String year = intent.getStringExtra("Year");
        String percentage = intent.getStringExtra("Precentage");
        String startDate = intent.getStringExtra("StartDate");

        if (bankName != null && amount != null && year != null && percentage != null) {
            addNewAccount(bankName, Integer.parseInt(year), Float.parseFloat(amount), Float.parseFloat(percentage),startDate);
        }

        loadUserAccounts();
    }

    private void initializeViews() {
        userListView = findViewById(R.id.userListView);
        userAccounts = new ArrayList<>();
    }

    private void setupAdapter() {
        adapter = new UserAccountAdapter(this, userAccounts);
        userListView.setAdapter(adapter);
    }

    private void setupFooter() {
        View footerView = getLayoutInflater().inflate(R.layout.list_footer, null);
        userListView.addFooterView(footerView);

        Button addAccountButton = footerView.findViewById(R.id.addAccountButton);
        addAccountButton.setOnClickListener(v -> startActivity(new Intent(ShowAccounts.this, form.class)));
    }

    private void loadUserAccounts() {
        userAccount dbHelper = new userAccount(this);
        List<userAccount> accounts = dbHelper.getUserAccountList();
        userAccounts.clear();
        userAccounts.addAll(accounts);
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addNewAccount(String bankName, int year, float amount, float percentage, String startDate) {
        userAccount newUser = new userAccount(this);
        if (newUser.insertData(bankName, year, amount, percentage, startDate)) {
            loadUserAccounts();
        }
    }
}
