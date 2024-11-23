package com.example.bank1;

import static java.lang.Math.floor;
import static java.lang.Math.pow;

import android.app.DatePickerDialog;
import android.content.Context;
import android.health.connect.datatypes.units.Power;
import android.os.Build;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.nio.file.FileVisitOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.bank1.R;

public class UserAccountAdapter extends ArrayAdapter<userAccount> {
    private Context context;
    private List<userAccount> userAccounts;

    public UserAccountAdapter(Context context, List<userAccount> userAccounts) {
        super(context, 0, userAccounts);
        this.context = context;
        this.userAccounts = userAccounts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        userAccount userAccount = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView bankNameTextView = convertView.findViewById(R.id.bankNameTextView);
        TextView yearTextView = convertView.findViewById(R.id.yearTextView);
        TextView amountTextView = convertView.findViewById(R.id.amountTextView);
        TextView percentageTextView = convertView.findViewById(R.id.percentageTextView);
        TextView todayAmountTextView = convertView.findViewById(R.id.amountToday);
        TextView finalAmount = convertView.findViewById(R.id.finalAmount);
        Button dateButton = convertView.findViewById(R.id.button);
        TextView selectedAmountTextView = convertView.findViewById(R.id.selectedAmountTextView);
        ImageView delete = convertView.findViewById(R.id.iconImageView);

        bankNameTextView.setText("Bank Name: " + userAccount.getBankName());
        yearTextView.setText("Number Of Years: " + String.valueOf(userAccount.getYear()));
        amountTextView.setText("Amount: " + String.valueOf(userAccount.getAmount()) + " L.E");
        percentageTextView.setText("Percentage: " + String.valueOf(userAccount.getPercentage()) + "%");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            todayAmountTextView.setText("Amount Today: " + String.valueOf(calculateNewAmount(userAccount.getStartDate(), userAccount.getAmount(), userAccount.getPercentage(),userAccount.getYear(),userAccount.getFinalAmount())));
        }
        finalAmount.setText("Final Amount: " + String.valueOf(userAccount.getFinalAmount()) + " L.E");

        dateButton.setOnClickListener(v -> showDatePickerDialog(userAccount, selectedAmountTextView));
        delete.setOnClickListener(v -> deleteAcount(userAccount));

        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String calculateNewAmount(String startDate, float amount, float percentage, int year, double finalAmount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate savedDate = LocalDate.parse(startDate, formatter);
        long days = java.time.temporal.ChronoUnit.DAYS.between(savedDate, LocalDate.now());
        double result = 0;
        String formattedAmount = "";
        if (days <= year * 365) {
            result = amount + (amount * ((percentage / 365) / 100) * days);
            formattedAmount = String.format("%.2f", result);
            return formattedAmount;

        } else {
            result = finalAmount;
            formattedAmount = String.format("%.2f", result);
            return formattedAmount;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public double calculateNewAmountAfterUnknownDate(String startDate, float amount, float percentage, int year, double finalAmount, LocalDate selectedDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate savedDate = LocalDate.parse(startDate, formatter);
        long days = java.time.temporal.ChronoUnit.DAYS.between(savedDate, selectedDate);

        if (days >= 0 && days <= year * 365) {
            if (days <= 365) {
                return amount + (amount * ((percentage / 365) / 100) * days);
            } else {
                long remainingDays = days % 365;
                double tempAmount = amount + (amount * ((percentage / 365) / 100) * remainingDays);
                return tempAmount + (amount * Math.pow(1 + (percentage / 100), (int) days / 365.0));
            }
        }

            Toast.makeText(context, "Please select a date between the start date and the end date", Toast.LENGTH_SHORT).show();
         //shake the dateButton if the user selected a date after the end date
        return finalAmount;
    }


    public void deleteAcount(userAccount userAccount) {
        userAccounts.remove(userAccount);

        userAccount dbHelper = new userAccount(context);
        dbHelper.deleteData(String.valueOf(userAccount.getId()));

        notifyDataSetChanged();

        Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog(userAccount userAccount, TextView selectedAmountTextView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        LocalDate startDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            startDate = LocalDate.parse(userAccount.getStartDate(), formatter);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            LocalDate selectedDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
            }

            double newAmount = 0;
            String formattedAmount = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                newAmount = calculateNewAmountAfterUnknownDate(userAccount.getStartDate(), userAccount.getAmount(), userAccount.getPercentage(), userAccount.getYear(), userAccount.getFinalAmount(), selectedDate);
                formattedAmount = String.format("%.2f", newAmount);
            }
            selectedAmountTextView.setText("Amount By " + selectedDate.toString() + " : " + formattedAmount + " L.E");
            selectedAmountTextView.setVisibility(View.VISIBLE);
        }, year, month, day);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePickerDialog.getDatePicker().setMinDate(startDate.toEpochDay() * 24 * 60 * 60 * 1000); // Convert to milliseconds
        }

        datePickerDialog.show();
    }


}
