package com.wtb.fragment.tasks;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "tasks.sqlite";
	private static final int DB_VERSION = 1;
	
	private static final String TABLE_TASK = "tasks";
	private static final String COLUMN_TASK_DETAILS = "task_details";
	private static final String COLUMN_ID = "id";
	
	//Create db statement
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_TASK + "(" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TASK_DETAILS
			+ " text not null);";	

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create task table
		db.execSQL(DATABASE_CREATE);
	}
	
	public TaskDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS tasks");
		
		this.onCreate(db);
	}
	
	/* CRUD Operations */
	public void addTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TASK_DETAILS, task.toString());
		db.insert(TABLE_TASK, null, cv);
		
		db.close();
	}
	
	public Task getTask(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
				
		String query = "SELECT * FROM tasks WHERE id = " + Integer.toString(id);
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		Task task = new Task();
		task.setId(Integer.parseInt(cursor.getString(0)));
		task.setTaskDetails(cursor.getString(1));
		
		Log.d("getTask("+id+")", task.toString());
		
		return task;		
	}
	
	public List<Task> getAllTasks() {
		List<Task> tasks = new LinkedList<Task>();
		String query = "SELECT * FROM " + TABLE_TASK;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Task task = null;
		if (cursor.moveToFirst()){
			do {
				task = new Task();
				task.setId(Integer.parseInt(cursor.getString(0)));
				task.setTaskDetails(cursor.getString(1));
				
				tasks.add(task);
			} while (cursor.moveToNext());
		}
		
		Log.d("getAllTasks", tasks.toString());
		return tasks;		
	}
	
	public Cursor getAllTasksCursor() {
		String query = "SELECT * FROM " + TABLE_TASK;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		return cursor;
		
	}
	
	public int updateTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("id", task.getId());
		values.put("task_details", task.getTaskDetails());
		
		int i = db.update(TABLE_TASK,
				values,
				COLUMN_ID+" = ?",
				new String[] { String.valueOf(task.getId()) });
		db.close();
		
		return i;
	}
	
	public void deleteTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_TASK,
				COLUMN_ID+" = ?",
				new String [] {String.valueOf(task.getId())});
	}
	
	public void deleteTaskInt(int i) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_TASK,
				COLUMN_ID+" = ?",
				new String [] {String.valueOf(Integer.toString(i))});
	}

}
