package todo.spielesammlungprototyp.model.util;

import android.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public final class TextUtils {

    private TextUtils() {
    }

    /**
     * Allows hyperlinks in an alertDialog to be clicked
     * Important: Call this method AFTER showing the dialog
     *
     * @param alertDialog The dialog
     */
    public static void setClickableLinks(AlertDialog alertDialog) {
        TextView message = (TextView) alertDialog.findViewById(android.R.id.message);
        setClickableLinks(message);
    }

    public static void setClickableLinks(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
