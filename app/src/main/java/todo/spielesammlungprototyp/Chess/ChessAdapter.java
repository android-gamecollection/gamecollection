package todo.spielesammlungprototyp.Chess;

import android.os.AsyncTask;
import android.text.TextUtils;

import todo.spielesammlungprototyp.Brettspiele;

public class ChessAdapter {

    private Brettspiele brettspielInstance;

    public ChessAdapter(Brettspiele brettspielInstance) {
        this.brettspielInstance = brettspielInstance;
    }

    private class ProcessInputTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String str = params[0];
            String[] cmd = splitString(str);

            // dummy return just for testing
            return TextUtils.join(" ", cmd);
        }

        @Override
        protected void onPostExecute(String str) {
            addOutput(str);
        }

        private String[] splitString(String str) {
            return str.split("\\s+");
        }
    }

    public void processInput(String input) {
        new ProcessInputTask().execute(input);
    }

    private void addOutput(String str) {
        brettspielInstance.addOutputln(str);
    }
}
