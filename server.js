
var PORT = process.env['app_port'] || 8080;

var http = require('http');
var httpServer = http.createServer(function (request, response) {
}).listen(PORT);

var io = require('socket.io').listen(httpServer);


var users = {};


io.sockets.on('connection', function (socket) {
	var newId = getNewId();
	users[newId] = {'x':480, 'y':240};
	socket.set('id', newId);
	socket.emit('id', {'id': newId});
  
  
	socket.on('newPos', function (pos) {
		socket.get('id', function (err, name) {
			console.log('pos de ' + name + 'en ' + pos);
			users[name] = pos;
			socket.broadcast.emit('newPos', {'id': name, 'pos': users[name]});
		});
	});
  
});

function getNewId(){
	for(var i=0;i<20;++i){
		if(users[i] == null){
			return i;
		}
	}
	console.log("Mas de 20 personas");
	return 21;
}






