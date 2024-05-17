var _supplierContext = '/purchase'
var url = baseUrl + _supplierContext;

$(document).ready(function() {
    var supplierId = $('#sourceId').val();
    simpleCall(url+'/supplier/'+supplierId, 'get', '', '', '', loadDatatable);
//alert($('#sourceId').val())
});

function loadDatatable(response) {
    $('#supplierPurchaseTable').DataTable().destroy();
    $('#supplierPurchaseTable').DataTable({
        data: response,
        columns: [
            {data: 'purchaseBillNo',render: function (data, type, row) {
                                         return '<a href="/purchase/view?id='+row.id+'" class="purchase-link" data-purchase-id="' + row.id + '">' + data + '</a>';
                                       }},
            {data: 'purchaseDate'},
            {data: 'totalPurchaseAmount', render: function (data, type, row) {
                                                     return toCurrency(data);
                                                  }},
            {data: 'paidAmount', render: function (data, type, row) {
                                            return toCurrency(data);
                                         }},
            {data: 'balAmount', render: function (data, type, row) {
                                           return toCurrency(data);
                                        }},
//                {
//                  data: 'id',
//                  render: function (data, type, row) {
//                      return '<button type="button" class="btn btn-sm btn-primary supplierPurchases" data-id="' + data +'" title="Supplier Purchases / Orders" data-toggle="modal" data-target="#exampleModal"><i class="fa-brands fa-first-order-alt"></i></button> ';
//                  }
//                }
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }],
        order: [[0, 'desc']]
    });
}