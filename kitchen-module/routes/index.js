var express = require('express');
var router = express.Router();
var firebase = require('firebase');
var config = {
    apiKey: "AIzaSyAJhcwWoTxW2czDSkPH-zC8YxUc9y2UdwU",
    authDomain: "orderit-3e188.firebaseapp.com",
    databaseURL: "https://orderit-3e188.firebaseio.com",
    storageBucket: "orderit-3e188.appspot.com",
    messagingSenderId: "784106592734"
  };
firebase.initializeApp(config);

var provider = new firebase.auth.GoogleAuthProvider();
firebase.auth().signInWithPopup(provider).then(function(result) {
  // This gives you a Google Access Token. You can use it to access the Google API.
  var token = result.credential.accessToken;
  // The signed-in user info.
  var user = result.user;
  // ...
}).catch(function(error) {
  // Handle Errors here.
  var errorCode = error.code;
  var errorMessage = error.message;
  // The email of the user's account used.
  var email = error.email;
  // The firebase.auth.AuthCredential type that was used.
  var credential = error.credential;
  // ...
});
/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Order-It Kitchen' });
});

var user = firebase.auth().currentUser;

if (user) {
  console.log("Usage is signed in!");
} else {
  console.log("Usage is NOT signed in!");
}

module.exports = router;
