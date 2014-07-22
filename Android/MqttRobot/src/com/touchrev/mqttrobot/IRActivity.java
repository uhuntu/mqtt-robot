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

public class IRActivity extends Activity {
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

	private void pubMsg(final String msg) {
		mMqttService.publishMessage(msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ir);
		
		bindMqttService();
		
		Button bStart = (Button) findViewById(R.id.buttonStart);
		bStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("ir_start");
			}
		});

		Button bMyPC = (Button) findViewById(R.id.buttonMyPC);
		bMyPC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("ir_mypc");
			}
		});

		Button bDesktop = (Button) findViewById(R.id.buttonDesktop);
		bDesktop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("ir_desktop");
			}
		});

		Button bSwitch = (Button) findViewById(R.id.buttonSwitch);
		bSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("ir_switch");
			}
		});

		Button bClose = (Button) findViewById(R.id.buttonClose);
		bClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pubMsg("ir_close");
			}
		});
	}

	@Override
	protected void onDestroy() {
		unbindMqttService();
		super.onDestroy();
	}
}
