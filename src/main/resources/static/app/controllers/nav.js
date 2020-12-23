angular.module('NAProject')
// Creating the Angular Controller
    .controller('NavController', function ($http, $scope, AuthService, $state, $rootScope,$timeout) {
        $scope.user = AuthService.user;

        $scope.$on('LoginSuccessful', function () {
            $scope.user = AuthService.user;
        });
        $scope.$on('LogoutSuccessful', function () {
            $scope.user = null;
        });
        $scope.$on('showload', function () {
            $scope.mainload = true;
            $timeout(function () {
                $scope.mainload = false;
            }, 3000);

        });

        $scope.$on('hideload', function () {
            $scope.mainload = false;
        });
        $scope.logout = function () {
            localStorage.clear();
            AuthService.user = null;
            $rootScope.$broadcast('LogoutSuccessful');
            $state.go('login');
        };
        $scope.showload = function () {
            $rootScope.$broadcast('showload');
        };

    });
