package todo.gamecollection.model.games.consolechess;

import android.os.AsyncTask;
import android.text.TextUtils;

import todo.gamecollection.R;
import todo.gamecollection.model.games.chess.ChessWrapper;
import todo.gamecollection.view.activity.ConsoleChess;

public class CmdProcessor {

    private final ConsoleChess consoleChessActivity;
    private final ChessWrapper wrapper;

    public CmdProcessor(ConsoleChess consoleChessActivity) {
        this(consoleChessActivity, null);
    }

    public CmdProcessor(ConsoleChess consoleChessActivity, String savegame) {
        this.consoleChessActivity = consoleChessActivity;
        this.wrapper = new ChessWrapper();
        if (savegame == null) {
            wrapper.setStartPosition();
        } else {
            wrapper.setPosition(savegame);
        }
        processInput("ov");
    }

    public String getFen() {
        return wrapper.getBoard();
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
                wrapper.setStartPosition();
                break;
            case "show":
                response.output = wrapper.getBoard();
                break;
            case "ov":
            case "overview":
                response.output = wrapper.getOverview();
                break;
            case "mv":
            case "move":
                if (cmd.length < 3) {
                    response.errorMessage = getString(R.string.game_consolechess_err_invalid_cmd);
                } else {
                    boolean validMove = wrapper.move(cmd[1],cmd[2]);
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
                wrapper.aimove();
                break;
            case "hint":
                String zug = wrapper.getBestMove();
                response.output = "Ein möglicher Zug wäre: " + zug;
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

    private class ProcessInputTask extends AsyncTask<String, Void, ConsoleResponse> {

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
                consoleChessActivity.addOutput(response.output);
        }
    }

    private class ConsoleResponse {
        String output;
        String errorMessage;
        String escapeSequence;
    }
}
