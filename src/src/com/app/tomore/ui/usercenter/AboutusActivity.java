package com.app.tomore.ui.usercenter;

import com.app.tomore.R;
import com.app.tomore.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutusActivity extends Activity {

	private View layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		
		layout = findViewById(R.id.AboutUSActivitylayout);
		final Button btnBack = (Button) layout.findViewById(R.id.bar_title_about_us_go_back);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
}
