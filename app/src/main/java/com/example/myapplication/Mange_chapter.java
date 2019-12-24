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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
    private RunAsyncTast A;
    //  private runAsyncTask runAsyncTask;
    //  private Adap a;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        aVoid.interrupted();
        A.cancel(true);

        //  a.cancel(true);
        //    runAsyncTask.cancel(true);
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
        final ArrayList<Bitmap> bitmaps = new ArrayList<>(num);

        try {
            aa.join();
            Log.e("PAGE",String.valueOf(num));
        }catch (Exception e)
        {
        }

        RunAsyncTast A = new RunAsyncTast();
        A.execute(url);


    }


    class RunAsyncTast extends AsyncTask<String, ArrayList<Bitmap> ,Boolean>
    {


            ArrayList<Bitmap> TEST = new ArrayList<>();
            MyAdapter CC= new MyAdapter(TEST);
            GridView gridView = findViewById(R.id.gv_manga_chapter);

            @Override
            protected void onPreExecute() {
                if(this.isCancelled()) return;
            for (int i=0;i<num;i++) {
                TEST.add(null);
            }
            gridView.setAdapter(CC);
        }
            @Override
            protected Boolean doInBackground(String... data) {
                if(this.isCancelled()) return null;
            ProviderDm5 dm5 = new ProviderDm5();
            //抓到bitmap 存成 arraylist
            chapterLink chapterLink = new chapterLink();
            ArrayList<Bitmap> bitmapD= new ArrayList<>();

            try {
                for (int i = 0; i < dm5.getChapterPage(data[0]); i++) {
                    if (this.isCancelled()) return null;
                    chapterLink = dm5.getChapterImageUrl(data[0], String.valueOf(i + 1));
                    String imgUrl = chapterLink.imUrl;
                    URL url = new URL(imgUrl);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Referer", chapterLink.Referer);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    System.out.println("仔個一半");
                    bitmapD.add(bitmap);
                    publishProgress(bitmapD);
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
            @Override
            protected void onProgressUpdate(ArrayList<Bitmap>...  Nm) {
            super.onProgressUpdate();
                if(this.isCancelled()) return ;

            int length = Nm[0].size()-1;
            TEST.set(length,Nm[0].get(length));
            System.out.println("正在載路");

            CC.notifyDataSetChanged();
        }

            protected void  onPostExecute(Boolean boo){
                if(this.isCancelled()) return;
            super.onPostExecute(true);
            //將ArrayList<bitmap>放入

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
    private class MyAdapter extends BaseAdapter {
        private  ArrayList<Bitmap> m = null;

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
