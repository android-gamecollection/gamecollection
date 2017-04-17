package todo.spielesammlungprototyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;

public class Brettspiele extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brettspiele);
    }

    @Override
    protected void onStart() {
        super.onStart();
        scrollToBottom();
    }

    private void scrollToBottom() {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_console);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
