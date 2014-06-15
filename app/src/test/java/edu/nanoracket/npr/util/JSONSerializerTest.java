package edu.nanoracket.npr.util;

import android.content.Context;
import edu.nanoracket.npr.model.Program;
import junit.framework.Assert;
import org.json.JSONException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class JSONSerializerTest {
    private Context context;
    private String fileName;
    private ArrayList<Program> programs;
    private JSONSerializer jsonSerializer;
    private File expectedFile;

    @Before
    public void setUp(){
        context = Robolectric.application;
        fileName = "programs.json";
        programs = new ArrayList<Program>();
        jsonSerializer = new JSONSerializer(context, fileName);
    }

    @Test
    public void testSaveProgramsOK() throws IOException, JSONException {
        Program program = new Program();
        program.setId("1");
        program.setName("cartalk");
        program.setSource("http://www..npr.org");
        programs.add(program);
        jsonSerializer.savePrograms(programs);
        expectedFile = new File(fileName);
        Assert.assertNotNull(expectedFile.exists());
        expectedFile.delete();
    }

    @Test
    public void testLoadProgramsOK() throws IOException, JSONException {
        Program program = new Program();
        program.setId("1");
        program.setName("cartalk");
        program.setSource("http://www..npr.org");
        programs.add(program);
        jsonSerializer.savePrograms(programs);
        programs = jsonSerializer.loadPrograms();
        Assert.assertEquals(1, programs.size());
        expectedFile = new File(fileName);
        expectedFile.delete();
    }
}
