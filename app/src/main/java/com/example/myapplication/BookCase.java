package com.example.myapplication;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import com.example.myapplication.Providers.Page.MangaInfo;
public class  BookCase
{



    private File f  = null;

    public BookCase()
    {

        //String path = Environment.getExternalStorageDirectory() + "/" + "MyFirstApp/";
        File dir = new File(Environment.getExternalStorageDirectory() + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fullName = Environment.getExternalStorageDirectory() + "/" + "BookCase.txt";
       f = new File (fullName);
       try
       {
           if(!f.exists()) {
               f.createNewFile();
               Log.e("AAA","aa");
           }
           }catch (IOException e )
       {
           e.printStackTrace();
       }
        System.out.println(f.getAbsolutePath());
    }
    public Boolean isInBookList (MangaInfo mangaInfo) throws IOException {
        ArrayList<MangaInfo> bookList = new ArrayList<>();
        bookList = this.readFileURL();
        for(int i = 0;i<bookList.size();i++)
        {
            if(bookList.get(i).name.equals(mangaInfo.name))
            {
                System.out.println("get URL" + mangaInfo.name);
                return true;
            }
        }
        return false;
    }


    public ArrayList<MangaInfo> readFileURL() throws IOException {

        FileReader fr = null;
        BufferedReader br =null;
        ArrayList<MangaInfo> bookList = new ArrayList<>();
        String line;



            fr = new FileReader(f);
            br =new BufferedReader(fr );
        while ((line = br.readLine()) != null)
        {
            MangaInfo mangaInfo = new MangaInfo();
            mangaInfo.name = line.split(";")[0];
            mangaInfo.path= line.split(";")[1];
            mangaInfo.imageUrl = line.split(";")[2];

            Log.e("BookCASE",mangaInfo.name+mangaInfo.path);
            bookList.add(mangaInfo);
        }

        for (MangaInfo mI:bookList) {
            Log.e("BookCASE",mI.name+mI.path);
        }
        fr.close();
        br.close();
        return bookList;
    }
    public void writeFileURL(MangaInfo mangaInfo) throws IOException
    {
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
        }catch (FileNotFoundException e )
        {
            f.createNewFile();
            fw = new FileWriter(f, true);
        }

        PrintWriter pw = new PrintWriter(fw);
        pw.print(mangaInfo.name+";");
        pw.print(mangaInfo.path+";");
        pw.print(mangaInfo.imageUrl+";\n");

        pw.close();
        fw.close();

    }




    public void RemoveFileURL(MangaInfo mangaInfo) throws IOException
    {
        ArrayList<MangaInfo> bookList = new ArrayList<>();
        bookList = this.readFileURL();
        for(int i = 0;i<bookList.size();i++)
        {
            if(bookList.get(i).name.equals(mangaInfo.name))
            {
                System.out.println("get URL" + mangaInfo.name);
                bookList.remove(i);
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(f, false);
        }catch (FileNotFoundException e )
        {
            f.createNewFile();
            fw = new FileWriter(f, false);
        }
        PrintWriter pw = new PrintWriter(fw);
        for(int i = 0;i<bookList.size();i++)
        {
            pw.print(mangaInfo.name+";");
            pw.print(mangaInfo.path+";");
            pw.print(mangaInfo.imageUrl+";\n");
        }
        pw.close();
        fw.close();

    }






}