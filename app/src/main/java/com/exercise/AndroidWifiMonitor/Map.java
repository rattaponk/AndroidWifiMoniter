package com.exercise.AndroidWifiMonitor;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Map extends Activity {
    double x, y;
    ArrayList<String> M = new ArrayList<String>();
    HashMap<String, Double> X = new HashMap<String, Double>();
    HashMap<String, Double> Y = new HashMap<String, Double>();
    HashMap<String, Double> Rssi = new HashMap<String, Double>();
    HashMap<String, String> Name = new HashMap<String, String>();
    Context ctx;
    Handler mHandler;
    WifiManager myWifiManager;
    List<ScanResult> result;
    WifiList List;
    String APs = "";
    ArrayList<WifiPoint> APFiltered = new ArrayList<WifiPoint>();

    TextView tvInfo;

    class Worker extends Thread {
        Context ctx;

        public Worker(Context ctx) {
            mHandler = new Handler();
            this.ctx = ctx;
        }

        @SuppressWarnings("static-access")
        public void run() {

            while (true) {
                mHandler.post(new Runnable() {
                    public void run() {
                        myWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        myWifiManager.startScan();
                        result = myWifiManager.getScanResults();
                        calculateLose();
                        Draw2d d = new Draw2d(Map.this);

                        TextView tvInfo = new TextView(Map.this);
                        String Info = "position: x=" + String.valueOf((int) x) + " , y=" + String.valueOf((int) y) + "\n" + APs;
                        tvInfo.setText(Info);
                        tvInfo.setTextColor(Color.BLACK);
                        tvInfo.setBackgroundColor(Color.WHITE);

                        LinearLayout linearLayout = new LinearLayout(Map.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        linearLayout.addView(tvInfo);
                        linearLayout.addView(d);
                        setContentView(linearLayout);

//                        setContentView(d);


                        Log.v("position", x + " : " + y);
                        Log.d("APs", APs);
//                        Toast.makeText(Map.this, "position: x=" + String.valueOf((int) x) + " , y=" + String.valueOf((int) y) + "\n" + APs, Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    this.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void calculateLose() {

        for (int i = 0; i < result.size(); i++) {
            int index = List.isAvailable(result.get(i).BSSID);
            if (index != -1) {
                List.updateAt(index, result.get(i).level);
            } else {
                List.insertNew(result.get(i).BSSID, result.get(i).SSID, result.get(i).level);
            }
        }

        // List of Wifi Accesspoints
        ArrayList<WifiPoint> AP = List.List;
        Collections.sort(AP, new Comparator<WifiPoint>() {
            public int compare(WifiPoint arg0, WifiPoint arg1) {
                // TODO Auto-generated method stub
                return arg1.average - arg0.average;
            }
        });

        APFiltered.clear();
        APs = "";
        int count = 0;
        for (int i = 0; i < AP.size(); i++) {
            if (M.contains(AP.get(i).BSSID) && count < 3) {
                count++;
                APFiltered.add(AP.get(i));
            }
        }

        double[][] positions = new double[3][2];
        double[] distances = new double[3];

        HashMap<String, Double> RangeFromAps = new HashMap<String, Double>();
        int n = 0;
        for (int i = 0; i < APFiltered.size(); i++) {
            // System.out.println("i="+i);
            if (M.contains(APFiltered.get(i).BSSID)) {

//                double Ldbm = APFiltered.get(i).average - Rssi.get(APFiltered.get(i).BSSID);
//                // System.out.println(Ldbm+" "+AP.get(i).BSSID);
//                double Lwatt = (0.001 * Math.pow(10, Ldbm / 10));
//                // System.out.println(Lwatt+" "+AP.get(i).BSSID);
//                double Range = Math.sqrt((Math.pow(0.125, 2))
//                        / (Lwatt * (Math.pow(4 * Math.PI, 2)))) / 2;

                double Ldbm = Rssi.get(APFiltered.get(i).BSSID) - APFiltered.get(i).average;
                double Range = Math.pow(10, (Ldbm + 7.36) / 26) / 2;
//                double Range = ((Ldbm + 1.82) / 1.77) / 2;

                RangeFromAps.put(APFiltered.get(i).BSSID, Range);
                // System.out.println(Range+" "+AP.get(i).BSSID);

                distances[n] = Range;
                positions[n][0] = X.get(APFiltered.get(i).BSSID);
                positions[n][1] = Y.get(APFiltered.get(i).BSSID);
                n++;
                String range = new DecimalFormat("##.####").format(Range);
                APs += Name.get(APFiltered.get(i).BSSID) + "\t\t" + APFiltered.get(i).BSSID + "\t\t" + APFiltered.get(i).average + "\t" + range +"\n";
            }
        }

//        try {
//            // System.out.println(AP.get(0).x + "" + AP.get(0).y);
//            double[] B = new double[APFiltered.size() - 1];
//            double[][] A = new double[APFiltered.size() - 1][2];
//
//            // System.out.print("APsize =  "+AP.size());
//            String defaultBBSID = APFiltered.get(0).BSSID;
//            for (int i = 1; i < APFiltered.size(); i++) {
//
//                // System.out.println("b1 = "+(Math.pow(RangeFromAps.get(AP.get(i).BSSID),
//                // 2) - Math.pow(
//                // RangeFromAps.get(defaultBBSID), 2)));
//                //
//                // System.out.println("b2 = "+(Math.pow(X.get(AP.get(i).BSSID), 2) -
//                // Math.pow(
//                // X.get(defaultBBSID), 2)));
//                // System.out.println("b3 = "+(Math.pow(Y.get(AP.get(i).BSSID), 2) -
//                // Math.pow(
//                // Y.get(defaultBBSID), 2)));
//
//                B[i - 1] = (Math.pow(RangeFromAps.get(APFiltered.get(i).BSSID), 2) - Math.pow(RangeFromAps.get(defaultBBSID), 2))
//                            - (Math.pow(X.get(APFiltered.get(i).BSSID), 2) - Math.pow(X.get(defaultBBSID), 2))
//                            - (Math.pow(Y.get(APFiltered.get(i).BSSID), 2) - Math.pow(Y.get(defaultBBSID), 2));
//
//                // System.out.println("B:"+B[i-1]);
//                A[i - 1][0] = (X.get(defaultBBSID) - X.get(APFiltered.get(i).BSSID)) * 2;
//                A[i - 1][1] = (Y.get(defaultBBSID) - Y.get(APFiltered.get(i).BSSID)) * 2;
//            }
//            double Aa1 = 0;
//            double Aa2 = 0;
//            double Aa3 = 0;
//            double Aa4 = 0;
//
//            ArrayList<Integer> resultA = new ArrayList<Integer>();
//            for (int i = 0; i < APFiltered.size() - 1; i++) {
//                Aa1 += A[i][0] * A[i][0];
//                Aa2 += A[i][0] * A[i][1];
//                Aa3 += A[i][0] * A[i][1];
//                Aa4 += A[i][1] * A[i][1];
//            }
//
//            double Ab1 = 0;
//            double Ab2 = 0;
//
//            ArrayList<Integer> resultB = new ArrayList<Integer>();
//            for (int i = 0; i < APFiltered.size() - 1; i++) {
//                Ab1 += A[i][0] * B[i];
//                Ab2 += A[i][1] * B[i];
//            }
//
//            double Determinent = (Aa1 * Aa4) - (Aa3 * Aa2);
//            double DeterminentX = (Ab1 * Aa4) - (Ab2 * Aa2);
//            double DeterminentY = (Aa1 * Ab2) - (Aa3 * Ab1);
//            x = DeterminentX / Determinent;
//            y = DeterminentY / Determinent;
//        }catch (Throwable e) {
//            e.printStackTrace();
//        }

        try {
            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();

            // the answer
            double[] calculatedPosition = optimum.getPoint().toArray();
            x = calculatedPosition[0];
            y = calculatedPosition[1];
        }catch (Throwable e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        System.out.println("X:" + x);
        System.out.println("Y:" + y);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        ctx = this;
        List = new WifiList();
        tvInfo = (TextView) findViewById(R.id.tv_Info);

        initData();
        Worker t = new Worker(ctx);
        t.start();

    }

    public void initData() {
        M.add("0c:f5:a4:41:80:df");
        X.put("0c:f5:a4:41:80:df", 7.0);
        Y.put("0c:f5:a4:41:80:df", 4.0);
        Rssi.put("0c:f5:a4:41:80:df", -45.0);
        Name.put("0c:f5:a4:41:80:df","Fr R412");

        M.add("00:3a:99:05:f4:f0");
        X.put("00:3a:99:05:f4:f0", 11.0);
        Y.put("00:3a:99:05:f4:f0", 2.0);
        Rssi.put("00:3a:99:05:f4:f0", -51.0);
        Name.put("00:3a:99:05:f4:f0","In Llnw");


        M.add("00:3a:99:38:84:40");
        X.put("00:3a:99:38:84:40", 3.0);
        Y.put("00:3a:99:38:84:40", 3.0);
        Rssi.put("00:3a:99:38:84:40", -45.0);
        Name.put("00:3a:99:38:84:40","In Soi");

//        M.add("00:11:3b:0d:05:29");
//        X.put("00:11:3b:0d:05:29", 8.0);
//        Y.put("00:11:3b:0d:05:29", 11.0);
//        Rssi.put("00:11:3b:0d:05:29", -40.0);
//        Name.put("00:11:3b:0d:05:29","In Llil");

//        M.add("4c:5e:0c:72:da:24");
//        X.put("4c:5e:0c:72:da:24", 10.0);
//        Y.put("4c:5e:0c:72:da:24", 12.0);
//        Rssi.put("4c:5e:0c:72:da:24", -44.0);
//        Name.put("4c:5e:0c:72:da:24","In R416");

        M.add("50:67:ae:c2:41:1f");
        X.put("50:67:ae:c2:41:1f", 13.0);
        Y.put("50:67:ae:c2:41:1f", 9.0);
        Rssi.put("50:67:ae:c2:41:1f", -51.0);
        Name.put("50:67:ae:c2:41:1f","Fr R416");

        M.add("00:b0:e1:98:f1:a0");
        X.put("00:b0:e1:98:f1:a0", 18.0);
        Y.put("00:b0:e1:98:f1:a0", 12.0);
        Rssi.put("00:b0:e1:98:f1:a0", -47.0);
        Name.put("00:b0:e1:98:f1:a0","In admin");

        M.add("00:3a:99:40:44:b0");
        X.put("00:3a:99:40:44:b0", 20.0);
        Y.put("00:3a:99:40:44:b0", 14.0);
        Rssi.put("00:3a:99:40:44:b0", -46.0);
        Name.put("00:3a:99:40:44:b0","Fr ISNE");

        M.add("94:d4:69:fc:8d:60");
        X.put("94:d4:69:fc:8d:60", 23.0);
        Y.put("94:d4:69:fc:8d:60", 17.0);
        Rssi.put("94:d4:69:fc:8d:60", -46.0);
        Name.put("94:d4:69:fc:8d:60","In R402");

    }

    public class Draw2d extends View {
        public Draw2d(Context context) {
            super(context);
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
                //ymin = (getHeight() - img.getHeight()) / 2;
                ymax = ((getHeight() - img.getHeight()) / 2) + img.getHeight();
            } else {
                img = Bitmap.createScaledBitmap(bmp, bmp.getWidth() * getHeight() / bmp.getHeight(),
                        getHeight(), true);
                c.drawBitmap(img, (getWidth() - img.getWidth()) / 2, 0, paint);
                xmin = (getWidth() - img.getWidth()) / 2;
                //ymin = 0;
                ymax = img.getHeight();
            }

            //draw Line
            for (int i = 0; i <= 25; i++) {
                if (i % 5 == 0) {
                    paint.setColor(Color.GREEN);
                } else {
                    paint.setColor(Color.GRAY);
                }
                c.drawLine(xmin + (i * img.getWidth() / 25), ymax - (0 * img.getHeight() / 20), xmin + (i * img.getWidth() / 25), ymax - (20 * img.getHeight() / 20), paint);
            }
            for (int i = 0; i <= 20; i++) {
                if (i % 5 == 0) {
                    paint.setColor(Color.GREEN);
                } else {
                    paint.setColor(Color.GRAY);
                }
                c.drawLine(xmin + (0 * img.getWidth() / 25), ymax - (i * img.getHeight() / 20), xmin + (25 * img.getWidth() / 25), ymax - (i * img.getHeight() / 20), paint);
            }

            //draw dot
            paint.setColor(Color.GRAY);
            for (int i = 0; i <= 25; i++) {
                for (int j = 0; j <= 20; j++) {
//                    if ((i % 5 == 0) && (j % 5 == 0)) {
//                        paint.setColor(Color.GREEN);
//                        c.drawCircle(xmin + (i * img.getWidth() / 25), ymax - (j * img.getHeight() / 20), 10, paint);
//                    } else {
//                        paint.setColor(Color.GRAY);
//                        c.drawCircle(xmin + (i * img.getWidth() / 25), ymax - (j * img.getHeight() / 20), 5, paint);
//                    }
                    c.drawCircle(xmin + (i * img.getWidth() / 25), ymax - (j * img.getHeight() / 20), 2, paint);
                }
            }

            //draw APs
            paint.setColor(Color.RED);
            for(int i = 0; i < M.size() ;i++) {
                double xx = X.get(M.get(i));
                double yy = Y.get(M.get(i));

                    c.drawCircle(xmin + ((int) xx * img.getWidth() / 25), ymax - ((int) yy * img.getHeight() / 20), 10, paint);
            }
            paint.setColor(Color.GREEN);
            for (int i = 0; i < APFiltered.size(); i++) {
                double xx = X.get(APFiltered.get(i).BSSID);
                double yy = Y.get(APFiltered.get(i).BSSID);
                if (M.contains(APFiltered.get(i).BSSID)) {
                    c.drawCircle(xmin + ((int) xx * img.getWidth() / 25), ymax - ((int) yy * img.getHeight() / 20), 10, paint);
                }
            }

            //draw user
            paint.setColor(Color.BLUE);
            c.drawCircle(xmin + ((int)x * img.getWidth() / 25), ymax - ((int)y * img.getHeight() / 20), 15, paint);

//            paint.setColor(Color.RED);
//            c.drawCircle(xmin + ((int)X * img.getWidth() / 25), ymax - ((int)Y * img.getHeight() / 20), 15, paint);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

