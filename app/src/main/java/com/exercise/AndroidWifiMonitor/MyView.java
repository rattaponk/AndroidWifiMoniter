package com.exercise.AndroidWifiMonitor;

import android.content.Context;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyView extends LinearLayout{
	TextView Name,Rssi,Mac;
	public MyView(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(0, 10, 0, 10);
		Name = new TextView(context);
		Rssi = new TextView(context);
		Mac = new TextView(context);
		Name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		Rssi.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		Mac.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		this.addView(Name);
		this.addView(Mac);
		this.addView(Rssi);
		
	}

}
