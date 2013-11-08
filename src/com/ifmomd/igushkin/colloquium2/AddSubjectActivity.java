package com.ifmomd.igushkin.colloquium2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Sergey on 11/8/13.
 */
public class AddSubjectActivity extends Activity implements View.OnClickListener {
    EditText edtSubjectName;
    Button btnAddSubject;

    long id = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subject_activity_layout);
        edtSubjectName = (EditText)findViewById(R.id.edtSubjectName);
        btnAddSubject = (Button)findViewById(R.id.btnAddSubject);
        btnAddSubject.setOnClickListener(this);

        id = getIntent().getLongExtra(SubjectsActivity.EXTRA_SUBJECT_ID, -1);
        if (id != -1) {
            edtSubjectName.setText(getIntent().getStringExtra(EXTRA_SUBJECT_NAME));
            setTitle(getString(R.string.txtEditSubject));
            edtSubjectName.setText(getIntent().getStringExtra(EXTRA_SUBJECT_NAME));
            edtSubjectName.setSelection(edtSubjectName.length());
            btnAddSubject.setText(getString(R.string.btnAddPoints_edit_text));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (id != -1) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public static final String EXTRA_DELETE = "delete";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuDelete) {
            Intent result = new Intent();
            result.putExtra(EXTRA_DELETE, true);
            result.putExtra(SubjectsActivity.EXTRA_SUBJECT_ID, id);
            setResult(RESULT_OK, result);
            finish();
        }
        return true;
    }

    public static final String EXTRA_SUBJECT_NAME = "subject name";

    @Override
    public void onClick(View v) {
        Intent result = new Intent();
        result.putExtra(EXTRA_SUBJECT_NAME, edtSubjectName.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }
}