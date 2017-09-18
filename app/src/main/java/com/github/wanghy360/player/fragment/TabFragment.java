package com.github.wanghy360.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.wanghy360.player.R;

/**
 * Created by Wanghy on 2017/9/18
 */
public class TabFragment extends BaseFragment {
    private static final String ARG_TITLE = "title";
    private String title;


    public static TabFragment newInstance(String title) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setTextColor(getResources().getColor(R.color.app_font_t2_color));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setText(title == null ? "" : title);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER);
        container.addView(textView);
        return textView;
    }
}
