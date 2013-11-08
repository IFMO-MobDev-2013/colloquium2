package com.itmo.gosugdr.cdo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ViewTaskActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		Intent intent = getIntent();
		int id = Integer.parseInt(intent.getStringExtra("id"));
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		Task item = db.getTask(id);
		((TextView) findViewById(R.id.labelValue)).setText(item
				.getLabel());
		((TextView) findViewById(R.id.amountValue)).setText(Double
				.toString(item.getAmount()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}
	public void onCancelButtonClick(View v) {
		finish();
	}
}
