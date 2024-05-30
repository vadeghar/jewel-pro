var _saleContext = '/estimation'
var url = baseUrl + _saleContext;

$(document).ready(function() {
    simpleCall(url, 'get', '', '', '', loadDatatable);
})

function loadDatatable(response) {
    $('#estimationListTable').DataTable().destroy();
    $('#estimationListTable').DataTable({
        data: response,
        columns: [
            {data: 'estimationNo'},
            {data: 'estimationDate', render: function (data, type, row) {
                                   return data != null ? moment(data).format('DD/MM/yyyy') : '';
                                 }},
            {data: 'grandTotalEstimationAmount', render: function (data, type, row) {return toCurrency(data)}},
            {
              data: 'id',
              render: function (data, type, row) {
                  return '<a class="btn btn-sm btn-primary viewSale" href="/estimation/view?id='+data+'"  data-id="' + data +'"><i class="fas fa-eye"></i></a> ';
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

$(document).on('click', '.addPayment', function(){
    var source = 'Sale';
    var sourceId = $(this).data('id');
    var name = $(this).data('name');
    console.log('sourceId: '+sourceId);
    $.get('/sale/add-payment', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        $('#myModalPlaceHolder').html(htmlData);
    });
//        $('#myModalPlaceHolder').html('content.html');
    $('#myModal').modal({show:true});
})

$(document).on('click', '.showPayments', function(){
    var source = 'Sale';
    var sourceId = $(this).data('id');
    var name = $(this).data('name');
    var billNo = $(this).data('billno');
    console.log('sourceId: '+sourceId);
    $.get('/sale/payment-modal', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        $('#myModalLabel').text(name+' Payments (Bill No. '+billNo+')')
        $('#myModalPlaceHolder').html(htmlData);
    });
    $('#myModal').modal({show:true});
})