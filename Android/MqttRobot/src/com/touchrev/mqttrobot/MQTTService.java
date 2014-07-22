package com.touchrev.mqttrobot;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MQTTService extends Service {
	public static final String BROKER_URL = "tcp://192.168.11.11:1883";
	public static final String TOPIC_PLAY = "play";
	public static final String TOPIC_RAW = "raw";
	protected static final String TAG = "MQTTService";

	private MqttClient mqttClient;

	private MyBinder mBinder = new MyBinder();

	public class MyBinder extends Binder {
		MQTTService getService() {
			return MQTTService.this;
		}
	}

	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void publishMessage(final String msg) {
		final MqttTopic messageTopic = mqttClient.getTopic(TOPIC_PLAY);
		final MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(2);
		try {
			messageTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void publishMessage(final String topic, final String msg) {
		final MqttTopic messageTopic = mqttClient.getTopic(topic);
		final MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(2);
		try {
			messageTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread() {
			@Override
			public void run() {
				try {
					mqttClient = new MqttClient(BROKER_URL,
							MqttClient.generateClientId(),
							new MemoryPersistence());
					mqttClient.setCallback(new PushCallback());
					Log.d(TAG, "mqtt connect.");
					mqttClient.connect();
					mqttClient.subscribe(TOPIC_RAW);
				} catch (MqttException e) {
					// Toast.makeText(getApplicationContext(),
					// "Something went wrong!" + e.getMessage(),
					// Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				super.run();
			}
		}.start();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		try {
			Log.d(TAG, "mqtt disconnect.");
			mqttClient.disconnect(0);
		} catch (MqttException e) {
			// Toast.makeText(getApplicationContext(),
			// "Something went wrong!" + e.getMessage(),
			// Toast.LENGTH_LONG)
			// .show();
			e.printStackTrace();
		}
	}
}