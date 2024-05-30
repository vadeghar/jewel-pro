var _customerContext = '/customer'
var url = baseUrl + _customerContext;

$(document).ready(function() {
    var urlParams = getUrlVars();
    if(urlParams.id) {
        // Get the value of the 'id' parameter
        id = urlParams.id;
        console.log("ID:", id);
        simpleCall(url+'/'+id, 'get', '', '', '', loadCustomerCallback);
    }
});

$(document).on('click', '#saveCustomer', function() {
    //customerForm
    var customerRequest = formToJson('#customerForm');
    console.log(JSON.stringify(customerRequest));
    simpleCall(url, 'post', '', '', JSON.stringify(customerRequest), saveCustomerCallback);
});
$(document).on('click', '#resetCustomer', function() {
    resetForm('#customerForm');
});

function saveCustomerCallback(response) {
    $('#id').val(response.id);
    $('#alert').html('<div class="alert alert-success fade show" role="alert">'+
                        '<strong>Success!</strong> Customer '+response.name+' is successfully saved.'+
                      '</div>');
}

function loadCustomerCallback(response) {
    $('#id').val(response.id);
    $('#name').val(response.name);
    $('#phone').val(response.phone);
    $('#email').val(response.email);
    $('#address').val(response.address);
    $('#resetCustomer').addClass('d-none');
    var btn = '<button type="button" class="btn btn-primary customerPurchases" data-id="' + response.id +'" data-name="' + response.name + '" title="Customer Purchases / Orders" data-toggle="modal" data-target="#exampleModal">Purchases</button> ' ;
    $('.myBtn').append(btn);
}

$(document).on('click', '.customerPurchases', function() {
    var source = 'Customer';
    var sourceId = $(this).attr('data-id');
    var name = $(this).attr('data-name');
    console.log('sourceId: '+sourceId);
    $.get('/customer/customer-purchase', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        // Update the content of the modal body with the HTML content
        $('#myModalLabel').text(name+' Purchases List')
        $('#myModalPlaceHolder').html(htmlData);
    });
//        $('#myModalPlaceHolder').html('content.html');
    $('#myModal').modal({show:true});
});


