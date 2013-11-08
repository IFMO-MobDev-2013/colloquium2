package ru.ifmo.rain.loboda.colloq2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddSubject extends Activity {
    private int id = -1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subject);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("id")){
            id = bundle.getInt("id");
            ((EditText)findViewById(R.id.editText)).setText(bundle.getString("name"));
        }
        findViewById(R.id.success_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String text = ((EditText)findViewById(R.id.editText)).getText().toString();
                if(text.equals("")){
                    Toast.makeText(getBaseContext(), R.string.empty_name, Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("name", text);
                int code = MainActivity.CODE;
                if(id != -1){
                    code = MainActivity.UPDATE_CODE;
                    intent.putExtra("id", id);
                }
                setResult(code, intent);
                finish();
            }
        });
    }
}