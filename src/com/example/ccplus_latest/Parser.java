package com.example.ccplus_latest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class Parser {
	public URL url;

	public Parser(URL url){
		if (url != null){			
			this.url = url;
		}
	}

	public double getRate(){
		InputStreamReader isr;
		BufferedReader reader;
		double rate = 0;
		try {
			isr = new InputStreamReader(url.openStream());
			reader = new BufferedReader(isr);
			String line = reader.readLine();
			String[] split = line.split(",");

//			String from = split[0].substring(split[0].indexOf(":")+3, split[0].length()-1);
//			String to = split[2].substring(split[2].indexOf(":")+3, split[2].length()-2);
			rate = Double.parseDouble(split[1].substring(split[1].indexOf(":")+2));

		} catch (IOException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rate;
	}
}
