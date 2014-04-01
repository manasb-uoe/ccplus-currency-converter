package com.example.ccplus_latest;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class FragmentAbout extends DialogFragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().setTitle("About");	
//		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		return inflater.inflate(R.layout.fragment_about, container, false);
	}
}
