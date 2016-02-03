var pusher = new Pusher('e395e0ba85e39a4f0248', {
    encrypted: true,
    authEndpoint: '/authenticate'
});

var channel = pusher.subscribe('private-messages');

channel.bind('new_message', function(data) {
    console.log('got event', data);
    var p = document.createElement('p');
    p.innerHTML = data.text;
    document.body.appendChild(p);
});

var button = document.querySelector('button');
button.addEventListener('click', function() {
    fetch('/message', {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ text: 'New notification!' })
    });

});