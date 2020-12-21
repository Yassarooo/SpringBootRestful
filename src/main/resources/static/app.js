// Creating angular App with module name "NAProject"
angular.module('NAProject', ['ui.router'])


// the following method will run at the time of initializing the module. That
// means it will run only one time.
    .run(function ($http, AuthService, $rootScope, $state) {
        // For implementing the authentication with ui-router we need to listen the
        // state change. For every state change the ui-router module will broadcast
        // the '$stateChangeStart'.
        $rootScope.$on('$stateChangeStart', function (event, toState) {
            // checking the user is logged in or not
            if (!AuthService.user) {
                if (localStorage.getItem("token") && localStorage.getItem("user")) {
                    $http.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem("token");
                    AuthService.user = JSON.parse(localStorage.getItem("user"));
                }
                // To avoiding the infinite looping of state change we have to add a
                // if condition.
                else if (toState.name != 'login' && toState.name != 'register') {
                    event.preventDefault();
                    $state.go('login');
                }
            } else {
                // checking the user is authorized to view the states
                if (toState.data && toState.data.role) {
                    var hasAccess = true;

                    if (!hasAccess) {
                        event.preventDefault();
                        $state.go('access-denied');
                    }

                }
            }
        });
    });