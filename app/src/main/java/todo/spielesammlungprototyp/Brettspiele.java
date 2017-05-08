package todo.spielesammlungprototyp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import todo.spielesammlungprototyp.Chess.ChessAdapter;

public class Brettspiele extends AppCompatActivity {

    private ScrollView scroll_console;
    private TextView text_console;
    private EditText input_console;
    private Toolbar toolbar;
    private ChessAdapter chessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brettspiele);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scroll_console = (ScrollView) findViewById(R.id.scroll_console);
        text_console = (TextView) findViewById(R.id.text_console);
        input_console = (EditText) findViewById(R.id.input_console);
        setKeyboardListener();

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

    public void processInput(View view) {
        String input = input_console.getText().toString();

        clearInput();
        chessAdapter.processInput(input);
    }

    public void displayError(String errMessage) {
        errMessage = "Error: " + errMessage;
        Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
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

    private void setKeyboardListener() {
        input_console.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return processOnEnter(actionId == EditorInfo.IME_ACTION_GO);
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return processOnEnter(keyCode == KeyEvent.KEYCODE_ENTER);
    }

    private boolean processOnEnter(boolean condition) {
        if (condition) {
            processInput(getCurrentFocus());
            return true;
        } else {
            return false;
        }
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
