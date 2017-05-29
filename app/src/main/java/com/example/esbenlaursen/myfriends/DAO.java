package com.example.esbenlaursen.myfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EsbenLaursen on 21-03-2017.
 */

public class DAO {

    public static final String TABLE_NAME = "Friend";

    private SQLiteDatabase database;
    private SQLiteStatement statement;

    Context context;

    //ADD FRIEND
    private static final String INSERT = "insert into " + TABLE_NAME
            + "(name, email, address, latitude, longitude, phonenumber, url, image) values (?,?,?,?,?,?,?,?)";

    //UPDATE FRIEND
   // private static final String UPDATE = "UPDATE " + TABLE_NAME
    //        + "SET name (?)=  ";



    public DAO(Context context)
    {
        this.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        this.database = openHelper.getWritableDatabase();

       // this.database.execSQL("delete from "+ TABLE_NAME); //CLEAR

        this.statement = this.database.compileStatement(INSERT); //Prepared statement
    }

    public long insert(Friend f) {
        this.statement.bindString(1, f.name);
        this.statement.bindString(2, f.email);
        this.statement.bindString(3, f.address);
        this.statement.bindDouble(4, f.latitude);
        this.statement.bindDouble(5, f.longitude);
        this.statement.bindString(6, f.phoneNumber);
        this.statement.bindString(7, f.URL);
        this.statement.bindString(8, f.image);
        return this.statement.executeInsert();
    }

    public void deleteAll() {
        this.database.delete(TABLE_NAME, null, null);
    }

    public void deleteOne(int id) {

        String query = "delete from friend where id = " + id;
        this.database.execSQL(query);
    }

    public void Update(Friend f)
    {
        ContentValues cv = new ContentValues();
        cv.put("name", f.name);
        cv.put("address", f.address);
        cv.put("latitude", f.latitude);
        cv.put("longitude", f.longitude);
        cv.put("email", f.email);
        cv.put("phonenumber", f.phoneNumber);
        cv.put("URL", f.URL);
        cv.put("image", f.image);
        this.database.update(TABLE_NAME, cv, "id=?", new String[]{f.id+""});
    }




    public Friend getOne(int id){
        String query = "Select * FROM " + TABLE_NAME + " WHERE id = " + id;
        Cursor cursor = database.rawQuery(query, null);
        Friend f;
        if(cursor.moveToFirst())
        {
            cursor.moveToFirst();
            f = new Friend(cursor.getInt(0), cursor.getString(1),cursor.getString(2), cursor.getDouble(3),
                    cursor.getDouble(4) ,cursor.getString(5),cursor.getString(6),cursor.getString(7), cursor.getString(8));
        } else
        {
            f = null;
        }

        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return f;
    }

    public List<Friend> getAll()
    {
        List<Friend> friends = new ArrayList<>();
        Cursor cursor = this.database.query(TABLE_NAME, new String[] { "id", "name", "address", "latitude", "longitude", "email", "phonenumber", "url", "image" },
                null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            do {
                friends.add(new Friend(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)));
            } while (cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return friends;
    }















}


