var _customerContext = '/customer'
var url = baseUrl + _customerContext;

$(document).ready(function() {
    simpleCall(url, 'get', '', '', '', loadDatatable);
});

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

function loadDatatable(response) {
    $('#customerTable').DataTable().destroy();
    $('#customerTable').DataTable({
        data: response,
        columns: [
            {data: 'id'},
            {data: 'name'},
            {data: 'phone'},
            {data: 'address'},
            {
              data: 'id',
              render: function (data, type, row) {
                  return '<button type="button" class="btn btn-sm btn-primary customerPurchases" data-id="' + data +'" data-name="' + row.name + '" title="Customer Purchases / Orders" data-toggle="modal" data-target="#exampleModal"><i class="fa-brands fa-first-order-alt"></i></button> ' +
                      '<button type="button" class="btn btn-sm btn-primary editRowCls" data-id="' + data +'" title="Edit Customer" data-toggle="modal" data-target="#exampleModal"><i class="fas fa-edit"></i></button> ';
              }
            }
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }],
        order: [[1, 'desc']]
    });
}
