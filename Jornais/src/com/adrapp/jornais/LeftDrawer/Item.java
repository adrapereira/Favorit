package com.adrapp.jornais.LeftDrawer;

import android.view.LayoutInflater;
import android.view.View;

public interface Item {
	public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
    
    public Item clone();
    public String getSite();
}
