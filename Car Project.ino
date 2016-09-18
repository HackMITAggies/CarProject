#include <Wire.h>
#include <SoftwareSerial.h>
#define MotorSpeedSet             0x82
#define PWMFrequenceSet           0x84
#define DirectionSet              0xaa
#define MotorSetA                 0xa1
#define MotorSetB                 0xa5
#define Nothing                   0x01
#define EnableStepper             0x1a
#define UnenableStepper           0x1b
#define Stepernu                  0x1c
//#define I2CMotorDriverAdd         0x0f   // Set the address of the I2CMotorDriver



void MotorSpeedSetAB(unsigned char MotorSpeedA , unsigned char MotorSpeedB, unsigned char I2CMotorDriverAdd)  {
  MotorSpeedA=map(MotorSpeedA,0,100,0,255);
  MotorSpeedB=map(MotorSpeedB,0,100,0,255);
  Wire.beginTransmission(I2CMotorDriverAdd); // transmit to device I2CMotorDriverAdd
  Wire.write(MotorSpeedSet);        // set pwm header 
  Wire.write(MotorSpeedA);              // send pwma 
  Wire.write(MotorSpeedB);              // send pwmb    
  Wire.endTransmission();    // stop transmitting
}
//set the prescale frequency of PWM, 0x03 default;
void MotorPWMFrequenceSet(unsigned char Frequence, unsigned char I2CMotorDriverAdd)  {    
  Wire.beginTransmission(I2CMotorDriverAdd); // transmit to device I2CMotorDriverAdd
  Wire.write(PWMFrequenceSet);        // set frequence header
  Wire.write(Frequence);              //  send frequence 
  Wire.write(Nothing);              //  need to send this byte as the third byte(no meaning)  
  Wire.endTransmission();    // stop transmitting
}
//set the direction of DC motor. 
void MotorDirectionSet(unsigned char Direction, unsigned char I2CMotorDriverAdd)  {     //  Adjust the direction of the motors 0b0000 I4 I3 I2 I1
  Wire.beginTransmission(I2CMotorDriverAdd); // transmit to device I2CMotorDriverAdd
  Wire.write(DirectionSet);        // Direction control header
  Wire.write(Direction);              // send direction control information
  Wire.write(Nothing);              // need to send this byte as the third byte(no meaning)  
  Wire.endTransmission();    // stop transmitting 
}

void MotorDriectionAndSpeedSet(unsigned char Direction,unsigned char MotorSpeedA,unsigned char MotorSpeedB, unsigned char I2CMotorDriverAdd)  {  //you can adjust the driection and speed together
  MotorDirectionSet(Direction, I2CMotorDriverAdd);
  MotorSpeedSetAB(MotorSpeedA,MotorSpeedB, I2CMotorDriverAdd);  
}

void Left()
{
    MotorSpeedSetAB(80,80, 0x0f);
    MotorSpeedSetAB(80,80, 0x0a);
    delay(10); //this delay needed
    MotorDirectionSet(0b1010, 0x0a);  //0b1010  Rotating in the positive direction 
    MotorDirectionSet(0b1010, 0x0f); 
    delay(1000); 
    MotorSpeedSetAB(0,0, 0x0f);
    MotorSpeedSetAB(0,0, 0x0a);
    delay(10);
    MotorDirectionSet(0b1010, 0x0f); 
    MotorDirectionSet(0b1010, 0x0a); 
    delay(1000);
}

void Right() //right
{
    MotorSpeedSetAB(80,80, 0x0f);
    MotorSpeedSetAB(80,80, 0x0a);
    delay(10); //this delay needed
    MotorDirectionSet(0b0101, 0x0a);  //0b1010  Rotating in the positive direction 
    MotorDirectionSet(0b0101, 0x0f); 
    delay(1000); 
    MotorSpeedSetAB(0,0, 0x0f);
    MotorSpeedSetAB(0,0, 0x0a);
    delay(10);
    MotorDirectionSet(0b0101, 0x0f); 
    MotorDirectionSet(0b0101, 0x0a); 
    delay(1000);
}

void Reverse() //0x0a is the left side //reverse
{
    MotorSpeedSetAB(50,50, 0x0f);
    MotorSpeedSetAB(50,50, 0x0a);
    delay(10); //this delay needed
    MotorDirectionSet(0b1010, 0x0a);  //0b1010  Rotating in the positive direction 
    MotorDirectionSet(0b0101, 0x0f); 
    delay(1000); 
    MotorSpeedSetAB(0,0, 0x0f);
    MotorSpeedSetAB(0,0, 0x0a);
    delay(10);
    MotorDirectionSet(0b1010, 0x0f); 
    MotorDirectionSet(0b0101, 0x0a); 
    delay(1000);
}

void Forward() //0x0f is the right side //forward
{
    MotorSpeedSetAB(50,50, 0x0f);
    MotorSpeedSetAB(50,50, 0x0a);
    delay(10); //this delay needed
    MotorDirectionSet(0b1010, 0x0f);  //0b1010  Rotating in the positive direction 
    MotorDirectionSet(0b0101, 0x0a); 
    delay(1000); 
    MotorSpeedSetAB(0,0, 0x0f);
    MotorSpeedSetAB(0,0, 0x0a);
    delay(10);
    MotorDirectionSet(0b1010, 0x0f); 
    MotorDirectionSet(0b0101, 0x0a); 
    delay(1000);
}

SoftwareSerial mySerial(10, 11);
void setup() {
  // put your setup code here, to run once:
  Wire.begin();
  Serial.begin(9600);
  delayMicroseconds(10000);
  //mySerial.begin(4800);
  pinMode(13, OUTPUT);
  digitalWrite(13, HIGH);
  delay(500);
  digitalWrite(13, LOW);
    Serial.write("TEMP");
}

void loop() {
  // put your main code here, to run repeatedly:
if(Serial.available())
  digitalWrite(13, LOW);
  if(Serial.available()){
    switch (Serial.read()) {
    case 'l':
    digitalWrite(13, HIGH);
      Left();
      digitalWrite(13, LOW);
      break;
    case 'r':
    digitalWrite(13, HIGH);
      Right();
      digitalWrite(13, LOW);
      break;
    case 'f':
    digitalWrite(13, HIGH);
      Forward();
      digitalWrite(13, LOW);
      break;
     case 'R':
     digitalWrite(13, HIGH);
      Reverse();
      digitalWrite(13, LOW);
      break;
    }
  }
  
}
