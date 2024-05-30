Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';
var _graphContext = '/graph'
var url = baseUrl + _graphContext;
var myLineChart;
var myPieChart;
$(document).ready(function() {
    // Set start date to the first day of the current month
    var startDate = moment().startOf('month').format('YYYY-MM-DD');
    $('#startDate').val(startDate);

    // Set end date to today's date
    var endDate = moment().format('YYYY-MM-DD');
    $('#endDate').val(endDate);
    $('#graphType').val('weekly');

    var startDate = $('#startDate').val();
    var endDate = $('#endDate').val();
    var graphType = $('#graphType').val('weekly');
    loadGraphData(startDate, endDate, 'weekly');

    $('#go').on('click', function(){
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        var graphType = $('#graphType').val();
        loadGraphData(startDate, endDate, graphType);
    });
    simpleCall(url+'/purchase/current-month-total', 'get', '', '', '', setCurrentMonthPurchaseTotal);
    simpleCall(url+'/sale/current-month-total', 'get', '', '', '', setCurrentMonthSaleTotal);
    simpleCall(url+'/sale/top-records', 'get', '', '', '', loadPieChart);
});

function loadGraphData(startDate, endDate, graphType) {
    simpleCall(url+'/sale/'+graphType+'?startDate='+startDate+'&endDate='+endDate, 'get', '', '', '', generateGraph);
}
function loadPieChart(response) {
    var labels = [];
    var values = [];
    let totalAmount = response.reduce((sum, item) => sum + item.value, 0);
    response.forEach(item => {
        let percentage = ((item.value / totalAmount) * 100).toFixed(0);
        //console.log(`Customer ID: ${item.label}, Amount: ${item.value}, Percentage: ${percentage}%`);
        labels.push(item.label);
        values.push(percentage);
    });
    //console.log(labels);
    //console.log(values);
    updatePieChart(labels, values);
}
function generateGraph(response) {
    console.log(response);
    var labels = [];
    var values = [];
    $.each(response, function(index, item) {
        labels.push(item.label);
        values.push(item.value);
    });
    updateChart(labels, values);
}

function setCurrentMonthPurchaseTotal(response) {
    $('#currentMonthPurchase').text('₹' + number_format(response))
}
function setCurrentMonthSaleTotal(response) {
    $('#currentMonthSale').text('₹' + number_format(response))
}


function updateChart(labels, values) {
    var ctx = document.getElementById("myAreaChart");
    if(myLineChart) {
        myLineChart.destroy();
    }
    myLineChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: "Earnings",
          lineTension: 0.3,
          backgroundColor: "rgba(78, 115, 223, 0.05)",
          borderColor: "rgba(78, 115, 223, 1)",
          pointRadius: 3,
          pointBackgroundColor: "rgba(78, 115, 223, 1)",
          pointBorderColor: "rgba(78, 115, 223, 1)",
          pointHoverRadius: 3,
          pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
          pointHoverBorderColor: "rgba(78, 115, 223, 1)",
          pointHitRadius: 10,
          pointBorderWidth: 2,
          data: values,
        }],
      },
      options: {
        maintainAspectRatio: false,
        layout: {
          padding: {
            left: 10,
            right: 25,
            top: 25,
            bottom: 0
          }
        },
        scales: {
          xAxes: [{
            time: {
              unit: 'date'
            },
            gridLines: {
              display: false,
              drawBorder: false
            },
            ticks: {
              maxTicksLimit: 7
            }
          }],
          yAxes: [{
            ticks: {
              maxTicksLimit: 5,
              padding: 10,
              // Include a dollar sign in the ticks
              callback: function(value, index, values) {
                return '₹' + number_format(value);
              }
            },
            gridLines: {
              color: "rgb(234, 236, 244)",
              zeroLineColor: "rgb(234, 236, 244)",
              drawBorder: false,
              borderDash: [2],
              zeroLineBorderDash: [2]
            }
          }],
        },
        legend: {
          display: false
        },
        tooltips: {
          backgroundColor: "rgb(255,255,255)",
          bodyFontColor: "#858796",
          titleMarginBottom: 10,
          titleFontColor: '#6e707e',
          titleFontSize: 14,
          borderColor: '#dddfeb',
          borderWidth: 1,
          xPadding: 15,
          yPadding: 15,
          displayColors: false,
          intersect: false,
          mode: 'index',
          caretPadding: 10,
          callbacks: {
            label: function(tooltipItem, chart) {
              var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
              return datasetLabel + ': ₹' + number_format(tooltipItem.yLabel);
            }
          }
        }
      }
    });
}

function updatePieChart(labels, values) {
var ctx = document.getElementById("myPieChart");
if(myPieChart) {
    myPieChart.destroy();
}
myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: labels,
    datasets: [{
      data: values,
      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }],
  },
  options: {
    maintainAspectRatio: false,
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
    },
    legend: {
      display: false
    },
    cutoutPercentage: 80,
  },
});
}