angular.module('NAProject').config(function ($stateProvider, $urlRouterProvider) {

    // the ui router will redirect if a invalid state has come.
    $urlRouterProvider.otherwise('/');

    // parent view - navigation state
    $stateProvider.state('nav', {
        abstract: true,
        url: '',
        views: {
            'nav@': {
                templateUrl: 'app/views/nav.html',
                controller: 'NavController'
            }
        }
    }).state('login', {
        parent: 'nav',
        url: '/login',
        views: {
            'content@': {
                templateUrl: 'app/views/login.html',
                controller: 'LoginController'
            }
        }
    }).state('users', {
        parent: 'nav',
        url: '/users',
        data: {
            role: 'ADMIN'
        },
        views: {
            'content@': {
                templateUrl: 'app/views/users.html',
                controller: 'UsersController',
            }
        }
    }).state('params', {
        parent: 'nav',
        url: '/params',
        data: {
            role: 'ADMIN'
        },
        views: {
            'content@': {
                templateUrl: 'app/views/params.html',
                controller: 'ParamsController',
            }
        }
    }).state('cars', {
        parent: 'nav',
        url: '/cars',
        views: {
            'content@': {
                templateUrl: 'app/views/cars.html',
                controller: 'CarsController',
            }
        }
    }).state('sellcar', {
        parent: 'nav',
        url: '/sellcar',
        views: {
            'content@': {
                templateUrl: 'app/views/sellcar.html',
                controller: 'SellCarController',
            }
        }
    }).state('MQ', {
        parent: 'nav',
        url: '/MQ',
        views: {
            'content@': {
                templateUrl: 'app/views/MQ.html',
                controller: 'MQController',
            }
        }
    }).state('about', {
        parent: 'nav',
        url: '/about',
        views: {
            'content@': {
                templateUrl: 'app/views/about.html',
                controller: 'AboutController',
            }
        }
    }).state('home', {
        parent: 'nav',
        url: '/',
        views: {
            'content@': {
                templateUrl: 'app/views/home.html',
                controller: 'HomeController'
            }
        }
    }).state('page-not-found', {
        parent: 'nav',
        url: '/page-not-found',
        views: {
            'content@': {
                templateUrl: 'app/views/page-not-found.html',
                controller: 'PageNotFoundController'
            }
        }
    }).state('access-denied', {
        parent: 'nav',
        url: '/access-denied',
        views: {
            'content@': {
                templateUrl: 'app/views/access-denied.html',
                controller: 'AccessDeniedController'
            }
        }
    }).state('register', {
        parent: 'nav',
        url: '/register',
        views: {
            'content@': {
                templateUrl: 'app/views/register.html',
                controller: 'RegisterController'
            }
        }
    }).state('registrationConfirm', {
        parent: 'nav',
        url: '/registrationConfirm',
        views: {
            'content@': {
                templateUrl: 'app/views/registrationConfirm.html',
                controller: 'registrationConfirmController'
            }
        }
    });
});
