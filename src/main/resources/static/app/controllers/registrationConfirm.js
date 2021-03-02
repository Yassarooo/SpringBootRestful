angular.module('NAProject')
// Creating the Angular Controller
    .controller('registrationConfirmController', ['$scope', '$state', function ($http,$scope, $state) {
        //alert($state.params.token);
        $http({
            url: 'registrationConfirm',
            method: "GET",
            params: {
                token: $state.params.token
            }
        }).success(function () {
            $scope.register.$setPristine();
            $scope.message = "Registration successful ! ";
        }).error(function (error) {
            $scope.message = error.message;
        });
    }]);
