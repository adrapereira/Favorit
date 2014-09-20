package com.adrapp.jornais.DragAndDropList;

import java.util.Stack;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adrapp.jofav.R;
import com.adrapp.jornais.LeftDrawer.CriaLista;
import com.adrapp.jornais.LeftDrawer.Item;
import com.adrapp.jornais.LeftDrawer.TwoTextArrayAdapter;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;


public class DSLVFragment extends ListFragment {

	private TwoTextArrayAdapter adapter;

	private final DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				Log.d("from - to", "From = " + from  + " ;To = " + to);
				Item item = adapter.getItem(from);
				if(item.getClass().getSimpleName().equals("Header")){
					reposicionarSubLista(from, to);
				}else{
					adapter.remove(item);
					adapter.insert(item, to);
				}
			}
		}
	};

	private void reposicionarSubLista(int from, int to){
		if(!passaPorOutroHeader(from, to)) return;
		
		to = novoTo(to);
		
		Stack<Item> listaAux = new Stack<Item>();

		//reposicionar o header
		Item itemHeader = adapter.getItem(from); 
		from++;
		
		Item item;
		//reposicionar o resto dos itens dessa sub-lista
		while(from < adapter.getCount() && !(item = adapter.getItem(from)).getClass().getSimpleName().equals("Header") ){
			listaAux.push(item);
			from++;
		}
		
		try {
			while((item = listaAux.pop()) != null){
				adapter.remove(item);
				if(to > adapter.getCount())
					adapter.insert(item, adapter.getCount());
				else adapter.insert(item, to);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		adapter.remove(itemHeader);
		if(to > adapter.getCount())
			adapter.insert(itemHeader, adapter.getCount());
		else adapter.insert(itemHeader, to);
		
		adapter.notifyDataSetChanged();
	}

	private boolean passaPorOutroHeader(int from, int to){
		boolean res = false;
		int valor;
		if(from < to) valor = 1;
		else valor = -1;

		from += valor;

		while(!res && from < adapter.getCount() && from >= 0){
			if(adapter.getItem(from).getClass().getSimpleName().equals("Header"))
				res = true;

			if(valor == -1){
				if(from < to +1) break;
			}else if(from > to - 1) break;
			
			from += valor;
		}
		Log.d("passa", "Passa: " + res + "\n" + "from = " + from );
		return res;
	}

	/*retorna o novo indice da lista, onde se começa a colocar o "header + sites"
	 * itera até encontrar um header ou o fim da lista
	 * */
	private int novoTo(int to){
		Log.d("passa", "Começar novo To" );
		while(to < adapter.getCount()){
			Log.d("passa", "To = " + to );
			if(adapter.getItem(to).getClass().getSimpleName().equals("Header")) break;
			to++;
		}
		Log.d("passa", "To final = " + to );
		return to;
	}
	
	private final DragSortListView.RemoveListener onRemove = 
			new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			adapter.remove(adapter.getItem(which));
		}
	};

	protected int getLayout() {
		// this DSLV xml declaration does not call for the use
		// of the default DragSortController; therefore,
		// DSLVFragment has a buildController() method.
		return R.layout.dslv_fragment_main;
	}

	/** 
	 * Return list item layout resource passed to the ArrayAdapter.
	 */
	protected int getItemLayout() {
		return R.layout.list_item_handle_right;
	}

	private DragSortListView mDslv;
	private DragSortController mController;

	public int dragStartMode = DragSortController.ON_DOWN;
	public boolean removeEnabled = false;
	public int removeMode = DragSortController.FLING_REMOVE;
	public boolean sortEnabled = true;
	public boolean dragEnabled = true;

	public static DSLVFragment newInstance() {
		return new DSLVFragment();
	}

	public DragSortController getController() {
		return mController;
	}

	/**
	 * Called from DSLVFragment.onActivityCreated(). Override to
	 * set a different adapter.
	 */
	public void setListAdapter() {
		CriaLista list = new CriaLista(getActivity(), true);

		adapter = new TwoTextArrayAdapter(getActivity(), R.id.text, list.getItems());
		setListAdapter(adapter);
	}

	/**
	 * Called in onCreateView. Override this to provide a custom
	 * DragSortController.
	 */
	public DragSortController buildController(DragSortListView dslv) {
		DragSortController controller = new DragSortController(dslv);
		controller.setDragHandleId(R.id.drag_handle);
		controller.setRemoveEnabled(removeEnabled);
		controller.setSortEnabled(sortEnabled);
		controller.setDragInitMode(dragStartMode);
		controller.setRemoveMode(removeMode);
		controller.setBackgroundColor(getResources().getColor(R.color.blue));
		return controller;
	}


	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDslv = (DragSortListView) inflater.inflate(getLayout(), container, false);

		mController = buildController(mDslv);
		mDslv.setFloatViewManager(mController);
		mDslv.setOnTouchListener(mController);
		mDslv.setDragEnabled(dragEnabled);

		return mDslv;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mDslv = (DragSortListView) getListView(); 

		mDslv.setDropListener(onDrop);
		mDslv.setRemoveListener(onRemove);

		setListAdapter();
	}

	public TwoTextArrayAdapter getAdapter(){
		return adapter;
	}
}
