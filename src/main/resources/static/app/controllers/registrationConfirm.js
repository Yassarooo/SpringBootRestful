angular.module('NAProject')
// Creating the Angular Controller
    .controller('registrationConfirmController', ['$scope', '$routeParams', function ($scope, $routeParams) {
        var token = $routeParams.token;
        alert(token);
    }]);
