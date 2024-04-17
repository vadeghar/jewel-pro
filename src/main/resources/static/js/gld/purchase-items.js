var baseUrl = 'http://localhost:8080/api/v1';
var _purchaseContext = '/purchase'
var url = baseUrl + _purchaseContext;

$(document).ready(function() {
    var id = $('#purchaseId').val();
    simpleCall(url+'?id='+id, 'get', '', '', '', getPurchaseCallback)
});

function getPurchaseCallback(jsonResponse) {
    console.log(JSON.stringify(jsonResponse));
    $('#purchaseDate').text(jsonResponse.purchaseDate);
    $('#purchaseType').text(jsonResponse.purchaseType);
    $('#supplierName').text(jsonResponse.supplierName);
    $('#purchaseBillNo').text(jsonResponse.purchaseBillNo);
    $('#metalType').text(jsonResponse.metalType);
    $('#totalGrossWeight').text(jsonResponse.totalGrossWeight);
    $('#balAmount').text(jsonResponse.balAmount);
    $('#isGstPurchase').text(jsonResponse.isGstPurchase);
    $('#totalPcs').text(jsonResponse.totalPcs);
    $('#totalWeight').text(jsonResponse.totalGrossWeight);
    $('#totalNoOfPcs').text(jsonResponse.totalPcs);
}

$("#addPurchaseItem").click(function() {
    var totalAddedPcs = $('#addedPcs').text();
    var totalNoOfPcs = $('#totalNoOfPcs').text();

    if(parseInt(totalAddedPcs) >= parseInt(totalNoOfPcs)) {
        alert("No of pcs are matched, adding row is not allowed this purchase.");
        return;
    }

    var totalAddedWt = $('#addedWeight').text();
    var totalWt = $('#totalWeight').text();

    if(parseInt(totalAddedWt) >= parseInt(totalWt)) {
        alert("Weight is matched, adding row is not allowed this purchase.");
        return;
    }

    var rowCount = $('#purchaseItemsTable tbody tr').length;
    var newRow = '<tr>' +
     '<td><input type="text" name="name" id="purchaseItems[' + rowCount + '].name" class="form-control" autocomplete="off"></td>' +
     '<td>' +
         '<input type="text" name="code" id="purchaseItems[' + rowCount + '].code" class="form-control" autocomplete="off">' +
         '<input type="hidden" name="id" id="purchaseItems[' + rowCount + '].id" class="form-control">' +
     '</td>' +
     '<td><input type="number" name="weight" id="purchaseItems[' + rowCount + '].weight" class="form-control purchaseItemWeightCls" autocomplete="off"></td>' +
     '<td><input type="number" name="vaWeight" id="purchaseItems[' + rowCount + '].vaWeight" class="form-control" autocomplete="off"></td>' +
     '<td><input type="number" name="saleMC" id="purchaseItems[' + rowCount + '].saleMC" class="form-control" autocomplete="off"></td>' +
     '<td><input type="number" name="stnWeight" id="purchaseItems[' + rowCount + '].stnWeight" class="form-control" autocomplete="off"></td>' +
     '<td><input type="text" name="stnType" id="purchaseItems[' + rowCount + '].stnType" class="form-control" autocomplete="off"></td>' +
     '<td><input type="number" name="stnCostPerCt" id="purchaseItems[' + rowCount + '].stnCostPerCt" class="form-control" autocomplete="off"></td>' +
     '<td><input type="number" name="pcs" id="purchaseItems[' + rowCount + '].pcs" class="form-control purchaseItemPcs" autocomplete="off"></td>' +
     '<td>' +
         '<select name="active" id="purchaseItems[' + rowCount + '].active" class="form-control">' +
             '<option value="true">YES</option>' +
             '<option value="false">NO</option>' +
         '</select>' +
     '<td><button type="button" class="btn btn-danger removePurchaseItemRow"><i class="fas fa-trash"></i></button></td>' +
    '</tr>';
    $("#purchaseItemsTable tbody").append(newRow);
});

$(document).on('input', '.purchaseItemPcs', function(){
    adjustPurchaseItemHeadingAttrs();
});

$(document).on('input', '.purchaseItemWeightCls', function(){
    adjustPurchaseItemHeadingAttrs();
});

function adjustPurchaseItemHeadingAttrs() {
    var totalPurchaseItemsWeight = 0;
    var totalPurchaseItemsPcs = 0;
    $('.purchaseItemWeightCls').each(function() {
        var weight = parseFloat($(this).val());
        if (!isNaN(weight)) {
            totalPurchaseItemsWeight += weight;
        }
    });
    $('#addedWeight').text(parseFloat(totalPurchaseItemsWeight).toFixed(3));
    $('.purchaseItemPcs').each(function() {
        var pcs = parseFloat($(this).val());
        if (!isNaN(pcs)) {
            totalPurchaseItemsPcs += pcs;
        }
    });
    $('#addedPcs').text(parseFloat(totalPurchaseItemsPcs).toFixed(0));
}

$("#purchaseItemsTable").on("click", ".removePurchaseItemRow", function() {
    var rowCount = $('#purchaseItemsTable tbody tr').length;
    if(rowCount > 1) {
        $(this).closest("tr").remove();
        adjustPurchaseItemHeadingAttrs();
    } else {
        alert("At least 1 Item to be added to do a purchase.")
    }
});

$('#savePurchaseItems').on('click', function(){
    var purchaseId = $('#purchaseId').val();
    var jsonList = [];
    $('table tbody tr').each(function() {
        var jsonData = formToJson(this);
        jsonList.push(jsonData);
    });
    var jsonString = JSON.stringify(jsonList, null, 2);
    console.log(jsonString);
    simpleCall(url+'/'+purchaseId+'/items', 'post', '', '', jsonString, savePurchaseItemsCallback)
});

function savePurchaseItemsCallback(response) {
    console.log(response);
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
