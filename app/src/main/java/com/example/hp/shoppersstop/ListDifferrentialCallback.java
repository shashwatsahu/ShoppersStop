package com.example.hp.shoppersstop;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by hp on 20-03-2018.
 */

public class ListDifferrentialCallback extends DiffUtil.Callback {


    private List<ListItem> mNewList;
    private List<ListItem> mOldList;

    public ListDifferrentialCallback(List<ListItem> mOldList, List<ListItem> mNewList){
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getName() == mNewList.get(newItemPosition).getName();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ListItem oldData = mOldList.get(oldItemPosition);
        ListItem newData = mNewList.get(newItemPosition);

        if(oldData.getName() == newData.getName() && oldData.getWeight() == newData.getWeight()){
            return true;
        }
        return false;
    }}
