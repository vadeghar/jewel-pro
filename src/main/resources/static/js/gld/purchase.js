var baseUrl = 'http://localhost:8080/api/v1';
var _purchaseContext = '/purchase'

$(document).ready(function() {
    var url = baseUrl + _purchaseContext;

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
        var balAmount = 0;
        var totalPurchaseAmount = $('#totalPurchaseAmount').val();
        if(totalPurchaseAmount) {
            balAmount = parseFloat(totalPurchaseAmount) - parseFloat($(this).val());
            $('#balAmount').val(parseFloat(balAmount).toFixed(2));
            $('#balAmountLbl').text(parseFloat(balAmount).toFixed(2));
        }
    });

    $('#savePurchase').on('click', function(e){
        e.preventDefault();
        validateForm();
        savePurchase();
    });

setDefaultsOnPageLoad();
});

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

    }

}
function setDefaultsOnPageLoad() {
    $('#rate').val($('#currentGoldRate').text());
    var today = new Date().toISOString().split('T')[0];
    // Set the value of the input element to today's date
    $('#purchaseDate').val(today);
}

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
    simpleCall(baseUrl+_purchaseContext+'/save', 'post', '', '', jsonData, savePurchaseCallback)

}

function savePurchaseCallback(response) {
    console.log(response);
}

function simpleCall(url, method, title, tabId, requestData, responseCallback) {
//	var $content = $(tabId).find('.ibox-content:first');
	doSignAndSend({
		url: url,
		type: method,
		contentType: 'application/json',
		data: requestData,
		beforeSend: function() {
            $("#spinner-div").show();
		},
		success: function(data, textStatus, jqXHR) {
		    $("#spinner-div").hide();
			responseCallback(data);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			responseCallback(null);
//			handleGlobalError(jqXHR, textStatus, errorThrown);
		},
		complete: function() {
//			window.setTimeout(function() {
//				$content.toggleClass('sk-loading');
//			}, 10000);
		}
	});
}


function doSignAndSend(settings) {
	$.ajax(settings);
}