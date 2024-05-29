var baseUrl = 'http://localhost:8080/api/v1';
var _estimationContext = '/estimation'
var url = baseUrl + _estimationContext;
var itemList = [];

$(document).on('focus', '.code', function() {
    $(this).autocomplete({
        source: function(request, response) {
            $.ajax({
                url: 'http://localhost:8080/api/v1/stock',
                method: 'GET',
                dataType: 'json',
                data: {
                    code: request.term
                },
                success: function(data) {
                    // Manipulate data to extract only item codes
                    var itemCodes = $.map(data, function(item) {
                        return {
                            label: item.code,
                            value: item.code,
                            item: item
                        };
                    });
                    response(itemCodes);
                }
            });
        },
        minLength: 2,
        select: function(event, ui) {
            var $inputElement = $(this); // Get the current input element
            var inputId = $inputElement.attr('id'); // Get the id attribute
            var inputName = $inputElement.attr('name'); // Get the name attribute
            var startIndex = inputId.indexOf('[') + 1;
            var endIndex = inputId.indexOf(']');
            var index = inputId.substring(startIndex, endIndex);
            // Use the id or name as needed
            console.log('Input ID:', inputId);
            console.log('Input Name:', inputName);
            $('#estimationItemList\\[' + index + '\\]\\.stockId').val(ui.item.item.id);
            $('#estimationItemList\\[' + index + '\\]\\.code').val(ui.item.item.code);
            $('#estimationItemList\\[' + index + '\\]\\.name').val(ui.item.item.name);
            $('#estimationItemList\\[' + index + '\\]\\.weight').val(ui.item.item.weight != null ? parseFloat(ui.item.item.weight).toFixed(3) : parseFloat(0).toFixed(3));
            $('#estimationItemList\\[' + index + '\\]\\.vaWeight').val(ui.item.item.vaWeight != null ? parseFloat(ui.item.item.vaWeight).toFixed(3) : parseFloat(0).toFixed(3));
            $('#estimationItemList\\[' + index + '\\]\\.stnWeight').val(ui.item.item.stnWeight != null ? parseFloat(ui.item.item.stnWeight).toFixed(3) : parseFloat(0).toFixed(3));
             $('#estimationItemList\\[' + index + '\\]\\.netWeight').val(calcNetWeight(ui.item.item));
            $('#estimationItemList\\[' + index + '\\]\\.makingCharge').val(ui.item.item.estimationMC != null ? parseFloat(ui.item.item.estimationMC).toFixed(2) : parseFloat(0).toFixed(3));

        }
    });
});

$(document).on('input', '.estimationItemCls', function() {
    var $inputElement = $(this); // Get the current input element
    var inputId = $inputElement.attr('id'); // Get the id attribute
    var inputName = $inputElement.attr('name'); // Get the name attribute
    var startIndex = inputId.indexOf('[') + 1;
    var endIndex = inputId.indexOf(']');
    var index = inputId.substring(startIndex, endIndex);
    calcItemTotal(index);
});

$(document).on('input', '.exchangeItemCls', function() {
    var $inputElement = $(this); // Get the current input element
    var inputId = $inputElement.attr('id'); // Get the id attribute
    var inputName = $inputElement.attr('name'); // Get the name attribute
    var startIndex = inputId.indexOf('[') + 1;
    var endIndex = inputId.indexOf(']');
    var index = inputId.substring(startIndex, endIndex);
    calcExchangeItemTotal(index);
});

