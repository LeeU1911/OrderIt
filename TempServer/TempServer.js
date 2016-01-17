var express = require('express');
var app = express();

const PORT= process.env.PORT || 8080;

app.get('/', function (req, res) {
  res.send('{"response":"success"}');
});

app.listen(PORT, function () {
  console.log('Example app listening on port %s!', PORT);
})