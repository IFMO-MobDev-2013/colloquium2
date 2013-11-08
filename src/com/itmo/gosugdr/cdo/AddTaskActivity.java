package com.itmo.gosugdr.cdo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends Activity {

	boolean isEdit = false;
	int id = -1;
	int subjectId = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);
		Intent intent = getIntent();
		subjectId = intent.getIntExtra("subjectid2", 0);
		if (intent.getAction().equals("com.itmo.gosugdr.coins.intent.action.task.EDIT")) {
			isEdit = true;
			((Button) findViewById(R.id.addButton)).setText(R.string.save);
			id = Integer.parseInt(intent.getStringExtra("id"));
//			subjectId = Integer.parseInt(intent.getStringExtra("subjectId"));
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			Task item = db.getTask(id);
			((EditText) findViewById(R.id.labelEditText)).setText(item
					.getLabel());
			((EditText) findViewById(R.id.amountEditText)).setText(Double
					.toString(item.getAmount()));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	private boolean checkInputData() {
		if (((EditText) findViewById(R.id.labelEditText)).getText().length() == 0) {
			return false;
		}
		String amount = ((EditText) findViewById(R.id.amountEditText))
				.getText().toString();
		try {
			if (amount.isEmpty()) {
				throw new NumberFormatException();
			}
			Double.parseDouble(amount);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public void onAddButtonClick(View v) {
		if (checkInputData()) {
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			String label = ((EditText) findViewById(R.id.labelEditText)).getText()
					.toString();
			double amount = Double.parseDouble(((EditText) findViewById(R.id.amountEditText))
					.getText().toString());
			Task item = new Task(label, amount, subjectId);
			if (isEdit) {
				item.id = id;
				db.updateTask(item);
			} else {
				db.addTask(item);
			}// send result code 100 to notify about product update
			setResult(100, getIntent());
			finish();
		} else {
			EditText amountEditText = (EditText) findViewById(R.id.amountEditText);
			amountEditText.setError(getString(R.string.validation));
		}
	}

	public void onCancelButtonClick(View v) {
		finish();
	}

}
