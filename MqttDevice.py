from Tkinter import *
import os, time
import mosquitto

def on_connect(mosq, obj, rc):
    mosq.subscribe("dev/#", 0)
    print("rc: "+str(rc))

def on_message(mosq, obj, msg):
    print(msg.topic+" "+str(msg.qos)+" "+str(msg.payload))
    mess = msg.topic+" "+str(msg.qos)+" "+str(msg.payload)
    if msg.topic == "dev/tv":
        mess = "TV is " + str(msg.payload)
        lab_tv.config(text=mess)
    if msg.topic == "dev/fan":
        mess = "FAN is " + str(msg.payload)
        lab_fan.config(text=mess)
    if msg.topic == "dev/switch":
        mess = "Switch is " + str(msg.payload)
        lab_switch.config(text=mess)
    if msg.topic == "dev/window":
        mess = "Window is " + str(msg.payload)
        lab_window.config(text=mess)
    if msg.topic == "dev/light":
        mess = "Light is " + str(msg.payload)
        lab_light.config(text=mess)

def on_publish(mosq, obj, mid):
    print("mid: "+str(mid))

def on_subscribe(mosq, obj, mid, granted_qos):
    print("Subscribed: "+str(mid)+" "+str(granted_qos))

def on_log(mosq, obj, level, string):
    print(string)

mqttc = mosquitto.Mosquitto()
mqttc.on_message = on_message
mqttc.on_connect = on_connect
mqttc.on_publish = on_publish
mqttc.on_subscribe = on_subscribe

mqttc.connect("localhost", 1883, 60)

win = Tk()
win.title('MQTT Device')
win.geometry('400x200')

lab_tv = Label(win, text="TV is off")
lab_tv.pack(side=TOP, expand=YES, fill=BOTH)
lab_fan = Label(win, text="FAN is off")
lab_fan.pack(side=TOP, expand=YES, fill=BOTH)
lab_switch = Label(win, text="Switch is off")
lab_switch.pack(side=TOP, expand=YES, fill=BOTH)
lab_window = Label(win, text="Window is off")
lab_window.pack(side=TOP, expand=YES, fill=BOTH)
lab_light = Label(win, text="Light is off")
lab_light.pack(side=TOP, expand=YES, fill=BOTH)

#mainloop()
#mqttc.loop_forever()

def task():
   mqttc.loop()
   win.update()
   time.sleep(0.1)
while 1:
   task()  
