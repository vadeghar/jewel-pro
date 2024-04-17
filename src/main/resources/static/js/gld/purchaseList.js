var baseUrl = 'http://localhost:8080/api/v1';
var _purchaseContext = '/purchase'
var url = baseUrl + _purchaseContext;

$(document).ready(function() {
    loadAllPurchases();
    $(document).on('click', '.addPurchaseItems', function() {
        var id = $(this).data('id');
        var form = $('<form></form>', {
            'method': 'POST',
            'action': 'purchase/purchase-items'
        });
        var idInput = $('<input>', {
            'type': 'hidden',
            'name': 'id',
            'value': id
        });
        form.append(idInput);
        $('body').append(form);
        form.submit();
    });

});

function loadAllPurchases() {
    simpleCall(url, 'get', '', '', '', loadPurchaseDataTable)
}

function loadPurchaseDataTable(response) {
    console.log(response);
    $('#saleItemsTable').DataTable({
        data: response,
        columns: [
            {data: 'id'},
            {data: 'supplierName'},
            {data: 'purchaseDate'},
            {data: 'totalNetWeight'},
            {data: 'totalPcs'},
            {
              data: 'id',
              render: function (data, type, row) {
                  return '<button type="button" class="btn btn-sm btn-danger" data-id="' + data + '"><i class="fas fa-trash"></i> Delete</button> ' +
                      '<button type="button" class="btn btn-sm btn-primary" data-id="' + data + '"><i class="fas fa-edit"></i> Edit</button> ' +
                      '<button type="button" class="btn btn-sm btn-success addPurchaseItems" data-id="' + data + '"><i class="fas fa-plus"></i> Add Items</button>';
              }
            }
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }]
    });
}