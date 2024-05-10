var _customerContext = '/sale'
var url = baseUrl + _customerContext;

$(document).ready(function() {
    var customerId = $('#sourceId').val();
    simpleCall(url+'/customer/'+customerId+'/sales', 'get', '', '', '', loadDatatable);
//alert($('#sourceId').val())
});

function loadDatatable(response) {
    $('#customerPurchaseTable').DataTable().destroy();
    $('#customerPurchaseTable').DataTable({
        data: response,
        columns: [
            {data: 'invoiceNo',render: function (data, type, row) {
                                         return '<a href="/sale/view?id='+row.id+'" class="invoice-link" data-sale-id="' + row.id + '">' + data + '</a>';
                                       }},
            {data: 'saleDate'},
            {data: 'grandTotalSaleAmount', render: function (data, type, row) {
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
//                      return '<button type="button" class="btn btn-sm btn-primary customerPurchases" data-id="' + data +'" title="Customer Purchases / Orders" data-toggle="modal" data-target="#exampleModal"><i class="fa-brands fa-first-order-alt"></i></button> ';
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