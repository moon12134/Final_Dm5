package com.example.myapplication.ui.notifications;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.BookCase;
import com.example.myapplication.MangaPage;
import com.example.myapplication.Manga_preview;
import com.example.myapplication.Providers.Page.MangaInfo;
import com.example.myapplication.Providers.Page.MangaList;
import com.example.myapplication.Providers.ProviderDm5;
import com.example.myapplication.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Provider;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private MangaList m ;
    ArrayList<MangaInfo> mangaInfoLIST= null;
    private BookCase bookCase = new BookCase();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        m =new MangaList();
        try {
            mangaInfoLIST = bookCase.readFileURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i= 0;i<mangaInfoLIST.size();i++)
        {
                m.add(i,mangaInfoLIST.get(i));
        }


        final Intent intent = new Intent(getActivity(), Manga_preview.class);
        final GridView gridView =root.findViewById(R.id.GridView);

        MyAdapter cubeeAdapter = new MyAdapter(m);
        //*/*******************************
        gridView.setNumColumns(3);
        gridView.setAdapter(cubeeAdapter);

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                intent.putExtra("path_MP", m.get(position).path);
                intent.putExtra("imagaUrl_MP", m.get(position).imageUrl);
                intent.putExtra("name_MP", m.get(position).name);
                startActivity(intent);
            }
        });

        //*******************************************
        return root;
    }



    private class MyAdapter extends BaseAdapter {
        MangaList mt = null;
        public MyAdapter(MangaList mangaList){
            mt=mangaList;
        }
        @Override
        public  int getCount(){
            return  mt.size();
        }
        @Override
        public Object getItem(int position){
            return mt.get(position);
        }
        @Override
        public  long getItemId(int position){
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            MangaPage.ViewHolder holder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.manga_adapter, parent, false);
                holder = new MangaPage.ViewHolder();
                holder.textView =convertView.findViewById(R.id.name);
                holder.textView_status=convertView.findViewById(R.id.status);
                holder.thumbnail=convertView.findViewById(R.id.IW);
                convertView.setTag(holder);
            }else {
                holder = (MangaPage.ViewHolder) convertView.getTag();
            }

            holder.textView.setText(mt.get(position).name);
            holder.position = position;
            holder.textView_status.setText("");
            if (mt.get(position).imageUrl_bitmap==null){
                holder.thumbnail.setImageResource(R.drawable.comic);
                new MangaPage.ThumbnailTask(position, holder,mt.get(position).imageUrl)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
            }else {
                holder.thumbnail.setImageBitmap(mt.get(position).imageUrl_bitmap);
            }
            return convertView;
        }
    }

    public static class ThumbnailTask extends AsyncTask<String, Void, Bitmap> {
        private int mPosition;
        private MangaPage.ViewHolder mHolder;
        private String url;

        public ThumbnailTask(int position, MangaPage.ViewHolder holder, String ur) {
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