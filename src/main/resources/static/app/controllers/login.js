angular.module('NAProject')
// Creating the Angular Controller
    .controller('LoginController', function ($http, $scope, $state, AuthService, $rootScope) {
        $rootScope.$broadcast('hideload');
        // method for login
        $scope.login = function () {
            $scope.loginload = true;
            // requesting the token by usename and passoword
            $http({
                url: 'authenticate',
                method: "POST",
                params: {
                    username: $scope.username,
                    password: $scope.password
                }
            }).success(function (res) {
                $scope.password = null;
                // checking if the token is available in the response
                if (res.token) {
                    $scope.message = '';
                    // setting the Authorization Bearer token with JWT token
                    $http.defaults.headers.common['Authorization'] = 'Bearer ' + res.token;

                    // setting the user in AuthService
                    AuthService.user = res.user;

                    //store token
                    //sessionStorage.token = res.token;
                    //sessionStorage.user = JSON.stringify(res.user);

                    localStorage.setItem("token", res.token);
                    localStorage.setItem("user", JSON.stringify(res.user));

                    $rootScope.$broadcast('LoginSuccessful');
                    $scope.loginload = false;

                    $rootScope.$broadcast('showload');

                    // going to the home page
                    $state.go('home');
                } else {
                    $scope.loginload = false;
                    // if the token is not present in the response then the
                    // authentication was not successful. Setting the error message.
                    $scope.message = 'Authetication Failed !';
                }
            }).error(function (error) {
                $scope.loginload = false;
                // if authentication was not successful. Setting the error message.
                $scope.message = 'Authetication Failed !';
            });
        };
    });
