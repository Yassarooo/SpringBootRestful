angular.module('NAProject')
    .controller('registrationConfirmController', function ($http, $scope, $rootScope) {

        $scope.message = "Please Wait ... ";

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

        $http({
            url: 'registrationConfirm',
            method: "GET",
            params: {
                token: params["token"]
            }
        }).success(function (res) {
            $scope.message = "Registration successful ! " + params["token"];
        }).error(function (error) {
            $scope.message = error.message;
        });
    });
