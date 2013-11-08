package ifmo.mobdev.dairy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SubEdit extends Activity {
    private RSSDBAdapter mDbHelper;
    Button ok;
    EditText txted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sub);

        ok = (Button) findViewById(R.id.button);
        txted = (EditText) findViewById(R.id.edtn);

        mDbHelper = new RSSDBAdapter(this);
        mDbHelper.open();

        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String name = txted.getText().toString();
                mDbHelper.createSub(name);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
