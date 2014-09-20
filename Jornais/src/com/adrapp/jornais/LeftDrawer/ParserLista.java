package com.adrapp.jornais.LeftDrawer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class ParserLista {
	private static final String nomeFicheiro = "drawerLinks.jrn";
	private final ArrayList<String> linhas;

	public ParserLista(Context context){
		linhas = new ArrayList<String>();

		lerFicheiroMemInterna(context);
	}

	public String[] parseLinha(int index){
		String linha = "", aux;
		String[] res;
		if(index < linhas.size())
			linha = linhas.get(index);

		res = linha.split("#%#");
		
		if(linha.length() <= 2 || linha == null ) return null;

		if(res.length == 2){
			String[] res2 = new String[3];
			aux = res[1];	//guardar o link
			res2[1] = res[0].substring(2); //guardar apenas o nome
			res2[0] = "" + res[0].charAt(0); //guardar apenas a classificaçao do item (header "h" ou link "l")
			res2[2] = aux; //adicionar o link ao array
			res = res2;
		}else {
			String[] res2 = new String[2];
			res2[1] = linha.substring(2); //guardar apenas o nome
			res2[0] = "" + linha.charAt(0); //guardar apenas a classificaçao do item (header "h" ou link "l")
			res = res2;
		}

		return res;
	}

	public String getSite(int i){
		String[] linha = parseLinha(i);
		if(linha.length == 3)
			return linha[2];
		else return null;
	}

	public int sizeLinhas(){
		return linhas.size();
	}

	private void lerFicheiro(Context context) {
		AssetManager am = context.getAssets();
		BufferedReader fichScan = null;
		String linha;
		try {
			InputStream is = am.open(nomeFicheiro);
			fichScan = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
			while ((linha = fichScan.readLine()) != null){
				linhas.add(linha);
			}
			fichScan.close();
		} catch (IOException e) {
			Log.d("Ler Ficheiro - ParserLista", e.getMessage());
			return;
		} catch (Exception e) {
			return;
		}
	}

	private void lerFicheiroMemInterna(Context context) {
		BufferedReader fichScan = null;
		String linha;
		try {
			FileInputStream fos = context.openFileInput(nomeFicheiro);
			fichScan = new BufferedReader(new InputStreamReader(fos, "ISO-8859-1"));
			while ((linha = fichScan.readLine()) != null){
				linhas.add(linha);
			}
			fichScan.close();
		} catch (IOException e) {
			Log.d("Ler Ficheiro - ParserLista", e.getMessage());
			return;
		} catch (Exception e) {
			return;
		}
	}

}
