package com.github.wanghy360.player.bean;
import android.support.v4.app.Fragment;

public class FragmentTabItem {
    private String title;
    private Fragment fragment;

    public FragmentTabItem(String title, Fragment fragment) {
        super();
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

}
