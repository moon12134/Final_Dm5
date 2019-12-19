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
import com.example.myapplication.Providers.MangaSummary;
import com.example.myapplication.Providers.Page.MangaInfo;
import com.example.myapplication.Providers.Page.MangaList;
import com.example.myapplication.Providers.ProviderDm5;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Manga_preview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_preview);
        String path_MP = getIntent().getExtras().getString("path_MP");
        String imagaUrl_MP = getIntent().getExtras().getString("imagaUrl_MP");
        String name_MP = getIntent().getExtras().getString("name_MP");
        Log.e("path_MP",path_MP);
        Log.e("imagaUrl_MP",imagaUrl_MP);
        Log.e("name_MP",name_MP);
        final MangaInfo mangaInfo = new MangaInfo();
        mangaInfo.path = path_MP;
        mangaInfo.imageUrl= imagaUrl_MP;
        mangaInfo.name =name_MP;
        TextView textView = findViewById(R.id.textView);
        textView.setText(name_MP);
        GetBitmap(mangaInfo.imageUrl);
        runAsyncTask(mangaInfo);

    }

    private void runAsyncTask(final MangaInfo mangaInfo){
        new AsyncTask<MangaInfo,Integer, MangaSummary>(){
            @Override
            protected MangaSummary doInBackground(MangaInfo... data){
                try {
                    ProviderDm5 Dm5= new ProviderDm5();
                    MangaSummary mangaSummary = Dm5.getDetailInfo(data[0]);
                    return mangaSummary;
                }catch (Exception e){
                    return null;
                }
            }
            @Override
            protected void onProgressUpdate(Integer... values){
                super.onProgressUpdate();
            }
            @Override
            protected void  onPostExecute(final MangaSummary mangaSummary){
                super.onPostExecute(mangaSummary);
                final Intent intent =new Intent(Manga_preview.this, Mange_chapter.class);
                final TextView discribtion =findViewById(R.id.discibtion);

                discribtion.setText(mangaSummary.description);
                MyAdapter cubeeAdapter = new MyAdapter(mangaSummary.chapters);
                GridView gridView = findViewById(R.id.gv_manga_preview1);
                gridView.setAdapter(cubeeAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        intent.putExtra("Chapter_ReadLink",mangaSummary.chapters.get(position).readLink);
                        startActivity(intent);
                    }
                });
            }

        }.execute(mangaInfo);
    }
    private class MyAdapter extends BaseAdapter {
        private ChaptersList m;
        public MyAdapter(ChaptersList chapters){
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
            convertView = getLayoutInflater().inflate(R.layout.manga_preview_item,parent,false);
            TextView name= convertView.findViewById(R.id.preview_item);
            name.setText(m.get(position).name);
            return convertView;
        }
    }

    private void GetBitmap(String url){                             //Get image Bitmap amd push into imageView
        new AsyncTask<String,Integer, Bitmap>(){
            @Override
            protected Bitmap doInBackground(String... url){
                try {
                    return getBitmapFromURL(url[0]);
                }catch (Exception e){
                    return null;
                }
            }
            @Override
            protected void onProgressUpdate(Integer... values){
                super.onProgressUpdate();
            }
            @Override
            protected void  onPostExecute(Bitmap Bitmap){
                super.onPostExecute(Bitmap);
                final ImageView imageView =findViewById(R.id.imageV);
                imageView.setImageBitmap (Bitmap);
            }
        }.execute(url);
    }
    private static Bitmap getBitmapFromURL(String imageUrl)
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
}
