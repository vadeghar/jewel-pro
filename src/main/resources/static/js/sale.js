var itemList = [];
$(document).on('focus', '.code', function() {
    $(this).autocomplete({
        source: function(request, response) {
            $.ajax({
                url: 'http://localhost:8080/api/v1/items',
                method: 'GET',
                dataType: 'json',
                data: {
                    term: request.term
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
            var startIndex = inputName.indexOf('[') + 1;
            var endIndex = inputName.indexOf(']');
            var index = inputName.substring(startIndex, endIndex);

            console.log('Index:', index);

            // Use the id or name as needed
            console.log('Input ID:', inputId);
            console.log('Input Name:', inputName);
            $('#saleItemList\\[' + index + '\\]\\.id').val(ui.item.item.id);
            $('#saleItemList\\[' + index + '\\]\\.code').val(ui.item.item.code);
            $('#saleItemList\\[' + index + '\\]\\.name').val(ui.item.item.name);
            $('#saleItemList\\[' + index + '\\]\\.weight').val(ui.item.item.weight != null ? parseFloat(ui.item.item.weight).toFixed(3) : 0.000);
            $('#saleItemList\\[' + index + '\\]\\.vaWeight').val(ui.item.item.vaWeight != null ? parseFloat(ui.item.item.vaWeight).toFixed(3) : 0.000);
            $('#saleItemList\\[' + index + '\\]\\.makingCharge').val(ui.item.item.makingCharge != null ? parseFloat(ui.item.item.makingCharge).toFixed(2) : 0.000);
            $('#saleItemList\\[' + index + '\\]\\.stnWeight').val(ui.item.item.stnWeight != null ? parseFloat(ui.item.item.stnWeight).toFixed(3) : 0.000);
            $('#saleItemList\\[' + index + '\\]\\.stnType').val(ui.item.item.stnType);
            $('#saleItemList\\[' + index + '\\]\\.stnCostPerCt').val(ui.item.item.stnCostPerCt);
            $('#saleItemList\\[' + index + '\\]\\.netWeight').val(calcNetWeight(ui.item.item));
            $('#saleItemList\\[' + index + '\\]\\.rate').val(ui.item.item.rate);

        }
    });
});

$(document).on('input', '.rate, .vaWeight, .makingCharge', function() {
    var $inputElement = $(this); // Get the current input element
    var inputId = $inputElement.attr('id'); // Get the id attribute
    var inputName = $inputElement.attr('name'); // Get the name attribute
    var startIndex = inputName.indexOf('[') + 1;
    var endIndex = inputName.indexOf(']');
    var index = inputName.substring(startIndex, endIndex);
    calcItemTotal(index);
});

$(document).ready(function(){

$('.enableGst').change(function() {
    // Check if "NO" option is selected
    if ($(this).val() === 'NO') {
        // Hide the div element
        $('#gstBlock').addClass('d-none'); // Replace '.div-to-hide' with the appropriate selector for your div
    } else {
        // Show the div element
        $('#gstBlock').removeClass('d-none'); // Replace '.div-to-hide' with the appropriate selector for your div
    }
});

     // Add row function
     $("#addNewItem").click(function() {
         var rowCount = $('#saleItemsTable tbody tr').length;
         var newRow = '<tr>' +
             '<td>' +
                 '<input type="text" name="saleItemList[' + rowCount + '].code" id="saleItemList[' + rowCount + '].code" class="form-control code">' +
                 '<input type="hidden" name="saleItemList[' + rowCount + '].id" id="saleItemList[' + rowCount + '].id" class="form-control">' +
             '</td>' +
             '<td><input type="text" name="saleItemList[' + rowCount + '].name" id="saleItemList[' + rowCount + '].name" class="form-control"></td>' +
             '<td><input type="text" name="saleItemList[' + rowCount + '].weight" id="saleItemList[' + rowCount + '].weight" class="form-control item-weight"></td>' +
             '<td><input type="text" name="saleItemList[' + rowCount + '].vaWeight" id="saleItemList[' + rowCount + '].vaWeight" class="form-control vaWeight"></td>' +
             '<td><input type="text" name="saleItemList[' + rowCount + '].makingCharge" id="saleItemList[' + rowCount + '].makingCharge" class="form-control makingCharge"></td>' +
             '<td><input type="text" name="saleItemList[' + rowCount + '].netWeight" id="saleItemList[' + rowCount + '].netWeight" class="form-control"></td>' +
             '<td><input type="text" name="saleItemList[' + rowCount + '].rate" id="saleItemList[' + rowCount + '].rate" class="form-control rate"></td>' +
             '<td><input type="text" name="saleItemList[' + rowCount + '].itemTotal" id="saleItemList[' + rowCount + '].itemTotal" class="form-control"></td>' +
             '<td><button type="button" class="btn btn-danger removeRow">Remove</button></td>' +
         '</tr>';
         $("#saleItemsTable tbody").append(newRow);
     });

     // Remove row
     $("#saleItemsTable").on("click", ".removeRow", function() {
        var rowCount = $('#saleItemsTable tbody tr').length;
        if(rowCount > 1) {
            $(this).closest("tr").remove();
        } else {
            alert("At least 1 Item to be added to do a sale.")
        }
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
//    var stnWeight = parseFloat($('#saleItemList\\[' + index + '\\]\\.stnWeight').val());
    var makingCharge = parseFloat($('#saleItemList\\[' + index + '\\]\\.makingCharge').val());
    var rate = parseFloat($('#saleItemList\\[' + index + '\\]\\.rate').val());
    console.log('index:'+index+', weight: '+weight+', vaWeight:'+vaWeight+', makingCharge:'+makingCharge+', rate:'+rate);
    // Calculate gross weight
    var grossWeight = weight + vaWeight;
    var itemTotal = (grossWeight * rate) + makingCharge;
    $('#saleItemList\\[' + index + '\\]\\.itemTotal').val(itemTotal.toFixed(2));

}

function getCurrentDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}
//
//// Function to add item to the itemList
//function addItemToList() {
//    // Get input values from modal
//    var newItem = {
//        code: $('#code').val(),
//        metalType: $('input[name="metalType"]:checked').val(),
//        name: $('#name').val(),
//        purity: $('#purity').val(),
//        weight: $('#weight').val(),
//        stnWeight: $('#stnWeight').val(),
//        vaWeight: $('#vaWeight').val(),
//        netWeight: $('#netWeight').val(),
//        makingCharge: $('#makingCharge').val(),
//        stnType: $('#stnType').val(),
//        stnCostPerCt: $('#stnCostPerCt').val(),
//        pcs: $('#pcs').val(),
//        huid: $('#huid').val(),
//        rate: $('#rate').val(),
//        itemTotal: $('#itemTotal').val()
//    };
//
//    // Add new item to the itemList
//    itemList.push(newItem);
//
//    // Clear modal inputs
//    $('#itemModal input').val('');
//
//    // Add item to table
//    addItemToTable(newItem);
//}
//
//// Function to add item to the table
//function addItemToTable(item) {
//    var tableRow = '<tr>';
//    tableRow += '<td>' + item.code + '</td>';
//    tableRow += '<td>' + item.metalType + '</td>';
//    tableRow += '<td>' + item.name + '</td>';
//    tableRow += '<td>' + item.weight + '</td>';
//    tableRow += '<td>' + item.vaWeight + '</td>';
//    tableRow += '<td>' + item.netWeight + '</td>';
//    tableRow += '<td>' + item.makingCharge + '</td>';
//    tableRow += '<td>' + item.rate + '</td>';
//    tableRow += '<td>' + item.itemTotal + '</td>';
//    tableRow += '</tr>';
//
//    $('#itemTable tbody').append(tableRow);
//}
//
//// Event listener for "Add Item" button click
//$('#itemModal').on('click', '.addItem', function() {
//    addItemToList();
//});