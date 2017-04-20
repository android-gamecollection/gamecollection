package todo.spielesammlungprototyp.Chess;

import android.os.AsyncTask;
import android.text.TextUtils;

import chesspresso.move.IllegalMoveException;
import todo.spielesammlungprototyp.Brettspiele;
import todo.spielesammlungprototyp.R;

public class ChessAdapter {

    private Brettspiele brettspielInstance;
    private ChessBoard chessBoard;

    public ChessAdapter(Brettspiele brettspielInstance) {
        this.brettspielInstance = brettspielInstance;
        this.chessBoard = new ChessBoard();
        chessBoard.startPosition();
    }

    private class ProcessInputTask extends AsyncTask<String, Integer, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String str = params[0];
            return processCommand(str);
        }

        @Override
        protected void onPostExecute(String[] str) {
            if (!TextUtils.isEmpty(str[0]))
                if (str[0].equals("\\clear")) {
                    brettspielInstance.clearOutput();
                } else {
                    addOutput(str[0]);
                }
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
            case "clear":
                output = "\\clear";
                break;
            case "restart":
                chessBoard.startPosition();
                break;
            case "show":
                output = chessBoard.getBoard();
                break;
            case "ov":
            case "overview":
                output = chessBoard.getOverview();
                break;
            case "mv":
            case "move":
                try {
                    chessBoard.move(cmd[1], cmd[2]);
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
