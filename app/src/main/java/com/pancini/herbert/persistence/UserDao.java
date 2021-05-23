package com.pancini.herbert.persistence;

import com.pancini.herbert.entities.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDao extends SQLiteOpenHelper {
    // Database attributes
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 4;

    // Table attributes
    private static final String TABLE_NAME = "user";
    private static final String FIELD_USER_ID = "user_id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PHONE_NUMBER = "phone_number";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_ROLE_ID = "role_id";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                                                    FIELD_USER_ID + " INTEGER PRIMARY KEY, " +
                                                    FIELD_NAME + " TEXT, " +
                                                    FIELD_PHONE_NUMBER + " TEXT, " +
                                                    FIELD_EMAIL + " TEXT, " +
                                                    FIELD_PASSWORD + " TEXT, " +
                                                    FIELD_ROLE_ID  + " INTEGER); ";

    public UserDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            // Create Table user
            db.execSQL(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        try {
            db.execSQL(SQL_DROP_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }

    private User getUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(FIELD_USER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
        user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(FIELD_PHONE_NUMBER)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(FIELD_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(FIELD_PASSWORD)));
        user.setRoleId(cursor.getInt(cursor.getColumnIndex(FIELD_ROLE_ID)));
        return user;
    }

    private ContentValues getContentValues(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_USER_ID, user.getId());
        contentValues.put(FIELD_NAME, user.getName());
        contentValues.put(FIELD_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(FIELD_EMAIL, user.getEmail());
        contentValues.put(FIELD_PASSWORD, user.getPassword());
        contentValues.put(FIELD_ROLE_ID, user.getRoleId());
        return contentValues;
    }

    public User get() {
        Cursor cursor = null;
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                user = this.getUser(cursor);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return user;
    }

    public long insert(User user) {
        long newRowId = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = this.getContentValues(user);
            newRowId = db.insert(TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch(SQLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return newRowId;
    }

    public int update(User user) {
        int rowId = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = this.getContentValues(user);
            String whereClause = FIELD_USER_ID + "=?";
            String[] whereArgs  = { String.valueOf(user.getId()) };
            rowId = db.update(TABLE_NAME, contentValues,  whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch(SQLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return rowId;
    }

    public int delete(int id) {
        int rowId = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            String whereClause = FIELD_USER_ID + "=?";
            String[] whereArgs = { String.valueOf(id) };
            rowId = db.delete(TABLE_NAME, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch(SQLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return rowId;
    }
}