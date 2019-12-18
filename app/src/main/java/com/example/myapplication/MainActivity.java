package com.example.myapplication;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Providers.Chapter.chapterLink;
import com.example.myapplication.Providers.MangaSummary;
import com.example.myapplication.Providers.Page.MangaInfo;
import com.example.myapplication.Providers.Page.MangaList;
import com.example.myapplication.Providers.ProviderDm5;

import org.jsoup.Jsoup;
//import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    MangaInfo m = new MangaInfo();
                    ProviderDm5 Dm= new ProviderDm5();
                    chapterLink chapterLinks ;
                    Log.e("chapterNumber",String.valueOf(Dm.getChapterPage("m945091")));

                    chapterLinks = Dm.getChapterImageUrl("m945091","1");

                    Log.e("aaa",chapterLinks.imUrl);
                    Log.e("aaa",chapterLinks.Referer);
                    //Dm.getchapter("/m938440/");
                    //Log.e("aaa",String.valueOf(a));
                    //Dm.getchapter("/m938440/");
                    //MangaSummary page = Dm.getDetailInfo(m);
                }catch (Exception e){
                    Log.e("error",e.toString());
                }
            }
        }).start();
*/
        final Intent intent =new Intent(this, MangaPage.class);
        MangaList mangaList= new MangaList();
        String genres[] = {"更新","全部","热血","恋爱","校园","百合","彩虹","冒险","后宫","科幻",
                "战争","悬疑","推理","搞笑","奇幻","魔法","恐怖","神鬼","历史","同人","运动","绅士","机甲","限制级"};
        for(int i=0;i<genres.length;i++){
            MangaInfo mangaInfo;
            mangaInfo =new MangaInfo();
            mangaInfo.name= genres[i];
            mangaList.add(mangaInfo);
        }

        MyAdapter cubeeAdapter = new MyAdapter(mangaList);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(cubeeAdapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                intent.putExtra("gerens",position);
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends BaseAdapter{
        private MangaList m;
        public MyAdapter(MangaList mangaList){
            m=mangaList;
        }
        @Override
        public  int getCount(){
            return  this.m.size();
        }
        @Override
        public Object getItem(int position){
            return m.get(position);
        }
        @Override
        public  long getItemId(int position){
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            convertView = getLayoutInflater().inflate(R.layout.genres_page,parent,false);
            TextView name= convertView.findViewById(R.id.gernes);
            name.setText(m.get(position).name);
            return convertView;
        }
    }


}

