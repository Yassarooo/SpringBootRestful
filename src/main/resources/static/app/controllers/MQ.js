angular.module('NAProject')
// Creating the Angular Controller
    .controller('MQController', function ($http, $scope, $rootScope) {
        $rootScope.$broadcast('hideload');
        this.checked = false;
        $scope.buttonText = 'Send';
        $scope.submit = function () {
            $scope.message = '';
            $http({
                url: '/api/MQ',
                method: "GET",
                params: {
                    email: $scope.email,
                    content: $scope.content,
                    date: $scope.date
                }
            }).success(function () {
                $scope.MQForm.$setPristine();
                $scope.message = 'Message Sent Successfully';
                this.checked = true;
            }).error(function (error) {
                $scope.message = error.message;
            });
        };

    });
