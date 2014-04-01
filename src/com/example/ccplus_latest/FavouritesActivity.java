package com.example.ccplus_latest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavouritesActivity extends Activity implements OnItemClickListener {

	String[] names;
	String[] codes;
	String[] symbols;
	int[] images;
	ListView listView;
	TextView txtCounter;
	SparseBooleanArray checkedItems;
	int totalItemsCount;
	int selectedItemsCount;
	SharedPreferences prefs;
	CustomListAdapter ad;
	int j; //counts the number of items in the preferences file
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fav);
		findViews();

		ad = new CustomListAdapter(this, names, codes, symbols, images);
		listView.setAdapter(ad);
		listView.setFastScrollEnabled(true);

		selectedItemsCount = prefs.getAll().size();
		totalItemsCount = listView.getCount();
		txtCounter.setText("Currently Selected (" + selectedItemsCount + "/" + totalItemsCount + ")");

		listView.setOnItemClickListener(this);
		if (prefs.getAll().size() != 0){
			Map<String,?> prefsMap = prefs.getAll();
			for (int i=0;i<prefsMap.size();i++){
				listView.setItemChecked((Integer) prefsMap.get(i+""), true);
			}

		}
	}

	public void findViews(){
		codes = getResources().getStringArray(R.array.codes);
		images = new int[]{R.drawable.usa, R.drawable.eur, R.drawable.india, R.drawable.uk, R.drawable.aus, R.drawable.canada, R.drawable.swiss, R.drawable.china, R.drawable.japan, R.drawable.hungary, R.drawable.sweden, R.drawable.hongkong};
		names = getResources().getStringArray(R.array.names);
		symbols = getResources().getStringArray(R.array.symbols);
		listView = (ListView) findViewById(R.id.listView1);
		txtCounter = (TextView) findViewById(R.id.textViewCounter);
		prefs = getSharedPreferences("prefs", 0);
		actionBar = getActionBar();
	}

	@Override
	public void onItemClick(AdapterView<?> adv, View v, int pos, long arg3) {
		// TODO Auto-generated method stub
		updateCounter();
	}

	public void updateCounter(){
		totalItemsCount = listView.getCount();
		selectedItemsCount = listView.getCheckedItemCount();
		txtCounter.setText("Currently Selected (" + selectedItemsCount + "/" + totalItemsCount + ")");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu_fav, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.action_save){
			if (selectedItemsCount < 2){
				Toast.makeText(this, "Please select at least 2 currencies!", Toast.LENGTH_SHORT).show();
			}
			else {
				checkedItems = listView.getCheckedItemPositions();
				j=0;
				prefs.edit().clear().commit();
				for (int i=0;i<totalItemsCount;i++){
					if (checkedItems.get(i)){
						prefs.edit().putInt(j+"", i).commit();
						j++;
					}		
				}
				Toast.makeText(this, "Changes have been applied!", Toast.LENGTH_SHORT).show();
				Intent applyChanges = new Intent(this, MainActivity.class);
				MainActivity.mainActivity.finish();
				startActivity(applyChanges);
			}
		}
		if (item.getItemId() == R.id.action_reset){
			Toast.makeText(this, "Favourites have been reset!", Toast.LENGTH_SHORT).show();
			listView.clearChoices();
			ad.notifyDataSetChanged();
			updateCounter();
		}
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}



//----------------------------------------------------------------------------------------------------------------


class CustomListAdapter extends BaseAdapter{
	ArrayList<Currency> list = new ArrayList<Currency>();
	Context c;

	public CustomListAdapter(Context c, String[] names, String[] codes, String[] symbols, int[] images){
		this.c = c;
		for (int i=0;i<codes.length;i++){
			list.add(new Currency(names[i], codes[i], symbols[i], images[i]));
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup container) {
		// TODO Auto-generated method stub
		View row = convertView;
		if (row == null){
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.single_row_listview, container, false);
		}
		TextView txtName = (TextView) row.findViewById(R.id.textView3NAME);
		TextView txtCode = (TextView) row.findViewById(R.id.textView1CODE);
		TextView txtSym = (TextView) row.findViewById(R.id.textView2SYM);
		ImageView img = (ImageView) row.findViewById(R.id.imageView1);

		txtName.setText(list.get(pos).name);
		txtCode.setText(list.get(pos).code);
		txtSym.setText(list.get(pos).symbol);
		img.setImageResource(list.get(pos).imgRes);

		return row;
	}

}
