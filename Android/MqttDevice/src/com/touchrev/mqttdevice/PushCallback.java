package com.touchrev.mqttdevice;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.content.Intent;
import android.util.Log;

public class PushCallback implements MqttCallback {
	private static final String TAG = "PushCallback";
	MQTTService mMqttService;
	public PushCallback(MQTTService mqttService) {
		mMqttService = mqttService;
	}

	@Override
	public void connectionLost(Throwable cause) {
		// We should reconnect here
	}

	@Override
	public void messageArrived(MqttTopic topic, MqttMessage message)
			throws Exception {
		Log.i(TAG, "TopicReceived: " + topic);
		Log.i(TAG, "messageArrived: " + message);
		
		Intent intent = new Intent();
		intent.setAction("com.justel.service");  
		intent.putExtra("topic", topic.toString());
		intent.putExtra("message", message.toString());
		mMqttService.sendBroadcast(intent);
	}

	@Override
	public void deliveryComplete(MqttDeliveryToken token) {
		// We do not need this because we do not publish
	}
}