package com.adrapp.jornais.LeftDrawer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TwoTextArrayAdapter extends ArrayAdapter<Item> {
    private final LayoutInflater mInflater;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public TwoTextArrayAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
    }

    public TwoTextArrayAdapter(Context context, int id, List<Item> items) {
        super(context, id, items);
        mInflater = LayoutInflater.from(context);
    }

    
    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
    
    @Override
    public void remove(Item object) {
    	super.remove(object);
    }
    
    @Override
    public void insert(Item object, int index) {
    	super.insert(object, index);
    }
    
    @Override
    public Item getItem(int position) {
    	return super.getItem(position);
    }
    
    @Override
	public String toString(){
    	StringBuilder sb = new StringBuilder();
    	
    	for(int i = 0; i < this.getCount(); i++){
    		Item it = getItem(i);
    		sb.append(it.toString());
    	}
    	
    	return sb.toString();
    }
    
}
