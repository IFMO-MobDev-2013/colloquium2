package com.example.col2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

public class ActivityAdd extends Activity {

    DB db;
    EditText etName;
    EditText etMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        etName = (EditText) findViewById(R.id.etNameAdd);
        etMark = (EditText) findViewById(R.id.etMarkAdd);
        db = new DB(this);

        Button btnSave = (Button) findViewById(R.id.btnSaveAdd);
        View.OnClickListener oclBtnSave = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newName = etName.getText().toString();
                String newMark = etMark.getText().toString();
                db.addInOb(newName, newMark);
                setResult(0);
                finish();
            }
        };
        btnSave.setOnClickListener(oclBtnSave);



    }
}
