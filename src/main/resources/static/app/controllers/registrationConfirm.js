angular.module('NAProject')
// Creating the Angular Controller
    .controller('registrationConfirmController', ['$scope', '$state', function ($scope, $state) {
        alert($state.params.token);
    }]);
