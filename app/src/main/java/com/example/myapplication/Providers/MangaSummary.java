package com.example.myapplication.Providers;

import com.example.myapplication.Providers.Chapter.ChaptersList;
import com.example.myapplication.Providers.Page.MangaInfo;

public class MangaSummary extends MangaInfo {
    public String description;
        public ChaptersList chapters;
    public MangaSummary(MangaInfo mangaInfo){
            this.name = mangaInfo.name;
            this.path = mangaInfo.path;
            this.preview =mangaInfo.preview;
            this.status = mangaInfo.status;
            this.chapters = new ChaptersList();
    }
}
