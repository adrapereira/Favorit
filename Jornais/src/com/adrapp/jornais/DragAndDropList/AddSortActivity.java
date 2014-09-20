package com.adrapp.jornais.DragAndDropList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.adrapp.jofav.R;
import com.adrapp.jornais.LeftDrawer.Header;
import com.adrapp.jornais.LeftDrawer.ListItem;
import com.adrapp.jornais.LeftDrawer.TwoTextArrayAdapter;
import com.mobeta.android.dslv.DragSortController;

public class AddSortActivity extends SherlockFragmentActivity{

	private final int mDragStartMode = DragSortController.ON_DOWN;
	private final boolean mRemoveEnabled = true;
	private final int mRemoveMode = DragSortController.FLING_REMOVE;
	private final boolean mSortEnabled = true;
	private final boolean mDragEnabled = true;

	private DSLVFragment frag;
	private EditText textTitulo, textEndereco;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_bed_main);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		frag = getNewDslvFragment();

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.test_bed, frag).commit();
		}
	}

	private DSLVFragment getNewDslvFragment() {
		DSLVFragment f = DSLVFragment.newInstance();
		f.removeMode = mRemoveMode;
		f.removeEnabled = mRemoveEnabled;
		f.dragStartMode = mDragStartMode;
		f.sortEnabled = mSortEnabled;
		f.dragEnabled = mDragEnabled;
		return f;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_edit_sites, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void guardaSites(){
		FileOutputStream fos = null;
		PrintStream pStream = null;
		try {
			fos = this.openFileOutput("drawerLinks.jrn", Context.MODE_PRIVATE);
			pStream = new PrintStream(fos, true, "ISO-8859-1");

			if (pStream != null) {
				pStream.println (getTextAdapter().toString());
				pStream.close();
				Log.d("Gravacao", "Save guardado em memoria interna!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action buttons
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_new:
			mostrarAddSiteDialog();
			return true;
		case R.id.action_save:
			guardaSites();
			setResult(RESULT_OK);

			Toast toast = Toast.makeText(this, "Guardado!", Toast.LENGTH_SHORT);
			toast.show();

			//			finish();
			return true;
		default:
			return true;
		}
	}

	private void mostrarAddSiteDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();

		final Activity act = this;

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View dialogView = inflater.inflate(R.layout.add_site_dialog, null);

		builder.setView(dialogView);
		textTitulo = (EditText)dialogView.findViewById(R.id.nomeSite);
		textEndereco = (EditText)dialogView.findViewById(R.id.site);

		// Add action buttons
		builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				String nome = "", site = "";
				if(textTitulo != null){
					nome = textTitulo.getText().toString();

					Locale loc = new Locale("pt", "PT");
					nome = nome.toUpperCase(loc);
				}

				if(textEndereco != null){
					String aux = textEndereco.getText().toString();
					if(!aux.equals("")){
						site = "http://";
						site = site.concat(aux);
					}
				}

				// Criar o novo elemento e adiciona-lo ao adapter
				if(site == null || site.equals("")){ // se o endereço estiver vazio
					Header head = new Header(getLayoutInflater(), nome, true);
					getTextAdapter().add(head);
				}else{
					ListItem item = new ListItem(act, nome, site, true);
					getTextAdapter().add(item);
				}
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			}
		});      

		builder.create();
		builder.show();
	}

	public TwoTextArrayAdapter getTextAdapter(){
		return frag.getAdapter();
	}

	@Override
	public void onBackPressed() {
		//imprimir a lista de sites para o ficheiro em memoria interna
		super.onBackPressed();
	}
}
