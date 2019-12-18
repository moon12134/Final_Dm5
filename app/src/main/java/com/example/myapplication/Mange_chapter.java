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
import java.util.ArrayList;

public class Mange_chapter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mange_chapter);
        String url = getIntent().getExtras().getString("aaaaaa");
        Log.e("/mXXXXXX/", url);
        //runAsyncTask(url);
    }



    /*
    private void runAsyncTask(final String url){
        new AsyncTask<String,Integer, ArrayList<chapterLink>>(){
            @Override
            protected ArrayList<chapterLink> doInBackground(String... data){
                try {
                    ProviderDm5 Dm5= new ProviderDm5();
                    ArrayList<chapterLink> page = Dm5.getchapter(data[0]);
                    return page;
                }catch (Exception e){
                    return null;
                }
            }
            @Override
            protected void onProgressUpdate(Integer... values){
                super.onProgressUpdate();
            }
            @Override
            protected void  onPostExecute(final ArrayList<chapterLink> page){
                super.onPostExecute(page);
                MyAdapter cubeeAdapter = new MyAdapter(page);
                GridView gridView = findViewById(R.id.gv_manga_chapter);
                gridView.setAdapter(cubeeAdapter);

            }

        }.execute(url);
    }*/
    private synchronized Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
/*
    private class MyAdapter extends BaseAdapter {
        private ArrayList<chapterLink> m;
        public MyAdapter(ArrayList<chapterLink> chapters){
            m=chapters;
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
            convertView = getLayoutInflater().inflate(R.layout.manga_chapter_item,parent,false);
            TextView name= convertView.findViewById(R.id.ImageLink);
            name.setText(m.get(position).url);
            try {
                final ImageView imageView =convertView.findViewById(R.id.imageView2);
                Log.e("aaaaaaaaaaaaaaaaa","https://cnc.dm5.com/"+m.get(position).url);
                new AsyncTask<String, Void, Bitmap>()
                {
                    @Override
                    protected Bitmap doInBackground(String... params)
                    {
                        String url = params[0];
                        return getBitmapFromURL(url);
                    }

                    @Override
                    protected void onPostExecute(Bitmap result)
                    {
                        imageView. setImageBitmap (result);
                        super.onPostExecute(result);
                    }
                }.execute("https://cnc.dm5.com/"+m.get(position).url);
            }catch (Exception e){
                Log.e("getBitmapFromURL",e.toString());
            }
            return convertView;
        }
    }
*/
}
