var stompClient = null;

var player;

function setConnected(connected) {
    $("#disconnect").prop("disabled", !connected);
    if (player == 1){
        $("#connect1").prop("disabled", false);}
    else {
        $("#connect2").prop("disabled", false);
    }
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connectPlayer(playernum) {/*
    if (playernum == 1){
        stompClient.send("/app/connectedPlayers", {}, JSON.stringify({'player1': false, 'player2':true }));}
    else {
        stompClient.send("/app/connectedPlayers", {}, JSON.stringify({'player1': true, 'player2':false }));}
    disconnect();*/

    var socket = new SockJS('/player1');
    player = playernum;
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/player/'+player, function (greeting) {
            console.log(greeting.body);
            showGreeting(JSON.parse(greeting.body));
        });
    });
}

function connectToLobbySocket(){/*
    var socket = new SockJS('/connections');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/players', function (connectedPlayers) {
            console.log(connectedPlayers.body);
            parsedPlayers = JSON.parse(connectedPlayers.body)
            // $("#connect1").prop("disabled", !parsedPlayers.player1);
            // $("#connect2").prop("disabled", !parsedPlayers.player2);
        });
    });*/
}
/*
    ToDo: LobbyWebSocket that triggers GET / POST request for Connected Players

    During Lobby the page is connected to LobbyWebSocket and received messages will trigger
    the GET / POST request to update the buttons

 */

function disconnect() {

    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendPlayerDisconnect(){
    if (player == 1){
        stompClient.send("/app/connectedPlayers", {}, JSON.stringify({'player1' : false, 'player2' : null }));}
    else {
        stompClient.send("/app/connectedPlayers", {}, JSON.stringify({'player1' : null, 'player2': false }));
    }
    player = 0;
}

function sendName() {
        stompClient.send("/app/"+player, {}, JSON.stringify({'placement': $("#name").val()}));
}

function sendPublicName() {
        stompClient.send("/app/1", {}, JSON.stringify({'placement': $("#name").val()}));
        stompClient.send("/app/2", {}, JSON.stringify({'placement': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message.x + message.y + message.z + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect1" ).click(function() { connectPlayer(1); });
    $( "#connect2" ).click(function() { connectPlayer(2); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#sendToAll" ).click(function() { sendPublicName(); });
});
