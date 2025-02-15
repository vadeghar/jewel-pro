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
    //reportRequest['isGstPurchase'] = 'YES';
    console.log(JSON.stringify(reportRequest));
    simpleCall(url+'/sale', 'post', '', '', JSON.stringify(reportRequest), loadDatatable);
})

var supplierList = [];

$('#supplierName').autocomplete({
    source: function(request, response) {
        $.ajax({
            url: url+'/customer/like',
            method: 'GET',
            dataType: 'json',
            data: {
                name: request.term
            },
            success: function(data) {
                var itemCodes = $.map(data, function(item) {
                    return {
                        label: item.name +' - '+item.phone,
                        value: item.name,
                        item: item
                    };
                });
                response(itemCodes);
            }
        });
    },
    minLength: 2,
    select: function(event, ui) {
        console.log('Customer name:', ui.item.item.name + ', Id: '+ui.item.item.id);
        $('#customerId').val(ui.item.item.id)
    }
});

function loadDatatable(response) {
    $('#reportResponseTable').DataTable().destroy();
    $('#reportResponseTable').DataTable({
            dom: 'Bfrtip', // This is necessary to enable buttons
            buttons: [{
                          extend: 'excel',
                          title: 'Custom Report Title',
                          messageTop: function () {
                              return moment($('#startDate').val()).format('DD/MM/yyyy'); // Read the value from the input
                          },
                          text: 'Export to Excel'
                      },
                      {
                          extend: 'pdf',
                          title: function() {
                            return $('#reportHeading').text();
                          },
                          customize: function (doc) {
                            var pdfHeading = ''+moment($('#startDate').val()).format('DD/MM/yyyy')+' - '+moment($('#endDate').val()).format('DD/MM/yyyy');
                              doc.content.splice(0, 0, {
                                  text: pdfHeading,
                                  style: 'header',
                                  alignment: 'center',
                                  margin: [0, 0, 0, 12]
                              });
                          }
                      },
                      'print',
                      'colvis'],
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
