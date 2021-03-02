angular.module('NAProject')
// Creating the Angular Controller
    .controller('registrationConfirmController', function ($http, $scope) {
        $scope.message = "Registration Confirmed successfully ! ";

        var queryString = window.location.search;
        var urlParams = new URLSearchParams(queryString);
        var token = urlParams.get('token')

        alert(token);

    });
