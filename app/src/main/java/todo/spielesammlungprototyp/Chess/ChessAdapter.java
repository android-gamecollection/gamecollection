package todo.spielesammlungprototyp.Chess;

import android.os.AsyncTask;
import android.text.TextUtils;

import todo.spielesammlungprototyp.Brettspiele;
import todo.spielesammlungprototyp.R;

public class ChessAdapter {

    private Brettspiele brettspielInstance;
    ChessWrapper wrapper;

    public ChessAdapter(Brettspiele brettspielInstance) {
        this.brettspielInstance = brettspielInstance;
        this.wrapper = new ChessWrapper();
        wrapper.restart();
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
                wrapper.restart();
                break;
            case "show":
                response.output = wrapper.getFen();
                break;
            case "ov":
            case "overview":
                response.output = wrapper.getOverview();
                break;
            case "mv":
            case "move":
                if (cmd.length < 3) {
                    response.errorMessage = getString(R.string.err_invalid_cmd);
                } else {
                     boolean validMove = wrapper.doMove(cmd[1]+" "+cmd[2]);
                    if (!validMove){
                        response.errorMessage = getString(R.string.err_invalid_move);
                    }
                }
                break;
            case "help":
                response.output = getString(R.string.cmd_help);
                break;
            case "ai":
            case "aimove":
                String move = wrapper.getBestMove();
                wrapper.doMove(move);
                break;
            case "hint":
                String zug = wrapper.getBestMove();
                response.output = "Ein möglicher Zug wäre: " + zug;
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
