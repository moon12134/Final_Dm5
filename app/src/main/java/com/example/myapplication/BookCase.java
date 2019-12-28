package com.example.myapplication;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class  BookCase
{



    private File f  = null;

    public BookCase()
    {
        String path = Environment.getExternalStorageDirectory() + "/" + "MyFirstApp/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fullName = path + "BookCase.txt";
       f = new File (fullName);
        System.out.println(f.getAbsolutePath());
    }
    public Boolean isInBookList (String url) throws IOException {
        ArrayList<String> bookList = new ArrayList<>();
        bookList = this.readFileURL();
        for(int i = 0;i<bookList.size();i++)
        {
            System.out.println(url);
            if(bookList.get(i).equals(url))
            {
                System.out.println("get URL" + url);
                return true;
            }
        }
        return false;
    }
    public ArrayList<String> readFileURL() throws IOException {

        FileReader fr = null;
        BufferedReader br =null;

        ArrayList<String> bookList = new ArrayList<>();
        String line;
        try {
            fr = new FileReader(f);
            br =new BufferedReader(fr );
        }catch(FileNotFoundException e ){
            f.createNewFile();
            fr = new FileReader(f);
            br =new BufferedReader(fr );
        }

        while ((line = br.readLine()) != null)
        {
            bookList.add(line);
        }

        fr.close();
        br.close();
        return bookList;
    }
    public void writeFileURL(String write_str) throws IOException
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
        pw.print(write_str+"\n");
        pw.close();
        fw.close();

    }
    public void RemoveFileURL(String write_str) throws IOException
    {
        ArrayList<String> fr = this.readFileURL();
        String temp;
        for(int i = 0;i<fr.size();i++)
        {
            System.out.println(write_str);
            if(fr.get(i).equals(write_str))
            {
                System.out.println("get URL" + write_str);
                fr.remove(i);
                break;
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
        for(int i = 0;i<fr.size();i++)
        {
            pw.println(fr.get(i));
        }
        pw.close();
        fw.close();

    }






}