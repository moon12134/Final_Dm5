package com.example.myapplication.Providers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Providers.Page.MangaInfo;
import com.example.myapplication.Providers.Page.MangaList;
import com.example.myapplication.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
            Log.e("Aaaaa","https://cnc.dm5.com/manhua-" + genresUrl[genere - 1] + "-p"+page + "/");
            Connection.Response response = Jsoup.connect("https://cnc.dm5.com/manhua-" + genresUrl[genere - 1] + "-p"+page + "/").execute();
            Document doc = Jsoup.parse(response.body());
            Elements tmc = doc.select("div.mh-item");
            MangaInfo manga;
            for (Element con : tmc){
                manga = new MangaInfo();
                Log.e("Name", con.select("div.mh-item-detali").select("a").first().attr("title"));
                Log.e("chapter", con.select("div.mh-item-detali").select("a").first().attr("href"));
                Log.e("Image",con.getElementsByAttributeValue("class","chapter").text());
                manga.name = con.select("div.mh-item-detali").select("a").first().attr("title");
                manga.path = con.select("div.mh-item-detali").select("a").first().attr("href");
                manga.status = con.getElementsByAttributeValue("class","chapter").get(0).text();
                mangalist.add(manga);
            }
        return mangalist;
    }
    public MangaSummary getDetailInfo(MangaInfo mangaInfo)throws Exception{
        MangaSummary summary =new MangaSummary(mangaInfo);
        Connection.Response response = Jsoup.connect("https://www.dm5.com/" + mangaInfo.path).execute();
        Document doc = Jsoup.parse(response.body());
        Elements tmc = doc.select("div.info");
        Log.e("getDetailInfo","https://www.dm5.com/" + mangaInfo.path);
        for (Element con : tmc){
            Log.e("description", con.getElementsByAttributeValue("class","content").text());
            summary.description= con.select("div.mh-item-detali").select("a").first().attr("title");
        }
        /*
        Log.e("chapterklist name", con.select("div.mh-item-detali").select("a").first().attr("href"));
        Log.e("chapterklist readLink", con.select("div.mh-item-tip").select("p").first().select("[src]").attr("style"));
        summary.name = con.select("div.mh-item-detali").select("a").first().attr("title");
        manga.preview = con.select("div.mh-item").select("p").first().select("[src]").attr("style");
        //find description
        //get chapter pathlist
        //chapterklist：name readLink*/
        return null;
    }



}

