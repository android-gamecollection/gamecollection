package todo.spielesammlungprototyp.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.view.fragment.GameSelection;

public class Hub extends AppCompatActivity {

    private final char tagHub = '0', tagCards = '1', tagBoard = '2', tagInfo = '3', tagSettings = '4';
    // flag to load home fragment when user presses back key
    private final boolean shouldLoadHomeFragOnBackPress = true;
    // index to identify current nav menu item
    public char currentTag = tagHub;
    //views
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;
    private FloatingActionButton fabNewGame, fabNewGameK, fabNewGameB;
    private TextView navDrawerTitle, navDrawerSubTitle;
    private ImageView navDrawerBackground, navDrawerIcon;

    // fab
    private Animation fabOpenUpper, fabCloseUpper, fabCloseLower, fabOpenLower, fabRotateClockwise, fabRotateAnticlockwise;
    private boolean fabOpen = false;
    private boolean fragHome = true;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hub);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // for settings_general, set default for first time
        setDefaultValuesOfSettings();

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fabNewGame = (FloatingActionButton) findViewById(R.id.fab_new_game);
        fabNewGameK = (FloatingActionButton) findViewById(R.id.fab_new_cardgame);
        fabNewGameB = (FloatingActionButton) findViewById(R.id.fab_new_boardgame);
        tintFabs();
        fabOpenUpper = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open_upper);
        fabCloseUpper = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close_upper);
        fabOpenLower = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open_lower);
        fabCloseLower = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close_lower);
        fabRotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clockwise);
        fabRotateAnticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlockwise);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        navDrawerTitle = (TextView) navHeader.findViewById(R.id.nav_drawer_title);
        navDrawerSubTitle = (TextView) navHeader.findViewById(R.id.nav_drawer_subtext);
        navDrawerBackground = (ImageView) navHeader.findViewById(R.id.nav_drawer_top_background);
        navDrawerIcon = (ImageView) navHeader.findViewById(R.id.nav_drawer_top_icon);

        fabNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab(fabOpen);
            }
        });

        fabNewGameB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTag = tagBoard;
                loadHomeFragment();
            }
        });

        fabNewGameK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTag = tagCards;
                loadHomeFragment();
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            currentTag = tagHub;
            loadHomeFragment();
        }
    }

    private void tintFabs() {
        String colorStr = PreferenceManager.getDefaultSharedPreferences(this).getString("settings_key_general_accent_color", "#795548");
        ColorStateList csl = ColorStateList.valueOf(Color.parseColor(colorStr));
        fabNewGame.setBackgroundTintList(csl);
        fabNewGameK.setBackgroundTintList(csl);
        fabNewGameB.setBackgroundTintList(csl);
    }

    private void setDefaultValuesOfSettings() {
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    private void animateFab(boolean isFabOpen) {
        if (isFabOpen) {
            fabNewGameB.startAnimation(fabCloseLower);
            fabNewGameK.startAnimation(fabCloseUpper);
            fabNewGame.startAnimation(fabRotateAnticlockwise);
            fabNewGameB.setClickable(false);
            fabNewGameK.setClickable(false);
            this.fabOpen = false;
        } else {
            fabNewGameB.startAnimation(fabOpenLower);
            fabNewGameK.startAnimation(fabOpenUpper);
            fabNewGame.startAnimation(fabRotateClockwise);
            fabNewGameB.setClickable(true);
            fabNewGameK.setClickable(true);
            this.fabOpen = true;
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     */

    private void loadNavHeader() {

        navDrawerTitle.setText(R.string.app_name);
        navDrawerSubTitle.setText(R.string.subTextHeader);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(Character.toString(currentTag)) != null) {
            drawer.closeDrawers();

            // show or hide the fab_new_game button
            toggleFab();
            return;
        }

        // Sometimes, when a fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
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

        // show or hide the fab_new_game button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (currentTag) {
            case tagHub:
                // Hub
                return new todo.spielesammlungprototyp.view.fragment.Hub();
            case tagCards:
                // Kartenspiele
                return GameSelection.newInstance("cardgames");
            case tagBoard:
                // Brettspiele
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
        return currentTag - 48;
    }

    public void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
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
                        // launch new intent instead of loading fragment
                        //startActivity(new Intent(Hub.this, ));
                        //drawer.closeDrawers();
                        return true;
                    case tagSettings:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(Hub.this, Settings.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        currentTag = tagHub;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
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
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (currentTag != tagHub) {
                currentTag = tagHub;
                loadHomeFragment();
            }
        }

        //super.onBackPressed(); //would close the app
    }

    // show or hide the fab_new_game
    private void toggleFab() {
        if (currentTag == tagHub) {
            fabNewGame.show();
            fabNewGame.setClickable(true);
            fragHome = true;
        } else {
            fabNewGame.hide();
            fabNewGame.setClickable(false);
            if (fragHome && fabOpen) animateFab(true);
            fragHome = false;
        }
    }
}
