package todo.spielesammlungprototyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

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

    public void confirmInput(View view) {
        final EditText input_console = (EditText) findViewById(R.id.input_console);
        String input = input_console.getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        clearInput();
    }

    private void clearInput() {
        final EditText editText = (EditText) findViewById(R.id.input_console);
        editText.getText().clear();
    }
}
