package todo.spielesammlungprototyp.model.games.consolechess;

import android.os.AsyncTask;
import android.text.TextUtils;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.view.activity.ConsoleChess;

public class CmdProcessor {

    private final ConsoleChess consoleChessActivity;
    private final ChessBoard chessBoard;

    public CmdProcessor(ConsoleChess consoleChessActivity) {
        this.consoleChessActivity = consoleChessActivity;
        this.chessBoard = new ChessBoard();
        chessBoard.setStartPosition();
        processInput("ov");
    }

    public CmdProcessor(ConsoleChess consoleChessActivity, String savegame) {
        this.consoleChessActivity = consoleChessActivity;
        this.chessBoard = new ChessBoard();
        chessBoard.setStartPosition(savegame);
        processInput("ov");
    }

    public String getFen() {
        return chessBoard.getBoard();
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
                    response.errorMessage = getString(R.string.game_consolechess_err_invalid_cmd);
                } else {
                    boolean validMove = chessBoard.move(cmd[1], cmd[2]);
                    if (!validMove) {
                        response.errorMessage = getString(R.string.game_consolechess_err_invalid_move);
                    }
                }
                break;
            case "help":
                response.output = getString(R.string.game_consolechess_cmd_help);
                break;
            case "ai":
            case "aimove":
                chessBoard.aimove();
                break;
            default:
                response.errorMessage = getString(R.string.game_consolechess_err_invalid_cmd);
        }

        return response;
    }

    private String getString(int xmlString) {
        return consoleChessActivity.getString(xmlString);
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
            if (!TextUtils.isEmpty(response.escapeSequence))
                switch (response.escapeSequence) {
                    case "clear":
                        consoleChessActivity.clearOutput();
                        break;
                }
            if (!TextUtils.isEmpty(response.errorMessage))
                consoleChessActivity.displayError(response.errorMessage);
            if (!TextUtils.isEmpty(response.output))
                consoleChessActivity.addOutputln(response.output);
        }
    }

    private class ConsoleResponse {
        String output;
        String errorMessage;
        String escapeSequence;
    }
}
