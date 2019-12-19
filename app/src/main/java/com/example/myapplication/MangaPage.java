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
        int data = getIntent().getExtras().getInt("Gerens");
        runAsyncTask(data);
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
                        //Toast.makeText(MangaPage.this, "" + position, Toast.LENGTH_SHORT).show();
                        intent.putExtra("path_MP",mangaList.get(position).path);
                        intent.putExtra("imagaUrl_MP",mangaList.get(position).imageUrl);
                        intent.putExtra("name_MP",mangaList.get(position).name);
                        intent.putExtra("imgBitmap",mangaList.get(position).imageUrl_bitmap);
                        startActivity(intent);

                    }
                });

            }

        }.execute(data);
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
            ViewHolder holder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.manga_adapter, parent, false);
                holder = new ViewHolder();
                holder.textView =convertView.findViewById(R.id.name);
                holder.textView_status=convertView.findViewById(R.id.status);
                holder.thumbnail=convertView.findViewById(R.id.IW);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(m.get(position).name);
            holder.textView_status.setText(m.get(position).status);
            holder.position = position;
            if (m.get(position).imageUrl_bitmap==null){
                new ThumbnailTask(position, holder,m.get(position).imageUrl)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
            }else {
                holder.thumbnail.setImageBitmap(m.get(position).imageUrl_bitmap);
            }
            return convertView;
        }
    }
    public static class ThumbnailTask extends AsyncTask<String, Void, Bitmap> {
        private int mPosition;
        private ViewHolder mHolder;
        private String url;

        public ThumbnailTask(int position, ViewHolder holder,String ur) {
            mPosition = position;
            mHolder = holder;
            this.url=ur;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            return getBitmapFromURL(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mHolder.position == mPosition) {
                mHolder.thumbnail.setImageBitmap(bitmap);
            }
        }
    }
    public static class ViewHolder {
        public ImageView thumbnail;
        public TextView textView;
        public TextView textView_status;
        public int position;
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
