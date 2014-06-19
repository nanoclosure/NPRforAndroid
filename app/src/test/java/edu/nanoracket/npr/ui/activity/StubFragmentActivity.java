package edu.nanoracket.npr.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class StubFragmentActivity extends NprFragmentActivity{
    private String id;

    @Override
    protected Fragment createFragment() {
        id = getIntent().getStringExtra(StubFragment.ID);
        return StubFragment.getInstance(id);
    }

    static class StubFragment extends Fragment{
        public static final String ID = "id";

        public static StubFragment getInstance(String id){
            Bundle args = new Bundle();
            args.putString(ID, id);
            StubFragment fragment = new StubFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        }
    }
}
