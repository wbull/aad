package com.wtb.fragment.tasks;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class TaskActivity extends Activity {
	
	public static final String TAG = "TaskActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		TaskDatabaseHelper db = new TaskDatabaseHelper(this);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.task_entry);
		
		Bundle b = getIntent().getExtras();
		
		
		TextView tv = (TextView) findViewById(R.id.task_entry);
		if (b == null) {
			ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
			switcher.showNext();
			
		} else {
			int taskId = b.getInt("id");
			if (taskId != -1) {
				
				Task task = db.getTask(taskId);
				tv.setText(task.toString());	
				}
			}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.delete_task:
	    		Bundle b = getIntent().getExtras();
	    		int taskId = b.getInt("id");
	            deleteTask(taskId);
	            return true;
	        case R.id.add_task:
	        	EditText et = (EditText) findViewById(R.id.hidden_edit_view);
	        	String text = et.getText().toString();
	        	addTask(text);
	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	private void addTask(String text) {
		Log.d(TAG, "Adding task: " + text);
		TaskDatabaseHelper tdh = new TaskDatabaseHelper(this);
		Task task = new Task();
		task.setTaskDetails(text);
		tdh.addTask(task);
		
	}

	private void deleteTask(int id) {
		Log.d(TAG, "DELETING TASK: " + Integer.toString(id));
		TaskDatabaseHelper tdh = new TaskDatabaseHelper(this);
		Task taskToDelete = tdh.getTask(id);
		tdh.deleteTask(taskToDelete);
		Intent i = new Intent(this, MainActivity.class);
	        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(i);	
	}


}
