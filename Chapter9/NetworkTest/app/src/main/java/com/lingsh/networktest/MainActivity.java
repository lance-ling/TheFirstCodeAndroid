package com.lingsh.networktest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testWebView = (Button) findViewById(R.id.web_view_test);
        Button testHttpUrlConnection = (Button) findViewById(R.id.http_url_connection_test);
        Button testOkHttp = (Button) findViewById(R.id.ok_http_test);

        testWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.jump(MainActivity.this);
            }
        });

        testHttpUrlConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUrlConnectionActivity.jump(MainActivity.this);
            }
        });

        testOkHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpActivity.jump(MainActivity.this);
            }
        });

        Button xmlPullParser = (Button) findViewById(R.id.xml_pull_parser_test);
        Button xmlSAXParser = (Button) findViewById(R.id.xml_sax_parser_test);
        Button jsonParser = (Button) findViewById(R.id.json_normal_parser_test);
        Button jsonGsonParser = (Button) findViewById(R.id.json_gson_parser_test);

        xmlPullParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XmlOrJsonParser.parserXMLByPull(XmlOrJsonParser.xmlTestData);
            }
        });

        xmlSAXParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XmlOrJsonParser.parserXMLBySAX(XmlOrJsonParser.xmlTestData);
            }
        });

        jsonParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XmlOrJsonParser.parserJsonByJsonObject(XmlOrJsonParser.jsonTestData);
            }
        });

        jsonGsonParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XmlOrJsonParser.parserJsonByGson(XmlOrJsonParser.jsonTestData);
            }
        });

    }
}
