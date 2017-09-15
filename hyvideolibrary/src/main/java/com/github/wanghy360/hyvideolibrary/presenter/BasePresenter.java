package com.github.wanghy360.hyvideolibrary.presenter;


import com.github.wanghy360.hyvideolibrary.view.BaseView;

/**
 * Created by Wanghy on 2017/9/8
 */
public abstract class BasePresenter<T extends BaseView> {
    protected T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    public abstract void subscribe();

    public abstract void unSubscribe();
}
