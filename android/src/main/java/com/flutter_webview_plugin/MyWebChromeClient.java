package com.flutter_webview_plugin;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.webkit.WebChromeClient;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

public class MyWebChromeClient extends WebChromeClient{
    private View currentView;
    private ViewGroup rootViewGroup;
    private View mCustomView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    private FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);

    Activity act;
    MyWebChromeClient(Activity activity){
        act = activity;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }
        act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        currentView = act.findViewById(android.R.id.content).getRootView();
        rootViewGroup = (ViewGroup) currentView;
        //currentView.setVisibility(View.GONE);
        mCustomViewContainer = new FrameLayout(act);
        mCustomViewContainer.setLayoutParams(LayoutParameters);
        mCustomViewContainer.setBackgroundResource(android.R.color.black);
        mCustomViewContainer.addView(view);
        mCustomView = view;
        mCustomViewCallback = callback;
        mCustomViewContainer.setId(31425);
        mCustomViewContainer.setVisibility(View.VISIBLE);
        rootViewGroup.addView(mCustomViewContainer);
        for(int i = 0;i < rootViewGroup.getChildCount();i++){
            if(rootViewGroup.getChildAt(i).getId() != mCustomViewContainer.getId()){
                rootViewGroup.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }



    @Override
    public void onHideCustomView() {
        if (mCustomView == null) {
            return;
        } else {
            mCustomViewContainer.setVisibility(View.GONE);
            rootViewGroup.removeView(mCustomViewContainer);
            mCustomView = null;
            mCustomViewCallback.onCustomViewHidden();
            mCustomViewContainer = null;
            currentView = null;
            for(int i=0; i < rootViewGroup.getChildCount();i++){
                rootViewGroup.getChildAt(i).setVisibility(View.VISIBLE);
            }
            //act.setContentView(currentView);
            act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}