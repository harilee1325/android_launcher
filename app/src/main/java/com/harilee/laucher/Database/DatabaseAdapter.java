package com.harilee.laucher.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseAdapter {

    DatabaseHelper myhelper;
    private ArrayList<HomeModel> homeModels = new ArrayList<>();

    public DatabaseAdapter(Context context)
    {
        myhelper = new DatabaseHelper(context);
    }

    public long insertHomeData(String name, String packageName, String appIcon)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.APP_LABEL, name);
        contentValues.put(DatabaseHelper.PACKAGE_NAME, packageName);
        contentValues.put(DatabaseHelper.APP_ICON, appIcon);
        long id = dbb.insert(DatabaseHelper.TABLE_NAME_HOME, null , contentValues);
        return id;
    }

    public ArrayList<HomeModel> getHomeData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {DatabaseHelper.APP_ICON,DatabaseHelper.APP_LABEL, DatabaseHelper.PACKAGE_NAME};
        Cursor cursor =db.query(DatabaseHelper.TABLE_NAME_HOME,columns,null,null,null,null,null);
        HomeModel homeModel = null;
        while (cursor.moveToNext())
        {
            homeModel = new HomeModel();
            String appIcon =cursor.getString(cursor.getColumnIndex(DatabaseHelper.APP_ICON));
            String appLabel =cursor.getString(cursor.getColumnIndex(DatabaseHelper.APP_LABEL));
            String packageName =cursor.getString(cursor.getColumnIndex(DatabaseHelper.PACKAGE_NAME));
            homeModel.setAppLabel(appLabel);
            homeModel.setIcon(appIcon);
            homeModel.setPackageName(packageName);
            homeModels.add(homeModel);

        }
        return homeModels;
    }

    public String getHomeCount() {
        int counter = 0;
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {DatabaseHelper.APP_ID};
        Cursor cursor =db.query(DatabaseHelper.TABLE_NAME_HOME,columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
          counter++;

        }
        return String.valueOf(counter);
    }
//    public  int deleteHomeData(String uname)
//    {
//        SQLiteDatabase db = myhelper.getWritableDatabase();
//        String[] whereArgs ={uname};
//
//        int count =db.delete(DatabaseHelper.TABLE_NAME ,DatabaseHelper.NAME+" = ?",whereArgs);
//        return  count;
//    }

//    public int updateName(String oldName , String newName)
//    {
//        SQLiteDatabase db = myhelper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(myDbHelper.NAME,newName);
//        String[] whereArgs= {oldName};
//        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
//        return count;
//    }

    static class DatabaseHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "LauncherDatabase";    // Database Name
        private static final String TABLE_NAME_HOME = "HomeTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String APP_ID = "app_id";
        private static final String PACKAGE_NAME = "package_name";
        private static final String APP_LABEL = "app_label";
        private static final String APP_ICON = "app_icon";


       //command to create a table to store the data of home page icon list
        private static final String CREATE_TABLE_HOME = "CREATE TABLE "+TABLE_NAME_HOME+
                " ("+APP_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+PACKAGE_NAME+" VARCHAR(255) ,"+ APP_LABEL+" VARCHAR(225),"+APP_ICON+" VARCHAR(225));";

        //to drop the table if it exits
        private static final String DROP_TABLE_HOME ="DROP TABLE IF EXISTS "+TABLE_NAME_HOME;
        private Context context;
        private String TAG = "Database";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE_HOME);
            } catch (Exception e) {
                Log.e(TAG, "onCreate: "+e.getMessage() );
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE_HOME);
                onCreate(db);
            }catch (Exception e) {
                Log.e(TAG, "onCreate: "+e.getMessage() );
            }
        }
    }

}
