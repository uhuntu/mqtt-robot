int pin1 = 8; // 定义I1 接口
int pin2 = 9; // 定义I2 接口
int speedpin1 = 10;  // 定义EA(PWM 调速)接口
int pin3 = 7; // 定义I3 接口
int pin4 = 6; // 定义I4 接口
int speedpin2 = 5;   // 定义EB(PWM 调速)接口

void setup()
{
  pinMode(pin1, OUTPUT); // 定义该接口为输出接口
  pinMode(pin2, OUTPUT);
  pinMode(speedpin1, OUTPUT);
  pinMode(pin3, OUTPUT); //
  pinMode(pin4, OUTPUT);
  pinMode(speedpin2, OUTPUT);
  
  analogWrite(speedpin1, 250); // 输入PWM 信号速度设定值100
  analogWrite(speedpin2, 250); // 输入PWM 信号速度设定值100
}

void loop()
{
  digitalWrite(pin1, LOW); // 使直流电机顺时针转
  digitalWrite(pin2, HIGH);
  digitalWrite(pin3, LOW);
  digitalWrite(pin4, HIGH);
  delay(250);

  digitalWrite(pin1, LOW); // 使直流电机刹车停止
  digitalWrite(pin2, LOW);
  digitalWrite(pin3, LOW);
  digitalWrite(pin4, LOW);
  delay(250);

  digitalWrite(pin1, HIGH); // 使直流电机逆时针转
  digitalWrite(pin2, LOW);
  digitalWrite(pin3, HIGH);
  digitalWrite(pin4, LOW);
  delay(250);
  
  digitalWrite(pin1, HIGH); // 使直流电机刹车停止
  digitalWrite(pin2, HIGH);
  digitalWrite(pin3, HIGH);
  digitalWrite(pin4, HIGH);
  delay(250);
}


