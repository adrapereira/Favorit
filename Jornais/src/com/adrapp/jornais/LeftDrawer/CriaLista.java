package com.adrapp.jornais.LeftDrawer;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;

public class CriaLista {
	private final ArrayList<Item> items;

	public CriaLista(Context context){
		ParserLista parser = new ParserLista(context);
		String[] aux;
		items = new ArrayList<Item>();
		for(int i = 0; i < parser.sizeLinhas(); i++){
			aux = parser.parseLinha(i);

			if(aux != null)
				if(aux[0].equals("h")){ //se for um header
					items.add(new Header(LayoutInflater.from(context), aux[1]));
				}else items.add(new ListItem(context, aux[1], aux[2], false)); //se for um elemento da lista

		}
	}

	public CriaLista(Context context, boolean ord){
		ParserLista parser = new ParserLista(context);
		String[] aux;
		items = new ArrayList<Item>();
		for(int i = 0; i < parser.sizeLinhas(); i++){
			aux = parser.parseLinha(i);

			if(aux != null)
				if(aux[0].equals("h")){ //se for um header
					items.add(new Header(LayoutInflater.from(context), aux[1], ord));
				}else items.add(new ListItem(context, aux[1], aux[2], ord)); //se for um elemento da lista

		}
	}

	public ArrayList<Item> getItems(){
		return items;
	}

}
