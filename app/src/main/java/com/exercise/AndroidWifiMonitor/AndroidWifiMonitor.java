package com.exercise.AndroidWifiMonitor;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidWifiMonitor extends Activity {
    int i = 0;
    double x, y, X, Y;
    ArrayList<String> M = new ArrayList<String>();
    Context ctx;
    Handler mHandler;
    WifiManager myWifiManager;
    List<ScanResult> result;
    WifiList List;
    Boolean record = false;

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
                        myWifiManager.startScan();
                        result = myWifiManager.getScanResults();
                        LinearLayout linear = (LinearLayout) findViewById(R.id.linearList);
                        linear.removeAllViews();
                        for (int i = 0; i < result.size(); i++) {
                            MyView view = new MyView(ctx);

                            view.Mac.setText("Mac : " + result.get(i).BSSID);
                            view.Name.setText("Name : " + result.get(i).SSID);
                            view.Rssi.setText("Rssi : " + result.get(i).level
                                    + "");
                            linear.addView(view);
                            linear.invalidate();
                        }

                    }

                });
                try {
                    this.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
    }


    public void FingerPrint(ArrayList<WifiPoint> AP) {
        String minFileName = null;
        double minRSSI = -1;
        try {
            AssetManager assetManager = this.getAssets();

            String[] filelist = assetManager.list("");

            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].contains(".txt")) {
                    int count = 0;
                    double tempRSSI = 0;

                    //System.out.println(filelist[i]);

                    InputStream input = assetManager.open(filelist[i]);

                    int size = input.available();
                    byte[] buffer = new byte[size];
                    input.read(buffer);
                    input.close();

                    String text = new String(buffer);

                    Scanner scan = new Scanner(text);
                    while (scan.hasNext()) {
                        String MAC = scan.next();
                        double RSSI = scan.nextDouble();

                        //System.out.println(filelist[i] + " " + RSSI);

                        for (int j = 0; j < AP.size(); j++) {
                            if (AP.get(j).BSSID.equalsIgnoreCase(MAC)) {
                                count++;
                                tempRSSI += Math.sqrt(Math.pow(RSSI - AP.get(j).average, 2));
                            }
                        }
                    }
                    //System.out.println(filelist[i] + " " + tempRSSI/count);

                    if (tempRSSI / count < minRSSI || minRSSI == -1) {
                        minRSSI = tempRSSI / count;
                        minFileName = filelist[i];
                    }
                }

            }

            minFileName = minFileName.replace(".txt", "");
            String[] tempString = minFileName.split(",");
            X = Integer.parseInt(tempString[0]);
            Y = Integer.parseInt(tempString[1]);

            System.out.println(minFileName + " " + X + "," + Y);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;
        List = new WifiList();

        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        final Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        final Button btnClear = (Button) findViewById(R.id.btnClear);
        final TextView txtStatus = (TextView) findViewById(R.id.txtStatus);
        Button btnMap = (Button) findViewById(R.id.btnMap);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);

        btnMap.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent a = new Intent(AndroidWifiMonitor.this, Map.class);
                startActivity(a);

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent b = new Intent(AndroidWifiMonitor.this, ListLocation.class);
                startActivity(b);

            }
        });

        btnAdd.setFocusable(false);

        DisplayWifiState();

        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                i++;
//				M.add("00:22:6b:91:95:55");
//				M.add("00:3a:99:05:f4:f0");
//				M.add("00:90:4c:91:00:01");
//				M.add("00:22:57:50:99:c0");
//				M.add("00:27:22:4c:cf:a2");
//				M.add("58:6d:8f:b6:76:6b");
//				M.add("00:3a:99:40:37:f0");
//				M.add("00:3a:99:40:21:d0");
//				M.add("00:22:6b:58:f2:40");
//				M.add("00:22:6b:58:f4:2c");
//				M.add("00:1a:70:34:7b:d1");
//				M.add("00:0f:66:38:a0:51");

                M.add("0c:f5:a4:41:80:df");
                M.add("4c:5e:0c:72:da:24");
                M.add("4e:5e:0c:72:da:24");
                M.add("50:67:ae:c2:41:19");
