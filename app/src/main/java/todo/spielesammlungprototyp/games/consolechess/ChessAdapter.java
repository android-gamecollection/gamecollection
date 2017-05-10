package todo.spielesammlungprototyp.games.consolechess;

import android.os.AsyncTask;
import android.text.TextUtils;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.activity.Brettspiele;

public class ChessAdapter {

    private final Brettspiele brettspielInstance;
    private final ChessBoard chessBoard;

    public ChessAdapter(Brettspiele brettspielInstance) {
        this.brettspielInstance = brettspielInstance;
        this.chessBoard = new ChessBoard();
        chessBoard.setStartPosition();
    }

    public void processInput(String input) {
        new ProcessInputTask().execute(input);
    }

    private ConsoleResponse processCommand(String str) {
        String[] cmd = splitString(str);

        ConsoleResponse response = new ConsoleResponse();

        switch (cmd[0].toLowerCase()) {
            case "clear":
                response.escapeSequence = "clear";
                break;
            case "restart":
                chessBoard.setStartPosition();
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
                if (cmd.length < 3) {
                    response.errorMessage = getString(R.string.err_invalid_cmd);
                } else {
                    boolean validMove = chessBoard.move(cmd[1], cmd[2]);
                    if (!validMove) {
                        response.errorMessage = getString(R.string.err_invalid_move);
                    }
                }
                break;
            case "help":
                response.output = getString(R.string.cmd_help);
                break;
            case "ai":
            case "aimove":
                chessBoard.aimove();
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

    private class ProcessInputTask extends AsyncTask<String, Integer, ConsoleResponse> {
        @Override
        protected ConsoleResponse doInBackground(String... params) {
            String str = params[0];
            return processCommand(str);
        }

        @Override
        protected void onPostExecute(ConsoleResponse response) {
            if (!TextUtils.isEmpty(response.escapeSequence)) {
                switch (response.escapeSequence) {
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
}