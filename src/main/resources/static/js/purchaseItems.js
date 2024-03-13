$(document).ready(function() {
    // Counter to keep track of the last index of added rows


    // Add row function
    $("#addRow").click(function() {
        var rowCount = $('#itemTable tbody tr').length;
//        rowCount++; // Increment row count
        var newRow = '<tr>' +
            '<td><input type="hidden" name="purchaseItems[' + rowCount + '].id" class="form-control"><input type="text" name="purchaseItems[' + rowCount + '].name" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].code" class="form-control"></td>' +
            '<td><input type="text" name="purchaseItems[' + rowCount + '].weight" class="form-control"></td>' +
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


});