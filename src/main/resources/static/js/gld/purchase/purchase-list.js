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

    $(document).on('click', '.editPurchase', function() {
        var id = $(this).data('id');
        var form = $('<form></form>', {
            'method': 'POST',
            'action': '/purchase/edit-purchase'
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

    $(document).on('click', '.deletePurchase', function() {
        var id = $(this).data('id');
        createDynamicModal(id, "Delete Purchase.", "Are you sure want to delete?", "Yes", deletePurchase);
//        var form = $('<form></form>', {
//            'method': 'POST',
//            'action': 'purchase/edit-purchase'
//        });
//        var idInput = $('<input>', {
//            'type': 'hidden',
//            'name': 'id',
//            'value': id
//        });
//        form.append(idInput);
//        $('body').append(form);
//        form.submit();
    });



});

function deletePurchase(purchaseId) {
    simpleCall(url+'/'+purchaseId, 'delete', '', '', '', loadAllPurchases)
}

function loadAllPurchases() {
    simpleCall(url, 'get', '', '', '', loadPurchaseDataTable)
}

function loadPurchaseDataTable(response) {
    $('#saleItemsTable').DataTable().destroy();
    $('#saleItemsTable').DataTable({
        data: response,
        columns: [
            {data: 'metalType'},
            {data: 'supplierName'},
            {data: 'purchaseDate'},
            {data: 'totalNetWeight'},
            {data: 'balAmount'},
            {
              data: 'id',
              render: function (data, type, row) {
                 var addPaymentBtn = toCurrency(row.balAmount) > 0 ? '<button type="button" class="btn btn-sm btn-primary addPayment" data-name="'+row.supplierName+'" data-id="' + data +'" title="Add New Payment" >+<i class="fa-solid fa-coins"></i></button> ' : '';
                                  return '<button type="button" class="btn btn-sm btn-primary showPayments" data-billno="'+row.purchaseBillNo+'" data-name="'+row.supplierName+'" data-id="' + data +'" title="Show Payments" ><i class="fa-solid fa-coins"></i></button> ' +
                                  addPaymentBtn +
                                      '<a class="btn btn-sm btn-primary viewSale" href="/purchase/view?id='+data+'"  data-id="' + data +'"><i class="fas fa-eye"></i></a> ';
              }
            }
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }],
        order: [[2, 'desc']]
    });
}

$(document).on('click', '.addPayment', function(){
    var source = 'Purchase';
    var sourceId = $(this).data('id');
    var name = $(this).data('name');
    console.log('sourceId: '+sourceId);
    $.get('/purchase/add-payment', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        $('#myModalPlaceHolder').html(htmlData);
    });
//        $('#myModalPlaceHolder').html('content.html');
    $('#myModal').modal({show:true});
})

$(document).on('click', '.showPayments', function(){
    var source = 'Purchase';
    var sourceId = $(this).data('id');
    var name = $(this).data('name');
    var billNo = $(this).data('billno');
    console.log('sourceId: '+sourceId);
    $.get('/purchase/payment-modal', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        $('#myModalLabel').text(name+' Payments (Bill No. '+billNo+')')
        $('#myModalPlaceHolder').html(htmlData);
    });
    $('#myModal').modal({show:true});
})