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

import java.util.ArrayList;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.consolechess.CmdProcessor;
import todo.spielesammlungprototyp.tools.SavegameStorage;
import todo.spielesammlungprototyp.tools.Savegame;

public class ConsoleChess extends GameActivity {

    private ScrollView scrollConsole;
    private TextView textConsole;
    private EditText inputConsole;
    private CmdProcessor cmdProcessor;
    SavegameStorage savegameStorage;
    Savegame currentSGO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(GameActivity.KEY_LAYOUT, R.layout.activity_console_chess);
        super.onCreate(savedInstanceState);

        scrollConsole = (ScrollView) findViewById(R.id.scroll_output);
        textConsole = (TextView) findViewById(R.id.text_output);
        inputConsole = (EditText) findViewById(R.id.edittext_input);
        setKeyboardListener();

        savegameStorage = new SavegameStorage(this);

        ArrayList<Savegame> listOfGames = savegameStorage.getSavegameList();

        if(!listOfGames.isEmpty()) {
            int last = listOfGames.size()-1;
            currentSGO = listOfGames.get(last);
            cmdProcessor = new CmdProcessor(this, currentSGO.getValue());

        } else {
            cmdProcessor = new CmdProcessor(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(currentSGO == null) {
            savegameStorage.addSavegame(this, cmdProcessor.getFen());
        } else {
            currentSGO.setValue(cmdProcessor.getFen());
            savegameStorage.updateSavegame(this, currentSGO);
        }
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
