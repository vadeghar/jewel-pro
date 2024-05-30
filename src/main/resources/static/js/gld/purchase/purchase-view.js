var _purchaseContext = '/purchase'
var url = baseUrl + _purchaseContext;
var id = null;
$(document).ready(function() {
var urlParams = getUrlVars();

if(urlParams.id) {
    // Get the value of the 'id' parameter
    id = urlParams.id;
    console.log("ID:", id);
} else {
    console.log("ID parameter not found in the URL.");
    createDynamicModal(id, "Invalid purchase", "Purchase not found", "Close", closePurchase)
}

    simpleCall(url+'/'+id, 'get', '', '', null, viewPurchaseCallback);
    // Print button functionality
    $('#printInvoice').on('click', function() {
        window.print();
    });

    $('#editPurchase').on('click', function() {
        $(window).attr('location','/purchase?id='+id)
    });
});

function viewPurchaseCallback(response) {
    if (response == null || response == '') {
//        $('#printInvoice').addClass('d-none');
//        $('#cancelBtn').addClass('d-none');
        createDynamicModal(id, "Invalid Purchase", "Purchase not found", "Close", closePurchase);
    }
    console.log(JSON.stringify(response));
    $('#id').val(response.id);
    $('#purchaseType').text(response.purchaseType);
    $('#purchaseBillNo').text(response.purchaseBillNo);
    $('#purchaseDate').text(response.purchaseDate);
    $('#metalType').text(response.metalType);
    $('#totalGrossWeight').text(toWt(response.totalGrossWeight));
    $('#actualPurity').text(response.actualPurity);
    $('#purchasePurity').text(response.purchasePurity);
    $('#totalNetWeight').text(toWt(response.totalNetWeight));
    $('#purchaseAmount').text(toCurrency(response.purchaseAmount));
    $('#rate').text(toCurrency(response.rate));
    $('#totalPcs').text(response.totalPcs);
    $('#totalStnWeight').text(toWt(response.totalStnWeight));
    $('#paymentMode').text(response.paymentMode);
    $('#paidAmount').text(toCurrency(response.paidAmount));
    $('#balAmount').text(toCurrency(response.balAmount));
    $('#purchaseRate').text(toCurrency(response.purchaseRate));
    $('#isGstPurchase').text(response.isGstPurchase);


    $('#totalPurchaseAmount').text(toCurrency(response.totalPurchaseAmount));
    $('#description').text(response.description);
    $('#supplierName').text(response.supplierName);
    $('#supplierId').val(response.supplierId);
    $('#createdBy').text(response.createdBy);
    if (response.isGstPurchase == 'YES') {
        $('.gstBlock').removeClass('d-none')
        $('#totalCgstAmount').text(toCurrency(response.totalCgstAmount));
        $('#totalSgstAmount').text(toCurrency(response.totalSgstAmount));
        $('#gstNo').text(response.gstNo);
    } else {
        $('.gstBlock').addClass('d-none')
        $('#totalCgstAmount').text('');
        $('#totalSgstAmount').text('');
    }
    $('#payments').attr('data-id', response.id);
    $('#addPayment').attr('data-id', response.id);
    $('#printInvoice').removeClass('d-none');
    $('#cancelBtn').removeClass('d-none');
}

function closePurchase() {

}


$(document).on('click', '#payments', function() {
    var source = 'Purchase';
    var sourceId = $(this).attr('data-id');
    var name = $('#name').text();
    console.log('sourceId: '+sourceId);
    $.get('/purchase/payment-modal', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        // Update the content of the modal body with the HTML content
        $('#myModalLabel').text(name+' Payments (Bill No. '+$('#purchaseBillNo').text()+')')
        $('#myModalPlaceHolder').html(htmlData);
    });
//        $('#myModalPlaceHolder').html('content.html');
    $('#myModal').modal({show:true});
});

$(document).on('click', '#addPayment', function() {
    var source = 'Purchase';
    var sourceId = $(this).attr('data-id');
    var name = $('#name').text();
    console.log('sourceId: '+sourceId);
    $.get('/purchase/add-payment', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        $('#myModalPlaceHolder').html(htmlData);
    });
//        $('#myModalPlaceHolder').html('content.html');
    $('#myModal').modal({show:true});
});