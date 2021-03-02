angular.module('NAProject')
// Creating the Angular Controller
    .controller('registrationConfirmController', function ($http, $scope) {
        $scope.message = "Registration Confirmed successfully ! ";

        var parseQueryString = function () {

            var str = window.location.search;
            var objURL = {};

            str.replace(
                new RegExp("([^?=&]+)(=([^&]*))?", "g"),
                function ($0, $1, $2, $3) {
                    objURL[$1] = $3;
                }
            );
            return objURL;
        };

//Example how to use it:
        var params = parseQueryString();
        alert(params["token"]);

    });
