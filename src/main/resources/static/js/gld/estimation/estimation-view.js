var _estimationContext = '/estimation'
var url = baseUrl + _estimationContext;
var id = null;
$(document).ready(function() {
var urlParams = getUrlVars();

if(urlParams.id) {
    // Get the value of the 'id' parameter
    id = urlParams.id;
    console.log("ID:", id);
} else {
    console.log("ID parameter not found in the URL.");
    createDynamicModal(id, "Invalid estimation", "Estimation not found", "Close", closeEstimation)
}

    simpleCall(url+'/'+id, 'get', '', '', null, getEstimationCallback);
    // Print button functionality
    $('#printInvoice').on('click', function() {
        window.print();
    });

    $('#editEstimation').on('click', function() {
        $(window).attr('location','/estimation?id='+id)
    });
});

function getEstimationCallback(response) {
    if (response == null || response == '') {
        $('#printInvoice').addClass('d-none');
        $('#cancelBtn').addClass('d-none');
        createDynamicModal(id, "Invalid estimation", "Estimation not found", "Close", closeEstimation);
    } else {
        // Fill customer details

        var estimationItemList = response.estimationItemList;

        // Iterate over each item in estimationItemList
        $.each(estimationItemList, function(index, estimationItem) {
            // Append a new row to the table body for each item
            $('#estimationItemsTable tbody').append('<tr>' +
                '<td>' + estimationItem.code + '</td>' +
                '<td>' + estimationItem.name + '</td>' +
                '<td>' + toWt(estimationItem.weight) + '</td>' +
                '<td>' + toWt(estimationItem.vaWeight) + '</td>' +
                '<td>' + toWt(estimationItem.stnWeight)+ '</td>' +
                '<td>' + toWt(estimationItem.netWeight) + '</td>' +
                '<td>' + toCurrency(estimationItem.makingCharge) + '</td>' +
                '<td>' + toCurrency(estimationItem.rate) + '</td>' +
                '<td>' + toCurrency(estimationItem.itemTotal) + '</td>' +
                '</tr>');
        });

        var exchangeItemList = response.exchangeItemList;
        $.each(exchangeItemList, function(index, exchangeItem) {
            $('#exchangeItemsTable tbody').append('<tr>' +
            '<td>' + exchangeItem.itemDesc + '</td>' +
            '<td>' + toWt(exchangeItem.weight) + '</td>' +
            '<td>' + exchangeItem.meltPercentage + '</td>' +
            '<td>' + toWt(exchangeItem.wastageInGms) + '</td>' +
            '<td>' + toWt(exchangeItem.netWeight) + '</td>' +
            '<td>' + toCurrency(exchangeItem.rate) + '</td>' +
            '<td>' + toCurrency(exchangeItem.exchangeValue) + '</td>' +
            '</tr>');
        });

        // Fill other details
        $('#estimationNo').text(response.estimationNo);
        $('#estimationDate').text(response.estimationDate);
        $('#totalEstimationAmount').text(toCurrency(response.totalEstimationAmount));
        $('#totalExchangeAmount').text(toCurrency(response.totalExchangeAmount));
        $('#grandTotalEstimationAmount').text(toCurrency(response.grandTotalEstimationAmount));
        $('#paidAmount').text(toCurrency(response.paidAmount));
        $('#balAmount').text(toCurrency(response.balAmount));
        $('#estimationType').text(response.estimationType);
        $('#isGstEstimation').text(response.isGstEstimation);
        $('#paymentMode').text(response.paymentMode);
        $('#description').text(response.description);
        if (response.isGstEstimation == 'YES') {
            $('#gstBlock').removeClass('d-none')
            $('#cgstAmount').text(toCurrency(response.cgstAmount));
            $('#sgstAmount').text(toCurrency(response.sgstAmount));
        } else {
            $('#gstBlock').addClass('d-none')
        }
        $('#payments').attr('data-id', response.id);
        $('#addPayment').attr('data-id', response.id);
        $('#printInvoice').removeClass('d-none');
        $('#cancelBtn').removeClass('d-none');
    }

}

function closeEstimation(id) {

}

$(document).on('click', '#payments', function() {
    var source = 'Estimation';
    var sourceId = $(this).attr('data-id');
    var name = $('#name').text();
    console.log('sourceId: '+sourceId);
    $.get('/estimation/payment-modal', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        // Update the content of the modal body with the HTML content
        $('#myModalLabel').text(name+' Payments (Bill No. '+$('#invoiceNo').text()+')')
        $('#myModalPlaceHolder').html(htmlData);
    });
//        $('#myModalPlaceHolder').html('content.html');
    $('#myModal').modal({show:true});
});

$(document).on('click', '#addPayment', function() {
    var source = 'Estimation';
    var sourceId = $(this).attr('data-id');
    var name = $('#name').text();
    console.log('sourceId: '+sourceId);
    $.get('/estimation/add-payment', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        $('#myModalPlaceHolder').html(htmlData);
    });
//        $('#myModalPlaceHolder').html('content.html');
    $('#myModal').modal({show:true});
});