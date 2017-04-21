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

    private class ProcessInputTask extends AsyncTask<String, Integer, ConsoleResponse> {
        @Override
        protected ConsoleResponse doInBackground(String... params) {
            String str = params[0];
            return processCommand(str);
        }

        @Override
        protected void onPostExecute(ConsoleResponse response) {
            if (!TextUtils.isEmpty(response.escapeSequence)) {
                switch(response.escapeSequence) {
                    case "clear":
                        brettspielInstance.clearOutput();
                }
            } else {
                if (!TextUtils.isEmpty(response.output))
                    brettspielInstance.addOutputln(response.output);
                if (!TextUtils.isEmpty(response.errorMessage))
                    brettspielInstance.displayError(response.errorMessage);
            }
        }
    }

    private class ConsoleResponse {
        String output;
        String errorMessage;
        String escapeSequence;
    }

    public void processInput(String input) {
        new ProcessInputTask().execute(input);
    }

    private ConsoleResponse processCommand(String str) {
        String[] cmd = splitString(str);

        ConsoleResponse response = new ConsoleResponse();

        switch(cmd[0].toLowerCase()) {
            case "clear":
                response.escapeSequence = "clear";
                break;
            case "restart":
                chessBoard.startPosition();
                break;
            case "show":
                response.output = chessBoard.getBoard();
                break;
            case "ov":
            case "overview":
                response.output = chessBoard.getOverview();
                break;
            case "mv":
            case "move":
                try {
                    chessBoard.move(cmd[1], cmd[2]);
                } catch (IllegalMoveException e) {
                    response.errorMessage = e.getClass().getName() + ": " + e.getMessage();
                    e.printStackTrace();
                }
                break;
            case "help":
                response.output = getString(R.string.cmd_help);
                break;
            default:
                response.errorMessage = getString(R.string.err_invalid_cmd);
        }

        return response;
    }

    private String getString(int xmlString) {
        return brettspielInstance.getString(xmlString);
    }

    private String[] splitString(String str) {
        return str.split("\\s+");
    }
}
