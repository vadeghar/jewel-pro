var _saleContext = '/sale'
var url = baseUrl + _saleContext;
var itemList = [];

$(document).on('focus', '.code', function() {
    $(this).autocomplete({
        source: function(request, response) {
            $.ajax({
                url: baseUrl+'/stock',
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
            $('#saleItemList\\[' + index + '\\]\\.stockId').val(ui.item.item.id);
            $('#saleItemList\\[' + index + '\\]\\.code').val(ui.item.item.code);
            $('#saleItemList\\[' + index + '\\]\\.name').val(ui.item.item.name);
            $('#saleItemList\\[' + index + '\\]\\.weight').val(ui.item.item.weight != null ? parseFloat(ui.item.item.weight).toFixed(3) : parseFloat(0).toFixed(3));
            $('#saleItemList\\[' + index + '\\]\\.vaWeight').val(ui.item.item.vaWeight != null ? parseFloat(ui.item.item.vaWeight).toFixed(3) : parseFloat(0).toFixed(3));
            $('#saleItemList\\[' + index + '\\]\\.stnWeight').val(ui.item.item.stnWeight != null ? parseFloat(ui.item.item.stnWeight).toFixed(3) : parseFloat(0).toFixed(3));
             $('#saleItemList\\[' + index + '\\]\\.netWeight').val(calcNetWeight(ui.item.item));
            $('#saleItemList\\[' + index + '\\]\\.makingCharge').val(ui.item.item.saleMC != null ? parseFloat(ui.item.item.saleMC).toFixed(2) : parseFloat(0).toFixed(3));

        }
    });
});

$(document).on('input', '.saleItemCls', function() {
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
    simpleCall(url+'/'+$('#id').val(), 'get', '', '', null, loadSaleCallback);
}

$('.gstBlock').addClass('d-none');
$('#invoiceNoLbl').addClass('d-none');
$('#isGstSale').change(function() {
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
 $("#addSaleItem").click(function() {
     var rowCount = $('#saleItemsTable tbody tr').length;
     var newRow = '<tr>'+
                       '<td>'+
                           '<input type="text" name="code" id="saleItemList['+rowCount+'].code" class="form-control code table-input">'+
                           '<input type="hidden" name="id" id="saleItemList['+rowCount+'].id" class="form-control">'+
                           '<input type="hidden" name="stockId" id="saleItemList['+rowCount+'].stockId">'+
                       '</td>'+
                       '<td><input type="text" name="name" id="saleItemList['+rowCount+'].name" class="form-control table-input"></td>'+
                       '<td><input type="text" name="weight" id="saleItemList['+rowCount+'].weight" class="form-control saleItemCls table-input"></td>'+
                       '<td><input type="text" name="vaWeight" id="saleItemList['+rowCount+'].vaWeight" class="form-control saleItemCls table-input"></td>'+
                       '<td><input type="text" name="stnWeight" id="saleItemList['+rowCount+'].stnWeight" class="form-control table-input"></td>'+
                       '<td><input type="text" name="netWeight" id="saleItemList['+rowCount+'].netWeight" class="form-control table-input"></td>'+
                       '<td><input type="text" name="makingCharge" id="saleItemList['+rowCount+'].makingCharge" class="form-control saleItemCls table-input"></td>'+
                       '<td><input type="text" name="rate" id="saleItemList['+rowCount+'].rate" class="form-control saleItemCls table-input"></td>'+
                       '<td><input type="text" name="itemTotal" id="saleItemList['+rowCount+'].itemTotal" class="form-control table-input"></td>'+
                       '<td><button type="button" class="btn btn-danger removeRow"><i class="fas fa-trash"></i></button></td>'+
                   '</tr>';
     $("#saleItemsTable tbody").append(newRow);
 });

 // Remove row
 $("#saleItemsTable").on("click", ".removeRow", function() {
    var rowCount = $('#saleItemsTable tbody tr').length;
    if(rowCount > 1) {
        $(this).closest("tr").remove();
        calcMainTotals();
    } else {
        alert("At least 1 Item to be added to do a sale.")
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
        alert("At least 1 Item to be added to do a sale.")
    }
});
$('#discount').on('input', function() {
    calcMainTotals();
});

$('#paidAmount').on('input', function() {
    calcMainTotals();
});



$('#saleDate').val(getCurrentDate()).attr('disabled', true);
});

function calcNetWeight(item) {
    var weight = item.weight != null ? parseFloat(item.weight) : 0.000;
    var vaWeight = item.vaWeight != null ? parseFloat(item.vaWeight) : 0.000;
    var stnWeight = item.stnWeight != null ? parseFloat(item.stnWeight) : 0.000;
    var grossWeight = weight + vaWeight - stnWeight;
    return grossWeight.toFixed(3);
}


function calcItemTotal(index) {

    var weight = parseFloat($('#saleItemList\\[' + index + '\\]\\.weight').val());
    var vaWeight = parseFloat($('#saleItemList\\[' + index + '\\]\\.vaWeight').val());
    var stnWeight = parseFloat($('#saleItemList\\[' + index + '\\]\\.stnWeight').val());
    var makingCharge = parseFloat($('#saleItemList\\[' + index + '\\]\\.makingCharge').val());
    var rate = parseFloat($('#saleItemList\\[' + index + '\\]\\.rate').val());
    console.log('index:'+index+', weight: '+weight+', vaWeight:'+vaWeight+', makingCharge:'+makingCharge+', rate:'+rate);
    // Calculate gross weight
    var grossWeight = weight + vaWeight;
    var itemTotal = (grossWeight * rate) + makingCharge;
    $('#saleItemList\\[' + index + '\\]\\.itemTotal').val(itemTotal.toFixed(2));
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
    $("#saleItemsTable tbody tr").each(function() {
        // Get the exchange value from the current row and add it to the total
        $(this).find("input[id*='saleItemList'][id$='.itemTotal']").each(function() {
            var itemValue = parseFloat($(this).val());
            if (!isNaN(itemValue)) {
                itemTotalValue += itemValue;
            }
        });
    });
    // Display the total exchange value
    itemTotalValue = Math.round(itemTotalValue);
    $("#totalSaleAmountLbl").text(itemTotalValue.toFixed(2));
    $("#totalSaleAmount").val(itemTotalValue.toFixed(2));

    if ($('#isGstSale').val() === 'YES') {
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
    var totalSaleAmount = $('#totalSaleAmount').val() != '' ? parseFloat($('#totalSaleAmount').val()) : 0.00;
    var totalExchangeAmount = $('#totalExchangeAmount').val() != '' ? parseFloat($('#totalExchangeAmount').val()) : 0.00;
    var discount = $('#discount').val() != '' ? parseFloat($('#discount').val()) : 0.00;
    var cGstAmount = $('#cgstAmount').val() != '' ? parseFloat($('#cgstAmount').val()) : 0.00;
    var sGstAmount = $('#sgstAmount').val() != '' ? parseFloat($('#sgstAmount').val()) : 0.00;

    var grandTotalSaleAmountLbl = ((totalSaleAmount - totalExchangeAmount) - discount) + (cGstAmount + sGstAmount);
    if(grandTotalSaleAmountLbl > 0) {
        $('#grandTotalSaleAmount').val(grandTotalSaleAmountLbl.toFixed(2));
        $('#grandTotalSaleAmountLbl').text(grandTotalSaleAmountLbl.toFixed(2));

    }
}

function calcBalAmount() {
    var grandTotalSaleAmount = $('#grandTotalSaleAmount').val();
    var balAmount = $('#balAmount').val();
    var paidAmount = $('#paidAmount').val();
    var balAmount = grandTotalSaleAmount - paidAmount;
    $('#balAmount').val(balAmount.toFixed(2));
    $('#balAmountLbl').text(balAmount.toFixed(2));
}

$('#saveSale').on('click', function(){
    submit();
});

function submit() {
    var saleDTO = {};
    var customer = {};
    customer.name = $('#name').val();
    customer.phone = $('#phone').val();
    customer.address = $('#address').val();
    customer.id = $('#customerId').val();

    var saleId = $('#id').val();
    var saleItemList = [];
    var exchangeItemList = [];
    $('#saleItemsTBody tr').each(function() {
        var jsonData = formToJson(this);
        saleItemList.push(jsonData);
    });
    $('#exchangeItemsTBody tr').each(function() {
        var jsonData = formToJson(this);
        exchangeItemList.push(jsonData);
    });
    saleDTO.id = saleId;
//    saleDTO.saleDate = $('#saleDate').val();
    saleDTO.customer = customer;
    saleDTO.saleItemList = saleItemList
    saleDTO.exchangeItemList = exchangeItemList;

    $('.saleDto input').each(function() {
        if (!$(this).closest('#saleItemsTable, #exchangeItemsTable').length) {
            // Get the name and value of each input element
            var name = $(this).attr('name');
            var value = $(this).val();

            // If the name and value are not empty, add them to the jsonData object
            if (name !== undefined && name !== '' && value !== undefined && value !== '') {
                saleDTO[name] = value;
            }
        }
    });
    $('.saleDto select').each(function() {
        if (!$(this).closest('#saleItemsTable, #exchangeItemsTable').length) {
            // Get the name and value of each input element
            var name = $(this).attr('name');
            var value = $(this).val();

            // If the name and value are not empty, add them to the jsonData object
            if (name !== undefined && name !== '' && value !== undefined && value !== '') {
                saleDTO[name] = value;
            }
        }
    });

    $('.saleDto textarea').each(function() {
        if (!$(this).closest('#saleItemsTable, #exchangeItemsTable').length) {
            // Get the name and value of each input element
            var name = $(this).attr('name');
            var value = $(this).val();

            // If the name and value are not empty, add them to the jsonData object
            if (name !== undefined && name !== '' && value !== undefined && value !== '') {
                saleDTO[name] = value;
            }
        }
    });
    simpleCall(url, 'post', '', '', JSON.stringify(saleDTO, null, 2), saveSaleCallback)
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

function saveSaleCallback(response) {
    navigateWindow('/sale/view?id='+response.id);
}


$('#name').autocomplete({
    source: function(request, response) {
        $.ajax({
            url: baseUrl+'/customer/like',
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

function loadSaleCallback(response) {
    if (response != null && response != '') {
        $('#invoiceNo').val(response.invoiceNo);
        $('#invoiceNoLbl').text('Invoice No. '+response.invoiceNo);
        $('#invoiceNoLbl').removeClass('d-none');
        $('#saleDate').val(response.saleDate);
        $('#saleType').val(response.saleType);
        $('#isGstSale').val(response.isGstSale);

        $('#name').val(response.customer.name);
        $('#phone').val(response.customer.phone);
        $('#address').val(response.customer.address);
        $('#customerId').val(response.customer.id);

        var saleItemList = response.saleItemList;
        $('#saleItemsTable tbody').empty();
        // Iterate over each item in saleItemList
        $.each(saleItemList, function(index, saleItem) {
            // Append a new row to the table body for each item
            $('#saleItemsTable tbody').append(
            '<tr>' +
                 '<td>' +
                     '<input type="text" name="code" id="saleItemList['+index+'].code" value="' + saleItem.code + '" class="form-control code table-input">' +
                     '<input type="hidden" name="id" id="saleItemList['+index+'].id" value="' + saleItem.id + '" class="form-control">' +
                     '<input type="hidden" name="stockId" id="saleItemList['+index+'].stockId "value="' + saleItem.stockId + '" >' +
                 '</td>' +
                 '<td><input type="text" name="name" id="saleItemList['+index+'].name"value="' + saleItem.name + '"  class="form-control table-input"></td>' +
                 '<td><input type="text" name="weight" id="saleItemList['+index+'].weight" value="' + toWt(saleItem.weight) + '"  class="form-control saleItemCls table-input"></td>' +
                 '<td><input type="text" name="vaWeight" id="saleItemList['+index+'].vaWeight" value="' + toWt(saleItem.vaWeight) + '"  class="form-control saleItemCls table-input"></td>' +
                 '<td><input type="text" name="stnWeight" id="saleItemList['+index+'].stnWeight" value="' + toWt(saleItem.stnWeight) + '" class="form-control table-input"></td>' +
                 '<td><input type="text" name="netWeight" id="saleItemList['+index+'].netWeight" value="' + toWt(saleItem.netWeight) + '" class="form-control table-input"></td>' +
                 '<td><input type="text" name="makingCharge" id="saleItemList['+index+'].makingCharge" value="' + toCurrency(saleItem.makingCharge) + '" class="form-control saleItemCls table-input"></td>' +
                 '<td><input type="text" name="rate" id="saleItemList['+index+'].rate" value="' + toCurrency(saleItem.rate) + '" class="form-control saleItemCls table-input"></td>' +
                 '<td><input type="text" name="itemTotal" id="saleItemList['+index+'].itemTotal" value="' + toCurrency(saleItem.itemTotal) + '" class="form-control table-input" readonly></td>' +
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

        $('#totalSaleAmount').val(toCurrency(response.totalSaleAmount));
        $('#totalSaleAmountLbl').text(toCurrency(response.totalSaleAmount));
        $('#totalExchangeAmount').val(toCurrency(response.totalExchangeAmount));
        $('#totalExchangeAmountLbl').text(toCurrency(response.totalExchangeAmount));
        $('#discount').val(toCurrency(response.discount))
        $('#grandTotalSaleAmount').val(toCurrency(response.grandTotalSaleAmount));
        $('#grandTotalSaleAmountLbl').text(toCurrency(response.grandTotalSaleAmount));
        $('#paymentMode').val(response.paymentMode);
        $('#paidAmount').val(toCurrency(response.paidAmount));
        $('#balAmount').val(toCurrency(response.balAmount));
        $('#balAmountLbl').text(toCurrency(response.balAmount));
        $('#saleType').val(response.saleType);


        $('#description').val(response.description);
        if (response.isGstSale == 'YES') {
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