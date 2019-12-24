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
    //  private runAsyncTask runAsyncTask;
    //  private Adap a;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        aVoid.interrupted();
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


        new AsyncTask<String, ArrayList<Bitmap> ,Boolean>()
        {
            ArrayList<Bitmap> TEST = new ArrayList<>(num);

            MyAdapter CC= new MyAdapter(TEST);
            GridView gridView = findViewById(R.id.gv_manga_chapter);

            @Override
            protected void onPreExecute() {
                /*
                for (int i=0;i<num;i++) {
                    TEST.add(null);
                }

                 */
                //gridView.setAdapter(CC);
            }
            @Override
            protected Boolean doInBackground(String... data) {
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
              //  CC.notifyDataSetChanged();

            }

            protected void  onPostExecute(Boolean boo){
                super.onPostExecute(true);
                //將ArrayList<bitmap>放入

            }
        }.execute(url);



        //      runAsyncTask =new runAsyncTask();
        //   runAsyncTask.execute(url);
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

 /*
    class runAsyncTask extends AsyncTask<String,ArrayList<Bitmap>, ArrayList<Bitmap>>{
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
                        publishProgress(m);
                    }

                    return bitmaps;
                }catch (Exception e){
                    return null;
                }
            }
            @Override
            protected void onProgressUpdate(ArrayList<Bitmap>... Nm){
                super.onProgressUpdate();

                for (int i=0;i<=Nm.length;i++) {
                    if (Nm[i]==null) {
                        GridView gridView = findViewById(R.id.gv_manga_chapter);
                        MyAdapter ADDpter = new MyAdapter(Nm[i]);
                        gridView.setAdapter(ADDpter);
                    }
                }




                if(this.isCancelled()) return;
            }
            @Override
            protected void  onPostExecute(final ArrayList<Bitmap> Bitmap){
                super.onPostExecute(Bitmap);
                if(this.isCancelled()) return;
                //將ArrayList<bitmap>放入
            }
    }
*/

/*

    private class MyAdapter extends BaseAdapter {
        private  Bitmap m = null;

        public MyAdapter(Bitmap bitmaps){
            m=bitmaps;
        }
        @Override
        public s
        @Override
        public  int getCount(){
            return 0;
        }
        @Override
        public Object getItem(int position){

            return m;
        }
        @Override
        public  long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            convertView = getLayoutInflater().inflate(R.layout.manga_chapter_item,parent,false);
            final ImageView imageView =convertView.findViewById(R.id.imageView2);
            if(m!=null){
                imageView.setImageBitmap(m);
            }else {
                imageView.setImageResource(R.drawable.chapter_loading);
            }
            return convertView;
        }
    }

 */

    private class MyAdapter extends BaseAdapter {
        public  ArrayList<Bitmap> m;
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
