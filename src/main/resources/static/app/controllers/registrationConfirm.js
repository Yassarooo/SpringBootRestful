angular.module('NAProject')
// Creating the Angular Controller
    .controller('registrationConfirmController', ['$http','$scope', '$state', function ($http,$scope, $state) {
        var token  = $state.params.token;
        alert($state.params.token);
        $http({
            url: 'registrationConfirm',
            method: "GET",
            params: {
                token: token
            }
        }).success(function () {
            $scope.register.$setPristine();
            $scope.message = "Registration successful ! ";
        }).error(function (error) {
            $scope.message = error.message;
        });
    }]);
