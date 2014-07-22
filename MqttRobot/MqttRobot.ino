#include <IRremote.h>

int led = 13;

int I1 = 8;
int I2 = 9;
int EA = 10;
int I3 = 7;
int I4 = 6;
int EB = 5;

int SEND_PIN = 3;
int RECV_PIN = 11;
IRrecv irrecv(RECV_PIN);
IRsend irsend;
decode_results results;
unsigned int rawCodes[RAWBUF]; // The durations if raw
unsigned int irCode[5][33] = {
    {300,1800,250,1850,250,1850,250,800,250,800,250,800,250,1850,300,750,250,1850,300,1800,250,800,250,1850,300,750,250,800,250,800,250,800,250}, // start
    {300,1800,300,1850,250,1850,250,750,300,800,250,800,250,1800,300,800,250,1850,250,1800,300,1800,300,1850,250,800,250,800,250,800,250,800,250}, // mypc
    {300,1800,300,1800,250,1850,250,800,250,800,300,750,300,1800,300,750,300,1800,250,1850,250,1850,300,750,300,750,300,750,250,800,250,800,300}, // desktop
    {300,1800,250,1850,300,1800,300,750,300,750,300,750,250,1800,300,800,250,1850,300,1800,300,1800,300,750,300,1800,250,800,250,750,300,750,350}, // switch
    {300,1800,300,1800,300,1800,300,750,300,750,300,700,300,1800,350,750,300,750,300,750,300,1800,300,750,300,700,350,1750,300,750,300,750,350} // close
};

int head = 0;
int rear = 1;

int head_i = 0, head_val = 0, rear_i = 0, rear_val = 0;
int count = 0;
int pwm = 150;

String carStatus = "none";

String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

// the setup routine runs once when you press reset:
void setup() {
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
  // initialize the digital pin as an output.
  pinMode(led, OUTPUT);
  // reserve 200 bytes for the inputString:
  inputString.reserve(200);

  pinMode(head, INPUT);
  pinMode(rear, INPUT);

  pinMode(I1, OUTPUT);
  pinMode(I2, OUTPUT);
  pinMode(EA, OUTPUT);
  pinMode(I3, OUTPUT);
  pinMode(I4, OUTPUT);
  pinMode(EB, OUTPUT);
  
  carStop();
  
  irrecv.enableIRIn();
}

void carStart() {
  carStatus = "start";
  
  analogWrite(EA, pwm);
  analogWrite(EB, pwm);

  digitalWrite(led, HIGH);   // turn the LED on (HIGH is the voltage level)
}

void carStop() {
  carStatus = "stop";
  
  digitalWrite(I1, HIGH);
  digitalWrite(I2, HIGH);
  digitalWrite(I3, HIGH);
  digitalWrite(I4, HIGH);

  digitalWrite(led, LOW);    // turn the LED off by making the voltage LOW
}

void carMove() {
  carStatus = "move";
  
  digitalWrite(I1, HIGH);
  digitalWrite(I2, LOW);
  digitalWrite(I3, HIGH);
  digitalWrite(I4, LOW);
}

void carBack() {
  carStatus = "back";
  
  digitalWrite(I1, LOW);
  digitalWrite(I2, HIGH);
  digitalWrite(I3, LOW);
  digitalWrite(I4, HIGH);
}

void carLeft() {
  carStatus = "left";
  
  digitalWrite(I1, LOW);
  digitalWrite(I2, HIGH);
  digitalWrite(I3, HIGH);
  digitalWrite(I4, LOW);
}

void carRight() {
  carStatus = "right";
  
  digitalWrite(I1, HIGH);
  digitalWrite(I2, LOW);
  digitalWrite(I3, LOW);
  digitalWrite(I4, HIGH);
}

