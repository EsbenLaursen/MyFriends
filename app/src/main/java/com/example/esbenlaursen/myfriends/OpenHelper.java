package com.example.esbenlaursen.myfriends;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by EsbenLaursen on 17-04-2017.
 */
public class OpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sqlite.db";
    public static final int DATABASE_VERSION = 10;
    public static final String TABLE_NAME = "Friend";

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + "(id INTEGER PRIMARY KEY, name TEXT, address TEXT, latitude REAL, longitude REAL, email TEXT, phonenumber TEXT, url TEXT, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Works?
        //SQLiteStatement statement = db.compileStatement("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    } //sqlstatement

  public ArrayList<Cursor> getData(String Query){
      //get writable database
      SQLiteDatabase sqlDB = this.getWritableDatabase();
      String[] columns = new String[] { "message" };
      //an array list of cursor to save two cursors one has results from the query
      //other cursor stores error message if any errors are triggered
      ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
      MatrixCursor Cursor2= new MatrixCursor(columns);
      alc.add(null);
      alc.add(null);

      try{
          String maxQuery = Query ;
          //execute the query results will be save in Cursor c
          Cursor c = sqlDB.rawQuery(maxQuery, null);

          //add value to cursor2
          Cursor2.addRow(new Object[] { "Success" });

          alc.set(1,Cursor2);
          if (null != c && c.getCount() > 0) {

              alc.set(0,c);
              c.moveToFirst();

              return alc ;
          }
          return alc;
      } catch(SQLException sqlEx){
          Log.d("printing exception", sqlEx.getMessage());
          //if any exceptions are triggered save the error message to cursor an return the arraylist
          Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
          alc.set(1,Cursor2);
          return alc;
      } catch(Exception ex){
          Log.d("printing exception", ex.getMessage());

          //if any exceptions are triggered save the error message to cursor an return the arraylist
          Cursor2.addRow(new Object[] { ""+ex.getMessage() });
          alc.set(1,Cursor2);
          return alc;
      }
  }

}