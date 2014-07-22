package com.touchrev.mqttrobot;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.util.Log;

public class PushCallback implements MqttCallback {
	private static final String TAG = "PushCallback";

	@Override
	public void connectionLost(Throwable cause) {
		// We should reconnect here
	}

	@Override
	public void messageArrived(MqttTopic topic, MqttMessage message)
			throws Exception {
		Log.i(TAG, "messageArrived: " + message);
	}

	@Override
	public void deliveryComplete(MqttDeliveryToken token) {
		// We do not need this because we do not publish
	}
}