package com.example.myfirstapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.examlpe.myfirstapp.MESSAGE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   // Intent intent = getIntent();

	    //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

	    // Create the text view
	   // TextView textView = new TextView(this);
	   // textView.setTextSize(40);
	    //textView.setText(message);

	    // Set the text view as the activity layout
	   // setContentView(textView);
	    // Get the message from the intent
	    Intent intent = getIntent();
	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

	    // Create the text view

	    // Set the text view as the activity layout
	    setContentView(R.layout.activity_display_message);
	}

	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    
    public void setClient(View view) {
        Intent intent = new Intent(this, ClientActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
	
    
    public void setServer(View view) {
        Intent intent = new Intent(this, ServerActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}
