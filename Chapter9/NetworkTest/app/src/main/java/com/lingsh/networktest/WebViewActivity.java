package com.lingsh.networktest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * description
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/24 9:51
 **/


public class WebViewActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Button ret = (Button) findViewById(R.id.ret);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.finish();
            }
        });

        WebView view = (WebView) findViewById(R.id.web_view);
        view.getSettings().setJavaScriptEnabled(true);
        view.setWebViewClient(new WebViewClient());
        view.loadUrl(NetUtils.TEST_NET_STATION);
    }

    public static void jump(Context from) {
        from.startActivity(new Intent(from, WebViewActivity.class));
    }
}
