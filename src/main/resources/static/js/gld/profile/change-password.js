var _customerContext = '/profile'
var url = baseUrl + _customerContext;
var loggedInUser = $('#loggedInUser').text();
$(document).ready(function() {
})

$('#savePassword').on('click', function(){
    var pwd = $('#password').val();
    var cpwd = $('#confirmPassword').val();
    if (pwd != cpwd) {
        alert('Password and Confirm Password are not same.');
        return;
    }
    var request = formToJson('#profileForm');
    console.log(JSON.stringify(request));
    simpleCall(url+'/change-password/'+loggedInUser, 'put', '', '', JSON.stringify(request), saveCallback);
})

function saveCallback(response) {
    showSuccessMsg('Password updated successfully.')
}