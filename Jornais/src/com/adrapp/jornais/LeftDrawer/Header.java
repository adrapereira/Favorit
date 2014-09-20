package com.adrapp.jornais.LeftDrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.adrapp.jofav.R;
import com.adrapp.jornais.LeftDrawer.TwoTextArrayAdapter.RowType;

public class Header implements Item {
	private final String         name;
	private final LayoutInflater inflat;
	private final boolean ordenar;

	public Header(LayoutInflater inflater, String name) {
		this.name = name;
		this.inflat = inflater;
		this.ordenar = false;
	}

	public Header(LayoutInflater inflater, String name, boolean ord) {
		this.name = name;
		this.inflat = inflater;
		this.ordenar = ord;
	}

	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			if(ordenar)
				view = inflater.inflate(R.layout.header_handle_right, null);
			else view = inflater.inflate(R.layout.header, null);
			// Do some initialization
		} else {
			view = convertView;
		}

		TextView text;
		if(ordenar)
			text = (TextView) view.findViewById(R.id.text);
		else text = (TextView) view.findViewById(R.id.separator);
		
		text.setText(name);

		return view;
	}

	@Override
	public Item clone(){
		return new Header(inflat, name, ordenar);
	}
	
	@Override
	public String getSite(){
		return null;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("h ");
		sb.append(name);
		sb.append("\n");
		
		return sb.toString();
	}
	
}