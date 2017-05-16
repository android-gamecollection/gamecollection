package todo.spielesammlungprototyp.view.activity;

import android.os.Bundle;

import todo.spielesammlungprototyp.R;

public class Bauernkrieg extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) savedInstanceState = new Bundle();
        savedInstanceState.putInt("layout", R.layout.activity_bauernkrieg);
        super.onCreate(savedInstanceState);
    }
}
