angular.module('NAProject')
// Creating the Angular Controller
    .controller('CarsController', function ($http, $scope, $rootScope) {
        $rootScope.$broadcast('hideload');
        var edit = false;
        $scope.buttonText = 'Create';
        $scope.message = '';

        var init = function () {
            edit = false;
            $scope.carsload = true;
            $http.get('api/cars').success(function (res) {
                $scope.cars = res;
                $scope.carForm.$setPristine();
                $scope.car = null;
                $scope.buttonText = 'Create';
                $scope.carsload = false;

            }).error(function (error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function (car) {
            edit = true;
            $scope.car = car;
            $scope.message = '';
            $scope.buttonText = 'Update';
        };
        $scope.initAddCar = function () {
            edit = false;
            $scope.car = null;
            $scope.carForm.$setPristine();
            $scope.message = '';
            $scope.buttonText = 'Create';
        };
        $scope.deleteCar = function (car) {
            $scope.carsload = true;
            $http.delete('api/cars/' + car.id).success(function () {
                init();
                $scope.deleteMessage = "Deleted!";
            }).error(function (error) {
                $scope.deleteMessage = error.message;
            });
        };
        var editCar = function () {
            $http.put('api/cars', $scope.car).success(function () {
                $scope.car = null;
                $scope.confirmPassword = null;
                $scope.carForm.$setPristine();
                $scope.carsubload = false;
                $scope.message = "Editting Success";
                init();
            }).error(function (error) {
                $scope.carsubload = false;
                $scope.message = error.message;
            });
        };
        var addCar = function () {
            $http.post('api/cars', $scope.car).success(function () {
                $scope.car = null;
                $scope.confirmPassword = null;
                $scope.carForm.$setPristine();
                init();
                $scope.carsubload = false;
                $scope.message = "Car Created";
            }).error(function (error) {
                $scope.carsubload = false;
                $scope.message = error.message;
            });
        };
        $scope.submit = function () {
            $scope.carsubload = true;
            if (edit) {
                editCar();
            } else {
                addCar();
            }
        };
        init();
    });
