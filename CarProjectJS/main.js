/*jslint node:true, vars:true, bitwise:true, unparam:true */
/*jshint unused:true */
/*jshint esnext: true */
// Leave the above lines for propper jshinting
//Type Node.js Here :)\
var firebase = require("firebase");
var five = require("johnny-five");
var Edison = require("galileo-io");
var board = new five.Board({
        io: new Edison()
});

board.on("ready", () => {
    console.log("Start");
    
    var SerialPort = require("serialport");  
var port = "/dev/ttyACM0";  
var serialPort = new SerialPort(port, {  
  baudrate: 9600  
});
    SerialPort.list(function (err, ports) {
  ports.forEach(function(port) {
    console.log(port.comName);
  });
});
    
      serialPort.on("open",function() {
    console.log("open");
    console.log("Connected to "+port);
        serialPort.on('data', function(data) {    
            console.log('data received: ' + data);   
            
        });    
});
    
  //var lcd = new five.LCD({
//   controller: "JHD1313M1"
  //});
    
  //lcd.bgColor("white");
  //lcd.home().print("Hello World!");
    
    // Initialize Firebase
  var config = {
    apiKey: "AIzaSyC6Twgb_m-6gJLp7KCLG6PgaShMc1O_b20",
    authDomain: "hackmit-e9161.firebaseapp.com",
    databaseURL: "https://hackmit-e9161.firebaseio.com",
    storageBucket: "hackmit-e9161.appspot.com",
    messagingSenderId: "254290983004"
  };
  firebase.initializeApp(config);
  // Set up references
  var databaseRef = firebase.database().ref();
  //var storageRef = firebase.storage().ref();
  var auth = firebase.auth();
    
  //databaseRef.set({
  //    status: {
  //      message: "San Francisco"
  //    }
  //});
    
  databaseRef.child("status/message").on("value", function(snapshot) {
    //lcd.clear();
    //lcd.home().print(snapshot.val());
      console.log(snapshot.val());
      
      if(!serialPort.isOpen())
        serialPort.open();

      console.log("open");
      console.log("Connected to "+port);
      serialPort.write(snapshot.val());
      //serialPort.close(); 
  });

   

    

});