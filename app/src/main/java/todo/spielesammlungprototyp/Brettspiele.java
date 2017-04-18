package todo.spielesammlungprototyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import todo.spielesammlungprototyp.Chess.ChessAdapter;

public class Brettspiele extends AppCompatActivity {

    private ScrollView scroll_console;
    private TextView text_console;
    private EditText input_console;

    private ChessAdapter chessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brettspiele);
        scroll_console = (ScrollView) findViewById(R.id.scroll_console);
        text_console = (TextView) findViewById(R.id.text_console);
        input_console = (EditText) findViewById(R.id.input_console);

        chessAdapter = new ChessAdapter(this);
    }

    public void addOutput(String str) {
        if (!isOutputEmtpy())
            str = "\n" + str;
        text_console.append(str);
        scrollToBottom();
    }

    public void addOutputln(String str) {
        addOutput(str + "\n");
    }

    public void clearOutput() {
        text_console.setText("");
    }


    private void processInput(View view) {
        String input = input_console.getText().toString();

        clearInput();
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        chessAdapter.processInput(input);
    }

    private void clearInput() {
        input_console.getText().clear();
    }

    private boolean isOutputEmtpy() {
        return TextUtils.isEmpty(text_console.getText());
    }

    private void scrollToBottom() {
        scroll_console.post(new Runnable() {
            @Override
            public void run() {
                scroll_console.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
