angular.module('NAProject')
// Creating the Angular Controller
    .controller('registrationConfirmController', ['$http', '$scope', '$state', function ($http, $scope, $state) {
        var token = $state.params.token;
        var email = $state.params.email;
        $scope.confirmload = true;
        //alert($state.params.token);
        $http({
            url: 'registrationConfirm',
            method: "POST",
            params: {
                token: token,
                email: email
            }
        }).success(function () {
            $scope.message = "Registration successful ! ";
            $scope.confirmload = false;
        }).error(function (error) {
            $scope.message = error.message;
            $scope.confirmload = false;
        });
    }]);
