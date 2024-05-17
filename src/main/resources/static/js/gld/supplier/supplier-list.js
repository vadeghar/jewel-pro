var _supplierContext = '/supplier'
var url = baseUrl + _supplierContext;

$(document).ready(function() {
    simpleCall(url, 'get', '', '', '', loadDatatable);
});

$(document).on('click', '.supplierPurchases', function() {
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

function loadDatatable(response) {
    $('#supplierTable').DataTable().destroy();
    $('#supplierTable').DataTable({
        data: response,
        columns: [
            {data: 'id'},
            {data: 'name'},
            {data: 'location'},
            {data: 'phone'},
            {data: 'businessGstNo'},
            {
              data: 'id',
              render: function (data, type, row) {
                  return '<button type="button" class="btn btn-sm btn-primary supplierPurchases" data-id="' + data +'" data-name="' + row.name + '" title="Supplier Purchases / Orders" data-toggle="modal" data-target="#exampleModal"><i class="fa-brands fa-first-order-alt"></i></button> ' +
                      '<a href="/supplier?id='+data+'" class="btn btn-sm btn-primary editRowCls" data-id="' + data +'" title="Edit Supplier"><i class="fas fa-edit"></i></a> ';
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
