package ifmo.mobdev.dairy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TaskEdit extends Activity {
    private RSSDBAdapter mDbHelper;
    Button ok;
    EditText name;
    EditText points;
    String sub_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        ok = (Button) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.edtxtn);
        points = (EditText) findViewById(R.id.edtp);

        mDbHelper = new RSSDBAdapter(this);
        mDbHelper.open();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        sub_name = extras.getString("name");

        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String n = name.getText().toString();
                String p = points.getText().toString();
                mDbHelper.createTask(mDbHelper.getSubIdByName(sub_name), n, p);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}

