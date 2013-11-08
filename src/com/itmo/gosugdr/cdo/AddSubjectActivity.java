package com.itmo.gosugdr.cdo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddSubjectActivity extends Activity {

	boolean isEdit = false;
	int id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_subject);
		Intent intent = getIntent();
		if (intent.getAction().equals(
				"com.itmo.gosugdr.coins.intent.action.subject.EDIT")) {
			isEdit = true;
			((Button) findViewById(R.id.addButton)).setText(R.string.save);
			id = Integer.parseInt(intent.getStringExtra("id"));
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			Subject item = db.getSubject(id);
			((EditText) findViewById(R.id.labelEditText)).setText(item
					.getLabel());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_subject, menu);
		return true;
	}

	public void onAddButtonClick(View v) {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		String label = ((EditText) findViewById(R.id.labelEditText)).getText()
				.toString();
		Subject item = new Subject(label);
		if (isEdit) {
			item.setId(id);
			item.setLabel(((EditText)findViewById(R.id.labelEditText)).getText().toString());
			db.updateSubject(item);
		} else {
			db.addSubject(item.getLabel());
		}// send result code 100 to notify about product update
		setResult(100, getIntent());
		finish();
	}

	public void onCancelButtonClick(View v) {
		finish();
	}

}
