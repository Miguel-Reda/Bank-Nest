package com.example.bank1;

import static java.lang.Math.pow;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class userAccount extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BankDB.db";
    private static final String TABLE_NAME = "user_account";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "BANKNAME";
    private static final String COL_3 = "YEAR";
    private static final String COL_4 = "AMOUNT";
    private static final String COL_5 = "PERCENTAGE";
    private static final String COL_6 = "STARTDATE";
    private static final String COL_7 = "FINALAMOUNT";

    private int id;
    private String bankName;
    private int year;
    private float amount;
    private float percentage;
    private String startDate;
    private double finalAmount;

    public userAccount(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public userAccount(String bankName, int year, float amount, float percentage, String startDate) {
        super(null, DATABASE_NAME, null, 1);
        this.bankName = bankName;
        this.year = year;
        this.amount = amount;
        this.percentage = percentage;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public String getBankName() {
        return bankName;
    }

    public int getYear() {
        return year;
    }

    public float getAmount() {
        return amount;
    }

    public float getPercentage() {
        return percentage;
    }

    public String getStartDate() {
        return startDate;
    }



    public double getFinalAmount() {
        return finalAmount;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "BANKNAME TEXT, YEAR INTEGER, AMOUNT REAL, PERCENTAGE REAL, STARTDATE TEXT, FINALAMOUNT REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean insertData(String bankName, int year, float amount, float percentage, String startDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        double finalAmount = amount * (pow((1 + (percentage / 100.0)),year));

        contentValues.put(COL_2, bankName);
        contentValues.put(COL_3, year);
        contentValues.put(COL_4, amount);
        contentValues.put(COL_5, percentage);
        contentValues.put(COL_6, startDate);
        contentValues.put(COL_7, finalAmount);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    @SuppressLint("Range")
    public List<userAccount> getUserAccountList() {
        Cursor cursor = getAllData();
        List<userAccount> userAccountList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_1));
                String bankName = cursor.getString(cursor.getColumnIndex(COL_2));
                int year = cursor.getInt(cursor.getColumnIndex(COL_3));
                float amount = cursor.getFloat(cursor.getColumnIndex(COL_4));
                float percentage = cursor.getFloat(cursor.getColumnIndex(COL_5));
                String startDate = cursor.getString(cursor.getColumnIndex(COL_6));
                double finalAmount = cursor.getLong(cursor.getColumnIndex(COL_7));

                userAccount account = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    account = new userAccount(bankName, year, amount, percentage, startDate);
                    account.id = id;
                    account.finalAmount = finalAmount;
                    account.startDate = startDate;
                }
                userAccountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userAccountList;
    }

    public boolean updateData(String id, String bankName, int year, float amount, float percentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        double finalAmount = amount * (pow((1 + (percentage / 100.0)),year));

        contentValues.put(COL_2, bankName);
        contentValues.put(COL_3, year);
        contentValues.put(COL_4, amount);
        contentValues.put(COL_5, percentage);
        contentValues.put(COL_7, finalAmount);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public Cursor getAccount(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{id});
    }

    public Cursor getAccountByBankName(String bankName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE BANKNAME = ?", new String[]{bankName});
    }

    public Cursor getAccountByYear(String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE YEAR = ?", new String[]{year});
    }

    public Cursor getAccountByAmount(String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT = ?", new String[]{amount});
    }

    public Cursor getAccountByPercentage(String percentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE PERCENTAGE = ?", new String[]{percentage});
    }

    public Cursor getAccountByStartDate(String startDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE STARTDATE = ?", new String[]{startDate});
    }

    public Cursor getAccountStartDate(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT STARTDATE FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{id});
    }
}
