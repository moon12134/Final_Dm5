package com.example.myapplication;

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

import com.example.myapplication.Providers.Chapter.ChaptersList;
import com.example.myapplication.Providers.Chapter.MangaChapter;
import com.example.myapplication.Providers.Chapter.chapterLink;
import com.example.myapplication.Providers.MangaSummary;
import com.example.myapplication.Providers.Page.MangaInfo;
import com.example.myapplication.Providers.Page.MangaList;
import com.example.myapplication.Providers.ProviderDm5;

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

public class Mange_chapter extends AppCompatActivity {
    public static int num;
    public static String vvv;
    public int chapterPage;
    private runAsyncTask runAsyncTask;
    private Adap a;
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        aVoid.interrupted();
        a.cancel(true);
        runAsyncTask.cancel(true);
        Log.e("aaa","aaa");
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mange_chapter);
        final String url = getIntent().getExtras().getString("Chapter_ReadLink").replace("/", "");
        vvv=url;
        //Log.e("/mXXXXXX/", url);
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
        //以上解決FTTPS協定問題
        final aVoid aa = new aVoid();
        aa.start();
        final ArrayList<Bitmap> bitmaps = new ArrayList<>(chapterPage);
        try {
            aa.join();
            Log.e("PAGE",String.valueOf(num));
            for (int i = 0; i < num; i++) {
                bitmaps.add(null);
            }
        }catch (Exception e){
        }
        a=new Adap();
        a.execute(bitmaps);
        runAsyncTask =new runAsyncTask();
        runAsyncTask.execute(url);
    }
    class Adap extends AsyncTask<ArrayList<Bitmap>, Integer, ArrayList<Bitmap>>{
                @Override
                protected ArrayList doInBackground(ArrayList<Bitmap>... url) {
                    if(this.isCancelled()) return null;
                    return url[0];
                }
                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate();
                    if(this.isCancelled()) return;
                }
                @Override
                protected void onPostExecute(ArrayList<Bitmap> Bitmap) {
                    super.onPostExecute(Bitmap);
                    MyAdapter cubeeAdapter = new MyAdapter(Bitmap);
                    GridView gridView = findViewById(R.id.gv_manga_chapter);
                    gridView.setAdapter(cubeeAdapter);
                    if(this.isCancelled()) return;
                }
    }


    class aVoid extends Thread{
        @Override
        public void run() {
            try {
                final ProviderDm5 dm5 = new ProviderDm5();
                num = dm5.getChapterPage(vvv);
            } catch (Exception e) {
            }
        }
    }
    class runAsyncTask extends AsyncTask<String,Integer, ArrayList<Bitmap>>{
            @Override
            protected ArrayList<Bitmap> doInBackground(String... data){
                if(this.isCancelled()) return null;
                ProviderDm5 dm5=new ProviderDm5();
                //抓到bitmap 存成 arraylist
                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                chapterLink chapterLink =new chapterLink();
                try{
                    for(int i =0;i<dm5.getChapterPage(data[0]);i++){
                        if(this.isCancelled()) return null;
                        //chapterLink.add(dm5.getChapterImageUrl(data[0],String.valueOf(i)));
                        chapterLink = dm5.getChapterImageUrl(data[0],String.valueOf(i+1));
                        //chapterLink.Referer = dm5.getChapterImageUrl(data[0],String.valueOf(i)).Referer;
                        //chapterLink.imUrl = dm5.getChapterImageUrl(data[0],String.valueOf(i)).imUrl;

                        String imgUrl = chapterLink.imUrl;
                        URL url = new URL(imgUrl);
                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setRequestMethod("GET");
                        //connection.setRequestProperty("Host","manhua1025-104-250-150-12.cdndm5.com");
                        connection.setRequestProperty("Referer",chapterLink.Referer);
                        //connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0");
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(input);
                        bitmaps.add(bitmap);
                        m.set(i,bitmap);

                    }

                    return bitmaps;
                }catch (Exception e){
                    return null;
                }
            }
            @Override
            protected void onProgressUpdate(Integer... values){
                super.onProgressUpdate();
                if(this.isCancelled()) return;
            }
            @Override
            protected void  onPostExecute(final ArrayList<Bitmap> Bitmap){
                super.onPostExecute(Bitmap);
                if(this.isCancelled()) return;
                //將ArrayList<bitmap>放入
            }
    }
    public ArrayList<Bitmap> m;
    private class MyAdapter extends BaseAdapter {

        public MyAdapter(ArrayList<Bitmap> bitmaps){
            m=bitmaps;
        }
        @Override
        public  int getCount(){
            return  m.size();
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

            convertView = getLayoutInflater().inflate(R.layout.manga_chapter_item,parent,false);
            final ImageView imageView =convertView.findViewById(R.id.imageView2);
            if(m.get(position)!=null){
                imageView.setImageBitmap(m.get(position));
            }else {
                imageView.setImageResource(R.drawable.chapter_loading);
            }
            return convertView;
        }
    }
}
