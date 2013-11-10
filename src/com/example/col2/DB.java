package com.example.col2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;


public class DB {
    SQLiteDatabase sqLiteDatabase;

    public DB(Context context) {
        SQL helper = new SQL(context, "myDataBase",null, 1);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public Cursor getFeeds() {
        return sqLiteDatabase.query("ob", null, null, null, null, null, "_id");
    }

    public Cursor getObs() {
        return sqLiteDatabase.query("ob", null, null, null, null, null, "_id");
    }



    public void delete(long x) {
        sqLiteDatabase.delete("content", "idInRss="+(new Long(x)).toString(), null);
    }

    public void deleteInObById(long id) {
        sqLiteDatabase.delete("ob", "_id="+(new Long(id)).toString(), null);
    }

    public void insert(Long idInRss, String stringForName, String stringForSummary) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("idInOb", idInRss);
        contentValues.put("name", stringForName);
        contentValues.put("mark", stringForSummary);

        sqLiteDatabase.insert("ob", null, contentValues);
    }

    public void putNewFeed(String name, String url) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("url", url);

        sqLiteDatabase.insert("rss", null, contentValues);
    }

    public Cursor getName(Long idRss) {
        return sqLiteDatabase.query(
                "ob", new String[]{"_id", "name"}, "idInOb="+idRss.toString(), null, null, null, null);
    }

    public void addInOb(String newName, String newMark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", newName);
        contentValues.put("mark", newMark);

        sqLiteDatabase.insert("ob", null, contentValues);
    }

    public String getFromObNameById(long spinnerPosition) {
        Cursor c = sqLiteDatabase.query(
                "ob", new String[]{"name"}, "_id="+(new Long(spinnerPosition).toString()), null, null, null, null);
        c.moveToNext();
        String r =   c.getString(c.getColumnIndex("name"));

        return r;
    }

    public String getFromObMarkById(long spinnerPosition) {
        Cursor c = sqLiteDatabase.query("ob", new String[]{"mark"}, "_id="+(new Long(spinnerPosition).toString()), null, null, null, null);
        c.moveToNext();
        String r = c.getString(c.getColumnIndex("mark"));
        return r;
    }

    public String getSummary(long id) {
        Cursor c = sqLiteDatabase.query(
                "content", new String[]{"summary"}, "_id="+(new Long(id).toString()), null, null, null, null);
        c.moveToNext();
        return c.getString(c.getColumnIndex("summary"));
    }

    public String getObById(long id) {
        Cursor c = sqLiteDatabase.query(
                "ob", new String[]{"name"}, "_id="+(new Long(id).toString()), null, null, null, null);
        c.moveToNext();
        return c.getString(c.getColumnIndex("name"));
    }



}