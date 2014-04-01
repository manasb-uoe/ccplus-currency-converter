package com.example.ccplus_latest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ChartsActivity extends Activity implements OnItemClickListener{
	GridView gridView;
	TextView helpText;
	TextView tvFromTo;
	TextView tvUnitRate;
	ActionBar actionBar;
	String from;
	String to;
	ArrayList<Bitmap> images = new ArrayList<Bitmap>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charts);
		findViews();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		images = new ArrayList<Bitmap>(); 
		from = getIntent().getStringExtra("from");
		to = getIntent().getStringExtra("to");
		String oneRate = getIntent().getStringExtra("unit rate");
		
		try {
			URL url1 = (new URL("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=1d&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US"));
			URL url2 = (new URL("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=5d&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US"));
			URL url3 = (new URL("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=3m&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US"));
			URL url4 = (new URL("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=1y&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US"));
			URL url5 = (new URL("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=2y&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US"));
			URL url6 = (new URL("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=5y&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US"));
			URL[] urls = new URL[]{url1,url2,url3,url4,url5,url6};
			BackgroundCharts download = new BackgroundCharts();
			images = download.execute(urls).get();
		}
		catch (MalformedURLException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			tvFromTo.setText(from + " vs " + to);
			tvUnitRate.setText(oneRate);
			gridView.setAdapter(new CustomGridViewAdapter(this, images));
			gridView.setOnItemClickListener(this);
		}
	}
	
	public void findViews(){
		gridView = (GridView) findViewById(R.id.gridView1);
		helpText = (TextView) findViewById(R.id.textView1);
		tvFromTo = (TextView) findViewById(R.id.textViewFROMTO);
		tvUnitRate = (TextView) findViewById(R.id.textViewUnitRate);
		actionBar = getActionBar();
		
	}

	@Override
	public void onItemClick(AdapterView<?> adv, View v, int pos, long arg3) {
		// TODO Auto-generated method stub
		
		FragmentChart openChart = new FragmentChart();
		Bundle bundle = new Bundle();
		bundle.putInt("pos", pos);
		bundle.putString("from", from);
		bundle.putString("to", to);
		openChart.setArguments(bundle);
		
		openChart.show(getFragmentManager(), "dialog fragment");
	}
	
}



class CustomGridViewAdapter extends BaseAdapter{
	Context c;
	ArrayList<Chart> charts = new ArrayList<Chart>();
	String[] lables = new String[]{"Range: 1 day", "Range: 5 days", "Range: 3 months", "Range: 1 year", "Range: 2 years", "Range: 5 years"};
	public CustomGridViewAdapter(Context c, ArrayList<Bitmap> images){
		this.c = c;
		for (int i=0;i<lables.length;i++){
			charts.add(new Chart(lables[i], images.get(i)));
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return charts.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return charts.get(arg0);
	}
	
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup container) {
		// TODO Auto-generated method stub
		View item = convertView;
		if (item == null){
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			item = inflater.inflate(R.layout.single_item_gridview, container, false);
		}
		ImageView chart = (ImageView) item.findViewById(R.id.imageView1);
		TextView label = (TextView) item.findViewById(R.id.textView1);

		chart.setImageBitmap(charts.get(pos).img);
		label.setText(charts.get(pos).lable);

		return item;
	}

}

class BackgroundCharts extends AsyncTask<URL, Void, ArrayList<Bitmap>>{
	
	
	public ArrayList<Bitmap> getCharts(URL[] urls){
		ArrayList<Bitmap> images = new ArrayList<Bitmap>();
		BufferedInputStream bis = null;
		for (int i=0;i<urls.length;i++){
			try {
				bis = new BufferedInputStream(urls[i].openStream());
				Bitmap b = BitmapFactory.decodeStream(bis);
				images.add(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
			}
		}
		try {
			bis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return images;
		
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected ArrayList<Bitmap> doInBackground(URL... params) {
		// TODO Auto-generated method stub
		ArrayList<Bitmap> dls = new ArrayList<Bitmap>(); 
			dls =  getCharts(params);
		return dls;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Bitmap> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		MainActivity.progressDialog.dismiss();
	}

}
