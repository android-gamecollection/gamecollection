package todo.spielesammlungprototyp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class _Hub extends Activity {


    // "onCreate()" wird beim Start der Activity gerufen
    // hier sollten alle Initialisierungen implementiert werden
    // wie zum Beispiel: views, Daten zu Listen binden, etc.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // wenn die Activity nochmals gebaut werden muss ("onDestroy()" => "onCreate()")
        // wird der Status in einem Bundle gespeichert (bei "onDestroy()" wird er nochmals gespeichert)
        super.onCreate(savedInstanceState);
        // deklariert die UI (ruft das Layout auf):
        setContentView(R.layout._activity_hub);
    }

    // es existiert auch "onStart()", "onResume()", "onStop()" und weitere
    // zu sehen im Activity Lifecycle: "https://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle"
    // "onStart()" wird nach "onCreate()" oder "onRestart()" gerufen

    // Hier ein Beispiel für onStart()
    // Es wird ein "Toast" ausgegeben sobald der User die App offen hat
    // Wenn er sie pausiert und wieder öffnet auch
    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(this, "Das ist eine Toast Nachricht", // Hier sollte man eigentlich auch wieder den String referenzieren
//                Toast.LENGTH_SHORT).show(); // es gibt LENGTH_SHORT und LENGTH_LONG
                // "Toasts" sind schön für Error-Meldungen für den User
    }

    // Funktionen zum wechseln zur anderen Activities
    // wird mit "onClick" des jeweiligen Buttons aufgerufen
    // "onClick" wird von einem Feature aufgerufen, hier ein "Button"
    // (definiert in der zugehörigen Layout-Datei. Hier: "_activity_hub.xml")
    // =>
    public void gotoKartenspiele(View view) {
        Intent gotoActivity = new Intent(this, Kartenspiele.class);
        startActivity(gotoActivity);
    }

    public void gotoBrettspiele(View view) {
        Intent gotoActivity = new Intent(this, Brettspiele.class);
        startActivity(gotoActivity);
    }
    // <=
}
