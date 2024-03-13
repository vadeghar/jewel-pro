$(document).ready(function(){
    $('#supplierName').autocomplete({
        source: function(request, response) {
            $.ajax({
                url: 'http://localhost:8080/api/v1/suppliers',
                method: 'GET',
                dataType: 'json',
                data: {
                    term: request.term
                },
                success: function(data) {
                    response(data);
                }
            });
        },
        minLength: 2 // Minimum characters to trigger autocomplete
    });

    $('#grossWeight, #stnWeight, #salePurity, #rate, #mcAmount').on('change', function(event) {
        calcNetWeight();
    });


    $('.editPurchase').click(function(event) {
        event.preventDefault();
        var purchaseId = $(this).data('id');
        var form = $('<form>').attr({
            method: 'post',
            action: '', // Set the form action
            id: 'dynamicForm'
        });
        var inputField = $('<input>').attr({
            type: 'text',
            name: 'id',
            value: purchaseId // Set the value to the purchase ID
        });
        form.append(inputField);
        $('body').append(form);
        form.submit();
    });

    $('.deletePurchase').on('click', function(){
        alert($(this).data("id"));
    });

    $('.purchaseItems').click(function(event) {
        event.preventDefault();
        var purchaseId = $(this).data('id');
        var form = $('<form>').attr({
            method: 'post',
            action: '/purchase/items', // Set the form action
            id: 'dynamicForm'
        });
        var inputField = $('<input>').attr({
            type: 'text',
            name: 'id',
            value: purchaseId // Set the value to the purchase ID
        });
        form.append(inputField);
        $('body').append(form);
        form.submit();
    });


});

function calcNetWeight() {
    var grossWeight = $('#grossWeight').val();
    var stnWeight = $('#stnWeight').val();
    var netWeight = grossWeight;

    if (stnWeight != '' && stnWeight > 0) {
        netWeight = grossWeight - stnWeight;
    } else {
        stnWeight = 0.000;
    }
    var effectiveWeight = netWeight;
    if(grossWeight != '') {
        $('#grossWeight').val(parseFloat(grossWeight).toFixed(3));
    }
    if(stnWeight != '') {
        $('#stnWeight').val(parseFloat(stnWeight).toFixed(3));
    }
    if(netWeight != '') {
        $('#netWeight').val(parseFloat(netWeight).toFixed(3));
    }

    if ($('#salePurity').val() != '' && netWeight != '') {
        effectiveWeight = parseFloat(netWeight) * (parseFloat($('#salePurity').val()) / 100);
        $('#effectiveWeight').val(parseFloat(effectiveWeight).toFixed(3));
    }
    var totalPurchaseAmount = 0;
    if ($('#mcAmount').val() != '') {
        totalPurchaseAmount = parseFloat($('#mcAmount').val());
    }
    if ($('#rate').val() != '' && effectiveWeight != '') {
        totalPurchaseAmount = totalPurchaseAmount + parseFloat(effectiveWeight) * parseFloat($('#rate').val());
        $('#totalPurchaseAmount').val(parseFloat(totalPurchaseAmount).toFixed(2));
    }


}