$(document).ready(function(){

if ($('#id').val() != null && $('#id').val() != '') {
    simpleCall(url+'/'+$('#id').val(), 'get', '', '', null, loadEstimationCallback);
}

$('.gstBlock').addClass('d-none');
$('#invoiceNoLbl').addClass('d-none');
$('#isGstEstimation').change(function() {
    // Check if "NO" option is selected
    if ($(this).val() === 'NO') {
        // Hide the div element
        $('.gstBlock').addClass('d-none'); // Replace '.div-to-hide' with the appropriate selector for your div
        $('#cgstAmount').val(0);
        $('#cgstAmountLbl').text('');
        $('#sgstAmount').val(0)
        $('#sgstAmountLbl').text('')
    } else {
        // Show the div element
        $('.gstBlock').removeClass('d-none'); // Replace '.div-to-hide' with the appropriate selector for your div
    }
    calcMainTotals();
});

 // Add row function
 $("#addEstimationItem").click(function() {
     var rowCount = $('#estimationItemsTable tbody tr').length;
     var newRow = '<tr>'+
                       '<td>'+
                           '<input type="text" name="code" id="estimationItemList['+rowCount+'].code" class="form-control code table-input">'+
                           '<input type="hidden" name="id" id="estimationItemList['+rowCount+'].id" class="form-control">'+
                           '<input type="hidden" name="stockId" id="estimationItemList['+rowCount+'].stockId">'+
                       '</td>'+
                       '<td><input type="text" name="name" id="estimationItemList['+rowCount+'].name" class="form-control table-input"></td>'+
                       '<td><input type="text" name="weight" id="estimationItemList['+rowCount+'].weight" class="form-control estimationItemCls table-input"></td>'+
                       '<td><input type="text" name="vaWeight" id="estimationItemList['+rowCount+'].vaWeight" class="form-control estimationItemCls table-input"></td>'+
                       '<td><input type="text" name="stnWeight" id="estimationItemList['+rowCount+'].stnWeight" class="form-control table-input"></td>'+
                       '<td><input type="text" name="netWeight" id="estimationItemList['+rowCount+'].netWeight" class="form-control table-input"></td>'+
                       '<td><input type="text" name="makingCharge" id="estimationItemList['+rowCount+'].makingCharge" class="form-control estimationItemCls table-input"></td>'+
                       '<td><input type="text" name="rate" id="estimationItemList['+rowCount+'].rate" class="form-control estimationItemCls table-input"></td>'+
                       '<td><input type="text" name="itemTotal" id="estimationItemList['+rowCount+'].itemTotal" class="form-control table-input"></td>'+
                       '<td><button type="button" class="btn btn-danger removeRow"><i class="fas fa-trash"></i></button></td>'+
                   '</tr>';
     $("#estimationItemsTable tbody").append(newRow);
 });

 // Remove row
 $("#estimationItemsTable").on("click", ".removeRow", function() {
    var rowCount = $('#estimationItemsTable tbody tr').length;
    if(rowCount > 1) {
        $(this).closest("tr").remove();
        calcMainTotals();
    } else {
        alert("At least 1 Item to be added to do a estimation.")
    }
 });

 $("#addExchangeItem").click(function() {
    var rowCount = $('#exchangeItemsTable tbody tr').length;
    var newRow = '<tr>' +
                      '<td><input type="text" name="itemDesc" id="exchangedItems[' + rowCount + '].itemDesc" class="form-control table-input"></td>' +
                      '<td>'+
                          '<input type="text" name="weight" id="exchangedItems[' + rowCount + '].weight" class="form-control exchangeItemCls table-input">' +
                          '<input type="hidden" name="id" id="exchangedItems[' + rowCount + '].id" class="form-control table-input">' +
                      '</td>' +
                      '<td><input type="text" name="meltPercentage" id="exchangedItems[' + rowCount + '].meltPercentage" class="form-control exchangeItemCls table-input"></td>' +
                      '<td><input type="text" name="wastageInGms" id="exchangedItems[' + rowCount + '].wastageInGms" class="form-control table-input"></td>' +
                      '<td><input type="text" name="netWeight" id="exchangedItems[' + rowCount + '].netWeight" class="form-control table-input"></td>' +
                      '<td><input type="text" name="rate" id="exchangedItems[' + rowCount + '].rate" class="form-control exchangeItemCls table-input"></td>' +
                      '<td><input type="text" name="exchangeValue" id="exchangedItems[' + rowCount + '].exchangeValue" class="form-control table-input"></td>' +
                      '<td><button type="button" class="btn btn-danger removeExchangeRow"><i class="fas fa-trash"></i></button></td>' +
                  '</tr>';
    $("#exchangeItemsTable tbody").append(newRow);
 });
 $("#exchangeItemsTable").on("click", ".removeExchangeRow", function() {
    var rowCount = $('#exchangeItemsTable tbody tr').length;
    if(rowCount > 1) {
        $(this).closest("tr").remove();
        calcMainTotals();
    } else {
        alert("At least 1 Item to be added to do a estimation.")
    }
});
$('#discount').on('input', function() {
    calcMainTotals();
});

$('#paidAmount').on('input', function() {
    calcMainTotals();
});



$('#estimationDate').val(getCurrentDate()).attr('disabled', true);
});

