angular.module('NAProject')
// Creating the Angular Controller
    .controller('AboutController', function ($http, $scope, AuthService) {
        this.addchecked = false;
        this.destroychecked = false;

        $scope.sessionid = '';
        $scope.messageadd = '';
        $scope.messageget = '';
        $scope.messagedestroy = '';

        var getsessionid = function () {
            $scope.sessionid = '';
            $http.get('/api/sessionid').success(function (res) {
                $scope.sessions = res;
            }).error(function (error) {
                $scope.sessionid = error.message;
            });
        };
        $scope.addmessage = function () {
            $scope.messageadd = '';
            $http({
                url: '/api/addmessage',
                method: "POST",
                params: {
                    msg: $scope.content
                }
            }).success(function () {
                $scope.sessionForm.$setPristine();
                $scope.messageadd = 'Message Added Successfully';
                this.addchecked = true;
                getmessages();
            }).error(function (error) {
                $scope.messageadd = error.message;
            });
        };
        var getmessages = function () {
            $scope.messageget = '';
            $http.get('/api/messages').success(function (res) {
                $scope.msgs = res;
                $scope.sessionForm.$setPristine();
            }).error(function (error) {
                $scope.messageget = error.message;
            });
        };
        $scope.destroysession = function () {
            $scope.messagedestroy = '';
            $http.delete('/api/destroysession').success(function () {
                $scope.messagedestroy = 'Session Destroyed Successfully';
                this.destroychecked = true;
                getmessages();
            }).error(function (error) {
                $scope.messagedestroy = error.message;
            });
        };
        getsessionid();
        getmessages();
    });
