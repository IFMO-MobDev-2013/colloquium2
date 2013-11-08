package ru.ifmo.rain.loboda.colloq2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddMarkActivity extends Activity {
    private int id;
    int code;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mark_layout);
        Bundle bundle = getIntent().getExtras();
        code = MarksActivity.CODE;
        if(bundle != null && bundle.containsKey("id")){
            code = MarksActivity.UPDATE_CODE;
            id = bundle.getInt("id");
            ((EditText)findViewById(R.id.add_markname_edittext)).setText(bundle.getString("name"));
            ((EditText)findViewById(R.id.add_mark_edittext)).setText(bundle.getString("mark"));
        }
        findViewById(R.id.add_success_mark_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ename = ((EditText)findViewById(R.id.add_markname_edittext));
                EditText emark = ((EditText)findViewById(R.id.add_mark_edittext));
                if("".equals(ename)){
                    Toast.makeText(getBaseContext(), R.string.empty_job, Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = ename.getText().toString();
                int mark;
                try{
                    mark = Integer.parseInt(emark.getText().toString());
                } catch (NumberFormatException e){
                    Toast.makeText(getBaseContext(), R.string.bad_int, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("mark", mark);
                setResult(code, intent);
                finish();
            }
        });
    }
}