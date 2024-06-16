var _reportContext = '/reports'
var url = baseUrl + _reportContext;
var itemList = [];

$(document).ready(function(){
    var today = moment().format('YYYY-MM-DD');
   var firstDayOfMonth = moment().startOf('month').format('YYYY-MM-DD');
    $('#startDate').val(firstDayOfMonth);
    $('#endDate').val(today);

})

$('#report').on('click', function(){
    var reportRequest = formToJson('#reportForm');
    console.log(JSON.stringify(reportRequest));
    simpleCall(url+'/purchase', 'post', '', '', JSON.stringify(reportRequest), loadDatatable);
})

var supplierList = [];

$('#supplierName').autocomplete({
    source: function(request, response) {
        $.ajax({
            url: baseUrl+'/supplier/autocomplete',
            method: 'GET',
            dataType: 'json',
            data: {
                term: request.term
            },
            success: function(data) {
                // Manipulate data to extract only item codes
                var supplierList = $.map(data, function(supplier) {
                    return {
                        label: supplier.name,
                        value: supplier.name,
                        item: supplier
                    };
                });
                response(supplierList);
            }
        });
    },
    minLength: 2,
    select: function(event, ui) {
        console.log('Supplier name:', ui.item.item.name + ', Id: '+ui.item.item.id);
        $('#supplierId').val(ui.item.item.id)
    }
});

function loadDatatable(response) {
    $('#reportResponseTable').DataTable().destroy();
    $('#reportResponseTable').DataTable({
            data: response,
            columns: [
                {data: 'startDate', className: 'text-right', render: function (data, type, row) {return moment(data).format('DD/MM/YYYY');}},
                {data: 'totalNetWeight', className: 'text-right', render: function (data, type, row) {return toWt(data);}},
                {data: 'totalGst', className: 'text-right', render: function (data, type, row) {return toCurrency(data);}},
                {data: 'totalPurchaseAmount', className: 'text-right', render: function (data, type, row) {return toCurrency(data);}},
                {data: 'totalPaidAmount', className: 'text-right', render: function (data, type, row) {return toCurrency(data);}},
                {data: 'totalBalAmount', className: 'text-right', render: function (data, type, row) {return toCurrency(data);}}
            ],
            responsive: true,
            "columnDefs": [{
                    "targets": 'no-sort', // Target the columns with the class 'no-sort'
                    "orderable": false,   // Disable sorting for these columns
                }]
        });
}

function loadItemTypeDataTable(response) {
    console.log(response);

}