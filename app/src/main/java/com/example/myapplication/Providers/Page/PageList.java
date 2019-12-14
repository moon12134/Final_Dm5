package com.example.myapplication.Providers.Page;

import java.util.ArrayList;

public class PageList<T> extends ArrayList<T> {
    private int mPages = 0;
    private boolean mHasNext;

    public void appendPage(PageList<T> list) {
        mPages++;
        this.addAll(list);
    }

    @Override
    public void clear() {
        super.clear();
        mPages = 0;
    }

    public int getPagesCount() {
        return mPages;
    }

    public boolean isHasNext() {
        return mHasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.mHasNext = hasNext;
    }

    public void setPagesCount(int count) {
        mPages = Math.max(count, 0);
    }
}
