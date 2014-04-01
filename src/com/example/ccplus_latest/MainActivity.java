package com.example.ccplus_latest;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemSelectedListener, TextWatcher{
	public static MainActivity mainActivity;
	public static ProgressDialog progressDialog;
	public static ProgressDialog progressStart;
	EditText etInput;
	TextView tvInputSym;
	Spinner spin1;
	Spinner spin2;
	CustomSpinnerAdapter ad1;
	CustomSpinnerAdapter ad2;

	//LinearLayouOne
	LinearLayout layout1;
	TextView tvOuput;
	TextView tvInput;
	ImageView flag1;
	ImageView flag2;
	ImageView swap;

	//LinearLayouTwo
	LinearLayout layout2;
	TextView tvOuputS;
	TextView tvInputS;
	ImageView flag1S;
	ImageView flag2S;
	ImageView swapS;

	TextView tvFromTo;
	TextView tvTime;
	TextView tvOneRate;	
	String oneRate;

	SharedPreferences prefs;
	String[] names;
	String[] codes;
	String[] symbols;
	int[] images;
	ArrayList<Currency> selectedCursList = new ArrayList<Currency>();

	//extras
	int selectionFrom = 0;
	int selectionTo = 0;
	String from = "USD";	
	String to = "INR";
	String fromSym = "$";
	String toSym = "₹";
	URL url;
	URL url2;
	Parser p;
	double rate;
	double rateRev;
	double output;
	double outputRev;
	double input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_converter_activity);
		findViews();

		if(!isConnectedToInternet()){
			DialogNetworkCheck dialog = new DialogNetworkCheck();
			dialog.show(getFragmentManager(), "dialog");
		}
		
		progressStart = progressStart.show(this, "Please Wait", "Loading...", true);

		if (prefs.getAll().size() != 0){
			Log.d("TEST", "prefs detected");
			Map<String,?> prefsMap = prefs.getAll();
			for (int i=0;i<prefsMap.size();i++){
				selectedCursList.add(new Currency(names[(Integer) prefsMap.get(i+"")], codes[(Integer) prefsMap.get(i+"")], symbols[(Integer) prefsMap.get(i+"")], images[(Integer) prefsMap.get(i+"")]));
			}
			ad1 = new CustomSpinnerAdapter(this, selectedCursList);
			ad2 = new CustomSpinnerAdapter(this, selectedCursList);
			spin1.setAdapter(ad1);
			spin2.setAdapter(ad2);
			spin1.setSelection(0);
			spin2.setSelection(1);

		}
		else {
			for (int i=0;i<codes.length;i++){
				selectedCursList.add(new Currency(names[i], codes[i], symbols[i], images[i]));
			}
			ad1 = new CustomSpinnerAdapter(this, selectedCursList);
			ad2 = new CustomSpinnerAdapter(this, selectedCursList);
			spin1.setAdapter(ad1);
			spin2.setAdapter(ad2);
			spin1.setSelection(0);
			spin2.setSelection(1);

		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		tvTime.setText("(Last updated on " + sdf.format(date) + ")");

		//		set action listeners
		etInput.addTextChangedListener(this);
		spin1.setOnItemSelectedListener(this);
		spin2.setOnItemSelectedListener(this);
		swap.setOnClickListener(this);
		swapS.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	public void findViews(){
		mainActivity = this;

		etInput = (EditText) findViewById(R.id.editTextInput);
		tvInputSym = (TextView) findViewById(R.id.textViewSymbolINPUT);
		spin1 = (Spinner) findViewById(R.id.spinner1);
		spin2 = (Spinner) findViewById(R.id.spinner2);
		prefs = getSharedPreferences("prefs", 0);
		codes = getResources().getStringArray(R.array.codes);
		images = new int[]{R.drawable.usa, R.drawable.eur, R.drawable.india, R.drawable.uk, R.drawable.aus, R.drawable.canada, R.drawable.swiss, R.drawable.china, R.drawable.japan, R.drawable.hungary, R.drawable.sweden, R.drawable.hongkong};
		names = getResources().getStringArray(R.array.names);
		symbols = getResources().getStringArray(R.array.symbols);

		tvOuput = (TextView) findViewById(R.id.textViewOutput);
		tvInput =  (TextView) findViewById(R.id.textViewInput);
		flag1 = (ImageView) findViewById(R.id.imageViewFlag1);
		flag2 = (ImageView) findViewById(R.id.imageViewFlag2);
		swap = (ImageView) findViewById(R.id.imageViewCenterPic);

		tvOuputS = (TextView) findViewById(R.id.textViewOutputS);
		tvInputS =  (TextView) findViewById(R.id.textViewInputS);
		flag1S = (ImageView) findViewById(R.id.imageViewFlag1S);
		flag2S = (ImageView) findViewById(R.id.imageViewFlag2S);
		swapS = (ImageView) findViewById(R.id.imageViewCenterPicS);

		layout1 = (LinearLayout) findViewById(R.id.linearLayoutOne);
		layout2 = (LinearLayout) findViewById(R.id.linearLayoutTwo);

		tvFromTo = (TextView) findViewById(R.id.textViewFromTo);
		tvTime = (TextView) findViewById(R.id.textViewTime);
		tvOneRate = (TextView) findViewById(R.id.textViewUnitRate);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.action_favs){
			Intent goToFavs = new Intent(this, FavouritesActivity.class);
			startActivity(goToFavs);
		}
		if (item.getItemId() == R.id.action_charts){
			Intent goToCharts = new Intent(this, ChartsActivity.class);
			progressDialog = ProgressDialog.show(MainActivity.mainActivity, "Please Wait", "Loading...");
			goToCharts.putExtra("from", from);
			goToCharts.putExtra("to", to);
			goToCharts.putExtra("unit rate", oneRate);
			startActivity(goToCharts);
		}
		if (item.getItemId() == R.id.action_about){
			FragmentAbout about = new FragmentAbout();
			about.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Dialog_MinWidth);
			about.show(getFragmentManager(), "about");
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.imageViewCenterPic){
			tvInputSym.setText(toSym);
			layout1.setVisibility(LinearLayout.GONE);
			layout2.setVisibility(LinearLayout.VISIBLE);
		}

		if (v.getId() == R.id.imageViewCenterPicS){
			tvInputSym.setText(fromSym);
			layout1.setVisibility(LinearLayout.VISIBLE);
			layout2.setVisibility(LinearLayout.GONE);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> adView, View v, int i,
			long arg3) {
		// TODO Auto-generated method stub
		if (adView.getId() == R.id.spinner1){
			layout1.setVisibility(LinearLayout.VISIBLE);
			layout2.setVisibility(LinearLayout.GONE);

			flag1.setImageResource(selectedCursList.get(i).imgRes);
			flag2S.setImageResource(selectedCursList.get(i).imgRes);
			this.selectionFrom = i;
			from = selectedCursList.get(i).code;
			fromSym = selectedCursList.get(i).symbol;
			flag1.setImageResource(selectedCursList.get(i).imgRes);
			flag2S.setImageResource(selectedCursList.get(i).imgRes);
			tvInput.setText(selectedCursList.get(selectionFrom).symbol + " 0.00");
			tvOuput.setText(selectedCursList.get(selectionTo).symbol + " 0.00");
			tvInputS.setText(selectedCursList.get(selectionTo).symbol + " 0.00");
			tvOuputS.setText(selectedCursList.get(selectionTo).symbol + " 0.00");
			tvInputSym.setText(fromSym);
			etInput.setText("");

			try {
				String urlString = "http://rate-exchange.appspot.com/currency?from=" + from + "&to=" + to; 
				String urlString2 = "http://rate-exchange.appspot.com/currency?from=" + to + "&to=" + from; 
				url = new URL(urlString);
				url2 = new URL(urlString2);
				Double[] rates = new Background().execute(new URL[]{url, url2}).get(); 
				rate = rates[0];
				rateRev = rates[1];

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tvFromTo.setText(from + "/" + to);
			oneRate = "Unit Rate: " + "("+fromSym+")" + "1" + " = " + "(" + toSym + ")"+ rate;
			tvOneRate.setText(oneRate);

		}
		if (adView.getId() == R.id.spinner2){
			layout1.setVisibility(LinearLayout.VISIBLE);
			layout2.setVisibility(LinearLayout.GONE);

			flag2.setImageResource(selectedCursList.get(i).imgRes);
			flag1S.setImageResource(selectedCursList.get(i).imgRes);
			this.selectionTo = i;
			to = selectedCursList.get(i).code;
			toSym = selectedCursList.get(i).symbol;
			tvInput.setText(selectedCursList.get(selectionFrom).symbol + " 0");
			tvOuput.setText(selectedCursList.get(selectionTo).symbol + " 0");
			tvInputS.setText(selectedCursList.get(selectionFrom).symbol + " 0");
			tvOuputS.setText(selectedCursList.get(selectionTo).symbol + " 0");
			etInput.setText("");

			try {
				url = new URL("http://rate-exchange.appspot.com/currency?from=" + from + "&to=" + to);
				url2 = new URL("http://rate-exchange.appspot.com/currency?from=" + to + "&to=" + from);
				Double[] rates = new Background().execute(new URL[]{url, url2}).get(); 
				rate = rates[0];
				rateRev = rates[1];

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tvFromTo.setText(from + "/" + to);
			oneRate = "Unit Rate: " + "("+fromSym+")" + "1" + " = " + "(" + toSym + ")"+ rate;
			tvOneRate.setText(oneRate);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public boolean isConnectedToInternet(){
		boolean mobile = false;
		boolean wifi = false;
		ConnectivityManager manager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo[] allInfo = manager.getAllNetworkInfo();

		for (NetworkInfo info : allInfo){
			if (info.getType() == ConnectivityManager.TYPE_MOBILE && info.isConnected()){
				mobile = true;
			}
			if (info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected()){
				wifi = true;
			}
		}
		return mobile || wifi;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (!isConnectedToInternet()){
			DialogNetworkCheck dialog = new DialogNetworkCheck();
			dialog.show(getFragmentManager(), "dialog");
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (s.length() == 0){
			//do nothing
			output = 0;
			input = 0;
			outputRev = 0;
			String help = (String.format("%.2f", output));
			String help2 = (String.format("%.2f", input));
			String helpS = (String.format("%.2f", outputRev));

			tvOuput.setText(selectedCursList.get(selectionTo).symbol + " "+ help);
			tvInput.setText(selectedCursList.get(selectionFrom).symbol + " " + help2);

			tvOuputS.setText(selectedCursList.get(selectionFrom).symbol + " "+ helpS);
			tvInputS.setText(selectedCursList.get(selectionTo).symbol + " " + help2);
		}
		else {
			output = Double.parseDouble(s.toString()) * rate;
			input = Double.parseDouble(s.toString());
			outputRev = Double.parseDouble(s.toString()) * rateRev;

			String help = (String.format("%.2f", output));
			String help2 = (String.format("%.2f", input));
			String helpS = (String.format("%.2f", outputRev));

			tvOuput.setText(selectedCursList.get(selectionTo).symbol + " "+ help);
			tvInput.setText(selectedCursList.get(selectionFrom).symbol + " " + help2);

			tvOuputS.setText(selectedCursList.get(selectionFrom).symbol + " "+ helpS);
			tvInputS.setText(selectedCursList.get(selectionTo).symbol + " " + help2);
		}

	}
}



class CustomSpinnerAdapter extends BaseAdapter{

	ArrayList<Currency> list;
	Context c;
	public CustomSpinnerAdapter(Context c, ArrayList<Currency> list){
		this.c = c;
		this.list = list;
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
		txtSym.setText("(" + list.get(pos).symbol + ")");
		img.setImageResource(list.get(pos).imgRes);

		return row;
	}

}


class Background extends AsyncTask<URL, Void, Double[]>{
	URL url;
	URL urlRev;
	Double[] rates = new Double[2];
	@Override
	protected Double[] doInBackground(URL... params) {
		// TODO Auto-generated method stub
		this.url = params[0];
		this.urlRev = params[1];
		Parser p1 = new Parser(url);
		rates[0] = p1.getRate();
		Parser p2 = new Parser(urlRev);
		rates[1] = p2.getRate();

		return rates;
	}
	
	@Override
	protected void onPostExecute(Double[] result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		MainActivity.progressStart.dismiss();
	}
}



