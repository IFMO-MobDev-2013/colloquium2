package com.itmo.gosugdr.cdo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TasksActivity extends Activity {

	private static final String KEY_ID = "_id";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_LABEL = "label";

	ArrayList<HashMap<String, String>> itemsList;
	private ProgressDialog pDialog;
	String[] sqliteIds;
	ListView lv;
	int subjectId = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		Intent intent = getIntent();
		subjectId = Integer.parseInt(intent.getStringExtra("subjectid"));
		setUpView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			menu.add(Menu.NONE, 1, 1, R.string.delete);
			menu.add(Menu.NONE, 0, 0, R.string.edit);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			setUpView();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		if (menuItemIndex == 2) {
			Intent intent = new Intent(this, ViewTaskActivity.class);
			intent.putExtra("id", sqliteIds[info.position]);
			startActivity(intent);
		}
		if (menuItemIndex == 1) {
			DatabaseHandler rssDb = new DatabaseHandler(getApplicationContext());
			Task i = new Task();
			i.setId(Integer.parseInt(sqliteIds[info.position]));
			rssDb.deleteTask(i);
			setUpView();
		} else if (menuItemIndex == 0) {
			Intent intent = new Intent("com.itmo.gosugdr.coins.intent.action.task.EDIT");
			intent.putExtra("id", sqliteIds[info.position]);
			intent.putExtra("subjectid2", subjectId);
			startActivityForResult(intent, 100);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			Intent intent = new Intent("com.itmo.gosugdr.coins.intent.action.task.ADD");
			intent.putExtra("subjectid2", subjectId);
			startActivityForResult(intent, 100);
		}
		return true;
	}

	private void setUpView() {
		itemsList = new ArrayList<HashMap<String, String>>();
		lv = (ListView) findViewById(R.id.list);
		final Context context = (Context) this;
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(context, ViewTaskActivity.class);
				intent.putExtra("id", sqliteIds[arg2]);
				startActivity(intent);
			}
		});
		new LoadItems().execute();
	}

	class LoadItems extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TasksActivity.this);
			pDialog.setMessage(getText(R.string.loading));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		};

		@Override
		protected Void doInBackground(Void... params) {
			runOnUiThread(new Runnable() {
				public void run() {
					DatabaseHandler db = new DatabaseHandler(
							getApplicationContext());

					List<Task> items = db.getAllTasks(subjectId);

					sqliteIds = new String[items.size()];
					for (int i = 0; i < items.size(); i++) {

						Task s = items.get(i);

						HashMap<String, String> map = new HashMap<String, String>();

						map.put(KEY_ID, Long.toString(s.getId()));
						map.put(KEY_AMOUNT, Double.toString(s.getAmount()));
						map.put(KEY_LABEL, s.getLabel());

						itemsList.add(map);

						sqliteIds[i] = Long.toString(s.getId());
					}
					ListAdapter adapter = new SimpleAdapter(TasksActivity.this,
							itemsList, R.layout.task_list_item, new String[] {
									KEY_ID, KEY_AMOUNT, KEY_LABEL }, new int[] {
									R.id.sqlite_id, R.id.amount, R.id.label });
					lv.setAdapter(adapter);
					registerForContextMenu(lv);
					TextView amountTextView = (TextView) findViewById(R.id.totalValueTextView);
					amountTextView.setText(Double.toString(db.getTotalByTasks(subjectId)));
					db.close();
				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();

		}
	}
}
