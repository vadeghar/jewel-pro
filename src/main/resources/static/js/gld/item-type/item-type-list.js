var baseUrl = 'http://localhost:8080/api/v1';
var url = baseUrl + '/itemtype';

$(document).ready(function() {
    loadDatatable();
});

$(document).on('click', '.editRowCls', function() {
    var id = $(this).data('id');
    var form = $('<form></form>', {
        'method': 'POST',
        'action': 'itemtype/edit'
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

$(document).on('click', '.deleteRowCls', function() {
    var id = $(this).data('id');
    createDynamicModal(id, "Delete Item type.", "Are you sure want to delete?", "Yes", deleteItemType)
});

function deleteItemType(id) {
    //alert(id)
    simpleCall(url+'/'+id, 'delete', '', '', '', loadDatatable)
}

function loadDatatable() {
    $('#itemTypeDataTable').DataTable().destroy();
    simpleCall(url, 'get', '', '', '', loadItemTypeDataTable)
}

function loadItemTypeDataTable(response) {
    console.log(response);
    $('#itemTypeDataTable').DataTable({
        data: response,
        columns: [
            {data: 'id'},
            {data: 'itemType'},
            {
              data: 'id',
              render: function (data, type, row) {
                  return '<button type="button" class="btn btn-sm btn-danger deleteRowCls" data-id="' + data +'"><i class="fas fa-trash"></i> Delete</button> ' +
                      '<button type="button" class="btn btn-sm btn-primary editRowCls" data-id="' + data +'"><i class="fas fa-edit"></i> Edit</button> ';
              }
            }
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }],
        order: [[2, 'desc']]
    });
}
