package todo.spielesammlungprototyp.view.activity;

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

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.consolechess.CmdProcessor;

public class ConsoleChess extends AppCompatActivity {

    private ScrollView scrollConsole;
    private TextView textConsole;
    private EditText inputConsole;
    private Toolbar toolbar;
    private CmdProcessor cmdProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console_chess);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scrollConsole = (ScrollView) findViewById(R.id.scroll_console);
        textConsole = (TextView) findViewById(R.id.text_console);
        inputConsole = (EditText) findViewById(R.id.input_console);
        setKeyboardListener();

        cmdProcessor = new CmdProcessor(this);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return processOnEnter(keyCode == KeyEvent.KEYCODE_ENTER);
    }

    public void addOutput(String str) {
        if (!isOutputEmtpy())
            str = "\n" + str;
        textConsole.append(str);
        scrollToBottom();
    }

    public void addOutputln(String str) {
        addOutput(str + "\n");
    }

    public void clearOutput() {
        textConsole.setText("");
    }

    public void processInput(View view) {
        String input = inputConsole.getText().toString();

        clearInput();
        cmdProcessor.processInput(input);
    }

    public void displayError(String errMessage) {
        errMessage = "Error: " + errMessage;
        Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
    }

    private void clearInput() {
        inputConsole.getText().clear();
    }

    private boolean isOutputEmtpy() {
        return TextUtils.isEmpty(textConsole.getText());
    }

    private void scrollToBottom() {
        scrollConsole.post(new Runnable() {
            @Override
            public void run() {
                scrollConsole.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void setKeyboardListener() {
        inputConsole.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return processOnEnter(actionId == EditorInfo.IME_ACTION_GO);
            }
        });
    }

    private boolean processOnEnter(boolean condition) {
        if (condition) {
            processInput(getCurrentFocus());
            return true;
        } else {
            return false;
        }
    }
}
