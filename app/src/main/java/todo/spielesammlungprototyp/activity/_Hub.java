package todo.spielesammlungprototyp.activity;

import android.os.Bundle;
import android.os.Handler;
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
import todo.spielesammlungprototyp.fragment.Brettspiele_AuswahlFragment;
import todo.spielesammlungprototyp.fragment.HubFragment;
import todo.spielesammlungprototyp.fragment.Kartenspiele_AuswahlFragment;

public class _Hub extends AppCompatActivity {

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    // tags used to attach the fragments
    private static final String TAG_HUB = "hub";
    private static final String TAG_KARTENSPIELE = "kartenspiele_auswahl";
    private static final String TAG_BRETTSPIELE = "brettspiele_auswahl";
    public static String CURRENT_TAG = TAG_HUB;
    // index to identify current nav menu item
    private static int navItemIndex = 0;
    // flag to load home fragment when user presses back key
    private final boolean shouldLoadHomeFragOnBackPress = true;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;
    private FloatingActionButton fab_new_game, fab_new_game_k, fab_new_game_b;
    private ImageView navDrawerBackground, navDrawerIcon;
    private TextView navDrawerTitle, navDrawerSubTitle;
    private Animation fabOpen, fabClose, fabRotateClockwise, fabRotateAnticlockwise;
    private boolean isFabOpen = false;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private Handler mHandler;

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab_new_game = (FloatingActionButton) findViewById(R.id.fab_new_game);
        fab_new_game_k = (FloatingActionButton) findViewById(R.id.fab_new_game_kartenspiele);
        fab_new_game_b = (FloatingActionButton) findViewById(R.id.fab_new_game_brettspiele);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabRotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clockwise);
        fabRotateAnticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlockwise);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        navDrawerTitle = (TextView) navHeader.findViewById(R.id.nav_drawer_title);
        navDrawerSubTitle = (TextView) navHeader.findViewById(R.id.nav_drawer_subtext);
        navDrawerBackground = (ImageView) navHeader.findViewById(R.id.nav_drawer_top_background);
        navDrawerIcon = (ImageView) navHeader.findViewById(R.id.nav_drawer_top_icon);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab_new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFabOpen) {
                    fab_new_game_b.startAnimation(fabClose);
                    fab_new_game_k.startAnimation(fabClose);
                    fab_new_game.startAnimation(fabRotateAnticlockwise);
                    fab_new_game_b.setClickable(false);
                    fab_new_game_k.setClickable(false);
                    isFabOpen = false;
                } else {
                    fab_new_game_b.startAnimation(fabOpen);
                    fab_new_game_k.startAnimation(fabOpen);
                    fab_new_game.startAnimation(fabRotateClockwise);
                    fab_new_game_b.setClickable(true);
                    fab_new_game_k.setClickable(true);
                    isFabOpen = true;
                }

            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HUB;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */

    private void loadNavHeader() {

        navDrawerTitle.setText(R.string.app_name);
        navDrawerSubTitle.setText(R.string.subTextHeader);
        /*
        // loading header background image
        Glide.with(this).load("http://i.imgur.com/DvpvklR.png")
                .into(navDrawerBackground);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .into(navDrawerIcon);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);
        */
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
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab_new_game button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        mHandler.post(mPendingRunnable);

        // show or hide the fab_new_game button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                return new HubFragment();
            case 1:
                // Kartenspiele
                return new Kartenspiele_AuswahlFragment();
            case 2:
                // Brettspiele
                return new Brettspiele_AuswahlFragment();
            default:
                return new HubFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    public void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_hub:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HUB;
                        //drawer.closeDrawers();
                        break;
                    case R.id.nav_kartenspiele:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_KARTENSPIELE;
                        break;
                    case R.id.nav_brettspiele:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_BRETTSPIELE;
                        break;
                    case R.id.nav_settings:
                        // launch new intent instead of loading fragment
                        //startActivity(new Intent(_Hub.this, ));
                        //drawer.closeDrawers();
                        return true;
                    case R.id.nav_information:
                        // launch new intent instead of loading fragment
                        //startActivity(new Intent(_Hub.this, ));
                        //drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
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

    // es existiert auch "onStart()", "onResume()", "onStop()" und weitere
    // zu sehen im Activity Lifecycle: "https://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle"
    // "onStart()" wird nach "onCreate()" oder "onRestart()" gerufen

    // Hier ein Beispiel für onStart()
    // Es wird ein "Toast" ausgegeben sobald der User die App offen hat
    // Wenn er sie pausiert und wieder öffnet auch

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
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HUB;
                loadHomeFragment();
            }
        }

        //super.onBackPressed(); //would close the app
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 1) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    } */

    // show or hide the fab_new_game
    private void toggleFab() {
        if (navItemIndex == 0)
            fab_new_game.show();
        else
            fab_new_game.hide();
    }
}