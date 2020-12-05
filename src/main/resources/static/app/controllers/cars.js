angular.module('NAProject')
// Creating the Angular Controller
    .controller('CarsController', function ($http, $scope, AuthService) {
        var edit = false;
        $scope.buttonText = 'Create';
        var init = function () {
            $http.get('api/cars').success(function (res) {
                $scope.cars = res;

                $scope.carForm.$setPristine();
                $scope.message = '';
                $scope.car = null;
                $scope.buttonText = 'Create';

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
            $http.delete('api/cars/' + car.id).success(function (res) {
                $scope.deleteMessage = "Success!";
                init();
            }).error(function (error) {
                $scope.deleteMessage = error.message;
            });
        };
        var editCar = function () {
            $http.put('api/cars', $scope.car).success(function (res) {
                $scope.car = null;
                $scope.confirmPassword = null;
                $scope.carForm.$setPristine();
                $scope.message = "Editting Success";
                init();
            }).error(function (error) {
                $scope.message = error.message;
            });
        };
        var addCar = function () {
            $http.post('api/cars', $scope.car).success(function (res) {
                $scope.car = null;
                $scope.confirmPassword = null;
                $scope.carForm.$setPristine();
                $scope.message = "Car Created";
                init();
            }).error(function (error) {
                $scope.message = error.message;
            });
        };
        $scope.submit = function () {
            if (edit) {
                editCar();
            } else {
                addCar();
            }
        };
        init();

    });
