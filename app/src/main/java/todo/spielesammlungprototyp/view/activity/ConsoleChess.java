package todo.spielesammlungprototyp.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.consolechess.CmdProcessor;

public class ConsoleChess extends GameActivity {

    private ScrollView scrollConsole;
    private TextView textConsole;
    private EditText inputConsole;
    private CmdProcessor cmdProcessor;
    private String startValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scrollConsole = (ScrollView) findViewById(R.id.scroll_output);
        textConsole = (TextView) findViewById(R.id.text_output);
        inputConsole = (EditText) findViewById(R.id.edittext_input);
        setKeyboardListener();
    }

    @Override
    protected void onLoadGame() {
        // is this Activity started with a Savegame?
        if (currentSaveGame == null) {
            cmdProcessor = new CmdProcessor(this);
            startValue = cmdProcessor.getFen();
        } else {
            cmdProcessor = new CmdProcessor(this, currentSaveGame.value);
        }
    }

    @Override
    protected String onSaveGame() {
        // put String value in ( saveGame(String value) ) for ConsoleChess is no serialization needed
        String toSave = cmdProcessor.getFen();
        // is it a unchanged new game?
        if (startValue.equals(toSave)) {
            toSave = null;
        }
        return toSave;
    }

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_console_chess;
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
