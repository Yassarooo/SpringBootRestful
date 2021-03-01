angular.module('NAProject')
    .controller('registrationConfirmController', ['$scope', '$routeParams', function ($http, $scope, $routeParams, $rootScope) {
        $rootScope.$broadcast('hideload');
        $scope.message2 = "Please Wait ...";

        var token = $routeParams.token;

        $http({
            url: 'registrationConfirm',
            method: "GET",
            params: {
                token: token
            }
        }).success(function (res) {
            $scope.register.$setPristine();
            $scope.message = "Registration successful ! " + $routeParams.token;
        }).error(function (error) {
            $scope.message = error.message;
        });
    }]);
