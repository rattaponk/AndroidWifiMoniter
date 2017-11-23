package com.exercise.AndroidWifiMonitor;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

public class MapSearch extends Activity {
	String value;
	int x = 0;
	int y = 0;
	int myNum = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		Intent intent = getIntent();
		value = intent.getStringExtra("data");

		try {
			myNum = Integer.parseInt(value.toString());
		} catch (NumberFormatException nfe) {
			System.out.println("Could not parse " + nfe);
		}

		if (myNum == 401) {
			x = 19;
			y = 17;
		} else if (myNum == 402) {
			x = 23;
			y = 17;
		} else if (myNum == 403) {
			x = 23;
			y = 13;
		} else if (myNum == 404) {
			x = 24;
			y = 11;
		} else if (myNum == 405) {
			x = 21;
			y = 11;
		} else if (myNum == 406) {
			x = 17;
			y = 12;
		} else if (myNum == 407) {
			x = 19;
			y = 9;
		} else if (myNum == 408) {
			x = 17;
			y = 7;
		} else if (myNum == 409) {
			x = 20;
			y = 4;
		} else if (myNum == 410) {
			x = 17;
			y = 4;
		} else if (myNum == 411) {
			x = 12;
			y = 2;
		} else if (myNum == 412) {
			x = 5;
			y = 2;
		} else if (myNum == 413) {
			x = 2;
			y = 7;
		} else if (myNum == 414) {
			x = 7;
			y = 7;
		} else if (myNum == 415) {
			x = 8;
			y = 12;
		} else if (myNum == 417) {
			x = 7;
			y = 19;
		} else if (myNum == 418) {
			x = 9;
			y = 20;
		} else if (myNum == 419) {
			x = 11;
			y = 19;
		} else if (myNum == 420) {
			x = 11;
			y = 18;
		} else if (myNum == 421) {
			x = 11;
			y = 17;
		} else if (myNum == 422) {
			x = 14;
			y = 18;
		} else if (myNum == 423) {
			x = 19;
			y = 8;
		} else if (myNum == 424) {
			x = 19;
			y = 7;
		} else if (myNum == 425) {
			x = 8;
			y = 15;
		} else if (myNum == 426) {
			x = 14;
			y = 15;
		} else if (myNum == 427) {
			x = 11;
			y = 9;
		} else if (myNum == 428) {
			x = 15;
			y = 7;
		} else if (myNum == 429) {
			x = 11;
			y = 4;
		} else if (myNum == 430) {
			x = 18;
			y = 6;
		}

