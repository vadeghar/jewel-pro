$(document).ready(function(){
    $('.existingItem').change(function(){
        if($(this).val() == 'YES'){
            $('#itemTagNoId').removeClass('d-none')
        } else {
            $('#itemTagNoId').addClass('d-none')
            $('#itemContent').removeClass('d-none')
        }
    });

    if($('.existingItem').val() == 'YES'){
        $('#itemTagNoId').removeClass('d-none')
        $('#itemContent').removeClass('d-none')
    } if($('.existingItem').val() == 'NO'){
//          $('#itemTagNoId').removeClass('d-none')
          $('#itemContent').removeClass('d-none')
      } else {
        $('#itemTagNoId').addClass('d-none')
        $('#itemContent').removeClass('d-none')
    }
});