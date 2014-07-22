package com.touchrev.mqttdevice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

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

	@SuppressWarnings("unused")
	private void pubMsg(final String msg) {
		mMqttService.publishMessage(msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startMqttService();
	}

	@Override
	protected void onDestroy() {
		stopMqttService();
		super.onDestroy();
	}

	public class MsgReceiver extends BroadcastReceiver {
		private static final String TAG = "MsgReceiver";

		@Override
		public void onReceive(Context arg0, Intent it) {
			String topic = it.getStringExtra("topic");
			String message = it.getStringExtra("message");
			Log.i(TAG, "topic = " + topic);
			Log.i(TAG, "message = " + message);
			TextView tv;
			if (topic.equals("dev/tv")) {
				tv = (TextView) findViewById(R.id.textView1);
				tv.setText("TV is on");
			}
			if (topic.equals("dev/fan")) {
				tv = (TextView) findViewById(R.id.textView2);
				tv.setText("Fan is on");
			}
			if (topic.equals("dev/switch")) {
				tv = (TextView) findViewById(R.id.textView3);
				tv.setText("Switch is on");
			}
			if (topic.equals("dev/window")) {
				tv = (TextView) findViewById(R.id.textView4);
				tv.setText("Window is on");
			}
			if (topic.equals("dev/light")) {
				tv = (TextView) findViewById(R.id.textView5);
				tv.setText("Light is on");
			}
		}
	}

	@Override
	protected void onStart() {
		MsgReceiver msgReceiver = new MsgReceiver();  
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.justel.service");
		registerReceiver(msgReceiver, filter);
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
