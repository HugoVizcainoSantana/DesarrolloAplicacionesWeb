/*global $, document, LINECHARTEXMPLE*/

$(document).ready(function () {

    'use strict';

    var brandPrimary = 'rgba(51, 179, 90, 1)';

    /*var dataMonths = '{{.}}';

    console.log(dataMonths);

    */

    var LINECHARTEXMPLE11 = $('#lineChartExample11'),
        LINECHARTEXMPLE12 = $('#lineChartExample12'),
        LINECHARTEXMPLE13 = $('#lineChartExample13'),
        LINECHARTEXMPLE2 = $('#lineChartExample2'),
        PIECHARTEXMPLE = $('#pieChartExample'),
        BARCHARTEXMPLE = $('#barChartExample'),
        RADARCHARTEXMPLE = $('#radarChartExample'),
        POLARCHARTEXMPLE = $('#polarChartExample');

    $('.loadHomeChart').each(function () {
        var homeCanvas = $(this);
        console.log(homeCanvas);
        var homeId = homeCanvas.attr("itemid");
        console.log("Requesting data for house with id:" + homeId);
        $.get("/dashboard/analytics/" + homeId,
            //Callback
            function buildChart(chartData) {
                console.log("Chart data:");
                console.log(chartData);
                console.log("Home Canvas:");
                console.log(homeCanvas);
                if (Object.keys(chartData).length <= 0) {
                    console.log("No devices for this home");
                } else {
                    var domain = [];
                    var values = [];
                    Object.keys(chartData).forEach(function (key) {
                        domain.push(key);
                        values.push(chartData[key]);
                        // use val
                    });


                    var homeChart = new Chart(homeCanvas, {
                        type: 'line',
                        data: {
                            labels: domain,
                            datasets: [
                                {
                                    label: "Consumo Casa",
                                    fill: true,
                                    lineTension: 0.3,
                                    backgroundColor: "rgba(51, 179, 90, 0.38)",
                                    borderColor: brandPrimary,
                                    borderCapStyle: 'butt',
                                    borderDash: [],
                                    borderDashOffset: 0.0,
                                    borderJoinStyle: 'miter',
                                    borderWidth: 1,
                                    pointBorderColor: brandPrimary,
                                    pointBackgroundColor: "#fff",
                                    pointBorderWidth: 1,
                                    pointHoverRadius: 5,
                                    pointHoverBackgroundColor: brandPrimary,
                                    pointHoverBorderColor: "rgba(220,220,220,1)",
                                    pointHoverBorderWidth: 2,
                                    pointRadius: 1,
                                    pointHitRadius: 10,
                                    data: values,
                                    spanGaps: false
                                }
                            ]
                        }
                    });
                }
            });
    });


    /*
        var lineChartExample = new Chart(LINECHARTEXMPLE11, {
            type: 'line',
            data: {
                labels: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"],
                datasets: [
                    {
                        label: "Consumo Casa",
                        fill: true,
                        lineTension: 0.3,
                        backgroundColor: "rgba(51, 179, 90, 0.38)",
                        borderColor: brandPrimary,
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        borderWidth: 1,
                        pointBorderColor: brandPrimary,
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: brandPrimary,
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [50, 20, 40, 31, 32, 22],
                        spanGaps: false
                    },
                    {
                        label: "Consumo Media de los usuarios",
                        fill: true,
                        lineTension: 0.3,
                        backgroundColor: "rgba(75,192,192,0.4)",
                        borderColor: "rgba(75,192,192,1)",
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        borderWidth: 1,
                        pointBorderColor: "rgba(75,192,192,1)",
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(75,192,192,1)",
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [65, 59, 30, 81, 56, 55],
                        spanGaps: false
                    }
                ]
            }
        });

        var lineChartExample = new Chart(LINECHARTEXMPLE12, {
            type: 'line',
            data: {
                labels: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"],
                datasets: [
                    {
                        label: "Consumo Casa",
                        fill: true,
                        lineTension: 0.3,
                        backgroundColor: "rgba(51, 179, 90, 0.38)",
                        borderColor: brandPrimary,
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        borderWidth: 1,
                        pointBorderColor: brandPrimary,
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: brandPrimary,
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [10, 25, 15, 33, 56, 45],
                        spanGaps: false
                    },
                    {
                        label: "Consumo Media de los usuarios",
                        fill: true,
                        lineTension: 0.3,
                        backgroundColor: "rgba(75,192,192,0.4)",
                        borderColor: "rgba(75,192,192,1)",
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        borderWidth: 1,
                        pointBorderColor: "rgba(75,192,192,1)",
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(75,192,192,1)",
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [65, 59, 30, 81, 56, 55],
                        spanGaps: false
                    }
                ]
            }
        });

        var lineChartExample = new Chart(LINECHARTEXMPLE13, {
            type: 'line',
            data: {
                labels: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"],
                datasets: [
                    {
                        label: "Consumo Casa",
                        fill: true,
                        lineTension: 0.3,
                        backgroundColor: "rgba(51, 179, 90, 0.38)",
                        borderColor: brandPrimary,
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        borderWidth: 1,
                        pointBorderColor: brandPrimary,
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: brandPrimary,
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [31, 32, 15, 33, 26, 45],
                        spanGaps: false
                    },
                    {
                        label: "Consumo Media de los usuarios",
                        fill: true,
                        lineTension: 0.3,
                        backgroundColor: "rgba(75,192,192,0.4)",
                        borderColor: "rgba(75,192,192,1)",
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        borderWidth: 1,
                        pointBorderColor: "rgba(75,192,192,1)",
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(75,192,192,1)",
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [65, 59, 30, 81, 56, 55],
                        spanGaps: false
                    }
                ]
            }
        });

        var lineChartExample = new Chart(LINECHARTEXMPLE2, {
            type: 'line',
            data: {
                // Mustache?
                labels: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"],
                datasets: [
                    {
                        label: "Consumo Casa",
                        fill: true,
                        lineTension: 0.3,
                        backgroundColor: "rgba(51, 179, 90, 0.38)",
                        borderColor: brandPrimary,
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        borderWidth: 1,
                        pointBorderColor: brandPrimary,
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: brandPrimary,
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [50, 20, 40, 31, 32, 22, 10, 25, 15, 33, 56, 45],
                        spanGaps: false
                    },
                    {
                        label: "Consumo Media de los usuarios",
                        fill: true,
                        lineTension: 0.3,
                        backgroundColor: "rgba(75,192,192,0.4)",
                        borderColor: "rgba(75,192,192,1)",
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        borderWidth: 1,
                        pointBorderColor: "rgba(75,192,192,1)",
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(75,192,192,1)",
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [65, 59, 30, 81, 56, 55, 40, 20, 80, 67, 44, 50],
                        spanGaps: false
                    }
                ]
            }
        });

        var pieChartExample = new Chart(PIECHARTEXMPLE, {
            type: 'doughnut',
            data: {
                labels: [
                    "First",
                    "Second",
                    "Third"
                ],
                datasets: [
                    {
                        data: [300, 50, 100],
                        borderWidth: [1, 1, 1],
                        backgroundColor: [
                            brandPrimary,
                            "rgba(75,192,192,1)",
                            "#FFCE56"
                        ],
                        hoverBackgroundColor: [
                            brandPrimary,
                            "rgba(75,192,192,1)",
                            "#FFCE56"
                        ]
                    }]
            }
        });

        var pieChartExample = {
            responsive: true
        };

        var barChartExample = new Chart(BARCHARTEXMPLE, {
            type: 'bar',
            data: {
                labels: ["January", "February", "March", "April", "May", "June", "July"],
                datasets: [
                    {
                        label: "Data Set 1",
                        backgroundColor: [
                            'rgba(51, 179, 90, 0.6)',
                            'rgba(51, 179, 90, 0.6)',
                            'rgba(51, 179, 90, 0.6)',
                            'rgba(51, 179, 90, 0.6)',
                            'rgba(51, 179, 90, 0.6)',
                            'rgba(51, 179, 90, 0.6)',
                            'rgba(51, 179, 90, 0.6)'
                        ],
                        borderColor: [
                            'rgba(51, 179, 90, 1)',
                            'rgba(51, 179, 90, 1)',
                            'rgba(51, 179, 90, 1)',
                            'rgba(51, 179, 90, 1)',
                            'rgba(51, 179, 90, 1)',
                            'rgba(51, 179, 90, 1)',
                            'rgba(51, 179, 90, 1)'
                        ],
                        borderWidth: 1,
                        data: [65, 59, 80, 81, 56, 55, 40],
                    },
                    {
                        label: "Data Set 2",
                        backgroundColor: [
                            'rgba(203, 203, 203, 0.6)',
                            'rgba(203, 203, 203, 0.6)',
                            'rgba(203, 203, 203, 0.6)',
                            'rgba(203, 203, 203, 0.6)',
                            'rgba(203, 203, 203, 0.6)',
                            'rgba(203, 203, 203, 0.6)',
                            'rgba(203, 203, 203, 0.6)'
                        ],
                        borderColor: [
                            'rgba(203, 203, 203, 1)',
                            'rgba(203, 203, 203, 1)',
                            'rgba(203, 203, 203, 1)',
                            'rgba(203, 203, 203, 1)',
                            'rgba(203, 203, 203, 1)',
                            'rgba(203, 203, 203, 1)',
                            'rgba(203, 203, 203, 1)'
                        ],
                        borderWidth: 1,
                        data: [35, 40, 60, 47, 88, 27, 30],
                    }
                ]
            }
        });


        var polarChartExample = new Chart(POLARCHARTEXMPLE, {
            type: 'polarArea',
            data: {
                datasets: [{
                    data: [
                        11,
                        16,
                        7
                    ],
                    backgroundColor: [
                        "rgba(51, 179, 90, 1)",
                        "#FF6384",
                        "#FFCE56"
                    ],
                    label: 'My dataset' // for legend
                }],
                labels: [
                    "First",
                    "Second",
                    "Third"
                ]
            }
        });

        var polarChartExample = {
            responsive: true
        };


        var radarChartExample = new Chart(RADARCHARTEXMPLE, {
            type: 'radar',
            data: {
                labels: ["Eating", "Drinking", "Sleeping", "Designing", "Coding", "Cycling"],
                datasets: [
                    {
                        label: "My First dataset",
                        backgroundColor: "rgba(179,181,198,0.2)",
                        borderWidth: 2,
                        borderColor: "rgba(179,181,198,1)",
                        pointBackgroundColor: "rgba(179,181,198,1)",
                        pointBorderColor: "#fff",
                        pointHoverBackgroundColor: "#fff",
                        pointHoverBorderColor: "rgba(179,181,198,1)",
                        data: [65, 59, 90, 81, 56, 55]
                    },
                    {
                        label: "My Second dataset",
                        backgroundColor: "rgba(51, 179, 90, 0.2)",
                        borderWidth: 2,
                        borderColor: "rgba(51, 179, 90, 1)",
                        pointBackgroundColor: "rgba(51, 179, 90, 1)",
                        pointBorderColor: "#fff",
                        pointHoverBackgroundColor: "#fff",
                        pointHoverBorderColor: "rgba(51, 179, 90, 1)",
                        data: [28, 48, 40, 19, 96, 27]
                    }
                ]
            }
        });
        var radarChartExample = {
            responsive: true
        };

    */
});