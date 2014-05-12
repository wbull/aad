package com.wtb.fragment.tasks;

import java.util.List;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wtb.fragment.tasks.TaskDialog.EditTaskDialogListener;


public class MainActivity extends FragmentActivity implements EditTaskDialogListener {
	
	public final String TAG = "TASKS - MainActivity";
    private boolean mDualPane = true;
    private TaskFragment mTaskFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "In onCreate()");
		
		this.setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		
		View fragmentContainer = this.findViewById(R.id.fragmentContainer);
		mDualPane = fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE;
		
		// Update view and populate tasks
		updateView();
	}
		
	private void showTask(Task task) {
    	if (mDualPane) {
    		Log.d(TAG," dualPaneTrue, showTask() int: " + task.getId());   		
            FragmentTransaction ft = getFragmentManager().beginTransaction();            
            ft.replace(R.id.fragmentContainer, mTaskFragment);
            ft.commit();
    		mTaskFragment.setTask(task); 
    	} else {
    		Log.d(TAG,"dualPane fale, start TaskActivity for id: " + task.getId());
            Intent intent = new Intent();
            intent.setClass(this, TaskActivity.class);
            intent.putExtra("id", task.getId());
            startActivity(intent);
    	}
		
	}
	
    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        TaskDialog editTaskDialog = new TaskDialog();
        editTaskDialog.show(fm, "fragment_edit_name");
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_options, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.add_task:
	        	showEditDialog();
	    		Log.d(TAG,"showTask() without task int: ");
	            return true;
	        case R.id.delete_task2:
	        	Log.d(TAG,"delete() without task int: ");
	        	FragmentManager fm =  getFragmentManager();
	        	TaskFragment tf = (TaskFragment) fm.findFragmentById(R.id.fragmentContainer);
	        	TaskDatabaseHelper tdh = new TaskDatabaseHelper(this);
	        	// Null check in the case that the user hasn't selected a task yet
	        	if (tf != null) {
		        	String taskDetail = tdh.getTask(tf.taskId).toString();
		        	tdh.deleteTaskInt(tf.taskId);
		        	updateView();
		        	Toast.makeText(this, "Deleted task: " + taskDetail, Toast.LENGTH_SHORT).show();
	        	}
	        	
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    
	}
	
	public void updateView() {
		Log.d(TAG, "in UpdateView");
		TaskDatabaseHelper db = new TaskDatabaseHelper(this);
		List<Task> tasks = db.getAllTasks();
		final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ArrayAdapter<Task> taskAdapter = new ArrayAdapter<Task>(this, android.R.layout.list_content, tasks) {
		     @Override
		     public View getView(int position, View convertView, ViewGroup parent) {
		
		            View row;
		            
		            if (convertView == null) {
		                row = inflater.inflate(android.R.layout.simple_list_item_2, null);
		            }
		            else {
		                row = convertView;
		            }
		
		            TextView tv1 = (TextView) row.findViewById(android.R.id.text1);
		            tv1.setText("Task " + Integer.toString(position + 1));
		
		            return row;
		        }
		};
			
		FragmentManager fragmentManager = getFragmentManager();
		final ListFragment listFragment = (ListFragment) fragmentManager.findFragmentById(R.id.listFragment);
		listFragment.setListAdapter(taskAdapter);
		listFragment.setEmptyText("No tasks.  Tap on Add Tasks button to add one.");
		
		listFragment.getListView().setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View view, int position, long id) {
		    	av.setSelected(true);
		    	listFragment.getListView().setItemChecked(position, true);
		    	showTask((Task) av.getItemAtPosition(position));
		    	}
		    });
		mTaskFragment = new TaskFragment();	
	}

	public void onFinishEditDialog(String inputText) {
		Log.d(TAG, "in onFinishEditDialog");
		Toast.makeText(getApplicationContext(), "Added new Task: " + inputText, Toast.LENGTH_SHORT).show();
		updateView();
	}
}