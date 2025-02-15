var _supplierContext = '/rate'
var url = baseUrl + _supplierContext;
$(document).ready(function() {
    var source = $('#source').val();
    $('#itemMetal').val(source);
    $('#itemMetal').attr('disabled', true);
});

$('#saveRate').on('click', function(e){
    if($('#itemMetal').val() == '' || $('#rate').val() == '') {
        alert('Enter required data.');
        return;
    }
    var rateForm = formToJson('#rateForm');
//    console.log(JSON.stringify(rateForm));
    simpleCall(url+'/save', 'post', '', '', JSON.stringify(rateForm), saveRateCallback);
});

function saveRateCallback(response) {
    $('#currentSilverRate').text(toCurrency(response.silverRate));
    $('#currentGold24CtRate').text(toCurrency(response.goldRate));
    $('#currentGold22CtRate').text(toCurrency(response.gold22CtRate));
    $("#myModal").modal('hide');
}