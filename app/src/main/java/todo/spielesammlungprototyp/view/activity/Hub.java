package todo.spielesammlungprototyp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.io.Serializable;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.view.fragment.GameSelection;
import todo.spielesammlungprototyp.view.fragment.InfoFragment;
import todo.spielesammlungprototyp.view.fragment.SettingsFragment;

public class Hub extends AppCompatActivity {

    private final char tagHub = '0', tagCards = '1', tagBoard = '2', tagInfo = '3', tagSettings = '4';
    // index to identify current nav menu item
    public char currentTag = tagHub;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDefaultValuesOfSettingsFirstTime();

        setContentView(R.layout.activity_hub);
        setupActionBar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setUpNavigationView();

        if (savedInstanceState == null) {
            currentTag = tagHub;
            loadHomeFragment();
        } else {
            setToolbarTitle();
        }
    }

    private void setupActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setDefaultValuesOfSettingsFirstTime() {
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    public void switchFragment(char tag) {
        currentTag = tag;
        loadHomeFragment();
    }

    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(Character.toString(currentTag)) != null) {
            drawer.closeDrawers();
            return;
        }

        // add to the message queue
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, Character.toString(currentTag));
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        drawer.closeDrawers();
    }

    private Fragment getHomeFragment() {
        switch (currentTag) {
            case tagHub:
                return new todo.spielesammlungprototyp.view.fragment.Hub();
            case tagCards:
                return GameSelection.newInstance("cardgames");
            case tagBoard:
                return GameSelection.newInstance("boardgames");
            default:
                return new todo.spielesammlungprototyp.view.fragment.Hub();
        }
    }

    private void setToolbarTitle() {
        setTitle(navigationView.getMenu().getItem(toIndex(currentTag)).getTitle());
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(toIndex(currentTag)).setChecked(true);
    }

    private int toIndex(char currentTag) {
        return currentTag - '0';
    }

    public void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                char numericShortcut = menuItem.getNumericShortcut();
                switch (numericShortcut) {
                    case tagHub:
                    case tagCards:
                    case tagBoard:
                        currentTag = numericShortcut;
                        break;
                    case tagInfo:
                        openPreferenceScreen(getString(R.string.nav_item_info), new InfoFragment());
                        break;
                    case tagSettings:
                        openPreferenceScreen(getString(R.string.nav_item_settings), new SettingsFragment());
                        break;
                    default:
                        currentTag = tagHub;
                }

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void openPreferenceScreen(String toolbarTitle, Serializable fragment) {
        Intent intent = new Intent(Hub.this, SettingsActivity.class);
        intent.putExtra(SettingsActivity.KEY_TITLE, toolbarTitle);
        intent.putExtra(SettingsActivity.KEY_PREFERENCE_ITEMS, fragment);
        startActivity(intent);
        drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else if (currentTag != tagHub) {
            currentTag = tagHub;
            loadHomeFragment();
        }
    }
}
