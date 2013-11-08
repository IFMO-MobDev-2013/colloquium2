package com.itmo.gosugdr.cdo;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VESION);
	}

	private static final String DB_NAME = "coll.sqlite3";
	private static final String TABLE_NAME_TASKS = "tasks";
	private static final String TABLE_NAME_SUBJECTS = "subjects";
	private static final int DB_VESION = 15;
	
	
	private static final String KEY_ID_TASKS = "_id";
	private static final String KEY_SCORE_TASKS = "score";
	private static final String KEY_LABEL_TASKS = "labelTask";
	private static final String KEY_SUBJECT_ID_TASKS = "subjectId";
	
	
	private static final String KEY_ID_SUBJECTS = "_id";
	private static final String KEY_LABEL_SUBJECTS = "labelSubject";
	
	private static final int ID_TASKS_COLUMN = 0;
	private static final int AMOUNT_TASKS_COLUMN = 1;
	private static final int LABEL_TASKS_COLUMN = 2;
	private static final int SUBJECT_ID_TASKS_COLUMN = 3;

	private static final int ID_COLUMN_SUBJECTS = 0;
	private static final int LABEL_COLUMN_SUBJECTS = 1;

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String CREATE_DB_TASKS = "CREATE TABLE " + TABLE_NAME_TASKS + " (" + KEY_ID_TASKS
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_SCORE_TASKS
				+ " DOUBLE, " + KEY_LABEL_TASKS + " TEXT NOT NULL, "
				+ KEY_SUBJECT_ID_TASKS + " INTEGER NOT NULL);";
		final String CREATE_DB_SUBJECTS = "CREATE TABLE " + TABLE_NAME_SUBJECTS + " (" + KEY_ID_TASKS
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ KEY_LABEL_SUBJECTS + " TEXT NOT NULL);";
		db.execSQL(CREATE_DB_TASKS);
		db.execSQL(CREATE_DB_SUBJECTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TASKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SUBJECTS);
		onCreate(db);
	}

	public void addTask(Task item) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SCORE_TASKS, item.getAmount()); 
		values.put(KEY_LABEL_TASKS, item.getLabel());
		values.put(KEY_SUBJECT_ID_TASKS, item.getSubjectId());
		db.insert(TABLE_NAME_TASKS, null, values);
		db.close();
	}
	
	public void addSubject(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LABEL_SUBJECTS, name);
		db.insert(TABLE_NAME_SUBJECTS, null, values);
		db.close();
	}

	public List<Task> getAllTasks(int l) {
		List<Task> taskList = new ArrayList<Task>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME_TASKS + " where " + KEY_SUBJECT_ID_TASKS + " = " + Integer.toString(l) + " ORDER BY "
				+ KEY_ID_TASKS + " DESC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Task task = new Task();
				task.setId(Integer.parseInt(cursor.getString(ID_TASKS_COLUMN)));
				task.setAmount(Double.parseDouble(cursor
						.getString(AMOUNT_TASKS_COLUMN)));
				task.setLabel(cursor.getString(LABEL_TASKS_COLUMN));
				task.setSubjectId(Integer.parseInt(cursor.getString(SUBJECT_ID_TASKS_COLUMN)));
				taskList.add(task);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return taskList;
	}
	
	public List<Subject> getAllSubjects() {
		List<Subject> subjectList = new ArrayList<Subject>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME_SUBJECTS + " ORDER BY "
				+ KEY_ID_SUBJECTS + " DESC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Subject subject = new Subject();
				subject.setId(Integer.parseInt(cursor.getString(ID_COLUMN_SUBJECTS)));
				subject.setLabel(cursor.getString(LABEL_COLUMN_SUBJECTS));
				subject.setTotal((int) getTotalByTasks(subject.getId()));
				subjectList.add(subject);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return subjectList;
	}

	public void deleteTask(Task item) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME_TASKS, KEY_ID_TASKS + " = ?",
				new String[] { String.valueOf(item.getId()) });
		db.close();
	}
	
	public void deleteSubject(Subject item) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME_SUBJECTS, KEY_ID_SUBJECTS + " = ?",
				new String[] { String.valueOf(item.getId()) });
		db.close();
	}

	public int updateTask(Task item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SCORE_TASKS, item.getAmount());
		values.put(KEY_LABEL_TASKS, item.getLabel());

		int update = db.update(TABLE_NAME_TASKS, values, KEY_ID_TASKS + " = ?",
				new String[] { String.valueOf(item.getId()) });
		db.close();
		return update;
	}
	
	public int updateSubject(Subject item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LABEL_SUBJECTS, item.getLabel());
		int update = db.update(TABLE_NAME_SUBJECTS, values, KEY_ID_SUBJECTS + " = ?",
				new String[] { String.valueOf(item.getId()) });
		db.close();
		return update;
	}

	public double getTotalByTasks(long l) {
		double total = 0;
		String selectQuery = "select sum( " + KEY_SCORE_TASKS + ") from " + TABLE_NAME_TASKS + " where " + KEY_SUBJECT_ID_TASKS + " = " + Long.toString(l);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Task item = new Task();
				if (cursor.getString(0) == null) {
					item.setAmount(0);
				} else {
					item.setAmount(Double.parseDouble(cursor
							.getString(0)));
				}
				total += item.getAmount();
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return total;
	}

	public Task getTask(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME_TASKS, new String[] { KEY_ID_TASKS, KEY_SCORE_TASKS,
				KEY_LABEL_TASKS, KEY_SUBJECT_ID_TASKS}, KEY_ID_TASKS + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Task item = new Task();

		item.setId(Integer.parseInt(cursor.getString(ID_TASKS_COLUMN)));
		item.setAmount(Double.parseDouble(cursor.getString(AMOUNT_TASKS_COLUMN)));
		item.setLabel(cursor.getString(LABEL_TASKS_COLUMN));
		item.setSubjectId(Integer.parseInt(cursor.getString(SUBJECT_ID_TASKS_COLUMN)));
		cursor.close();
		db.close();
		return item;
	}
	
	public Subject getSubject(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME_SUBJECTS, new String[] { KEY_ID_SUBJECTS,
				KEY_LABEL_SUBJECTS}, KEY_ID_SUBJECTS + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Subject item = new Subject();

		item.setId(Integer.parseInt(cursor.getString(ID_COLUMN_SUBJECTS)));
		item.setLabel(cursor.getString(LABEL_COLUMN_SUBJECTS));
		cursor.close();
		db.close();
		return item;
	}
}
