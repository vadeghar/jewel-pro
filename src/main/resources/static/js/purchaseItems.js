$(document).ready(function() {
    // Counter to keep track of the last index of added rows


    // Add row function
    $("#addRow").click(function() {
        var rowCount = $('#itemTable tbody tr').length;
//        rowCount++; // Increment row count
        var newRow = '<tr>' +
            '<td><input type="hidden" name="purchaseItems[' + rowCount + '].id" class="form-control"><input type="text" name="purchaseItems[' + rowCount + '].name" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].code" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].weight" class="form-control item-weight"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].vaWeight" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].makingCharge" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].stnWeight" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].stnType" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].stnCostPerCt" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].pcs" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].huid" class="form-control"></td>' +
            '<td><button type="button" class="btn btn-danger removeRow">Remove</button></td>' +
            '</tr>';
        $("#itemTable tbody").append(newRow);
    });


    // Remove row
    $("#itemTable").on("click", ".removeRow", function() {
        $(this).closest("tr").remove();
    });

    var mainPurchaseWeight = $('#purchaseWeight').text();
    var totalPurchaseItemsWeight = 0;

    function calculateTotalWeight() {
        totalPurchaseItemsWeight = 0;
        $('.item-weight').each(function() {
            var weight = parseFloat($(this).val());
            if (!isNaN(weight)) {
                totalPurchaseItemsWeight += weight;
            }
        });
    }



    function checkWeightAndToggleAddButton() {
        console.log('totalPurchaseItemsWeight: '+totalPurchaseItemsWeight);
        console.log('mainPurchaseWeight: '+mainPurchaseWeight);
        $('#itemsWeight').text(totalPurchaseItemsWeight.toFixed(3))
        if (totalPurchaseItemsWeight > mainPurchaseWeight) {
            $('#addRow').prop('disabled', true);
            $('#saveItems').prop('disabled', true);
            $('#itemsWeight').attr('class', 'bg-danger text-white');
        } else if (totalPurchaseItemsWeight < mainPurchaseWeight) {
            $('#addRow').prop('disabled', false);
            $('#saveItems').prop('disabled', false);
            $('#itemsWeight').attr('class', 'bg-warning text-white');
        } else if (totalPurchaseItemsWeight == mainPurchaseWeight) {
            $('#addRow').prop('disabled', true);
            $('#saveItems').prop('disabled', false);
            $('#itemsWeight').attr('class', 'bg-primary text-white');
        }

    }

    $('.item-weight').on('input', function() {
        applyBgAdnToggleSaveAddBtns();
    });

    function applyBgAdnToggleSaveAddBtns() {
        calculateTotalWeight();
        checkWeightAndToggleAddButton();
    }

    applyBgAdnToggleSaveAddBtns();
});