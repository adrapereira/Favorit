package com.adrapp.jornais.LeftDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.adrapp.jofav.R;
import com.adrapp.jornais.LeftDrawer.TwoTextArrayAdapter.RowType;

public class ListItem implements Item {
	private final String titulo, site;
	private final Context cont;
	private final boolean ordenar;

	public ListItem(Context context, String text) {
		this.titulo = text;
		this.site = "";
		this.cont = context;
		this.ordenar = false;
	}

	public ListItem(Context context, String text, boolean ord) {
		this.titulo = text;
		this.site = "";
		this.cont = context;
		this.ordenar = ord;
	}

	public ListItem(Context context, String text, String end, boolean ord) {
		this.titulo = text;
		this.site = end;
		this.cont = context;
		this.ordenar = ord;
	}
	
	@Override
	public String getSite(){
		return site;
	}
	
	@Override
	public int getViewType() {
		return RowType.LIST_ITEM.ordinal();
	}
	
	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			if(ordenar)
				view = inflater.inflate(R.layout.list_item_handle_right, null);
			else view = inflater.inflate(R.layout.my_list_item, null);
			// Do some initialization
		} else {
			view = convertView;
		}
		TextView text;
		if(ordenar)
			text = (TextView) view.findViewById(R.id.text);
		else text = (TextView) view.findViewById(R.id.list_text);
		text.setText(titulo);

		return view;
	}

	@Override
	public Item clone(){
		return new ListItem(cont, titulo, ordenar);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		sb.append("l ");
		sb.append(this.titulo);
		sb.append("#%#");
		sb.append(this.site);
		sb.append("\n");
		
		return sb.toString();
	}
}

