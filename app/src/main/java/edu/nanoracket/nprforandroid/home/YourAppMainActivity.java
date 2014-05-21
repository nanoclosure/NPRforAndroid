package edu.nanoracket.nprforandroid.home;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;

import edu.nanoracket.nprforandroid.R;
import edu.nanoracket.nprforandroid.news.StoryLab;
import edu.nanoracket.nprforandroid.ui.fragment.AboutFragment;
import edu.nanoracket.nprforandroid.ui.fragment.NetworkDialogFragment;
import edu.nanoracket.nprforandroid.ui.fragment.NewsTopicsFragment;
import edu.nanoracket.nprforandroid.ui.fragment.NewscastFragment;
import edu.nanoracket.nprforandroid.ui.fragment.PrefsFragment;
import edu.nanoracket.nprforandroid.ui.fragment.ProgramListFragment;
import edu.nanoracket.nprforandroid.ui.navdrawer.AbstractNavDrawerActivity;
import edu.nanoracket.nprforandroid.ui.navdrawer.NavDrawerActivityConfiguration;
import edu.nanoracket.nprforandroid.ui.navdrawer.NavDrawerAdapter;
import edu.nanoracket.nprforandroid.ui.navdrawer.NavMenuItem;


public class YourAppMainActivity extends AbstractNavDrawerActivity {

    //private String[] drawerLists;
    StoryLab storyLab;

    private String newsTopicId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isNetworkConnected()){
            showNetworkAlert();
        }
        storyLab = StoryLab.getInstance(this);
            if ( savedInstanceState == null ) {
                newsTopicId = getIntent().getStringExtra(NewsListFragment.NEWS_TOPIC_ID);
                if(newsTopicId != null){
                    storyLab.clearStories();

                    /*newsTopicId = getIntent()
                            .getStringExtra(NewsListFragment.NEWS_TOPIC_ID);*/
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame,
                                    NewsListFragment.newInstance(newsTopicId))
                            .commit();
                }else{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame,
                                    NewsListFragment.newInstance("1003"))
                            .commit();
                }

        }
    }

    public boolean isNetworkConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        } else {
            return false;
        }
    }

    public void showNetworkAlert(){
        DialogFragment networkAlertFragment =
                NetworkDialogFragment.newInstance(R.string.network_alert_title,
                        R.string.network_alert_message, Settings.ACTION_SETTINGS);
        networkAlertFragment.show(getFragmentManager(),"dialog");

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        //drawerLists = getResources().getStringArray(R.array.navdrawer_list);
        //Log.i("TAG", "Arraylist:" + drawerLists);
        NavMenuItem[] menu = new NavMenuItem[] {

                NavMenuItem.create(101, "Top Stories", "navdrawer_news", true, true, this),
                NavMenuItem.create(102, "Hourly Newscast", "navdrawer_podcast", true, true, this),
                NavMenuItem.create(103, "News Topics", "navdrawer_topics", true, true, this),
                NavMenuItem.create(104, "Radio Programs", "navdrawer_program", true, true, this),
                NavMenuItem.create(105, "Settings", "navdrawer_settings", true, true,this),
                NavMenuItem.create(106, "About NPR", "navdrawer_about", true, true, this),
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
    protected void onStop() {
       super.onStop();
    }

    @Override
    protected void onNavItemSelected(int id) {
        switch (id) {
            case 101:
                storyLab.clearStories();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame,
                                NewsListFragment.newInstance("1003"))
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
                        .replace(R.id.content_frame, new PrefsFragment())
                        .commit();
                break;

            case 106:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AboutFragment())
                        .commit();
                break;

            /* case 104:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new TutorialListFragment())
                        .commit();
                break;
            case 105:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AroundMeFragment())
                        .commit();
                break;
            case 201:
                this.navController.showSettings(this);
                break;
            case 202:
                this.navController.startAppRating(this);
                break;
            case 203:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new DonateFragment())
                        .commit();
                break;
            case 204:
                this.navController.showChangeLog(this);
                break;
            case 205:
                this.navController.showEula(this);
                break;
            case 206:
                this.navController.showExitDialog(this);
                break; */
        }

    }

}
