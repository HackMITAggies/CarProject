var five = require("../lib/johnny-five");
var Edison = require("edison-io");
var board = new five.Board({
  io: new Edison()
});

board.on("ready", function() {
  var a = new five.Motor({
    controller: "GROVE_I2C_MOTOR_DRIVER",
    pin: "A",
  });

  var b = new five.Motor({
    controller: "GROVE_I2C_MOTOR_DRIVER",
    pin: "B",
  });


  this.wait(3000, function() {
    console.log("REVERSE");

    a.rev(127);
    b.rev(127);

    // Demonstrate motor stop in 2 seconds
    this.wait(3000, function() {
      console.log("STOP");
      a.stop();
      b.stop();

      this.wait(1000, function() {
        process.emit("SIGINT");
      });
    }.bind(this));
  }.bind(this));

  console.log("FORWARD");
  a.fwd(127);
  b.fwd(127);
});alize Firebase
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
  });

   

    

});