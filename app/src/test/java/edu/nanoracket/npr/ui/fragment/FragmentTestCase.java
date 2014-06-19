package edu.nanoracket.npr.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import org.junit.After;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

public class FragmentTestCase<T extends Fragment> {

    private static final String FRAGMENT_TAG = "fragment";

    private ActivityController controller;
    private FragmentActivity activity;
    private T fragment;

    public void startFragment(T fragment){
        this.fragment = fragment;
        controller = Robolectric.buildActivity(FragmentActivity.class);
        activity = (FragmentActivity) controller.create().start().visible().get();
        FragmentManager manager = activity.getSupportFragmentManager();
        manager.beginTransaction()
                .add(fragment, FRAGMENT_TAG).commit();
    }

    @After
    public void destroyFragment(){
        if(fragment != null){
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction().remove(fragment).commit();
            fragment = null;
            activity = null;
        }
    }

    public void pauseAndPauseFragment(){
        controller.pause().resume();
    }

    public T recreateFragment(){
        activity.recreate();
        fragment = (T)activity.getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_TAG);
        return fragment;
    }
}
