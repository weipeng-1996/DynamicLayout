package com.example.customdynamiclayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//    private LinearLayout linearLayout;
    private XmlPullParser parser;
    private int type;

    public LinearLayout recurseLayout(XmlPullParser parser, LinearLayout parent) throws XmlPullParserException, IOException {
        // 获取事件类型
        type = parser.next();
        while (type != XmlPullParser.END_DOCUMENT){
            switch (type){
                case XmlPullParser.START_TAG:
                    if ("WPlayout".equals(parser.getName())) {

                        LinearLayout linearLayout = new LinearLayout(this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        parent.addView(recurseLayout(parser, linearLayout));
                    }
                    if ("Text".equals(parser.getName())){
                        TextView textView = new TextView(this);
                        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        textView.setText("hello world");
                        parent.addView(textView);
                    }
                    if ("Button".equals(parser.getName())){
                        Button button = new Button(this);
                        String color = parser.getAttributeValue(null,"color");
                        if (color!=null){
                            Log.d("weip",color);
                            Log.d("weip",""+Color.parseColor(color));
                            button.setBackgroundColor(Color.parseColor(color));
                        }
                        button.setHeight(60);
                        button.setWidth(80);
                        parent.addView(button);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("WPlayout".equals(parser.getName())){
                        return parent;
                    }
            }
            type = parser.next();
        }
        return parent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> list = new ArrayList<>();


        try {
            InputStream is = getResources().getAssets().open("dynamic_layout.xml");
            //创建xmlPull解析器
            parser = Xml.newPullParser();
            //初始化xmlPull解析器
            parser.setInput(is, "utf-8");
            // 递归layout布局文件
            LinearLayout  rootView = new LinearLayout(this);
            rootView.setOrientation(LinearLayout.VERTICAL);
            recurseLayout(parser,rootView);
            super.setContentView(rootView);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}