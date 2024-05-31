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
                      var addPaymentBtn = toCurrency(row.balAmount) > 0 ? '<button type="button" class="btn btn-sm btn-primary addPayment" data-name="'+row.customer.name+'" data-id="' + data +'" title="Add New Payment" >+<i class="fa-solid fa-coins"></i></button> ' : '';
                  return '<button type="button" class="btn btn-sm btn-primary showPayments" data-billno="'+row.invoiceNo+'" data-name="'+row.customer.name+'" data-id="' + data +'" title="Show Payments" ><i class="fa-solid fa-coins"></i></button> ' +
                  addPaymentBtn +
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