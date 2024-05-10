var _saleContext = '/sale'
var url = baseUrl + _saleContext;

$(document).ready(function() {
    simpleCall(url, 'get', '', '', '', loadDatatable);
})

function loadDatatable(response) {
    $('#saleListTable').DataTable().destroy();
    $('#saleListTable').DataTable({
        data: response,
        columns: [
            {data: 'invoiceNo'},
            {data: 'saleDate', render: function (data, type, row) {
                                   return data != null ? moment(data).format('DD-MM-yyyy') : '';
                                 }},
            {data: 'customer', render: function (data, type, row) {
                                return (data != null && data.name != null) ? data.name : '';
                              }},
            {data: 'grandTotalSaleAmount', render: function (data, type, row) {return toCurrency(data)}},
            {data: 'paidAmount', render: function (data, type, row) {return toCurrency(data)}},
            {data: 'balAmount', render: function (data, type, row) {return toCurrency(data)}},
            {
              data: 'id',
              render: function (data, type, row) {
                  return '<button type="button" class="btn btn-sm btn-primary addPayment" data-id="' + data +'"><<i class="fa-solid fa-coins"></i></button> ' +
                      '<a class="btn btn-sm btn-primary viewSale" href="/sale/view?id='+data+'"  data-id="' + data +'"><i class="fas fa-eye"></i></a> ';
              }
            }
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }],
        order: [[0, 'desc']]
    });
}