MQTT_SERVER=192.168.11.11

cat /dev/ttyACM0 | while read line; do
        echo We got : $line
        mosquitto_pub -q 2 -t raw -h $MQTT_SERVER -m "$line"
done
