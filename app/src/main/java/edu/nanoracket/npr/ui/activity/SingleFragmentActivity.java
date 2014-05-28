package edu.nanoracket.npr.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import edu.nanoracket.npr.R;

public abstract class SingleFragmentActivity extends ActionBarActivity {

@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
        fragment = createFragment();
        manager.beginTransaction()
        .add(R.id.fragmentContainer, fragment)
        .commit();
        }
        }

        protected abstract Fragment createFragment();
}
