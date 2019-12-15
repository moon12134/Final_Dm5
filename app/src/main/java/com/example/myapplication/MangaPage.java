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

import com.example.myapplication.MainActivity;
import com.example.myapplication.Providers.Page.MangaInfo;
import com.example.myapplication.Providers.Page.MangaList;
import com.example.myapplication.Providers.ProviderDm5;
import com.example.myapplication.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MangaPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_page);
        int data = getIntent().getExtras().getInt("gerens");
        runAsyncTask(data);
    }
    private class MyAdapter extends BaseAdapter {
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
            convertView = getLayoutInflater().inflate(R.layout.manga_adapter,parent,false);
            TextView name= convertView.findViewById(R.id.name);
            name.setText(m.get(position).name);
            TextView status= convertView.findViewById(R.id.status);
            status.setText(m.get(position).status);

            try {
                final ImageView imageView =convertView.findViewById(R.id.IW);
                Log.e("aaaaaaaaaaaaaaaaa",m.get(position).imageUrl);
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
                }.execute(m.get(position).imageUrl);
            }catch (Exception e){
                Log.e("getBitmapFromURL",e.toString());
            }
            //ImageView imageView = convertView.findViewById(R.id.iw);
            //imageView.setImageBitmap(m.get(position).preview));
            return convertView;
        }
    }
    private void runAsyncTask(int data){
        new AsyncTask<Integer,Integer,MangaList>(){
            @Override
            protected MangaList doInBackground(Integer... data){
                try {
                    ProviderDm5 Dm5= new ProviderDm5();
                    MangaList page = Dm5.getList(1,data[0]);
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
            protected void  onPostExecute(final MangaList mangaList){
                super.onPostExecute(mangaList);
                final Intent intent =new Intent(MangaPage.this, Manga_preview.class);
                    MyAdapter cubeeAdapter = new MyAdapter(mangaList);
                    GridView gridView = findViewById(R.id.gv_manga_page);
                    gridView.setAdapter(cubeeAdapter);
                    gridView.setNumColumns(3);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Toast.makeText(MangaPage.this, "" + position, Toast.LENGTH_SHORT).show();
                        intent.putExtra("path_MP",mangaList.get(position).path);
                        intent.putExtra("imagaUrl_MP",mangaList.get(position).imageUrl);
                        intent.putExtra("name_MP",mangaList.get(position).name);
                        startActivity(intent);
                    }
                });
            }

        }.execute(data);
    }
    private synchronized static Bitmap getBitmapFromURL(String imageUrl)
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
