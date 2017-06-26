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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.view.fragment.GameSelection;

public class Hub extends AppCompatActivity {

    private final char tagHub = '0', tagCards = '1', tagBoard = '2', tagInfo = '3', tagSettings = '4';
    // index to identify current nav menu item
    public char currentTag = tagHub;
    //views
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;
    private TextView navDrawerTitle, navDrawerSubTitle;
    private ImageView navDrawerBackground, navDrawerIcon;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hub);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDefaultValuesOfSettingsFirstTime();

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        navDrawerTitle = (TextView) navHeader.findViewById(R.id.nav_drawer_title);
        navDrawerSubTitle = (TextView) navHeader.findViewById(R.id.nav_drawer_subtext);
        navDrawerBackground = (ImageView) navHeader.findViewById(R.id.nav_drawer_top_background);
        navDrawerIcon = (ImageView) navHeader.findViewById(R.id.nav_drawer_top_icon);

        loadNavHeader();
        setUpNavigationView();

        if (savedInstanceState == null) {
            currentTag = tagHub;
            loadHomeFragment();
        }
    }

    private void setDefaultValuesOfSettingsFirstTime() {
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    public void switchFragment(char tag) {
        currentTag = tag;
        loadHomeFragment();
    }

    private void loadNavHeader() {

        navDrawerTitle.setText(R.string.app_name);
        navDrawerSubTitle.setText(R.string.subTextHeader);
    }

    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(Character.toString(currentTag)) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, Character.toString(currentTag));
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
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
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(toIndex(currentTag)).getTitle());
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

                switch (menuItem.getNumericShortcut()) {
                    case tagHub:
                        currentTag = tagHub;
                        break;
                    case tagCards:
                        currentTag = tagCards;
                        break;
                    case tagBoard:
                        currentTag = tagBoard;
                        break;
                    case tagInfo:
                        return true;
                    case tagSettings:
                        startActivity(new Intent(Hub.this, Settings.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        currentTag = tagHub;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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
