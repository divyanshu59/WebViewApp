package com.appm.eldonlondon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    WebView webView;
    String website_link;
    Intent intent;
    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SwipeRefreshLayout refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh.setRefreshing(true);

                if (!isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, "Please On Mobile Data/Internet", Toast.LENGTH_LONG).show();
                }

                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

                webView.loadUrl(website_link);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Refresh Completed", Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    }
                },4000);

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        navigationMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.HORIZONTAL));

        navigationView.setNavigationItemSelectedListener(this);

        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        website_link = "https://eldonlondon.com";

        if (!isNetworkAvailable()) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            webView.getSettings().setAppCacheEnabled(true);
            Toast.makeText(this, "Please Open your Mobile data & refresh the page", Toast.LENGTH_SHORT).show();
        }

        webView.loadUrl(website_link);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait..\nApp is Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });

        webView.getSettings().setBuiltInZoomControls(true);

        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (webView.canGoBack()) {
            webView.goBack();
        }else{
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.app_name));
                builder.setMessage("Are yor sure you want to Quit");
                builder.setPositiveButton("OK!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            webView.loadUrl("https://eldonlondon.com");
            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    progressDialog1 = new ProgressDialog(MainActivity.this);
                    progressDialog1.setMessage("Please Wait..\nApp is Loading...");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                }

                @Override
                public void onFinish() {
                    progressDialog1.dismiss();
                }
            }.start();
        }

        else if (id == R.id.nav_Shop) {
            webView.loadUrl("https://eldonlondon.com/shop/");
            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    progressDialog1 = new ProgressDialog(MainActivity.this);
                    progressDialog1.setMessage("Please Wait..\nApp is Loading...");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                }

                @Override
                public void onFinish() {
                    progressDialog1.dismiss();
                }
            }.start();
        }else if (id == R.id.nav_Blog) {
            webView.loadUrl("https://eldonlondon.com/blog/");
            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    progressDialog1 = new ProgressDialog(MainActivity.this);
                    progressDialog1.setMessage("Please Wait..\nApp is Loading...");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                }

                @Override
                public void onFinish() {
                    progressDialog1.dismiss();
                }
            }.start();
        }else if (id == R.id.nav_About) {
            webView.loadUrl("https://eldonlondon.com/about-us/");
            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    progressDialog1 = new ProgressDialog(MainActivity.this);
                    progressDialog1.setMessage("Please Wait..\nApp is Loading...");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                }

                @Override
                public void onFinish() {
                    progressDialog1.dismiss();
                }
            }.start();
        }else if (id == R.id.nav_Contact) {
            webView.loadUrl("https://eldonlondon.com/contact-us/");
            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    progressDialog1 = new ProgressDialog(MainActivity.this);
                    progressDialog1.setMessage("Please Wait..\nApp is Loading...");
                    progressDialog1.setCancelable(false);
                    progressDialog1.show();
                }

                @Override
                public void onFinish() {
                    progressDialog1.dismiss();
                }
            }.start();
        }
        else if (id == R.id.nav_share){
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.app_share_text)+"\n\n"+"Url: "+getString(R.string.play_store_link));
            startActivity(intent);
        }

        else if (id == R.id.nav_rate_us)
        {
            Uri uri = Uri.parse(getString(R.string.rate_us_link));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            startActivity(intent);
        }
        else if (id == R.id.nav_privacyPolicy)
        {
            Uri uri = Uri.parse(getString(R.string.privacypolicy));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activenetworkInfo != null && activenetworkInfo.isConnected();
    }
}