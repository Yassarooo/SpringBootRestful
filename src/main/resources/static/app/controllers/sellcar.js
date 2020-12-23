angular.module('NAProject')
// Creating the Angular Controller
    .controller('SellCarController', function ($http, $scope, $rootScope) {
        $rootScope.$broadcast('hideload');
        var edit = false;
        $scope.buttonText = 'Sell';
        $scope.sellload = true;
        var init = function () {
            $http.get('api/sellcar').success(function (res) {
                $scope.sellcar = res;
                $scope.sellcarForm.$setPristine();
                $scope.message = 'Please Select Car to Sell';
                $scope.car = null;
                $scope.buttonText = 'Sell';
                $scope.sellload = false;

            }).error(function (error) {
                $scope.message = error.message;
                $scope.sellload = false;
            });
        };
        $scope.initSellCar = function (car) {
            edit = true;
            $scope.car = car;
            $scope.message = 'Enter Sell Info';
            $scope.carname = car.name;
            $scope.buttonText = 'Sell';
        };
        var SellCar = function () {
            $http.put('api/sellcar', $scope.car).success(function (res) {
                $scope.car = null;
                $scope.sellcarForm.$setPristine();
                init();
                $scope.sellsubload = false;
                $scope.message = "Sell Success";
            }).error(function (error) {
                $scope.sellsubload = false;
                $scope.message = error.message;
            });
        };
        $scope.submit = function () {
            $scope.sellsubload = true;
            if (edit) {
                SellCar();
            } else {
                if($scope.car == null){
                    $scope.message = 'Please Select Car First';
                }
            }
        };
        init();

    });
