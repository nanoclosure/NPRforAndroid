package edu.nanoracket.npr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import edu.nanoracket.npr.R;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class NprFragmentActivityTest {
    private Context context;
    private Intent intent;
    private StubFragmentActivity fragmentActivity;

    @Before
    public void setUp(){
        context = Robolectric.application;
        intent = new Intent(context, NprFragmentActivity.class);
        intent.putExtra(StubFragmentActivity.StubFragment.ID, "hello");
        fragmentActivity = Robolectric.buildActivity(StubFragmentActivity.class)
                .withIntent(intent).create().start().resume().get();
    }

    @Test
    public void shouldCreateFragment(){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        Assert.assertNotNull(fragment);
        Assert.assertEquals("hello",
                fragmentActivity.getIntent().getStringExtra(StubFragmentActivity.StubFragment.ID));
    }
}
