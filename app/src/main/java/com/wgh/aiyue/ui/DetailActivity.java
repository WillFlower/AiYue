package com.wgh.aiyue.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.wgh.aiyue.R;
import com.wgh.aiyue.ui.view.ProgressWebView;
import com.wgh.aiyue.util.DLog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by   : WGH.
 */
public class DetailActivity extends BaseActivity {
    public static final String EXTRA_Url = "extra_detail_url";
    @BindView(R.id.web_detail)
    ProgressWebView mWebView;
    @BindView(R.id.detail_frame_layout)
    FrameLayout frameLayout;

    private String mUrlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mUrlString = this.getIntent().getStringExtra(EXTRA_Url);

        if (TextUtils.isEmpty(mUrlString)) {
            DLog.e("TextUtils.isEmpty(mUrlString)");
            finish();
        }

        mWebView.setProgress(true);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setAllowFileAccess(false);
        settings.setSavePassword(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        mWebView.setVerticalScrollbarOverlay(true);

        mWebView.setWebViewClient(new DetailWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(mUrlString);
    }

    private class DetailWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mWebView.getProgressBar().setVisibility(View.GONE);
            } else {
                if (mWebView.isProgress()) {
                    if (mWebView.getProgressBar().getVisibility() == View.GONE) {
                        mWebView.getProgressBar().setVisibility(View.VISIBLE);
                    }
                    mWebView.getProgressBar().setProgress(newProgress);
                } else {
                    mWebView.getProgressBar().setVisibility(View.GONE);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
    }
}
