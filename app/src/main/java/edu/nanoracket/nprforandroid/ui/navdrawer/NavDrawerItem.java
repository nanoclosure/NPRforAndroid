package edu.nanoracket.nprforandroid.ui.navdrawer;

public interface NavDrawerItem {
    public int getId();
    public String getLabel();
    public int getType();
    public boolean updateActionBarTitle();
    public boolean isCheckable();

}
