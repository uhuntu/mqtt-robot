package com.touchrev.mqttrobot;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DevActivity extends Activity {
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
	
	private void bindMqttService() {
		final Intent intent = new Intent(this, MQTTService.class);
		bindService(intent, connection, BIND_AUTO_CREATE);
	}

	private void unbindMqttService() {
		unbindService(connection);
	}

	private void pubMsg(final String topic, final String msg) {
		mMqttService.publishMessage(topic, msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dev);
		
		bindMqttService();
		
		Button bStart = (Button) findViewById(R.id.devTV);
		bStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("dev/tv", "on");
			}
		});

		Button bMyPC = (Button) findViewById(R.id.devWindow);
		bMyPC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("dev/window", "on");
			}
		});

		Button bDesktop = (Button) findViewById(R.id.devFan);
		bDesktop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("dev/fan", "on");
			}
		});

		Button bSwitch = (Button) findViewById(R.id.devSwitch);
		bSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("dev/switch", "on");
			}
		});

		Button bClose = (Button) findViewById(R.id.devLight);
		bClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("dev/light", "on");
			}
		});
	}

	@Override
	protected void onDestroy() {
		unbindMqttService();
		super.onDestroy();
	}
}
