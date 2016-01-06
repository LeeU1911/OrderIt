//Lets require/import the HTTP module
var http = require('http');

//Lets define a port we want to listen to
const PORT= process.env.PORT || 8080;

//We need a function which handles requests and send response
function handleRequest(request, response){
console.log(request);
    response.end('{"request":' + JSON.stringify(request));
}

//Create a server
var server = http.createServer(handleRequest);

//Lets start our server
server.listen(PORT, function(){
    //Callback triggered when server is successfully listening. Hurray!
    console.log("Server listening on: http://localhost:%s", PORT);
});
