var baseUrl = 'http://localhost:8080/api/v1';
var _saleContext = '/sale'
var url = baseUrl + _saleContext;
var id = null;
$(document).ready(function() {
var urlParams = getUrlVars();

if(urlParams.id) {
    // Get the value of the 'id' parameter
    id = urlParams.id;
    console.log("ID:", id);
} else {
    console.log("ID parameter not found in the URL.");
    createDynamicModal(id, "Invalid sale", "Sale not found", "Close", closeSale)
}

    simpleCall(url+'/'+id, 'get', '', '', null, getSaleCallback);
    // Print button functionality
    $('#printInvoice').on('click', function() {
        window.print();
    });

    $('#editSale').on('click', function() {
        $(window).attr('location','http://localhost:8080/sale?id='+id)
    });
});

function getSaleCallback(response) {
    if (response == null || response == '') {
        $('#printInvoice').addClass('d-none');
        $('#cancelBtn').addClass('d-none');
        createDynamicModal(id, "Invalid sale", "Sale not found", "Close", closeSale);
    } else {
        // Fill customer details
        $('#name').text(response.customer.name);
        $('#phone').text(response.customer.phone);
        $('#address').text(response.customer.address);

        var saleItemList = response.saleItemList;

        // Iterate over each item in saleItemList
        $.each(saleItemList, function(index, saleItem) {
            // Append a new row to the table body for each item
            $('#saleItemsTable tbody').append('<tr>' +
                '<td>' + saleItem.code + '</td>' +
                '<td>' + saleItem.name + '</td>' +
                '<td>' + toWt(saleItem.weight) + '</td>' +
                '<td>' + toWt(saleItem.vaWeight) + '</td>' +
                '<td>' + toWt(saleItem.stnWeight)+ '</td>' +
                '<td>' + toWt(saleItem.netWeight) + '</td>' +
                '<td>' + toCurrency(saleItem.makingCharge) + '</td>' +
                '<td>' + toCurrency(saleItem.rate) + '</td>' +
                '<td>' + toCurrency(saleItem.itemTotal) + '</td>' +
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
        $('#invoiceNo').text(response.invoiceNo);
        $('#saleDate').text(response.saleDate);
        $('#totalSaleAmount').text(toCurrency(response.totalSaleAmount));
        $('#totalExchangeAmount').text(toCurrency(response.totalExchangeAmount));
        $('#grandTotalSaleAmount').text(toCurrency(response.grandTotalSaleAmount));
        $('#paidAmount').text(toCurrency(response.paidAmount));
        $('#balAmount').text(toCurrency(response.balAmount));
        $('#saleType').text(response.saleType);
        $('#isGstSale').text(response.isGstSale);
        $('#paymentMode').text(response.paymentMode);
        $('#description').text(response.description);
        if (response.isGstSale == 'YES') {
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

function closeSale(id) {

}

$(document).on('click', '#payments', function() {
    var source = 'Sale';
    var sourceId = $(this).attr('data-id');
    var name = $('#name').text();
    console.log('sourceId: '+sourceId);
    $.get('/sale/payment-modal', function(htmlData) {
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
    var source = 'Sale';
    var sourceId = $(this).attr('data-id');
    var name = $('#name').text();
    console.log('sourceId: '+sourceId);
    $.get('/sale/add-payment', function(htmlData) {
        $('#sourceId').val(sourceId);
        $('#source').val(source);
        $('#myModalPlaceHolder').html(htmlData);
    });
//        $('#myModalPlaceHolder').html('content.html');
    $('#myModal').modal({show:true});
});