MQTT_SERVER=192.168.11.11

echo "Ping'in MQTT server..."
ping -c1 $MQTT_SERVER > /dev/null
while [ $? != 0 ]; do
        echo MQTT server is unavailable.
        ping -c1 $MQTT_SERVER > /dev/null
done
echo MQTT server is available.

if [ ! -e /dev/ttyACM0 ]; then
        echo Can not detect serial port.
        exit
fi

stty -F /dev/ttyACM0 raw speed 9600 -echo > /dev/null
echo Setting Serial port properly.

killall cat 2&> /dev/null
killall mosquitto_sub 2&> /dev/null

#cat /dev/ttyACM0 > /dev/null &
/root/cat.sh > /dev/null &

sleep 2
echo start > /dev/ttyACM0

echo Start monitoring the commands from MQTT server...
mosquitto_sub -q 2 -t play -h $MQTT_SERVER | while read line; do
        echo We got action $line.;
        echo $line > /dev/ttyACM0;
done
