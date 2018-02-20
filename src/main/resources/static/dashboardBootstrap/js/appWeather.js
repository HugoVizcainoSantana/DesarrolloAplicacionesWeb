var latitude, longitude;
var unidades = 'metric';
var idioma = 'es';
var url = 'http://api.openweathermap.org/data/2.5/weather';
var key = '07e8c74adbda130b2740a53328140fb3';

$(document).ready(function () {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            latitude = (position.coords.latitude);
            longitude = (position.coords.longitude);

            var d = new Date();

            var weekday = ["Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"];
            var month = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Nomvienbre", "Diciembre"];

            $('#day').text(weekday[d.getDay()]);
            $('#date').text(d.getDate() + ' de ' + month[d.getMonth()] + ', ' + d.getFullYear());
            $('#time').html('Hora actual: ' + (d.getHours() > 12 ? (d.getHours() - 12) : d.getHours()).toString() + ":" + ((d.getMinutes() < 10 ? '0' : '').toString() + d.getMinutes().toString()) + (d.getHours() < 12 ? ' AM' : ' PM').toString());

            $.getJSON(url, {
                lat: latitude,
                lon: longitude,
                units: unidades,
                lang: idioma,
                APPID: key
            }).done(function (weather) {
                $('.city').html(weather.name);
                $('#temperature').html(Math.round(weather.main.temp) + 'ºC');
                $('#windSpeed').html(weather.wind.speed + ' meter/sec');
                $('#humidity').html(weather.main.humidity + ' %');
                $('#pressure').html(Math.round(weather.main.pressure / 1.33317) + ' mm Hg');

                $('#weather-status').html(weather.weather[0].main + '/' + weather.weather[0].description);
                $('#tempmin').html(weather.main.temp_min + 'ºC Min');
                $('#tempmax').html(weather.main.temp_max + 'ºC Max');

                var icon = '';
                var background = '';
                if (weather.weather[0].main == 'Thunderstorm') {
                    icon = '/dashboardBootstrap/svgIcon/wi-thunderstorm.svg';
                    background = '/dashboardBootstrap/backgrounds/thunderstorm.jpg';
                } else if (weather.weather[0].main == 'Drizzle') {
                		icon = '/dashboardBootstrap/svgIcon/wi-sleet.svg';
                     background = '/dashboardBootstrap/backgrounds/rain.jpg';
                } else if (weather.weather[0].main == 'Rain') {
                		icon = '/dashboardBootstrap/svgIcon/wi-showers.svg';
                     background = '/dashboardBootstrap/backgrounds/rain.jpg';
                } else if (weather.weather[0].main == 'Snow') {
                		icon = '/dashboardBootstrap/svgIcon/wi-snow-wind.svg';
                     background = '/dashboardBootstrap/backgrounds/snow.jpg';
                } else if (weather.weather[0].main == 'Atmosphere') {
                		icon = '/dashboardBootstrap/svgIcon/wi-day-fog.svg';
                     background = '/dashboardBootstrap/backgrounds/mist.jpg';
                } else if (weather.weather[0].main == 'Clear') {
	                	icon = '/dashboardBootstrap/svgIcon/wi-day-sunny.svg';
	                 background = '/dashboardBootstrap/backgrounds/sun.jpg';
                } else if (weather.weather[0].main == 'Clouds') {
                	icon = '/dashboardBootstrap/svgIcon/wi-cloudy.svg';
                    background = '/dashboardBootstrap/backgrounds/clouds.jpg';
                } else if (weather.weather[0].main == 'Extreme') {
                		icon = '/dashboardBootstrap/svgIcon/wi-night-cloudy-gusts.svg';
                    background = '/dashboardBootstrap/backgrounds/storm.jpg';
                }
                $('.weather-icon').attr('src', icon);

                $('#weather').css({'background-image': 'url(' + background + ')'});

                var sunriseDate = new Date(weather.sys.sunrise * 1000);
                $('#sunriseTime').html((sunriseDate.getHours() > 12 ? (sunriseDate.getHours() - 12) : sunriseDate.getHours()).toString() + ":" + ((sunriseDate.getMinutes() < 10 ? '0' : '').toString() + sunriseDate.getMinutes().toString()) + (sunriseDate.getHours() < 12 ? ' AM' : ' PM').toString());

                var sunsetDate = new Date(weather.sys.sunset * 1000);
                $('#sunsetTime').html((sunsetDate.getHours() > 12 ? (sunsetDate.getHours() - 12) : sunsetDate.getHours()).toString() + ":" + ((sunsetDate.getMinutes() < 10 ? '0' : '').toString() + sunsetDate.getMinutes().toString()) + (sunsetDate.getHours() < 12 ? ' AM' : ' PM').toString());
            })
        });


    }
    var html = '<div  class="card">' +
			        '<div  class="container">' +
				        '<div  class="row">' +
					        '<div id="updates-header" class="card-header d-flex justify-content-between align-items-center">' +
					        '<h3 class="city" id="city"></h3>' +
				        '</div>' +
			        '</div>' +
			        '<div class="row">' +
				        '<div  class="col">' +
						        '<h3 id="day"></h3>' +
						        '<h3 id="date"></h3>' +
						        '<h3 id="time"></h3>' +
						  '</div>' +
					        '<div class="col">' +
						        '<div class="col">' +
						        '<h3 id="weather-status"></h3>' +
						        '<h1 id="temperature"></h1>' +
						        '</div>' +
						        '<div class="col">' +
						        '<h5 id="tempmin"></h5>' +
						        '<h5 id="tempmax"></h5>' +
					        '</div>' +
						        '<div class="top-right">' +
							        '<h1 id="weather-status"></h1>' +
							        '<img class="weather-icon" src="" style="height: 100px;">' +
						        '</div>' +
					        '<div class="bottom-left">' +
					        '<h1 id="temperature"></h1>' +
					        '</div>' +
					        '</div>' +
					       
			        '</div>' +
		        '</div>';

    $('#weather').append(html);
});