package edu.nanoracket.npr.program;

public class Program {
    private String mName;
    private String mSource;
    private String mId;

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getSource() {
        return mSource;
    }
    public void setSource(String source) {
        mSource = source;
    }
    public String getId() {
        return mId;
    }
    public void setId(String nPRId) {
        mId = nPRId;
    }

    @Override
    public String toString(){
        return mName;
    }
}

