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
    $('#backToPurchase').attr('data-id', jsonResponse.id);
    $('#cancelBtn').attr('data-id', jsonResponse.id);
    if (jsonResponse.purchaseItems) {
        renderRows(jsonResponse.purchaseItems)
    }

}

function renderRows(purchaseItems) {
        var html = '';
        purchaseItems.forEach(function(item, index) {
            html += generateRowHTML(item, index);
        });
        $('#purchaseItemsTBody').html(html);
    }


// Function to generate HTML for a single row
    function generateRowHTML(item, index) {
        return `
            <tr>
                <td><input type="text" name="name" id="purchaseItems[${index}].name" class="form-control table-input" autocomplete="off" value="${item.name}"></td>
                <td>
                    <input type="text" name="code" id="purchaseItems[${index}].code" class="form-control table-input" autocomplete="off" value="${item.code}">
                    <input type="hidden" name="id" id="purchaseItems[${index}].id" class="form-control" value="${item.id}">
                </td>
                <td><input type="number" name="weight" id="purchaseItems[${index}].weight" class="form-control table-input purchaseItemWeightCls" autocomplete="off" value="${item.weight}"></td>
                <td><input type="number" name="vaWeight" id="purchaseItems[${index}].vaWeight" class="form-control table-input" autocomplete="off" value="${item.vaWeight}"></td>
                <td><input type="number" name="saleMC" id="purchaseItems[${index}].saleMC" class="form-control table-input" autocomplete="off" value="${item.saleMC}"></td>
                <td><input type="number" name="stnWeight" id="purchaseItems[${index}].stnWeight" class="form-control table-input" autocomplete="off" value="${item.stnWeight}"></td>
                <td><input type="text" name="stnType" id="purchaseItems[${index}].stnType" class="form-control table-input" autocomplete="off" value="${item.stnType}"></td>
                <td><input type="number" name="stnCostPerCt" id="purchaseItems[${index}].stnCostPerCt" class="form-control table-input" autocomplete="off" value="${item.stnCostPerCt}"></td>
                <td><input type="number" name="pcs" id="purchaseItems[${index}].pcs" class="form-control table-input purchaseItemPcs" autocomplete="off" value="${item.pcs}"></td>
                <td>
                    <select name="active" id="purchaseItems[${index}].active" class="form-control">
                        <option value="true" ${item.active ? 'selected' : ''}>YES</option>
                        <option value="false" ${!item.active ? 'selected' : ''}>NO</option>
                    </select>
                </td>
                <td><button type="button" class="btn btn-danger removePurchaseItemRow"><i class="fas fa-trash"></i></button></td>
            </tr>
        `;
    }

$(document).on('click', '.backToPurchase', function(e) {
    e.preventDefault();
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
     '<td><input type="text" name="name" id="purchaseItems[' + rowCount + '].name" class="form-control table-input" autocomplete="off"></td>' +
     '<td>' +
         '<input type="text" name="code" id="purchaseItems[' + rowCount + '].code" class="form-control table-input" autocomplete="off">' +
         '<input type="hidden" name="id" id="purchaseItems[' + rowCount + '].id" class="form-control">' +
     '</td>' +
     '<td><input type="number" name="weight" id="purchaseItems[' + rowCount + '].weight" class="form-control table-input purchaseItemWeightCls" autocomplete="off"></td>' +
     '<td><input type="number" name="vaWeight" id="purchaseItems[' + rowCount + '].vaWeight" class="form-control table-input" autocomplete="off"></td>' +
     '<td><input type="number" name="saleMC" id="purchaseItems[' + rowCount + '].saleMC" class="form-control table-input" autocomplete="off"></td>' +
     '<td><input type="number" name="stnWeight" id="purchaseItems[' + rowCount + '].stnWeight" class="form-control table-input" autocomplete="off"></td>' +
     '<td><input type="text" name="stnType" id="purchaseItems[' + rowCount + '].stnType" class="form-control table-input" autocomplete="off"></td>' +
     '<td><input type="number" name="stnCostPerCt" id="purchaseItems[' + rowCount + '].stnCostPerCt" class="form-control table-input" autocomplete="off"></td>' +
     '<td><input type="number" name="pcs" id="purchaseItems[' + rowCount + '].pcs" class="form-control table-input purchaseItemPcs" autocomplete="off"></td>' +
     '<td>' +
         '<select name="active" id="purchaseItems[' + rowCount + '].active" class="form-control table-input">' +
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
