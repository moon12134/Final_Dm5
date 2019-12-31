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
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProviderDm5 {
    public static int genres[] = {
            R.string.genre_new, R.string.genre_list, R.string.genre_rexue, R.string.genre_aiqing, R.string.genre_xiaoyuan, R.string.genre_baihe, R.string.genre_danmei, R.string.genre_maoxian, R.string.genre_hougong,
            R.string.genre_kehuan, R.string.genre_zhanzheng, R.string.genre_xuanyi, R.string.genre_zhentan, R.string.genre_gaoxiao, R.string.genre_qihuan, R.string.genre_mofa, R.string.genre_kongbu, R.string.genre_dongfangshengui,
            R.string.genre_lishi, R.string.genre_tongren, R.string.genre_jingji, R.string.genre_jiecao, R.string.genre_jizhan};
    public static String genresUrl[] = {
            "new", "list", "rexue", "aiqing", "xiaoyuan", "baihe", "danmei", "maoxian", "hougong",
            "kehuan", "zhanzheng", "xuanyi", "zhentan", "gaoxiao", "qihuan", "mofa", "kongbu",
            "dongfangshengui", "lishi", "tongren", "jingji", "jiecao", "jizhan"};

    public ProviderDm5() {
    }

    public MangaList getList(int page, int genere,String searchText)throws Exception {
        MangaList mangalist = new MangaList();
        String url = null;
        if (searchText != null) {
            url = "https://www.dm5.com/search?title=" + searchText + "&language=1";//Search
        } else {
            url = "https://cnc.dm5.com/manhua-" + genresUrl[genere] + "-p" + page + "/";
        }
        Document doc = Jsoup.connect(url).get();
        Elements tmc = doc.select("div.mh-item");
        Element element = null;
        MangaInfo manga;

        if (searchText != null) {
            element = doc.select("div.box-body").first();
            Log.e("div.box-body",element.text());
            manga = new MangaInfo();
            manga.name = element.select("div.banner_detail_form").select("a").first().attr("title");
            manga.path = element.select("div.banner_detail_form").select("a").first().attr("href");
            manga.status = element.select("div.info").select("p").toString().split("class=\"block\"")[1].split("</span")[0].split("<span>")[1];
            Log.e("manga.status",manga.status);
            manga.imageUrl = element.getElementsByAttributeValue("class", "cover").toString().split("\"")[3];
            if(manga.name==""){
                return null;
            }
            mangalist.add(manga);
        }
        for (Element con : tmc){
            manga = new MangaInfo();
            manga.name = con.select("div.mh-item-detali").select("a").first().attr("title");
            manga.path = con.select("div.mh-item-detali").select("a").first().attr("href");
            manga.status = con.getElementsByAttributeValue("class","chapter").get(0).text();
            manga.imageUrl =con.getElementsByAttributeValue("class","mh-cover").attr("style").replaceAll("background-image: url","").replace("(","").replace(")","");
            mangalist.add(manga);
        }
        return mangalist;
    }
    public MangaSummary getDetailInfo(MangaInfo mangaInfo)throws Exception {
        MangaSummary summary = new MangaSummary(mangaInfo);
        ChaptersList chaptersList = new ChaptersList();
        Document doc  = Jsoup.connect("https://cnc.dm5.com/" + mangaInfo.path).get();
        Log.e("aaaa", "https://cnc.dm5.com/" + mangaInfo.path);
        //Document doc = Jsoup.parse(response.body());
        Elements tmc = doc.select("div.banner_detail_form");
        for (Element con : tmc) {
            Log.e("description", con.getElementsByAttributeValue("class", "content").text());
            summary.description = con.getElementsByAttributeValue("class", "content").text();
        }
        Elements elements = doc.select("div.left-bar").select("ul.view-win-list.detail-list-select").select("ul li a");//select必去加在外面 內圈才會逐一選取
        MangaChapter mangaChapter;
        for (Element co : elements) {
            mangaChapter = new MangaChapter();
            mangaChapter.name = co.text();
            mangaChapter.readLink = co.attr("href");
            chaptersList.add(mangaChapter);
        }
        summary.chapters = chaptersList;
        return summary;
    }
    public int getChapterPage(String chapterPath)throws Exception{
        String path ="https://www.dm5.com/"+chapterPath;
        Document doc = Jsoup.connect(path).get();
        String[] strings = doc.getElementsByTag("script").eq(9).toString().split("var");
        return Integer.parseInt(strings[9].split("=")[1].replace(";", "").trim());//DM5_CID
    }
    public chapterLink getChapterImageUrl(String chapterPath,String pag)throws Exception{
        //要送的資料 到 chapterfun.ashx
        chapterLink chapterLink = new chapterLink();
        String cid ;//DM5_CID
        String page=pag;//DM5_PAGE
        String key;//mkey=""
        String language="1";
        String gtk="6";
        String _cid;//DM5_CID
        String _mid;//DM5_MID
        String sign;//DM5_VIEWSIGN
        String _dt;//DM5_VIEWSIGN_DT
        // http://www.dm5.com/comic_Url/
        String path ="https://www.dm5.com/"+chapterPath;
        Document doc = Jsoup.connect(path).get();
        String[] strings = doc.getElementsByTag("script").eq(9).toString().split("var");
        //Log.e("length",String.valueOf(strings.length));
        _mid = strings[7].split("=")[1].replace(";", "").trim();//DM5_MID
        _cid = strings[8].split("=")[1].replace(";", "").trim();//DM5_CID
        cid=_cid;
        sign = strings[23].split("=")[1].replace("\"","").replace(";", "").trim();//DM5_VIEWSIGN
        _dt = strings[24].split("=")[1].split(";")[0].replace("\"","");//DM5_VIEWSIGN_DT
        //Log.e("_mid",_mid);Log.e("_cid",_cid);Log.e("sign",sign);Log.e("_dt",_dt);
        String doc_code="";
        for(int i =0;i<20;i++){
            Log.e("TIME:",String.valueOf(i));
            try {
                Thread.sleep(10);
                Document document = Jsoup.connect("https://www.dm5.com/" + chapterPath + "/" + "chapterfun.ashx").data("cid", cid, "page", page, "key", "", "language", language, "gtk", gtk, "_cid", _cid, "_mid", _mid, "_dt", _dt, "_sign", sign).
                                header("Referer", "http://www.dm5.com/"+chapterPath)
                                .get();
                if (document.text().length()!=0) {
                    doc_code = document.body().text();
                    //Log.e("1", doc_code);
                    break;
                }
            }catch (Exception e){

            }
        }
        decodejs d = new decodejs();
        Log.e("doc_code:", d.decode(doc_code));
        chapterLink.imUrl =d.decode(doc_code);
        chapterLink.Referer = "https://www.dm5.com/"+chapterPath+"-p"+page;
        return chapterLink;
    }
    class decodejs {
        public String decode(String url){
            JsUnpacker a = new JsUnpacker(url);
            String t =a.unpack();

            String[] strlist = t.split("var");
            String cid="";
            String key="";
            String pix="";
            String[] pvalue;

            cid =  strlist[1].split("=")[1].replace("\'", "").replace(";", "");
            key =  strlist[2].split("=")[1].replace("\'", "").replace(";", "");
            pix =  strlist[3].split("=")[1].replace("\'", "").replace(";", "").replace("\"", "");
            pvalue =  strlist[4].split(";")[0].split("=")[1].replace("/", "").replace("\"", "").replace("[", "").replace("]", "").split(",");
            return pix+"/"+pvalue[0]+"?"+"cid="+cid+"&"+"key="+key+"&uk=";
        }
    }
    class JsUnpacker {
        //AUTHOR NOT ME
        //DECODE THE DOC FROM chapterfun.ashx
        private String packedJS = null;
        /**
         * @param  packedJS javascript P.A.C.K.E.R. coded.
         *
         **/
        public JsUnpacker(String packedJS) {
            this.packedJS = packedJS;
        }
        /**
         * Unpack the javascript
         *
         * @return the javascript unpacked or null.
         *
         **/
        public String unpack() {
            String js = new String(packedJS);
            try {
                Pattern p = Pattern.compile("\\}\\s*\\('(.*)',\\s*(.*?),\\s*(\\d+),\\s*'(.*?)'\\.split\\('\\|'\\)", Pattern.DOTALL);
                Matcher m = p.matcher(js);
                if(m.find() && m.groupCount() == 4) {
                    String payload = m.group(1).replace("\\'", "'");
                    String radixStr = m.group(2);
                    String countStr = m.group(3);
                    String[] symtab = m.group(4).split("\\|");

                    int radix = 36;
                    int count = 0;
                    try {
                        radix = Integer.parseInt(radixStr);
                        System.out.println(radix);
                    } catch(Exception e) {
                    }
                    try {
                        count = Integer.parseInt(countStr);
                    } catch(Exception e) {
                    }

                    if(symtab.length != count) {
                        throw new Exception ("Unknown p.a.c.k.e.r. encoding");
                    }

                    Unbase unbase = new Unbase(radix);
                    p = Pattern.compile("\\b\\w+\\b");
                    m = p.matcher(payload);
                    StringBuilder decoded = new StringBuilder(payload);
                    int replaceOffset = 0;
                    while(m.find()) {
                        String word = m.group(0);

                        int x = unbase.unbase(word);
                        String value = null;
                        if(x < symtab.length) {
                            value = symtab[x];
                        }

                        if(value != null && value.length() > 0) {
                            decoded.replace(m.start() + replaceOffset, m.end() + replaceOffset, value);
                            replaceOffset += (value.length() - word.length());
                        }
                    }
                    return decoded.toString();
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private class Unbase {
            private final String ALPHABET_62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            private final String ALPHABET_95 = " !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
            private String alphabet = null;
            private HashMap<String, Integer> dictionnary = null;
            private int radix;

            Unbase(int radix) {
                this.radix = radix;

                if (radix > 36) {
                    if (radix < 62) {
                        alphabet = ALPHABET_62.substring(0, radix);
                    } else if (radix > 62 && radix < 95) {
                        alphabet = ALPHABET_95.substring(0, radix);
                    } else if (radix == 62) {
                        alphabet = ALPHABET_62;
                    } else if (radix == 95) {
                        alphabet = ALPHABET_95;
                    }

                    dictionnary = new HashMap<>(95);
                    for (int i = 0; i < alphabet.length(); i++) {
                        dictionnary.put(alphabet.substring(i, i + 1), i);
                    }
                }
            }

            int unbase(String str) {
                int ret = 0;

                if (alphabet == null) {
                    ret = Integer.parseInt(str, radix);
                } else {
                    String tmp = new StringBuilder(str).reverse().toString();
                    for (int i = 0; i < tmp.length(); i++) {
                        ret += Math.pow(radix, i) * dictionnary.get(tmp.substring(i, i + 1));
                    }
                }
                return ret;
            }
        }
    }


}

