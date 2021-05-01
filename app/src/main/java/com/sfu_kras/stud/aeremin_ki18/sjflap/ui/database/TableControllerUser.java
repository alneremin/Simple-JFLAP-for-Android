package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TableControllerUser extends DatabaseHelper {

    private final String TABLE = "users";

    public TableControllerUser(Context context) {
        super(context);
    }

    public boolean create(User user) {

        ContentValues values = new ContentValues();

        values.put("name", user.name);
        values.put("email", user.email);
        values.put("pass", user.password);
        values.put("address", user.address);
        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert(TABLE, null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public int count(String email) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM " + TABLE + " WHERE email='" + email + "';";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public int count(String email, String pass) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM " + TABLE + " WHERE " +
                "email='" + email + "' and " +
                "pass='" + pass + "';";
        System.out.println(sql);
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }


    public List<User> read() {

        List<User> recordsList = new ArrayList<User>();

        String sql = "SELECT * FROM " + TABLE + " ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String userName = cursor.getString(cursor.getColumnIndex("name"));
                String userEmail = cursor.getString(cursor.getColumnIndex("email"));
                String userPass = cursor.getString(cursor.getColumnIndex("pass"));
                String userAddress = cursor.getString(cursor.getColumnIndex("address"));

                User user = new User(id, userName, userEmail, userPass, userAddress);

                recordsList.add(user);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public User readSingleRecord(String name, String pass) {

        User user = null;

        String sql = "SELECT * FROM " + TABLE + " WHERE " +
                "email='" + name + "' and " +
                "pass='" + pass + "';";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String userName = cursor.getString(cursor.getColumnIndex("name"));
            String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            String userPass = cursor.getString(cursor.getColumnIndex("pass"));
            String userAddress = cursor.getString(cursor.getColumnIndex("address"));

            user = new User(id, userName, userEmail, userPass, userAddress);
        }

        cursor.close();
        db.close();

        return user;

    }

    public boolean update(User user) {

        ContentValues values = new ContentValues();

        values.put("name", user.name);
        values.put("email", user.email);
        values.put("pass", user.password);
        values.put("address", user.address);

        String where = "id = ?";

        String[] whereArgs = { String.valueOf(user.id) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update(TABLE, values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete(TABLE, "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }

}
