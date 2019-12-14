package com.example.myapplication.Providers.Chapter;

import java.util.ArrayList;

public class ChaptersList extends ArrayList<MangaChapter> {
    public MangaChapter getByNumber(int number) {
        for (MangaChapter o : this) {
            if (o != null && o.ch_number == number) {
                return o;
            }
        }
        return null;
    }
}
