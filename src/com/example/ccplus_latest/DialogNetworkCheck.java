package com.example.ccplus_latest;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DialogNetworkCheck extends DialogFragment implements OnClickListener{
	Button btnYes;
	Button btnNo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_dialog, container, false);
		findViews(view);
		getDialog().setTitle("Internet Required!");
		
		btnYes.setOnClickListener(this);
		btnNo.setOnClickListener(this);
		
		return view;
	}
	
	public void findViews(View view){
		btnYes = (Button) view.findViewById(R.id.yes);
		btnNo = (Button) view.findViewById(R.id.no);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.yes){
			Intent goToSettings = new Intent();
			goToSettings.setAction(Settings.ACTION_WIFI_SETTINGS);
			startActivity(goToSettings);
			dismiss();
		}
		if (v.getId() == R.id.no){
			getActivity().finish();
		}
		
	}
	
	
}
