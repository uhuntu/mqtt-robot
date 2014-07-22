import web
import os

render = web.template.render('templates/')

urls = (
    '/', 'index',
    '/test', 'test',
    '/play', 'play'
)

class index:
    def GET(self):
        i = web.input(name=None)
        return render.index(i.name)

class test:
    def GET(self):
        os.system("mosquitto_pub -q 2 -t my/topic -m hello")
        return "time is up, return!"
		
class play:
    def GET(self):
        i = web.input(action = None)
        os.system("mosquitto_pub -q 2 -t play -m " + i.action)
        return "action = " + i.action

if __name__ == "__main__":
    app = web.application(urls, globals())
    app.run()
