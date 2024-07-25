var _customerContext = '/supplier'
var url = baseUrl + _customerContext;
var _rules = {
    name: "required",
    phone: {
        required: true,
        minlength: 10
    },
    location: "required",
    businessName: "required",
    businessGstNo: "required"
}

var _messages = {
    name: {
        required: "Please enter your name."
    },
    phone: {
        required: "Please select active status.",
        minlength: "Number should be 10 digits."
    },
    location:{
         required: "Please enter location."
    },
    businessName:{
         required: "Please enter Business name."
    },
    businessGstNo:{
      required: "Please enter GST No."
    }
}
$(document).ready(function() {
    var urlParams = getUrlVars();
    if(urlParams.id) {
        // Get the value of the 'id' parameter
        id = urlParams.id;
        console.log("ID:", id);
        simpleCall(url+'/'+id, 'get', '', '', '', loadSupplierCallback);
    }
});

$(document).on('click', '#saveSupplier', function(event) {
    var valid = validateForm("#supplierForm", _rules, _messages)
  if (!valid) {
    event.preventDefault();
    return;
  }
  event.preventDefault(); // Prevent form submission (optional)
  var customerRequest = formToJson('#supplierForm');
  console.log(JSON.stringify(customerRequest));
  simpleCall(url, 'post', '', '', JSON.stringify(customerRequest), saveSupplierCallback);
});
$(document).on('click', '#resetSupplier', function() {
    resetForm('#customerForm');
});

function saveSupplierCallback(response) {
    $('#id').val(response.id);
    $('#alert').html('<div class="alert alert-success fade show" role="alert">'+
                        '<strong>Success!</strong> Supplier '+response.name+' is successfully saved.'+
                      '</div>');
}

function loadSupplierCallback(response) {
    $('#id').val(response.id);
    $('#name').val(response.name);
    $('#location').val(response.location);
    $('#phone').val(response.phone);
    $('#active').val(response.active != null ? response.active.toString() : 'true');
    $('#businessName').val(response.businessName);
    $('#businessGstNo').val(response.businessGstNo);
    $('#resetSupplier').addClass('d-none');
    var btn = '<button type="button" class="btn btn-primary supplierSales" data-id="' + response.id +'" data-name="' + response.name + '" title="Supplier Sales" data-toggle="modal" data-target="#exampleModal">Sales</button> ' ;
    $('.myBtn').append(btn);
}

$(document).on('click', '.supplierSales', function() {
    var source = 'Supplier';
    var sourceId = $(this).attr('data-id');
    var name = $(this).attr('data-name');
    console.log('sourceId: '+sourceId);
    $.get('/supplier/supplier-sale', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        $('#myModalLabel').text(name+' Sale List')
        $('#myModalPlaceHolder').html(htmlData);
    });
    $('#myModal').modal({show:true});
});