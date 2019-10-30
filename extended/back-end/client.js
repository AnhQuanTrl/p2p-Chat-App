var express = require('express');
var path = require('path');
var ejs = require('ejs');
var bodyParser = require('body-parser');
var net = require('net');
var ip = require('ip');



// LISTEN PORT & HOST
const LISTEN_PORT = 3250;
const LISTEN_HOST = ip.address();

// SERVER PORT & HOST
const SERVER_PORT = 7000;
const SERVER_HOST = ip.address();

// Host of admin
const PORT = 8989;

var client = express();
var http = require('http').createServer(client);

const io = require('socket.io')(http);

// Client set-ups
client.use(bodyParser.urlencoded({ extended: true }));
client.use('/static', express.static(path.join(path.resolve(__dirname, '..'), 'public')));
client.set('views', path.join(path.resolve(__dirname, '..'), 'views'));
client.set('view engine', 'ejs');

var sockets = new Map();


// Handle GET request 
client.get('/', function (req, res) {

    io.once('connection', function (iosocket) {
        var room = iosocket.handshake.address;
        iosocket.join(room);

        iosocket.once('request_connection', function () {
            // If there is no connection yet, create one
            if (!sockets.has(req.ip)) {

                var socket_to_server = new net.createConnection(SERVER_PORT, SERVER_HOST, function () {
                    socket_to_server.setEncoding("utf-8");
                    socket_to_server.write('/WEB-FETCH\n');
                });


                socket_to_server.once('data', function (data) {
                    data = data.split(/ \r?\n/)[0].substring(data.search("/WEB-UNFETCH ") + "/WEB-UNFETCH ".length);
                    data = data.split(" ");
                    var available_admins = new Set();
                    data.forEach(element => {
                        available_admins.add(element.replace('/', ""));
                    });
                    socket_to_server.write('/EXIT');
                    socket_to_server.end();

                    if (available_admins.size != 0) {
                        var admin_chooser = Math.floor((Math.random() * (available_admins.size * 250)))  % available_admins.size ;

                        var chose_ip = "";

                        available_admins.forEach(element => {
                            if (admin_chooser == 0)
                                chose_ip = element;
                            admin_chooser -= 1;
                        });
                        var socket = new net.createConnection(PORT, chose_ip, function () {
                            socket.setEncoding("utf-8");
                            socket.write("/REQUEST-SESSION GUEST-" + req.ip + "\n");
                        });

                        sockets.set(req.ip, socket);

                        sockets.get(req.ip)
                            .once('error', function (err) {
                                console.log(err);
                                sockets.delete(req.ip);
                            })
                            ;

                        iosocket.emit('connection_established');
                    }
                    else {
                        iosocket.emit('connection_refused');
                    }

                    // If connection is eventually established, handle these events
                    if (sockets.has(req.ip)) {
                        // Handle messages from client
                        sockets.get(req.ip)
                            .on('data', function (data) {
                                var protocol;

                                if (data.search(" ") == -1)
                                    protocol = data.split(/\r?\n/)[0];
                                else {
                                    protocol = data.substring(0, data.indexOf(" "));
                                    data = data.substring(data.indexOf(" ") + 1, data.indexOf("\n"));
                                }

                                if (protocol == "/MESSAGE") {
                                    iosocket.emit('user_get_message', { message: data });
                                    iosocket.to(room).emit('user_get_message', { message: data });
                                }
                                else if (protocol == '/DENY-SESSION') {
                                    iosocket.in(room).emit('connection_refused');
                                    sockets.get(req.ip).emit('end');
                                }
                                else if (protocol == '/ACCEPT-SESSION') {
                                    iosocket.in(room).emit('connection_established');
                                }
                            })

                            .on('end', function () {
                                iosocket.disconnect();
                                sockets.delete(req.ip);
                            })
                            ;
                    }
                })

            }
            else {
                iosocket.emit('connection_established');
            }

            iosocket
                .on('user_send_message', function (data) {
                    sockets.get(req.ip).write('/MESSAGE ' + data.message + '\n');
                    iosocket.to(room).emit('user_get_self_message', { message: data.message });
                })

                .on('disconnect', function () {
                    if (!io.sockets.adapter.rooms[room])
                        if (sockets.get(req.ip))
                            sockets.get(req.ip).emit('end');
                            sockets.delete(req.ip);
                })
                ;
        });
    });

    // And render the scene
    res.render('chatSession');
});

// Listen server on port 3250
http.listen(LISTEN_PORT, LISTEN_HOST);
console.log("Server is listening on port 3250.");


/////////////////////////////////////////////////////////////////



