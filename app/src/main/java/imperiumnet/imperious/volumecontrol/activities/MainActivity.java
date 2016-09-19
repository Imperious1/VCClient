package imperiumnet.imperious.volumecontrol.activities;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

import imperiumnet.imperious.volumecontrol.Client.VCClient;
import imperiumnet.imperious.volumecontrol.R;
import imperiumnet.imperious.volumecontrol.callbacks.OnBtnPressedListener;
import imperiumnet.imperious.volumecontrol.callbacks.OnDisconnectListener;
import imperiumnet.imperious.volumecontrol.callbacks.OnTaskCompletedListener;
import imperiumnet.imperious.volumecontrol.fragments.ConnectDialog;
import imperiumnet.imperious.volumecontrol.fragments.SettingsFragment;
import imperiumnet.imperious.volumecontrol.fragments.VolumeFragment;

public class MainActivity extends AppCompatActivity implements OnTaskCompletedListener, OnDisconnectListener, OnBtnPressedListener, NavigationView.OnNavigationItemSelectedListener {

    VCClient client = null;
    private SharedPreferences preferences;
    private BroadcastReceiver br;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    public static boolean hasConnectedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        pushFragment(new VolumeFragment(), "volume");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (preferences.getBoolean("auto_connect", false) && !hasConnectedOnce) {
            try {
                client = new VCClient(Integer.parseInt(preferences.getString("default_port", "4044")), this);
                client.keepAlive();
                hasConnectedOnce = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRaisePress(View v) {
        if (client != null)
            client.raiseVolume(Integer.parseInt(preferences.getString("increment", "0")));
    }

    public void onLowerPress(View v) {
        if (client != null)
            client.lowerVolume(Integer.parseInt(preferences.getString("increment", "0")));
    }

    public void onMutePress(View v) {
        if (client != null)
            client.mute();
    }

    //Created by Cj Smith
    public void pushFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        clearFragment();
        if (fm.getFragments() != null) {
            for (Fragment f : fm.getFragments())
                if (f != null) ft.hide(f);
            if (fm.findFragmentByTag(tag) != null)
                ft.show(fm.findFragmentByTag(tag));
            else ft.add(R.id.coordinator, fragment, tag);
        } else ft.add(R.id.coordinator, fragment, tag);
        ft.commit();
    }

    //Created by Cj Smith
    private void clearFragment() {
        android.app.FragmentManager fm = getFragmentManager();
        while (fm.popBackStackImmediate()) ;
    }

    //Created by Cj Smith
    public void pushFragment(android.app.Fragment fragment, String tag) {
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        clearSupport();
        if (fm.findFragmentByTag(tag) != null) {
            ft.show(fm.findFragmentByTag(tag));
        } else {
            ft.addToBackStack(tag).add(R.id.coordinator, fragment, tag).commit();
        }
    }

    //Created by Cj Smith
    private void clearSupport() {
        if (getSupportFragmentManager().getFragments() != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : getSupportFragmentManager().getFragments())
                if (f != null) ft.hide(f);
            ft.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_connect) {
            if (client == null || !client.isConnected() || client.isClosed()) {
                ConnectDialog connect = new ConnectDialog();
                connect.show(getSupportFragmentManager(), "connect");
            } else {
                Snackbar.make(findViewById(R.id.coordinator), "You are already connected or attempting connection", Snackbar.LENGTH_LONG).show();
            }
        } else if (id == R.id.action_disconnect) {
            try {
                if (client != null) {
                    client.disconnectByUserRequest();
                    client = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted() {
        disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("ondestroy called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (client != null)
                client.disconnectByUserRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("onstop called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onresume called");
    }

    @Override
    public void disconnect() {
        try {
            if (client != null) client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });
    }

    @Override
    public void onBtnPressed(VCClient client) {
        this.client = client;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_volume) {
            pushFragment(new VolumeFragment(), "volume");
            mToolbar.setSubtitle(null);
        } else if (item.getItemId() == R.id.nav_settings) {
            pushFragment(new SettingsFragment(), "settings");
            mToolbar.setSubtitle("Settings");
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}