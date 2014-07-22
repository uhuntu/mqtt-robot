var client, destination;
function connectMqtt ()
{
    var host = 'localhost';    
    var port = '61614';
    var clientId = 'mqtt-robot';
    var user = 'admin';
    var password = 'password';

    destination = 'play';

    client = new Messaging.Client(host, Number(port), clientId);

    client.onConnect = onConnect;

    client.onMessageArrived = onMessageArrived;
    client.onConnectionLost = onConnectionLost;            

    client.connect({
        userName:user, 
        password:password, 
        onSuccess:onConnect, 
        onFailure:onFailure
    });
}

var onConnect = function(frame) {
    client.subscribe(destination);
    player.style.backgroundColor = "green";
};  

function onFailure(failure) {
    alert("failure: " + failure.errorMessage);
}  

function onMessageArrived(message) {
//    alert("message: " + message.payloadString);
    var m = message.payloadString;
    move(m);
    player.style.left = 100 + player.mapX*32;
    player.style.top = 100 + player.mapY*32;
}

function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        alert(client.clientId + ": " + responseObject.errorCode + "\n");
    }
}

var myMap = [
[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], 
[1, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1], 
[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1], 
[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1], 
[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 1], 
[1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1], 
[1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1], 
[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1], 
[1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1], 
[1, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1], 
[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
];

var device = new Array(5);
var device_mqtt_topic = [
    "dev/tv", "dev/fan", "dev/switch", "dev/window", "dev/light"
];
function buildMap (map)
{
    var tile;
    var type;
    var count = 0;
    for (var i = 0; i < map.length; ++i) {
        for (var j = 0; j < map[0].length; ++j) {
            tile = document.createElement("div");
            document.body.appendChild(tile);
            tile.className = "tile"+map[i][j];
            tile.style.left = 100+32*j;
            tile.style.top = 100+32*i;
            if (map[i][j] == 2) {
                tile.mqtt_topic = device_mqtt_topic[count];
                tile.mapX = j;
                tile.mapY = i;
                device[count] = tile;
                count++;
            }
        }
    }
}

function detectDevice()
{
    var dev = null;
    if (myMap[player.mapY-1][player.mapX] == 2) {
        dev = findDevice(player.mapY-1, player.mapX);
    } else if (myMap[player.mapY+1][player.mapX] == 2) {
        dev = findDevice(player.mapY+1, player.mapX);
    } else if (myMap[player.mapY][player.mapX-1] == 2) {
        dev = findDevice(player.mapY, player.mapX-1);
    } else if (myMap[player.mapY][player.mapX+1] == 2) {
        dev = findDevice(player.mapY, player.mapX+1);
    }
    if (dev != null) {
        if (dev.style.backgroundColor == "" || dev.style.backgroundColor == "yellow") {
            dev.style.backgroundColor = "red";
            sendMessage(dev.mqtt_topic, "on");
        } else if (dev.style.backgroundColor == "red" ) {
            dev.style.backgroundColor = "yellow";
            sendMessage(dev.mqtt_topic, "off");
        }
    }
}

function sendMessage (topic, msg) {
    message = new Messaging.Message(msg);
    message.destinationName = topic;
    client.send(message);
}

function findDevice (y, x) {
    for (var i = 0; i < 5; i ++) {
        if (device[i].mapX == x && device[i].mapY == y) {
            return device[i];
        }
    }
}

buildMap(myMap);
connectMqtt();

var player = document.createElement("div");
document.body.appendChild(player);
player.className = "player";
player.mapX = 1;
player.mapY = 2;
player.style.left = 100 + player.mapX*32;
player.style.top = 100 + player.mapY*32;

document.onkeydown = function (event)
{ 
   window.status = event.keyCode;
   switch(event.keyCode)
   { 
     case 37:
        move("left");
        break; 
     case 38:
        move("move");
        break; 
     case 39:
        move("right");
        break; 
     case 40:
        move("back");
        break; 
   } 
   player.style.left = 100 + player.mapX*32;
   player.style.top = 100 + player.mapY*32;
}

function move(m) {
    if (m == "move") {
        if(myMap[player.mapY-1][player.mapX]==0)
        {
            player.mapY -= 1;
            detectDevice();
        }
    } else if (m == "back") {
        if(myMap[player.mapY+1][player.mapX]==0)
        {
            player.mapY += 1;
            detectDevice();
        }
    } else if (m == "left") {
        if(myMap[player.mapY][player.mapX-1]==0)
        {
            player.mapX -= 1;
            detectDevice();
        }
    } else if (m == "right") {
        if(myMap[player.mapY][player.mapX+1]==0)
        {
            player.mapX += 1;
            detectDevice();
        }
    }
}