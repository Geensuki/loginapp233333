package com.example.loginapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Savingsly.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Plans Table
    public static final String TABLE_PLANS = "plans";
    public static final String COLUMN_PLAN_ID = "id";
    public static final String COLUMN_PLAN_USER_ID = "user_id";
    public static final String COLUMN_PLAN_NAME = "name";
    public static final String COLUMN_PLAN_TARGET = "target_amount";
    public static final String COLUMN_PLAN_START_DATE = "start_date";
    public static final String COLUMN_PLAN_END_DATE = "end_date";
    public static final String COLUMN_PLAN_FREQUENCY = "frequency";
    public static final String COLUMN_PLAN_ALLOWANCE = "allowance_amount";
    public static final String COLUMN_PLAN_PRIORITY = "priority";
    public static final String COLUMN_PLAN_NOTES = "notes";

    // Savings Table
    public static final String TABLE_SAVINGS = "savings";
    public static final String COLUMN_SAVING_ID = "id";
    public static final String COLUMN_SAVING_PLAN_ID = "plan_id";
    public static final String COLUMN_SAVING_AMOUNT = "amount";
    public static final String COLUMN_SAVING_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PLANS_TABLE = "CREATE TABLE " + TABLE_PLANS + "("
                + COLUMN_PLAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLAN_USER_ID + " INTEGER,"
                + COLUMN_PLAN_NAME + " TEXT,"
                + COLUMN_PLAN_TARGET + " REAL,"
                + COLUMN_PLAN_START_DATE + " TEXT,"
                + COLUMN_PLAN_END_DATE + " TEXT,"
                + COLUMN_PLAN_FREQUENCY + " TEXT,"
                + COLUMN_PLAN_ALLOWANCE + " REAL,"
                + COLUMN_PLAN_PRIORITY + " TEXT,"
                + COLUMN_PLAN_NOTES + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_PLAN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_PLANS_TABLE);

        String CREATE_SAVINGS_TABLE = "CREATE TABLE " + TABLE_SAVINGS + "("
                + COLUMN_SAVING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SAVING_PLAN_ID + " INTEGER,"
                + COLUMN_SAVING_AMOUNT + " REAL,"
                + COLUMN_SAVING_DATE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_SAVING_PLAN_ID + ") REFERENCES " + TABLE_PLANS + "(" + COLUMN_PLAN_ID + "))";
        db.execSQL(CREATE_SAVINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public int getUserId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public String getUserName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_NAME};
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {userId + ""};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public long addPlan(int userId, String name, double target, String startDate, String endDate, String frequency, double allowance, String priority, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAN_USER_ID, userId);
        values.put(COLUMN_PLAN_NAME, name);
        values.put(COLUMN_PLAN_TARGET, target);
        values.put(COLUMN_PLAN_START_DATE, startDate);
        values.put(COLUMN_PLAN_END_DATE, endDate);
        values.put(COLUMN_PLAN_FREQUENCY, frequency);
        values.put(COLUMN_PLAN_ALLOWANCE, allowance);
        values.put(COLUMN_PLAN_PRIORITY, priority);
        values.put(COLUMN_PLAN_NOTES, notes);
        return db.insert(TABLE_PLANS, null, values);
    }

    public Cursor getPlans(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PLANS, null, COLUMN_PLAN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    public long addSaving(int planId, double amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SAVING_PLAN_ID, planId);
        values.put(COLUMN_SAVING_AMOUNT, amount);
        values.put(COLUMN_SAVING_DATE, date);
        return db.insert(TABLE_SAVINGS, null, values);
    }

    public double getTotalSavedForPlan(int planId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_SAVING_AMOUNT + ") FROM " + TABLE_SAVINGS + " WHERE " + COLUMN_SAVING_PLAN_ID + " = ?", new String[]{String.valueOf(planId)});
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }
}