		Draw2d d = new Draw2d(this);
		setContentView(d);

	}

	public class Draw2d extends View {

		public Draw2d(Context context) {
			super(context);
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {

			final int action = ev.getAction();
			switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP: {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MapSearch.this);
				builder.setTitle("Room " + myNum);
				if (myNum == 401) {builder.setMessage("ห้องบรรยายใหญ่");}  //ห้องบรรยายใหญ่
				else if (myNum == 402) {builder.setMessage("ห้องประชุม");} //ห้องประชุม
				else if (myNum == 403) {builder.setMessage("โถงหนังสือโครงงาน");} //โถงหนังสือโครงงาน
				else if (myNum == 404) {builder.setMessage("ห้องหัวหน้าภาค");} //หัวหน้าภาค
				else if (myNum == 405) {builder.setMessage("");} 
				else if (myNum == 406) {builder.setMessage("ห้องธุรการภาควิชาวิศวกรรมคอมพิวเตอร์");} //ห้องธุรการภาควิชาวิศวกรรมคอมพิวเตอร์
				else if (myNum == 407) {builder.setMessage("ห้องพักอาจารย์");} //ห้องพักอาจารย์
				else if (myNum == 408) {builder.setMessage("");} //
				else if (myNum == 409) {builder.setMessage("ห้องพักอาจารย์");} //ห้องพักอาจารย์
				else if (myNum == 410) {builder.setMessage("ห้องพักอาจารย์");} //ห้องพักอาจารย์
				else if (myNum == 411) {builder.setMessage("ห้องวิจัยการจำลองทางคอมพิวเตอร์และการคำนวณ");} //ห้องวิจัยการจำลองทางคอมพิวเตอร์และการคำนวณ
				else if (myNum == 412) {builder.setMessage("");} //
				else if (myNum == 413) {builder.setMessage("ห้องวิจัยความฉลาดทางด้านการคำนวณ");} //ห้องวิจัยความฉลาดทางด้านการคำนวณ
				else if (myNum == 414) {builder.setMessage("ห้องวิจัย");} //ห้องวิจัย
				else if (myNum == 415) {builder.setMessage("ห้องปฏิบัติการวิศวกรรมระบบควบคุม");} //ห้องปฏิบัติการวิศวกรรมระบบควบคุม
				else if (myNum == 417) {builder.setMessage("ห้องน้ำ");} //ห้องน้ำ
				else if (myNum == 418) {builder.setMessage("ห้องน้ำ");} //ห้องน้ำ
				else if (myNum == 419) {builder.setMessage("ห้องน้ำ");} //ห้องน้ำ
				else if (myNum == 420) {builder.setMessage("ห้องไฟฟ้า");} //ห้องไฟฟ้า
				else if (myNum == 421) {builder.setMessage("ห้องสื่อสาร");} //ห้องสื่อสาร
				else if (myNum == 422) {builder.setMessage("ห้อง น.ศง ปริญญาโท");} //ห้อง น.ศง ปริญญาโท
				else if (myNum == 423) {builder.setMessage("ห้องน้ำอาจารย์");} //ห้องน้ำอาจารย์
				else if (myNum == 424) {builder.setMessage("ห้องน้ำอาจารย์");} //ห้องน้ำอาจารย์
				else if (myNum == 425) {builder.setMessage("โถงลิฟต์");} //โถงลิฟต์
				else if (myNum == 426) {builder.setMessage("");} 
				else if (myNum == 427) {builder.setMessage("");} 
				else if (myNum == 428) {builder.setMessage("");} 
				else if (myNum == 429) {builder.setMessage("");} 
				else if (myNum == 430) {builder.setMessage("");} 
				
				
				builder.setCancelable(false);
				builder.setPositiveButton("CLOSE",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
//				Toast.makeText(getApplicationContext(), "User clicked!",
//						Toast.LENGTH_SHORT).show();
				break;
			}
			}
			return true;
		}

		@Override
		protected void onDraw(Canvas c) {

			super.onDraw(c);
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);

			int xmin, xmax, ymin, ymax;

			// make the entire canvas white
			paint.setColor(Color.WHITE);
			c.drawPaint(paint);
			paint.setAntiAlias(true);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.map4);
			Bitmap img;
			if (getWidth() / getHeight() <= bmp.getWidth() / bmp.getHeight()) {
				img = Bitmap.createScaledBitmap(bmp, getWidth(),
						bmp.getHeight() * getWidth() / bmp.getWidth(), true);

				c.drawBitmap(img, 0, (getHeight() - img.getHeight()) / 2, paint);
				xmin = 0;
				// ymin = (getHeight() - img.getHeight()) / 2;
				ymax = ((getHeight() - img.getHeight()) / 2) + img.getHeight();
			} else {
				img = Bitmap.createScaledBitmap(bmp, bmp.getWidth()
						* getHeight() / bmp.getHeight(), getHeight(), true);
				c.drawBitmap(img, (getWidth() - img.getWidth()) / 2, 0, paint);
				xmin = (getWidth() - img.getWidth()) / 2;
				// ymin = 0;
				ymax = img.getHeight();
			}

			paint.setColor(Color.BLUE);
			c.drawCircle(xmin + (x * img.getWidth() / 25),
					ymax - (y * img.getHeight() / 20), 10, paint);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