function calcNetWeight(item) {
    var weight = item.weight != null ? parseFloat(item.weight) : 0.000;
    var vaWeight = item.vaWeight != null ? parseFloat(item.vaWeight) : 0.000;
    var stnWeight = item.stnWeight != null ? parseFloat(item.stnWeight) : 0.000;
    var grossWeight = weight + vaWeight - stnWeight;
    return grossWeight.toFixed(3);
}


function calcItemTotal(index) {

    var weight = parseFloat($('#estimationItemList\\[' + index + '\\]\\.weight').val());
    var vaWeight = parseFloat($('#estimationItemList\\[' + index + '\\]\\.vaWeight').val());
    var stnWeight = parseFloat($('#estimationItemList\\[' + index + '\\]\\.stnWeight').val());
    var makingCharge = parseFloat($('#estimationItemList\\[' + index + '\\]\\.makingCharge').val());
    var rate = parseFloat($('#estimationItemList\\[' + index + '\\]\\.rate').val());
    console.log('index:'+index+', weight: '+weight+', vaWeight:'+vaWeight+', makingCharge:'+makingCharge+', rate:'+rate);
    // Calculate gross weight
    var grossWeight = weight + vaWeight;
    var itemTotal = (grossWeight * rate) + makingCharge;
    $('#estimationItemList\\[' + index + '\\]\\.itemTotal').val(itemTotal.toFixed(2));
    calcMainTotals();
}

function calcExchangeItemTotal(index) {
    var weight = parseFloat($('#exchangedItems\\[' + index + '\\]\\.weight').val());
    var meltPercentage = parseFloat($('#exchangedItems\\[' + index + '\\]\\.meltPercentage').val());
    var wastageInGms = parseFloat($('#exchangedItems\\[' + index + '\\]\\.wastageInGms').val());
    var netWeight = parseFloat($('#exchangedItems\\[' + index + '\\]\\.netWeight').val());
    var rate = parseFloat($('#exchangedItems\\[' + index + '\\]\\.rate').val());
    var exchangeValue = parseFloat($('#exchangedItems\\[' + index + '\\]\\.exchangeValue').val());

    if (weight != '') {
        if (meltPercentage != '') {
            wastageInGms = weight - (weight * (meltPercentage / 100));
            netWeight = weight - wastageInGms;
            $('#exchangedItems\\[' + index + '\\]\\.wastageInGms').val(wastageInGms.toFixed(3));
            $('#exchangedItems\\[' + index + '\\]\\.netWeight').val(netWeight.toFixed(3));
            if (rate != null) {
                exchangeValue = netWeight * rate;
                $('#exchangedItems\\[' + index + '\\]\\.exchangeValue').val(exchangeValue.toFixed(2));
                calcMainTotals();
            } else {
                $('#exchangedItems\\[' + index + '\\]\\.exchangeValue').val(0.00);
            }
        }
    }

}

function calcMainTotals() {
    calcItemTotalAmount();
    calcTotalExchangeAmount();
    calcBalAmount();
}

function calcTotalExchangeAmount() {
    var totalExchangeValue = 0;
    // Iterate over each row in the table body
    $("#exchangeItemsTable tbody tr").each(function() {
        // Get the exchange value from the current row and add it to the total
        $(this).find("input[id*='exchangedItems'][id$='.exchangeValue']").each(function() {
            var exchangeValue = parseFloat($(this).val());
            if (!isNaN(exchangeValue)) {
                totalExchangeValue += exchangeValue;
            }
        });
    });
    $("#totalExchangeAmount").val(totalExchangeValue.toFixed(2));
    $("#totalExchangeAmountLbl").text(totalExchangeValue.toFixed(2));
    calcTotals();
}

