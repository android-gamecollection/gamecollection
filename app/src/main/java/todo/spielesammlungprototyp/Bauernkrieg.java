package todo.spielesammlungprototyp;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class Bauernkrieg extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bauernkrieg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);

        }
        return super.onOptionsItemSelected(item);
    }
}
