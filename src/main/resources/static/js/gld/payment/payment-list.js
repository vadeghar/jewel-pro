var _saleContext = '/payment'
var url = baseUrl + _saleContext;
$(document).ready(function() {
    var sourceId = $('#sourceId').val();
    if ($('#menuItem').val().indexOf('sales') > -1) {
        url = baseUrl + _saleContext +'/sales';
    } else {
        url = baseUrl + _saleContext +'/purchases';
    }
    simpleCall(url, 'get', '', '', '', loadDatatable);
});

function loadDatatable(response) {
    $('#paymentsTable').DataTable().destroy();
    $('#paymentsTable').DataTable({
        data: response,
        columns: [
            {data: 'id'},
            {data: 'paymentDate', render: function (data, type, row) {
                                   return moment(data).format('DD/MM/yyyy HH:mm');
                                 }},
            {data: 'paymentMode'},
            {data: 'paidAmount'},
            {data: 'receivedBy'}
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }],
        order: [[0, 'desc']]
    });
}