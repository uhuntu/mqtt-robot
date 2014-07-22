package com.touchrev.mqttrobot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity {

	private MQTTService mMqttService;

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMqttService = ((MQTTService.MyBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	private void startMqttService() {
		final Intent intent = new Intent(this, MQTTService.class);
		startService(intent);
		bindService(intent, connection, BIND_AUTO_CREATE);
	}

	private void stopMqttService() {
		unbindService(connection);
		final Intent intent = new Intent(this, MQTTService.class);
		stopService(intent);
	}

	private void pubMsg(final String msg) {
		mMqttService.publishMessage(msg);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startMqttService();

		WebView wv;
		wv = (WebView) this.findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wv.loadUrl("http://192.168.11.1:8080/javascript_simple.html");

		Button bTest = (Button) findViewById(R.id.buttonTest);
		bTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("test");
			}
		});

		Button bMove = (Button) findViewById(R.id.buttonMove);
		bMove.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pubMsg("move");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					pubMsg("stop");
				}
				return false;
			}
		});

		Button bBack = (Button) findViewById(R.id.buttonBack);
		bBack.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pubMsg("back");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					pubMsg("stop");
				}
				return false;
			}
		});

		Button bLeft = (Button) findViewById(R.id.buttonLeft);
		bLeft.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pubMsg("left");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					pubMsg("stop");
				}
				return false;
			}
		});

		Button bRight = (Button) findViewById(R.id.buttonRight);
		bRight.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pubMsg("right");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					pubMsg("stop");
				}
				return false;
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		stopMqttService();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String t = item.getTitle().toString();
		if (t.equals("IR")) {
			Intent intent = new Intent(this, IRActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, DevActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