function calcItemTotalAmount() {
    var itemTotalValue = 0;
    // Iterate over each row in the table body
    $("#estimationItemsTable tbody tr").each(function() {
        // Get the exchange value from the current row and add it to the total
        $(this).find("input[id*='estimationItemList'][id$='.itemTotal']").each(function() {
            var itemValue = parseFloat($(this).val());
            if (!isNaN(itemValue)) {
                itemTotalValue += itemValue;
            }
        });
    });
    // Display the total exchange value
    itemTotalValue = Math.round(itemTotalValue);
    $("#totalEstimationAmountLbl").text(itemTotalValue.toFixed(2));
    $("#totalEstimationAmount").val(itemTotalValue.toFixed(2));

    if ($('#isGstEstimation').val() === 'YES') {
        var gst = Math.round((itemTotalValue * 1.5) / 100);
        $('#cgstAmount').val(gst.toFixed(2));
        $('#cgstAmountLbl').text(gst.toFixed(2));
        $('#sgstAmount').val(gst.toFixed(2));
        $('#sgstAmountLbl').text(gst.toFixed(2));
    }
    calcTotals();
}

function getCurrentDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}


function calcTotals() {
    var totalEstimationAmount = $('#totalEstimationAmount').val() != '' ? parseFloat($('#totalEstimationAmount').val()) : 0.00;
    var totalExchangeAmount = $('#totalExchangeAmount').val() != '' ? parseFloat($('#totalExchangeAmount').val()) : 0.00;
    var discount = $('#discount').val() != '' ? parseFloat($('#discount').val()) : 0.00;
    var cGstAmount = $('#cgstAmount').val() != '' ? parseFloat($('#cgstAmount').val()) : 0.00;
    var sGstAmount = $('#sgstAmount').val() != '' ? parseFloat($('#sgstAmount').val()) : 0.00;

    var grandTotalEstimationAmountLbl = ((totalEstimationAmount - totalExchangeAmount) - discount) + (cGstAmount + sGstAmount);
    if(grandTotalEstimationAmountLbl > 0) {
        $('#grandTotalEstimationAmount').val(grandTotalEstimationAmountLbl.toFixed(2));
        $('#grandTotalEstimationAmountLbl').text(grandTotalEstimationAmountLbl.toFixed(2));

    }
}

function calcBalAmount() {
    var grandTotalEstimationAmount = $('#grandTotalEstimationAmount').val();
    var balAmount = $('#balAmount').val();
    var paidAmount = $('#paidAmount').val();
    var balAmount = grandTotalEstimationAmount - paidAmount;
    $('#balAmount').val(balAmount.toFixed(2));
    $('#balAmountLbl').text(balAmount.toFixed(2));
}

$('#saveEstimation').on('click', function(){
    submit();
});

function submit() {
    var estimationDTO = {};
    var customer = {};
    customer.name = $('#name').val();
    customer.phone = $('#phone').val();
    customer.address = $('#address').val();
    customer.id = $('#customerId').val();

    var estimationId = $('#id').val();
    var estimationItemList = [];
    var exchangeItemList = [];
    $('#estimationItemsTBody tr').each(function() {
        var jsonData = formToJson(this);
        estimationItemList.push(jsonData);
    });
    $('#exchangeItemsTBody tr').each(function() {
        var jsonData = formToJson(this);
        exchangeItemList.push(jsonData);
    });
    estimationDTO.id = estimationId;
//    estimationDTO.estimationDate = $('#estimationDate').val();
    estimationDTO.customer = customer;
    estimationDTO.estimationItemList = estimationItemList
    estimationDTO.exchangeItemList = exchangeItemList;

    $('.estimationDto input').each(function() {
        if (!$(this).closest('#estimationItemsTable, #exchangeItemsTable').length) {
            // Get the name and value of each input element
            var name = $(this).attr('name');
            var value = $(this).val();

            // If the name and value are not empty, add them to the jsonData object
            if (name !== undefined && name !== '' && value !== undefined && value !== '') {
                estimationDTO[name] = value;
            }
        }
    });
    $('.estimationDto select').each(function() {
        if (!$(this).closest('#estimationItemsTable, #exchangeItemsTable').length) {
            // Get the name and value of each input element
            var name = $(this).attr('name');
            var value = $(this).val();

            // If the name and value are not empty, add them to the jsonData object
            if (name !== undefined && name !== '' && value !== undefined && value !== '') {
                estimationDTO[name] = value;
            }
        }
    });

    $('.estimationDto textarea').each(function() {
        if (!$(this).closest('#estimationItemsTable, #exchangeItemsTable').length) {
            // Get the name and value of each input element
            var name = $(this).attr('name');
            var value = $(this).val();

            // If the name and value are not empty, add them to the jsonData object
            if (name !== undefined && name !== '' && value !== undefined && value !== '') {
                estimationDTO[name] = value;
            }
        }
    });
    simpleCall(url, 'post', '', '', JSON.stringify(estimationDTO, null, 2), saveEstimationCallback)
}

