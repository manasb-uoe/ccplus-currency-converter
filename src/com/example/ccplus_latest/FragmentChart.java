package com.example.ccplus_latest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FragmentChart extends DialogFragment implements OnClickListener{
	ImageView imageView;
	String[] urls;
	Button btnClose;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.large_chart, container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
	
		imageView = (ImageView) view.findViewById(R.id.imageView1);
		btnClose = (Button) view.findViewById(R.id.buttonClose);

		String from = getArguments().getString("from");
		String to = getArguments().getString("to");
		int pos = getArguments().getInt("pos");

		String url1 = ("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=1d&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US");
		String url2 = ("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=5d&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US");
		String url3 = ("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=3m&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US");
		String url4 = ("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=1y&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US");
		String url5 = ("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=2y&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US");
		String url6 = ("http://chart.finance.yahoo.com/z?s=" + from + to + "%3dX&t=5y&q=l&l=off&z=m&a=v&p=s&lang=en-US&region=US");
		urls = new String[]{url1,url2,url3,url4,url5,url6};

		try {
			Bitmap b = new task3().execute(new URL(urls[pos])).get();
			imageView.setImageBitmap(b);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btnClose.setOnClickListener(this);

		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.buttonClose){
			dismiss();
		}
	}
}


class task3 extends AsyncTask<URL, Void, Bitmap>{
	BufferedInputStream bis = null;
	Bitmap b = null;
	@Override
	protected Bitmap doInBackground(URL... params) {
		// TODO Auto-generated method stub
		try {
			BufferedInputStream bis = new BufferedInputStream(params[0].openStream());
			b = BitmapFactory.decodeStream(bis);
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();	
		} 

		return b;
	}

}