package edu.nanoracket.npr.home;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.news.StoryLab;
import edu.nanoracket.npr.service.NewsUpdateService;
import edu.nanoracket.npr.ui.fragment.AboutFragment;
import edu.nanoracket.npr.ui.fragment.ArticlesListFragment;
import edu.nanoracket.npr.ui.fragment.NetworkDialogFragment;
import edu.nanoracket.npr.ui.fragment.NewsTopicsFragment;
import edu.nanoracket.npr.ui.fragment.NewscastFragment;
import edu.nanoracket.npr.ui.fragment.PrefsFragment;
import edu.nanoracket.npr.ui.fragment.ProgramListFragment;
import edu.nanoracket.npr.ui.navdrawer.AbstractNavDrawerActivity;
import edu.nanoracket.npr.ui.navdrawer.NavDrawerActivityConfiguration;
import edu.nanoracket.npr.ui.navdrawer.NavDrawerAdapter;
import edu.nanoracket.npr.ui.navdrawer.NavMenuItem;

public class YourAppMainActivity extends AbstractNavDrawerActivity {

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String TAG = "YourAppMainActivity" ;

    private StoryLab storyLab;
    private String newsTopicId, newsTopic;
    private int proListFragmentID;
    private String networkPref;
    private boolean alarmPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isNetworkConnected()){
            showNetworkAlert();
        }
        storyLab = StoryLab.getInstance(this);
        if ( savedInstanceState == null ) {
            newsTopicId = getIntent().getStringExtra(NewsListFragment.NEWS_TOPIC_ID);
            newsTopic = getIntent().getStringExtra(NewsListFragment.NEWS_TOPIC);
            proListFragmentID = getIntent().getIntExtra(ProgramListFragment.ID, -1);
            if(newsTopicId != null){
                storyLab.clearStories();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame,
                                    NewsListFragment.newInstance(newsTopicId, newsTopic))
                        .commit();
            }else if(proListFragmentID == 888){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ProgramListFragment())
                        .commit();
            }else{
                getSupportFragmentManager().beginTransaction()
                         .replace(R.id.content_frame,
                                    NewsListFragment.newInstance("1001", "Stories"))
                         .commit();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showNetworkAlert(){
        DialogFragment networkAlertFragment =
                NetworkDialogFragment.newInstance(R.string.network_alert_title,
                        R.string.network_alert_message, Settings.ACTION_SETTINGS);
        networkAlertFragment.show(getFragmentManager(),"dialog");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(setServiceStatus()){
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(this);
                String updateFrequency = sharedPreferences.getString("updatePref", "60");
                NewsUpdateService.setServiceAlarm(this, Integer.valueOf(updateFrequency));
                Log.i(TAG, "NewsUpdateService is called.");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        NavMenuItem[] menu = new NavMenuItem[] {
                NavMenuItem.create(101, "Top Stories", "navdrawer_news", true, true, this),
                NavMenuItem.create(102, "Hourly Newscast", "navdrawer_podcast", true, true, this),
                NavMenuItem.create(103, "News Topics", "navdrawer_topics", true, true, this),
                NavMenuItem.create(104, "Radio Programs", "navdrawer_program", true, true, this),
                NavMenuItem.create(105, "Saved Articles", "navdrawer_articles", true, true, this),
                NavMenuItem.create(106, "Settings", "navdrawer_settings", true, true,this),
                NavMenuItem.create(107, "About NPR", "navdrawer_about", true, true, this),
         };

        NavDrawerActivityConfiguration navDrawerActivityConfiguration =
                                                             new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration
                .setDrawerShadow(R.drawable.drawer_shadow);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration
                .setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(new NavDrawerAdapter(
                this, R.layout.navdrawer_list_item, menu));
        navDrawerActivityConfiguration.setDrawerIcon(R.drawable.ic_drawer);
        return navDrawerActivityConfiguration;
    }

    @Override
    protected void onNavItemSelected(int id) {
        switch (id) {
            case 101:
                storyLab.clearStories();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame,
                                NewsListFragment.newInstance("1001", "Stories"))
                        .commit();
                break;
            case 102:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new NewscastFragment())
                        .commit();
                break;
            case 103:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new NewsTopicsFragment())
                        .commit();
                break;
            case 104:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ProgramListFragment())
                        .commit();
                break;
            case 105:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ArticlesListFragment())
                        .commit();
                break;
            case 106:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new PrefsFragment())
                        .commit();
                break;
            case 107:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AboutFragment())
                        .commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean isNetworkConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public boolean setServiceStatus(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        alarmPref = sharedPreferences.getBoolean("auto_update", false);
        networkPref = sharedPreferences.getString("networkPref", ANY);

        ConnectivityManager connectivityManager = (ConnectivityManager)this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean wifiConnected = false;
        boolean mobileConnected = false;
        if(networkInfo == null) return false;
        if(networkPref != null && networkInfo.isConnected()){
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
        boolean isStartService = (networkPref.equals(ANY) && (wifiConnected || mobileConnected))
                || (networkPref.equals(WIFI) && wifiConnected);
        return isStartService;
    }
}
