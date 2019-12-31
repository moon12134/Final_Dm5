package com.example.myapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MangaPage;
import com.example.myapplication.Providers.Page.MangaInfo;
import com.example.myapplication.Providers.Page.MangaList;
import com.example.myapplication.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_classification, container, false);
        final Intent intent =new Intent(getActivity(), MangaPage.class);
        MangaList mangaList= new MangaList();
        String genres[] = {"更新","全部","热血","戀愛","校園","百合","彩虹","冒险","后宮","科幻",
                "戰爭","懸疑","推理","搞笑","奇幻","魔法","恐怖","神鬼","歷史","同人","運動","绅士","機甲"};
        for(int i=0;i<genres.length;i++){
            MangaInfo mangaInfo;
            mangaInfo =new MangaInfo();
            mangaInfo.name= genres[i];
            mangaList.add(mangaInfo);
        }
        final Button button = root.findViewById(R.id.btn_search);
        final EditText editText=root.findViewById(R.id.edit_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Gerens",-1);
                intent.putExtra("Search_Text",String.valueOf(editText.getText()));
                startActivity(intent);
            }
        });

        MyAdapter cubeeAdapter = new MyAdapter(mangaList);
        GridView gridView = root.findViewById(R.id.gridView);
        gridView.setAdapter(cubeeAdapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                intent.putExtra("Gerens",position);
                startActivity(intent);
            }
        });
        return root;
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