package com.exercise.AndroidWifiMonitor;

 
import java.util.ArrayList;
import java.util.HashMap;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
 
public class ListLocation extends Activity {
 
    // List view
    private ListView lv;
 
    // Listview Adapter
    ArrayAdapter<String> adapter;
 
    // Search EditText
    EditText inputSearch;
 
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    private Object o;
	private String  value;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
 
        // Listview Data
        String products[] = {"401", "402", "403", "404", "405", "406", "407", "408"
        		, "409", "410", "411", "412", "413", "414", "415", "417", "418"
        		, "419", "420", "421", "422", "423", "424", "425", "426", "427", "428", "429", "430"};
 
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
 
        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, products);
        lv.setAdapter(adapter);       
        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {
 
           
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	ListLocation.this.adapter.getFilter().filter(cs);
            }
 
           
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
 
            }
 
           
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        
        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 o = lv.getItemAtPosition(position);
				 value = o.toString();
				 int myNum = 0;

			      	try {
			      	    myNum = Integer.parseInt(value.toString());
			      	} catch(NumberFormatException nfe) {
			      	   System.out.println("Could not parse " + nfe);
			      	} 
			      	
			      
				      	check();

			
			}
			
			
		});
    }
    public void check(){
    	
        
		Intent intent = new Intent(this, MapSearch.class);
		intent.putExtra("data", value);
	
	startActivity(intent);	
	
}
}