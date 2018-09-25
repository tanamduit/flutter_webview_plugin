package com.flutter_webview_plugin;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.webkit.WebChromeClient;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.view.View;

public class MyWebChromeClient extends WebChromeClient{
    private View currentView;
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
        currentView = act.findViewById(android.R.id.content).getRootView();
        currentView.setVisibility(View.GONE);
        mCustomViewContainer = new FrameLayout(act);
        mCustomViewContainer.setLayoutParams(LayoutParameters);
        mCustomViewContainer.setBackgroundResource(android.R.color.black);
        mCustomViewContainer.addView(view);
        mCustomView = view;
        mCustomViewCallback = callback;
        mCustomViewContainer.setVisibility(View.VISIBLE);
        act.setContentView(mCustomViewContainer);
        act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }



    @Override
    public void onHideCustomView() {
        if (mCustomView == null) {
            return;
        } else {
            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);
            // Remove the custom view from its container.
            mCustomViewContainer.removeView(mCustomView);
            mCustomView = null;
            mCustomViewContainer.setVisibility(View.GONE);
            mCustomViewCallback.onCustomViewHidden();
            // Show the content view.
            currentView.setVisibility(View.VISIBLE);
            //act.setContentView(currentView);
            act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}