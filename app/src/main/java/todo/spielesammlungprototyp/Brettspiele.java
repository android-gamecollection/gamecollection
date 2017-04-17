package todo.spielesammlungprototyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Brettspiele extends AppCompatActivity {

    private ScrollView scroll_console;
    private TextView text_console;
    private EditText input_console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brettspiele);
        scroll_console = (ScrollView) findViewById(R.id.scroll_console);
        text_console = (TextView) findViewById(R.id.text_console);
        input_console = (EditText) findViewById(R.id.input_console);
    }

    @Override
    protected void onStart() {
        super.onStart();
        scrollToBottom();
    }

    private void scrollToBottom() {
        scroll_console.post(new Runnable() {
            @Override
            public void run() {
                scroll_console.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void confirmInput(View view) {
        String input = input_console.getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        clearInput();
    }

    public void addOutput(String str) {
        text_console.append(str + "\n");
    }

    public void addOutputln(String str) {
        addOutput(str + "\n");
    }

    public void clearOutput() {
        text_console.setText("");
    }

    private void clearInput() {
        input_console.getText().clear();
    }
}
