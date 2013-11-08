package com.example.colloquium2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CourseSQLHelper extends SQLiteOpenHelper {

  public static final String TABLE_COURSES = "courses";
  public static final String TABLE_GRADES = "grades";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";

    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_GRADE = "grade";

  private static final String DATABASE_NAME = "grades.db";
  private static final int DATABASE_VERSION = 3;

  // Database creation sql statement
  private static final String CREATE_COURSES =
          "CREATE TABLE " + TABLE_COURSES +
          " (" + COLUMN_ID + " integer primary key autoincrement, " +
                 COLUMN_NAME + " text not null)";

    private static final String CREATE_GRADES =
          "CREATE TABLE " + TABLE_GRADES +
          " ( " + COLUMN_ID + " integer primary key autoincrement, " +
                  COLUMN_COURSE_ID + " integer, " +
                  COLUMN_NAME + " text not null, " +
                  COLUMN_GRADE + " integer, " +
                  "foreign key (grade) references courses(id))";

  public CourseSQLHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(CREATE_COURSES);
      database.execSQL(CREATE_GRADES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(CourseSQLHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
    onCreate(db);
  }

}
