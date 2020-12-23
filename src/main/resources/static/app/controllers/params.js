angular.module('NAProject')
// Creating the Angular Controller
    .controller('ParamsController', function ($http, $scope, $rootScope) {
        $rootScope.$broadcast('hideload');
        var edit = false;
        $scope.buttonText = 'Create';
        var init = function () {
            $scope.paramsload = true;
            $http.get('api/params').success(function (res) {
                $scope.params = res;

                $scope.paramsForm.$setPristine();
                $scope.message = '';
                $scope.param = null;
                $scope.buttonText = 'Create';
                $scope.paramsload = false;

            }).error(function (error) {
                $scope.message = error.message;
                $scope.paramsload = false;
            });
        };
        $scope.initEdit = function (param) {
            edit = true;
            $scope.param = param;
            $scope.message = '';
            $scope.buttonText = 'Update';
        };
        $scope.initAddParam = function () {
            edit = false;
            $scope.param = null;
            $scope.paramsForm.$setPristine();
            $scope.message = '';
            $scope.buttonText = 'Create';
        };
        $scope.deleteParam = function (param) {
            $scope.paramsload = true;
            $http.delete('api/params/' + param.id).success(function () {
                $scope.paramsubload = false;
                $scope.deleteMessage = "Success!";
                init();
            }).error(function (error) {
                $scope.paramsubload = false;
                $scope.deleteMessage = error.message;
            });
        };
        var editParam = function () {
            $http.put('api/params', $scope.param).success(function () {
                $scope.param = null;
                $scope.paramsForm.$setPristine();
                $scope.paramsubload = false;
                $scope.message = "Editting Success";
                init();
            }).error(function (error) {
                $scope.paramsubload = false;
                $scope.message = error.message;
            });
        };
        var addParam = function () {
            $http.post('api/params', $scope.param).success(function (res) {
                $scope.param = null;
                $scope.paramsForm.$setPristine();
                $scope.paramsubload = false;
                $scope.message = "Parameters Entity Created";
                init();
            }).error(function (error) {
                $scope.paramsubload = false;
                $scope.message = error.message;
            });
        };
        $scope.submit = function () {
            $scope.paramsubload = true;
            if (edit) {
                editParam();
            } else {
                addParam();
            }
        };
        init();

    });
