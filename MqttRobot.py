from Tkinter import *
import os

def hello(e):
    print('hello world!')

def bye(e):
    print('bye world!')
    
def move(e):
    print('move!')
    os.system("mosquitto_pub -q 2 -t play -m move")

def back(e):
    print('back!')
    os.system("mosquitto_pub -q 2 -t play -m back")

def left(e):
    print('left!')
    os.system("mosquitto_pub -q 2 -t play -m left")

def right(e):
    print('right!')
    os.system("mosquitto_pub -q 2 -t play -m right")
    
def stop(e):
    print('stop!')
    os.system("mosquitto_pub -q 2 -t play -m stop")
    
def test():
    print('test!')
    os.system("mosquitto_pub -q 2 -t play -m test")
    
win = Tk()
win.title('MQTT Robot')
win.geometry('400x200')

#btnHello = Button(win, text='Click me')
#btnHello.bind("<Button-1>", hello)
#btnHello.bind("<ButtonRelease-1>", bye)
#btnHello.pack(expand=YES, fill=BOTH)

btnMove = Button(win, text='Move')
btnMove.bind("<Button-1>", move)
btnMove.bind("<ButtonRelease-1>", stop)
btnMove.pack(side=TOP, expand=YES)

btnBack = Button(win, text='Back')
btnBack.bind("<Button-1>", back)
btnBack.bind("<ButtonRelease-1>", stop)
btnBack.pack(side=BOTTOM, expand=YES)

btnLeft = Button(win, text='Left')
btnLeft.bind("<Button-1>", left)
btnLeft.bind("<ButtonRelease-1>", stop)
btnLeft.pack(side=LEFT, expand=YES)

btnTest = Button(win, text='Test', command=test)
btnTest.pack(side=LEFT, expand=YES, fill=BOTH)

btnRight = Button(win, text='Right')
btnRight.bind("<Button-1>", right)
btnRight.bind("<ButtonRelease-1>", stop)
btnRight.pack(side=RIGHT, expand=YES)

mainloop()
