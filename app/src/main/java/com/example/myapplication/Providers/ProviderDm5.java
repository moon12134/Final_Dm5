package com.example.myapplication.Providers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Providers.Chapter.ChaptersList;
import com.example.myapplication.Providers.Chapter.MangaChapter;
import com.example.myapplication.Providers.Chapter.chapterLink;
import com.example.myapplication.Providers.Page.MangaInfo;
import com.example.myapplication.Providers.Page.MangaList;
import com.example.myapplication.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ProviderDm5 {
    public static int genres[] = {
            R.string.genre_new, R.string.genre_list, R.string.genre_rexue, R.string.genre_aiqing, R.string.genre_xiaoyuan, R.string.genre_baihe, R.string.genre_danmei, R.string.genre_maoxian, R.string.genre_hougong,
            R.string.genre_kehuan, R.string.genre_zhanzheng, R.string.genre_xuanyi, R.string.genre_zhentan, R.string.genre_gaoxiao, R.string.genre_qihuan, R.string.genre_mofa, R.string.genre_kongbu, R.string.genre_dongfangshengui,
            R.string.genre_lishi, R.string.genre_tongren, R.string.genre_jingji, R.string.genre_jiecao, R.string.genre_jizhan, R.string.genre_18_x};
    public static String genresUrl[] = {
            "new", "list", "rexue", "aiqing", "xiaoyuan", "baihe", "danmei", "maoxian", "hougong",
            "kehuan", "zhanzheng", "xuanyi", "zhentan", "gaoxiao", "qihuan", "mofa", "kongbu",
            "dongfangshengui", "lishi", "tongren", "jingji", "jiecao", "jizhan", "18-x"};

    public ProviderDm5() {
    }

    public MangaList getList(int page, int genere)throws Exception{
            MangaList mangalist = new MangaList();
            Connection.Response response = Jsoup.connect("https://cnc.dm5.com/manhua-" + genresUrl[genere - 1] + "-p"+page + "/").execute();
            Document doc = Jsoup.parse(response.body());
            Elements tmc = doc.select("div.mh-item");
            MangaInfo manga;
            for (Element con : tmc){
                manga = new MangaInfo();
                Log.e("chapter Name", con.select("div.mh-item-detali").select("a").first().attr("title"));
                Log.e("chapter Path", con.select("div.mh-item-detali").select("a").first().attr("href"));
                Log.e("chapter Status",con.getElementsByAttributeValue("class","chapter").get(0).text());
                Log.e("chapter Image Path",con.getElementsByAttributeValue("class","mh-cover").attr("style").replaceAll("background-image: url","").replace("(","").replace(")",""));//text 代表顯示的文字 toString 代表顯示的html
                manga.name = con.select("div.mh-item-detali").select("a").first().attr("title");
                manga.path = con.select("div.mh-item-detali").select("a").first().attr("href");
                manga.status = con.getElementsByAttributeValue("class","chapter").get(0).text();
                manga.imageUrl =con.getElementsByAttributeValue("class","mh-cover").attr("style").replaceAll("background-image: url","").replace("(","").replace(")","");
                mangalist.add(manga);
            }
        return mangalist;
    }
    public MangaSummary getDetailInfo(MangaInfo mangaInfo)throws Exception{
        MangaSummary summary =new MangaSummary(mangaInfo);
        ChaptersList chaptersList =new ChaptersList();

        Connection.Response response = Jsoup.connect("https://cnc.dm5.com/" + mangaInfo.path).execute();
        Log.e("aaaa","https://cnc.dm5.com/" + mangaInfo.path);
        Document doc = Jsoup.parse(response.body());
        Elements tmc = doc.select("div.banner_detail_form");
        for (Element con : tmc){
            Log.e("description", con.getElementsByAttributeValue("class","content").text());
            summary.description= con.getElementsByAttributeValue("class","content").text();


        }

        Elements elements = doc.select("div.left-bar").select("ul.view-win-list.detail-list-select").select("ul li a");//select必去加在外面 內圈才會逐一選取
        MangaChapter mangaChapter;
        for(Element co:elements){
            mangaChapter =new MangaChapter();
            Log.e("chapterklist name", co.attr("href"));
            Log.e("chapterklist name", co.text());
            mangaChapter.name = co.text();
            mangaChapter.readLink = co.attr("href");
            chaptersList.add(mangaChapter);
        }
        summary.chapters = chaptersList;
        //find description
        //get chapter pathlist
        //chapterklist：name readLink*/
        return summary;
    }
    public ArrayList<chapterLink> getchapter(String path)throws Exception{
        Connection.Response response = Jsoup.connect("https://cnc.dm5.com/" + path).execute();
        Log.e("aaaa","https://cnc.dm5.com/" + path);
        Document doc = Jsoup.parse(response.body());
        Element tmc = doc.select("div.chapterpager").select("a").last();
        Log.e("description", tmc.text());
        int a =Integer.parseInt(tmc.text());
        ArrayList arrayList =new ArrayList();

        for (int f=1; f <= a;f++){
            chapterLink ch =new chapterLink();
            ch.url = path.replaceAll("/","")+"-p"+ f;
            Log.e("URL",getChapterImageUrl(ch.url));
            arrayList.add(ch);
        }
        return arrayList;
    }
    public String getChapterImageUrl(String url)throws Exception{
        Connection.Response response = Jsoup.connect("https://cnc.dm5.com/" + url).execute();
        Log.e("aaaa","https://cnc.dm5.com/" + url);
        Document doc = Jsoup.parse(response.body());
        Element tmc = doc.getElementsByAttributeValue("id","cp_img").first();
        Log.e("description", tmc.toString());
        return url;
    }



}

