package com.github.wanghy360.player.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.wanghy360.player.bean.FragmentTabItem;

import java.util.ArrayList;
import java.util.List;

public class SlidTabViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentTabItem> list;

    public SlidTabViewPagerAdapter(FragmentManager fm, List<FragmentTabItem> datalist) {
        super(fm);
        list = new ArrayList<FragmentTabItem>();
        if (datalist != null)
            this.list = datalist;
    }

    public List<FragmentTabItem> getList() {
        return list;
    }

    public void setList(List<FragmentTabItem> showlist) {
        if (showlist != null) {
            if (this.list != null)
                this.list.clear();

            for (int i = 0; i < showlist.size(); i++) {
                this.list.add(showlist.get(i));
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0).getFragment();
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getTitle();
    }
}
