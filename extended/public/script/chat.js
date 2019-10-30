$(function(){
    // Host and port of admin
    var socket = io();

    var textInput = $('.txt-chat-field');
    var chatArea = $('.chat-container');

    textInput.on('keypress',function(e) {
        if(e.which == 13) {
            socket.emit('user_send_message', {message: textInput.val()});
            var text = `<div class="text-user-container">
            <p>Me: `
                + textInput.val() +
            `</p></div>`;
            chatArea.append(text);
            textInput.val('');            
        }
    });

    socket
    .on('user_get_message', function(data){
        var text = `<div class="text-server-container">
            <p>Server: `
                + data.message +
            `</p></div>`;
        chatArea.append(text);
    })

    .on('user_get_self_message', function(data){
        var text = `<div class="text-user-container">
        <p>Me: `
            + data.message +
        `</p></div>`;
        chatArea.append(text);
    })

    .on('disconnect', function()
    {
        textInput.prop('disabled', true);
    })
    
    .on('connection_refused', function()
    {
        textInput.prop('disabled', true);
    })

    .on('connection_established', function()
    {
        textInput.prop('disabled', false);
        socket.emit('new_window');
    })
    ;

    window.onbeforeunload = function () {
        socket.emit('window_close_or_reload');
    };


    $('.btn-danger').on('click', function(e){
        e.preventDefault();

        $('.form-popup').hide();
    })
    textInput.prop('disabled', true);
    $('.open-button').on('click', function(e)
    {
        e.preventDefault();
        // Firstly, disable the text input...

        // Then try to establish the connection
        socket.emit('request_connection');

        $('.form-popup').show();
        
    })

})


