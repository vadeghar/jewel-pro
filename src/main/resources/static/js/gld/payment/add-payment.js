var _saleContext = '/sale'
var url = baseUrl + _saleContext;

$(document).ready(function() {
    $(document).on('change', '#paymentModeMdl', function(){
    if ($(this).val() == 'card') {
        $('#lastFourDigitsMdl').parent().removeClass('d-none');
    } else {
        $('#lastFourDigitsMdl').parent().addClass('d-none');
    }
    })
});

$('#savePayment').on('click', function(){
//    alert('paid: '+$('#paidAmountMdl').val()+' bal: '+$('#balAmount').text()+' mode:'+$('#paymentModeMdl').val())
    var balAmount = $('#balAmount').text();
    var paidAmount = $('#paidAmountMdl').val();
    if (paidAmount == '') {
        alert('Enter amount.')
    } else if (parseFloat(paidAmount) > parseFloat(balAmount)) {
        alert('Enter amount less than or equal to '+balAmount)
    } else {
        if ($('#source').val().toUpperCase() == 'PURCHASE') {
            _saleContext = '/purchase'
            url = baseUrl + _saleContext;
        }
        var payment = {};
        payment.source = $('#source').val();
        payment.sourceId = $('#sourceId').val();
        payment.paymentMode = $('#paymentModeMdl').val();
        payment.lastFourDigits = $('#lastFourDigitsMdl').val();
        payment.paidAmount = paidAmount;
        simpleCall(url+'/'+$('#sourceId').val()+'/payment', 'post', '', '', JSON.stringify(payment), redirect);
    }

});
function redirect(response) {
    navigateWindow('http://localhost:8080'+_saleContext+'/view?id='+response.id);
}