//                M.add("84:80:2d:ab:4e:cf");
                M.add("00:b0:e1:98:f2:20");
                M.add("94:d4:69:fc:cc:af");

                String dir = Environment.getExternalStorageDirectory()
                        .toString();
                File folder = new File(dir + "/Wifi");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                String file = dir + "/Wifi/WifiState" + i + ".txt";
                File save = new File(file);
                Log.v("directory", file);
                try {
                    save.createNewFile();
                    FileWriter writer1 = new FileWriter(save, true);

                    // List of Wifi Accesspoints
                    ArrayList<WifiPoint> AP = List.List;
                    Collections.sort(AP, new Comparator<WifiPoint>() {
                        public int compare(WifiPoint arg0, WifiPoint arg1) {
                            // TODO Auto-generated method stub
                            return arg1.average - arg0.average;
                        }
                    });
                    ArrayList<WifiPoint> APFiltered = new ArrayList<WifiPoint>();
                    int count = 0;
                    for (int i = 0; i < AP.size(); i++) {
                        if (M.contains(AP.get(i).BSSID) && count < 3) {
                            count++;
                            System.out.print("" + AP.get(i).BSSID + "\t"
                                    + AP.get(i).SSID + "\t" + AP.get(i).average
                                    + "\n");
                            writer1.write("" + AP.get(i).BSSID + "\t"
                                    + AP.get(i).average + "\n");
                            // writer1.write("" + AP.get(i).BSSID + "\t"+
                            // AP.get(i).SSID + "\t" + AP.get(i).average +
                            // "\n");
                            APFiltered.add(AP.get(i));

                        }
                    }
                    FingerPrint(APFiltered);
                    writer1.close();
                    Toast.makeText(AndroidWifiMonitor.this,
                            "Save file complete..", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(AndroidWifiMonitor.this, "Failed to save",
                            Toast.LENGTH_SHORT).show();
                }

                btnAdd.setFocusable(false);
                List = new WifiList();

                txtStatus.setText("Data Saved!");
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                for (int i = 0; i < result.size(); i++) {
                    int index = List.isAvailable(result.get(i).BSSID);
                    if (index != -1) {
                        List.updateAt(index, result.get(i).level);
                    } else {
                        List.insertNew(result.get(i).BSSID, result.get(i).SSID,
                                result.get(i).level);
                    }
                }

                List.round = List.round + 1;

                txtStatus.setText("Round " + List.round + " Updated!");

                btnAdd.setFocusable(true);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                List = new WifiList();
                btnAdd.setFocusable(false);

                txtStatus.setText("Data Cleared!");
            }
        });

//        this.registerReceiver(this.myWifiReceiver, new IntentFilter(
//                ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private BroadcastReceiver myWifiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            NetworkInfo networkInfo = (NetworkInfo) arg1
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                DisplayWifiState();
            }
        }
    };

    private void DisplayWifiState() {

        myWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (myWifiManager.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(),
                    "wifi is disabled..making it enabled", Toast.LENGTH_LONG)
                    .show();
            myWifiManager.setWifiEnabled(true);
        }
        myWifiManager.startScan();

        List<ScanResult> result = myWifiManager.getScanResults();
        LinearLayout linear = (LinearLayout) findViewById(R.id.linearList);
        for (int i = 0; i < result.size(); i++) {
            MyView view = new MyView(ctx);
            view.Mac.setText("Mac : " + result.get(i).BSSID);
            view.Name.setText("Name : " + result.get(i).SSID);
            view.Rssi.setText("Rssi : " + result.get(i).level + "");
            linear.addView(view);
            linear.invalidate();

        }

        Worker t = new Worker(ctx);
        t.start();

    }
}