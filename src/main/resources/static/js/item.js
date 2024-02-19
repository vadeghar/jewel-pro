//function addHeader(id) {
//    var _form = $('#'+id);
//    var xhr = new XMLHttpRequest();
//    xhr.open("POST", _form.attr('action'), true);
//    xhr.setRequestHeader("X-Application-Name", "Spring-Bill");
//    xhr.send(new FormData(document.querySelector('form')));
//    return false; // Prevent default form submission
//}

$(document).ready(function() {
//    $('#cancelBtn').on('click', function(event) {
//        // Add header dynamically
//        $(this).append('<input type="hidden" name="X-Application-Name" value="Spring-Bill">');
//    });

    $('#cancelBtn').click(function() {
            // Redirect to the home page
            window.location.href = '/home'; // Replace '/' with the URL of your home page
        });
});