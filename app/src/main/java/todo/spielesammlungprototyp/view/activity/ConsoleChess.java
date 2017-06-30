package todo.spielesammlungprototyp.view.activity;

import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.consolechess.CmdProcessor;

public class ConsoleChess extends GameActivity {

    private static final String KEY_FEN = "fen";
    private ColorFilter buttonColorEnabled;
    private ScrollView scrollConsole;
    private TextView textConsole;
    private EditText inputConsole;
    private ImageButton buttonConfirm;
    private CmdProcessor cmdProcessor;
    private CoordinatorLayout coordinatorLayout;
    private String startValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scrollConsole = (ScrollView) findViewById(R.id.scroll_output);
        textConsole = (TextView) findViewById(R.id.text_output);
        inputConsole = (EditText) findViewById(R.id.edittext_input);
        buttonConfirm = (ImageButton) findViewById(R.id.button_confirm);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.chess_coordinatorlayout);

        buttonColorEnabled = buttonConfirm.getColorFilter();
        enableButton(false);
        inputConsole.addTextChangedListener(new ConsoleInputWatcher());

        setKeyboardListener();
    }

    @Override
    protected void onLoadGame(@Nullable Bundle savegame) {
        final String fen = savegame == null ? null : savegame.getString(KEY_FEN);
        cmdProcessor = new CmdProcessor(this, fen);
        startValue = cmdProcessor.getFen();
    }

    @Override
    protected void onSaveGame(Bundle savegame) {
        final String fen = cmdProcessor.getFen();
        // Only save when something has changed
        if (!startValue.equals(fen)) {
            savegame.putString(KEY_FEN, fen);
        }
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
            str = "\n\n" + str;
        textConsole.append(str);
        scrollToBottom();
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
        Snackbar.make(coordinatorLayout, errMessage, Snackbar.LENGTH_SHORT).show();
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
        inputConsole.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed();
                    return true;
                }
                return false;
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

    private void enableButton(boolean enable) {
        buttonConfirm.setEnabled(enable);
        buttonConfirm.setClickable(enable);
        if (enable) {
            buttonConfirm.setColorFilter(buttonColorEnabled);
        } else {
            buttonConfirm.setColorFilter(android.R.color.black);
        }
        buttonConfirm.setAlpha(enable ? 1 : 0.2f);
    }

    private class ConsoleInputWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enableButton(s.length() > 0);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
