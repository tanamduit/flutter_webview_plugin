package com.flutter_webview_plugin;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.net.http.SslError;
import android.webkit.WebViewClient;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.annotation.TargetApi;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;

/**
 * Created by lejard_h on 20/12/2017.
 */

public class BrowserClient extends WebViewClient {

    boolean isGetError = false;
    boolean isFinishingLoad = false;

    public BrowserClient() {
        super();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        isGetError = false;
        isFinishingLoad = false;
        super.onPageStarted(view, url, favicon);
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);

        FlutterWebviewPlugin.channel.invokeMethod("onUrlChanged", data);

        data.put("type", "startLoad");
        data.put("navigationType",0);
        FlutterWebviewPlugin.channel.invokeMethod("onState", data);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(!isGetError){
            isFinishingLoad = true;
            Log.d("webview","page finished");
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            data.put("type", "finishLoad");
            data.put("navigationType",0);
            FlutterWebviewPlugin.channel.invokeMethod("onState", data);
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
        if(!isFinishingLoad){
            isGetError = true;
            super.onReceivedError(view,errorCode,description,failingUrl);
            Log.d("webview","error in old onreceive ERror");
            FlutterWebviewPlugin.channel.invokeMethod("onError","general error");
        }
    }

    @TargetApi(23)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if(!isFinishingLoad){
            isGetError = true;
            super.onReceivedError(view, request, error);
            Log.d("webview","error in onreceive ERror");
            FlutterWebviewPlugin.channel.invokeMethod("onError","general error");
        }
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        // if(!isFinishingLoad){
        //     isGetError = true;
        //     super.onReceivedHttpError(view, request, errorResponse);
        //     Log.d("webview","errorResponse : "+errorResponse.getReasonPhrase());
        //     Log.d("webview","error in onreceive HTTP ERror");
        //     FlutterWebviewPlugin.channel.invokeMethod("onError","http error");
        // }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if(!isFinishingLoad){
            isGetError = true;
            super.onReceivedSslError(view, handler, error);
            Log.d("webview","error in onreceive SSL Error");
            FlutterWebviewPlugin.channel.invokeMethod("onError","ssl error");
        }
    }
}