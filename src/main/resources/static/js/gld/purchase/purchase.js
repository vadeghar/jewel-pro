var baseUrl = 'http://localhost:8080/api/v1';
var _purchaseContext = '/purchase'
var url = baseUrl + _purchaseContext;

$(document).ready(function() {

    $('#metalType').on('change', function(){
        var selectedValue = $(this).val();
        if (selectedValue == 'GOLD') {
            $('#rate').val($('#currentGoldRate').text());
        } else if (selectedValue == 'SILVER') {
            $('#rate').val($('#currentSilverRate').text());
        }
    });

    $('#isGstPurchase').on('change', function(){
            calcTotalPurchaseAmount();
    });
    $('.gstAttrs').addClass('d-none')

    $('#totalGrossWeight, #totalStnWeight, #purchasePurity, #rate').on('input', function(){
        calcTotalPurchaseAmount();
    });

    $('#paidAmount').on('input', function(){
        setPaymentDetails();
    });

    $('#savePurchase').on('click', function(e){
        e.preventDefault();
        validateForm();
        savePurchase();
    });

setDefaultsOnPageLoad();
});

function setPaymentDetails() {
    var balAmount = 0;
    var totalPurchaseAmount = $('#totalPurchaseAmount').val();
    if(totalPurchaseAmount) {
        balAmount = parseFloat(totalPurchaseAmount) - parseFloat($('#paidAmount').val());
        $('#balAmount').val(parseFloat(balAmount).toFixed(2));
        $('#balAmountLbl').text(parseFloat(balAmount).toFixed(2));
    }
}

var supplierList = [];

$('#supplierName').autocomplete({
    source: function(request, response) {
        $.ajax({
            url: baseUrl+'/supplier/autocomplete',
            method: 'GET',
            dataType: 'json',
            data: {
                term: request.term
            },
            success: function(data) {
                // Manipulate data to extract only item codes
                var supplierList = $.map(data, function(supplier) {
                    return {
                        label: supplier.name,
                        value: supplier.name,
                        item: supplier
                    };
                });
                response(supplierList);
            }
        });
    },
    minLength: 2,
    select: function(event, ui) {
        console.log('Supplier name:', ui.item.item.name + ', Id: '+ui.item.item.id);
        $('#supplierId').val(ui.item.item.id)
    }
});





function calcTotalPurchaseAmount() {
    var totalGrossWeight = $('#totalGrossWeight').val();
    var totalStnWeight = $('#totalStnWeight').val();
    var purchasePurity = $('#purchasePurity').val();
    var rate = $('#rate').val();
    var totalNetWeight = 0;
    var totalPurchaseAmount = 0;
    var isGstPurchase = $('#isGstPurchase').val();
    var totalCgstAmount = 0;
    var totalSgstAmount = 0;
    var purchaseAmount = 0;
    if (totalGrossWeight) {
        if (totalStnWeight) {
            totalGrossWeight = totalGrossWeight - totalStnWeight;
            $('#totalStnWeight').val(parseFloat(totalStnWeight).toFixed(3));
        } else {
            $('#totalStnWeight').val(0.000);
        }
//        $('#totalGrossWeight').val(parseFloat(totalGrossWeight).toFixed(3));
        if (purchasePurity) {
            totalNetWeight = totalGrossWeight * (purchasePurity/100)
            $('#totalNetWeight').val(parseFloat(totalNetWeight).toFixed(3));
            $('#totalNetWeightLbl').text(parseFloat(totalNetWeight).toFixed(3));
        }
        if (rate && totalNetWeight) {
            purchaseAmount = totalNetWeight * rate;
            $('#totalPurchaseAmount').val(parseFloat(purchaseAmount).toFixed(2));
            $('#totalPurchaseAmountLbl').text(parseFloat(purchaseAmount).toFixed(2));
            $('#purchaseAmount').val(parseFloat(purchaseAmount).toFixed(2));
            $('#purchaseAmountLbl').text(parseFloat(purchaseAmount).toFixed(2));

        }

        if (isGstPurchase && isGstPurchase == 'YES') {
            totalCgstAmount = parseFloat(purchaseAmount * (1.5 / 100)).toFixed(2);
            totalSgstAmount = parseFloat(purchaseAmount * (1.5 / 100)).toFixed(2);
            totalPurchaseAmount = parseFloat(purchaseAmount) + parseFloat(totalSgstAmount) + parseFloat(totalCgstAmount);
            $('#totalCgstAmount').val(parseFloat(totalCgstAmount).toFixed(2));
            $('#totalSgstAmount').val(parseFloat(totalSgstAmount).toFixed(2));
            $('#totalCgstAmountLbl').text(parseFloat(totalCgstAmount).toFixed(2));
            $('#totalSgstAmountLbl').text(parseFloat(totalSgstAmount).toFixed(2));
            $('#totalPurchaseAmount').val(parseFloat(totalPurchaseAmount).toFixed(2));
            $('#totalPurchaseAmountLbl').text(parseFloat(totalPurchaseAmount).toFixed(2));
            $('.gstAttrs').removeClass('d-none');
        } else {
            $('.gstAttrs').addClass('d-none')
            $('#totalCgstAmount').val(parseFloat(totalCgstAmount).toFixed(2));
            $('#totalSgstAmount').val(parseFloat(totalSgstAmount).toFixed(2));
            $('#totalCgstAmountLbl').text(parseFloat(totalCgstAmount).toFixed(2));
            $('#totalSgstAmountLbl').text(parseFloat(totalSgstAmount).toFixed(2));
        }
        setPaymentDetails();
    }

}
function setDefaultsOnPageLoad() {
    $('#rate').val($('#currentGoldRate').text());
    var today = new Date().toISOString().split('T')[0];
    // Set the value of the input element to today's date
    $('#purchaseDate').val(today);
    var purchaseId = $('#id').val();
    if(purchaseId) {
        simpleCall(url+'/'+purchaseId, 'get', '', '', '', loadPurchase);
        $('#savePurchase').text('Update')
    }
}

function loadPurchase(response) {
    var formData = $("#purchaseForm").serializeArray();
    var formObject = {};
    var textAttrs = ['totalPurchaseAmount', 'totalNetWeight', 'purchaseAmount', 'balAmount']
    $.each(formData, function(index, field) {
        if (textAttrs.indexOf(field.name) !== -1) {
//            alert('[#'+field.name+'Lbl]');
            $('#'+field.name+'Lbl').text(response[field.name]);
        }
        $('[name="'+field.name+'"]').val(response[field.name]);
        $('#addPurchaseItems').attr('data-id', response.id);
        $('#addPurchaseItems').removeClass('d-none');
//        formObject[field.name] = response[field.name];
    });
}

$(document).on('click', '.addPurchaseItems', function() {
        var id = $(this).data('id');
        var form = $('<form></form>', {
            'method': 'POST',
            'action': '/purchase/purchase-items'
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

function validateForm() {

}

function savePurchase() {
    var formData = $("#purchaseForm").serializeArray();
    var formObject = {};
    $.each(formData, function(index, field) {
        formObject[field.name] = field.value;
    });
    var jsonData = JSON.stringify(formObject);
    console.log(jsonData);
    simpleCall(url+'/save', 'post', '', '', jsonData, savePurchaseCallback)
}

function savePurchaseCallback(response) {
    console.log(response);
    $('#id').val(response.id);
    $('#savePurchase').text('Update');
    $('#addPurchaseItems').attr('data-id', response.id);
    $('#addPurchaseItems').removeClass('d-none');
}
