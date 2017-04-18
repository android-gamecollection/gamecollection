package todo.spielesammlungprototyp.Chess;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

import chesspresso.move.IllegalMoveException;
import todo.spielesammlungprototyp.Brettspiele;
import todo.spielesammlungprototyp.R;

public class ChessAdapter {

    private Brettspiele brettspielInstance;
    private Chesstest chessTest;

    public ChessAdapter(Brettspiele brettspielInstance) {
        this.brettspielInstance = brettspielInstance;
        this.chessTest = new Chesstest();
        chessTest.startPosition();
    }

    private class ProcessInputTask extends AsyncTask<String, Integer, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String str = params[0];
            String[] output = processCommand(str);
            return output;
        }

        @Override
        protected void onPostExecute(String[] str) {
            if (!TextUtils.isEmpty(str[0]))
                addOutput(str[0]);
            if (!TextUtils.isEmpty(str[1]))
                brettspielInstance.displayError(str[1]);
        }

    }

    public void processInput(String input) {
        new ProcessInputTask().execute(input);
    }

    private void addOutput(String str) {
        brettspielInstance.addOutputln(str);
    }

    private String[] processCommand(String str) {
        String[] cmd = splitString(str);

        String output = "";
        String error = "";

        switch(cmd[0].toLowerCase()) {
            case "restart":
                chessTest.startPosition();
                output = processCommand("show")[0];
                break;
            case "show":
                output = chessTest.getBoard();
                break;
            case "move":
                try {
                    chessTest.move(cmd[1], cmd[2]);
                    output = processCommand("show")[0];
                } catch (IllegalMoveException e) {
                    error = e.getClass().getName() + ": " + e.getMessage();
                    e.printStackTrace();
                }
                break;
            case "help":
                output = getString(R.string.cmd_help);
                break;
            default:
                error = getString(R.string.err_invalid_cmd);
        }

        return new String[] {output, error};
    }

    private String getString(int xmlString) {
        return brettspielInstance.getString(xmlString);
    }

    private String[] splitString(String str) {
        return str.split("\\s+");
    }
}
