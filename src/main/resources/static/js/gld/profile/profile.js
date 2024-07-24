var _customerContext = '/profile'
var url = baseUrl + _customerContext;
var loggedInUser = $('#loggedInUser').text();
$(document).ready(function() {
    if (loggedInUser && loggedInUser != '') {
        simpleCall(url+'/'+loggedInUser, 'get', '', '', '', loadUser);
    }
})

function loadUser(response) {
    $('#username').val(response.username);
    $('#name').val(response.name);
    $('#email').val(response.email);
    $('#id').val(response.id);
}


$('#saveProfile').on('click', function(){
    var request = formToJson('#profileForm');
    console.log(JSON.stringify(request));
    simpleCall(url+'/'+loggedInUser, 'put', '', '', JSON.stringify(request), saveCallback);
})

function saveCallback(response) {
    $('#username').val(response.username);
    $('#name').val(response.name);
    $('#email').val(response.email);
    $('#id').val(response.id);
    showSuccessMsg('User Profile updated successfully.')
}