function formToJson(form) {
    var formData = {};
    $(form).find(':input').each(function() {
        if(this.name) {
            formData[this.name] = $(this).val();
        }
    });
    return formData;
}

function saveEstimationCallback(response) {
    navigateWindow('http://localhost:8080/estimation/view?id='+response.id);
//    $(window).attr('location','http://localhost:8080/estimation/view?id='+response.id)
}


$('#name').autocomplete({
    source: function(request, response) {
        $.ajax({
            url: 'http://localhost:8080/api/v1/customer/like',
            method: 'GET',
            dataType: 'json',
            data: {
                name: request.term
            },
            success: function(data) {
                // Manipulate data to extract only item codes
                var itemCodes = $.map(data, function(item) {
                    return {
                        label: item.name +' - '+item.phone,
                        value: item.name,
                        item: item
                    };
                });
                response(itemCodes);
            }
        });
    },
    minLength: 3,
    select: function(event, ui) {
        $('#name').val(ui.item.item.name);
        $('#customerId').val(ui.item.item.id);
        $('#phone').val(ui.item.item.phone);
        $('#address').val(ui.item.item.address);
    }
});

function loadEstimationCallback(response) {
    if (response != null && response != '') {
        $('#invoiceNo').val(response.invoiceNo);
        $('#invoiceNoLbl').text('Invoice No. '+response.invoiceNo);
        $('#invoiceNoLbl').removeClass('d-none');
        $('#estimationDate').val(response.estimationDate);
        $('#estimationType').val(response.estimationType);
        $('#isGstEstimation').val(response.isGstSale);

        $('#name').val(response.customer.name);
        $('#phone').val(response.customer.phone);
        $('#address').val(response.customer.address);
        $('#customerId').val(response.customer.id);

        var estimationItemList = response.estimationItemList;
        $('#estimationItemsTable tbody').empty();
        // Iterate over each item in estimationItemList
        $.each(estimationItemList, function(index, estimationItem) {
            // Append a new row to the table body for each item
            $('#estimationItemsTable tbody').append(
            '<tr>' +
                 '<td>' +
                     '<input type="text" name="code" id="estimationItemList['+index+'].code" value="' + estimationItem.code + '" class="form-control code table-input">' +
                     '<input type="hidden" name="id" id="estimationItemList['+index+'].id" value="' + estimationItem.id + '" class="form-control">' +
                     '<input type="hidden" name="stockId" id="estimationItemList['+index+'].stockId "value="' + estimationItem.stockId + '" >' +
                 '</td>' +
                 '<td><input type="text" name="name" id="estimationItemList['+index+'].name"value="' + estimationItem.name + '"  class="form-control table-input"></td>' +
                 '<td><input type="text" name="weight" id="estimationItemList['+index+'].weight" value="' + toWt(estimationItem.weight) + '"  class="form-control estimationItemCls table-input"></td>' +
                 '<td><input type="text" name="vaWeight" id="estimationItemList['+index+'].vaWeight" value="' + toWt(estimationItem.vaWeight) + '"  class="form-control estimationItemCls table-input"></td>' +
                 '<td><input type="text" name="stnWeight" id="estimationItemList['+index+'].stnWeight" value="' + toWt(estimationItem.stnWeight) + '" class="form-control table-input"></td>' +
                 '<td><input type="text" name="netWeight" id="estimationItemList['+index+'].netWeight" value="' + toWt(estimationItem.netWeight) + '" class="form-control table-input"></td>' +
                 '<td><input type="text" name="makingCharge" id="estimationItemList['+index+'].makingCharge" value="' + toCurrency(estimationItem.makingCharge) + '" class="form-control estimationItemCls table-input"></td>' +
                 '<td><input type="text" name="rate" id="estimationItemList['+index+'].rate" value="' + toCurrency(estimationItem.rate) + '" class="form-control estimationItemCls table-input"></td>' +
                 '<td><input type="text" name="itemTotal" id="estimationItemList['+index+'].itemTotal" value="' + toCurrency(estimationItem.itemTotal) + '" class="form-control table-input" readonly></td>' +
                 '<td><button type="button" class="btn btn-danger removeRow"><i class="fas fa-trash"></i></button></td>' +
             '</tr>');
        });

        var exchangeItemList = response.exchangeItemList;
        $('#exchangeItemsTable tbody').empty();
        $.each(exchangeItemList, function(index, exchangeItem) {
            $('#exchangeItemsTable tbody').append(
            '<tr>' +
                '<td><input type="text" name="itemDesc" id="exchangedItems['+index+'].itemDesc" value="' + exchangeItem.itemDesc + '" class="form-control table-input"></td>' +
                '<td>' +
                    '<input type="text" name="weight" id="exchangedItems['+index+'].weight" value="' + toWt(exchangeItem.weight) + '" class="form-control exchangeItemCls table-input">' +
                    '<input type="hidden" name="id" id="exchangedItems['+index+'].id" value="' + exchangeItem.id + '" class="form-control table-input">' +
                '</td>' +
                '<td><input type="text" name="meltPercentage" id="exchangedItems['+index+'].meltPercentage" value="' + exchangeItem.meltPercentage + '" class="form-control exchangeItemCls table-input"></td>' +
                '<td><input type="text" name="wastageInGms" id="exchangedItems['+index+'].wastageInGms" value="' + toWt(exchangeItem.wastageInGms) + '" class="form-control table-input" readonly></td>' +
                '<td><input type="text" name="netWeight" id="exchangedItems['+index+'].netWeight" value="' + toWt(exchangeItem.netWeight) + '" class="form-control table-input" readonly></td>' +
                '<td><input type="text" name="rate" id="exchangedItems['+index+'].rate" value="' + toCurrency(exchangeItem.rate) + '" class="form-control exchangeItemCls table-input"></td>' +
                '<td><input type="text" name="exchangeValue" id="exchangedItems['+index+'].exchangeValue" value="' + toCurrency(exchangeItem.exchangeValue) + '" class="form-control table-input" readonly></td>' +
                '<td><button type="button" class="btn btn-danger removeExchangeRow"><i class="fas fa-trash"></i></button></td>' +
            '</tr>');
        });

        $('#totalEstimationAmount').val(toCurrency(response.totalEstimationAmount));
        $('#totalEstimationAmountLbl').text(toCurrency(response.totalEstimationAmount));
        $('#totalExchangeAmount').val(toCurrency(response.totalExchangeAmount));
        $('#totalExchangeAmountLbl').text(toCurrency(response.totalExchangeAmount));
        $('#discount').val(toCurrency(response.discount))
        $('#grandTotalEstimationAmount').val(toCurrency(response.grandTotalEstimationAmount));
        $('#grandTotalEstimationAmountLbl').text(toCurrency(response.grandTotalEstimationAmount));
        $('#paymentMode').val(response.paymentMode);
        $('#paidAmount').val(toCurrency(response.paidAmount));
        $('#balAmount').val(toCurrency(response.balAmount));
        $('#balAmountLbl').text(toCurrency(response.balAmount));
        $('#estimationType').val(response.estimationType);


        $('#description').val(response.description);
        if (response.isGstEstimation == 'YES') {
            $('#gstBlock').removeClass('d-none')
            $('#cgstAmount').val(toCurrency(response.cgstAmount));
            $('#sgstAmount').val(toCurrency(response.sgstAmount));
            $('#cgstAmountLbl').text(toCurrency(response.cgstAmount));
            $('#sgstAmountLbl').text(toCurrency(response.sgstAmount));
        } else {
            $('#gstBlock').addClass('d-none')
        }
    }
}