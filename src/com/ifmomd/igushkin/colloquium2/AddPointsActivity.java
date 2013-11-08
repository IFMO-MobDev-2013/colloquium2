package com.ifmomd.igushkin.colloquium2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Sergey on 11/8/13.
 */
public class AddPointsActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    EditText edtDescription;
    SeekBar sbPoints;
    TextView tvPoints;
    Button btnAddPoints;

    long id = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_points_activity_layout);
        edtDescription = (EditText)findViewById(R.id.edtDescription);
        sbPoints =(SeekBar)findViewById(R.id.sbPoints);
        sbPoints.setOnSeekBarChangeListener(this);
        tvPoints = (TextView)findViewById(R.id.tvPoints);
        btnAddPoints = (Button)findViewById(R.id.btnAddPonts);
        btnAddPoints.setOnClickListener(this);

        id = getIntent().getLongExtra(SubjectsActivity.EXTRA_SUBJECT_ID, -1);
        if (id != -1) {
            edtDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            setTitle(getString(R.string.txtEditDetails));
            sbPoints.setProgress(getIntent().getIntExtra(EXTRA_POINTS, 0));
            btnAddPoints.setText(getString(R.string.btnAddPoints_edit_text));
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (id != -1) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuDelete) {
            Intent i = new Intent();
            i.putExtra(SubjectsActivity.EXTRA_SUBJECT_ID, id);
            i.putExtra(EXTRA_DELETE, true);
            setResult(RESULT_OK, i);
            finish();
        }
        return true;
    }

    public static final String EXTRA_DELETE = "delete";

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvPoints.setText(Integer.toString(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_POINTS = "points";

    @Override
    public void onClick(View v) {
        Intent result = new Intent();
        if (id != -1)
            result.putExtra(SubjectsActivity.EXTRA_SUBJECT_ID, id);
        result.putExtra(EXTRA_DESCRIPTION, edtDescription.getText().toString());
        result.putExtra(EXTRA_POINTS, sbPoints.getProgress());
        setResult(RESULT_OK, result);
        finish();
    }
}