package com.example.col2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityEdit extends Activity {

    DB db;
    EditText etName;
    EditText etMark;
    long idOb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        Bundle extras = getIntent().getExtras();
        idOb = extras.getLong("st");

        db = new DB(this);
        etName = (EditText) findViewById(R.id.etNameEdit);
        etMark = (EditText) findViewById(R.id.etMarkEdit);

        etName.setText(db.getFromObNameById(idOb));
        etMark.setText(db.getFromObMarkById(idOb));

        Button btnSave = (Button) findViewById(R.id.btnSaveEdit);
        View.OnClickListener oclBtnSave = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newName = etName.getText().toString();
                String newMark = etMark.getText().toString();
                db.deleteInObById(idOb);
                db.addInOb(newName, newMark);
                setResult(0);
                finish();
            }
        };
        btnSave.setOnClickListener(oclBtnSave);

        Button btnDel = (Button) findViewById(R.id.btnDelEdit);
        View.OnClickListener oclBtnDel = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newUrl = etName.getText().toString();
                String newName = etMark.getText().toString();
                db.deleteInObById(idOb);
                setResult(0);
                finish();
            }
        };
        btnDel.setOnClickListener(oclBtnDel);


    }
}
