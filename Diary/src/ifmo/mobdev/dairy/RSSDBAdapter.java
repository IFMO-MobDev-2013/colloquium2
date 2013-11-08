package ifmo.mobdev.dairy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RSSDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static String DROP = " DROP TABLE IF EXISTS ";

    private static final String TAG = "SUBDBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "rssdata";

    //----- sub ----
    private static final String SUB_DATABASE_TABLE = "sub";
    public static final String KEY_POINTS = "all_points";
    public static final String KEY_NAME = "name";
    private static final int DATABASE_VERSION = 2;
    private static final String SUB_DATABASE_CREATE =
            "create table " + SUB_DATABASE_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_POINTS + " integer not null, " + KEY_NAME + " text not null);";
    //---- marks -----
    private static final String MARKS_DATABASE_TABLE = "marks";
    public static final String KEY_SUB_ID = "sub_id";
    public static final String KEY_TASK_POINTS = "points";
    private static final String MARKS_DATABASE_CREATE =
            "create table " + MARKS_DATABASE_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_SUB_ID + " integer not null, " + KEY_NAME + " integer not null, " + KEY_TASK_POINTS + " text not null);";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SUB_DATABASE_CREATE);
            db.execSQL(MARKS_DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL(DROP + SUB_DATABASE_TABLE);
            db.execSQL(DROP + MARKS_DATABASE_TABLE);
            onCreate(db);
        }
    }


    public RSSDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public RSSDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (Exception e) {
            try {
                mDb = mDbHelper.getReadableDatabase();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void drop() {
        mDb.execSQL(DROP + SUB_DATABASE_TABLE);
        mDb.execSQL(DROP + MARKS_DATABASE_TABLE);
        mDb.execSQL(SUB_DATABASE_CREATE);
        mDb.execSQL(MARKS_DATABASE_CREATE);
    }
    public long createSub(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_POINTS, "0");

        return mDb.insert(SUB_DATABASE_TABLE, null, initialValues);
    }

    public long createTask(int sub_id, String name, String points) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SUB_ID, sub_id);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_TASK_POINTS, points);

        int p = getSubPointsByID(sub_id);
        String sub_name = getSubNameByID(sub_id);
        updateSub(sub_id, sub_name, Integer.toString(p + Integer.parseInt(points)));

        return mDb.insert(MARKS_DATABASE_TABLE, null, initialValues);
    }

    public int getSubIdByName(String name) {
        if (name == null) return -1;
        Cursor cursor = mDb.query(SUB_DATABASE_TABLE, new String[] {KEY_ROWID},
                KEY_NAME + "=?", new String[] {name}, null, null, null, null);
        int index = cursor.getColumnIndex(KEY_ROWID);
        cursor.moveToNext();
        int a = -1;
        try {
            a = cursor.getInt(index);
        } catch (Exception e) {
        }
        return a;
    }

    public int getSubPointsByName(String name) {
        if (name == null) return -1;
        Cursor cursor = mDb.query(SUB_DATABASE_TABLE, new String[] {KEY_POINTS},
                KEY_NAME + "=?", new String[] {name}, null, null, null, null);
        int index = cursor.getColumnIndex(KEY_POINTS);
        cursor.moveToNext();
        int a = -1;
        try {
            a = cursor.getInt(index);
        } catch (Exception e) {
        }
        return a;
    }

    public int getSubPointsByID(int id) {
        Cursor cursor = mDb.query(SUB_DATABASE_TABLE, new String[] {KEY_POINTS},
                KEY_ROWID + "=" + id, null, null, null, null);
        int index = cursor.getColumnIndex(KEY_POINTS);
        cursor.moveToNext();
        int a = -1;
        try {
            a = cursor.getInt(index);
        } catch (Exception e) {
        }
        return a;
    }

    public String getSubNameByID(int id) {
        Cursor cursor = mDb.query(SUB_DATABASE_TABLE, new String[] {KEY_NAME},
                KEY_ROWID + "=" + id, null, null, null, null);
        int index = cursor.getColumnIndex(KEY_NAME);
        cursor.moveToNext();
        String a = null;
        try {
            a = cursor.getString(index);
        } catch (Exception e) {
        }
        return a;
    }

    public int getSubIDByTaskID(long id) {
        Cursor cursor = mDb.query(MARKS_DATABASE_TABLE, new String[] {KEY_SUB_ID},
                KEY_ROWID + "=" + id, null, null, null, null);
        int index = cursor.getColumnIndex(KEY_SUB_ID);
        cursor.moveToNext();
        int a = -1;
        try {
            a = cursor.getInt(index);
        } catch (Exception e) {
        }
        return a;
    }

    public int getPointsByTaskID(long id) {
        Cursor cursor = mDb.query(MARKS_DATABASE_TABLE, new String[] {KEY_TASK_POINTS},
                KEY_ROWID + "=" + id, null, null, null, null);
        int index = cursor.getColumnIndex(KEY_TASK_POINTS);
        cursor.moveToNext();
        int a = -1;
        try {
            a = cursor.getInt(index);
        } catch (Exception e) {
        }
        return a;
    }

    public boolean deleteSub(long rowId) {
        return mDb.delete(SUB_DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteTask(long rowId) {
        int sub_id = getSubIDByTaskID(rowId);
        int points = getPointsByTaskID(rowId);
        int p = getSubPointsByID(sub_id);
        String sub_name = getSubNameByID(sub_id);
        updateSub(sub_id, sub_name, Integer.toString(p - points));
        return mDb.delete(MARKS_DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllSubs() {
        return mDb.query(SUB_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_POINTS,
                KEY_NAME}, null, null, null, null, null);
    }

    public Cursor fetchAllTasks() {
        return mDb.query(MARKS_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SUB_ID,
                KEY_NAME, KEY_TASK_POINTS}, null, null, null, null, null);
    }

    public boolean updateSub(long rowId, String name, String points) {
        ContentValues args = new ContentValues();
        args.put(KEY_POINTS, points);
        args.put(KEY_NAME, name);

        return mDb.update(SUB_DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllTasksBySub(int chID) {
        Cursor mCursor = mDb.query(MARKS_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SUB_ID, KEY_NAME, KEY_TASK_POINTS},
                KEY_SUB_ID + "=" + chID, null, null, null, null);
        return mCursor;
    }
}
