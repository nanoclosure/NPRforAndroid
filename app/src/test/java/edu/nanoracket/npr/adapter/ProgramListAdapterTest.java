package edu.nanoracket.npr.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.nanoracket.npr.R;
import edu.nanoracket.npr.model.Program;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class ProgramListAdapterTest {
    private Context context;
    private int[] imageItems;
    ArrayList<Program> programs;
    ProgramListAdapter programListAdapter;

    @Before
    public void setUp(){
        context = Robolectric.application;
        programs = new ArrayList<Program>();
        imageItems = new int[]{R.drawable.cartalk, R.drawable.tedradiohour};

        Program cartalk = new Program();
        cartalk.setId("1");
        cartalk.setName("cartalk");
        cartalk.setSource("http://cartalk");
        programs.add(cartalk);

        Program tedradio = new Program();
        tedradio.setId("2");
        tedradio.setName("tedradiohour");
        tedradio.setSource("http://tedradiohour");
        programs.add(tedradio);
        programListAdapter = new ProgramListAdapter(context, programs, imageItems);
    }

    @Test
    public void testGetView(){
        View convertView = new View(context);
        ViewGroup parent = new LinearLayout(context);

        View view = programListAdapter.getView(0,null, null);
        TextView textView = (TextView)view.findViewById(R.id.programTextView);
        Assert.assertNotNull(textView);
        Assert.assertEquals("cartalk",textView.getText());
        Assert.assertEquals(2, programListAdapter.getCount());
    }
}