// the loop routine runs over and over again forever:
void loop() {
#if 0
/*
  digitalWrite(led, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(1000);               // wait for a second
  Serial.println(1);
  digitalWrite(led, LOW);    // turn the LED off by making the voltage LOW
  delay(1000);               // wait for a second
  Serial.println(0);
*/
#if 0
  float volts = analogRead(head)*0.0048828125;    // value from sensor * (5/1024) - if running 3.3.volts then change 5 to 3.3
  float distance = 65*pow(volts, -1.10);          // worked out from graph 65 = theretical distance / (1/Volts)S - luckylarry.co.uk
  Serial.println(distance);                      // print the distance
  delay(100);  // arbitary wait time.
#endif
#else
  int temp_val;

  head_i = analogRead(head);
  temp_val = (6762 / (head_i - 9)) - 4;
#if 0
  if (temp_val != head_val) {
    Serial.print("head = ");
    Serial.println(head_val);
  }
#endif
  head_val = temp_val;
  
  rear_i = analogRead(rear);
  temp_val = (6762 / (rear_i - 9)) - 4;
#if 0
  if (temp_val != rear_val) {
    Serial.print("rear = ");
    Serial.println(rear_val);
  }
#endif
  rear_val = temp_val;

#if 0
  Serial.print("carStatus = ");
  Serial.print(carStatus);
  Serial.print(" | head = ");
  Serial.print(head_val);
  Serial.print(" | rear = ");
  Serial.println(rear_val);
#endif

  if ((head_val < 15 && carStatus == "move") || (rear_val < 15 && carStatus == "back")) {
    carStop();
    Serial.print("Car Stop. ");
    Serial.print("head = ");
    Serial.print(head_val);
    Serial.print(" | rear = ");
    Serial.println(rear_val);
  }

#if 0
  if (count ++ > 1000) {  
    Serial.print("head = ");
    Serial.print(head_val);
    Serial.print(" | rear = ");
    Serial.println(rear_val);
    count = 0;
  }
#endif

  // print the string when a newline arrives:
  if (stringComplete) {
    Serial.print("Got Message: ");
    Serial.println(inputString);
    if (inputString == "ir_start") {
      ir_send(0);
    }
    else if (inputString == "ir_mypc") {
      ir_send(1);
    }
    else if (inputString == "ir_desktop") {
      ir_send(2);
    }
    else if (inputString == "ir_switch") {
      ir_send(3);
    }
    else if (inputString == "ir_close") {
      ir_send(4);
    }
    else if (inputString == "start") {
      carStart();
    }
    else if (inputString == "stop") {
      carStop();
    }
    else if (inputString == "move") {
      carStart();
      carMove();
    }
    else if (inputString == "back") {
      carStart();
      carBack();
    }
    else if (inputString == "left") {
      carStart();
      carLeft();
    }
    else if (inputString == "right") {
      carStart();
      carRight();
    }
    else if (inputString == "test") {
#if 1
      carStart();
      carLeft();
      delay(100);
      carRight();
      delay(200);
      carLeft();
      delay(100);
      carMove();
      delay(100);
      carBack();
      delay(200);
      carMove();
      delay(100);
      carStop();
#endif
    }
    //    Serial.flush();
    // clear the string:
    inputString = "";
    stringComplete = false;
  }
#endif

  if (irrecv.decode(&results)) {
//    Serial.println(results.value, HEX);
    dump(&results);
    irrecv.resume(); // Receive the next value
  }
}

void ir_send(int i) {
  irsend.sendRaw(irCode[i], 33, 38);
  Serial.println("Sent IR");
  irrecv.enableIRIn();
}

void dump(decode_results *results) {
  int count = results->rawlen;
  Serial.print(results->value, HEX);
  Serial.print(" (");
  Serial.print(results->bits, DEC);
  Serial.println(" bits)");
  Serial.print("Raw (");
  Serial.print(count, DEC);
  Serial.print("): ");

  for (int i = 1; i < count; i++) {
    if ((i % 2) == 1) {
      rawCodes[i - 1] = results->rawbuf[i]*USECPERTICK/* - MARK_EXCESS*/;
      Serial.print(results->rawbuf[i]*USECPERTICK, DEC);
    } 
    else {
      rawCodes[i - 1] = results->rawbuf[i]*USECPERTICK/* + MARK_EXCESS*/;
      Serial.print(-(int)results->rawbuf[i]*USECPERTICK, DEC);
    }
    Serial.print(" ");
  }
  Serial.println("");

  irsend.sendRaw(rawCodes, count - 1, 38);
  Serial.println("Sent raw");
  
  irrecv.enableIRIn(); // Start the receiver
}

void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read(); 
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      stringComplete = true;
      break;
    } 
    else {
      // add it to the inputString:
      inputString += inChar;
    }
  }
}

