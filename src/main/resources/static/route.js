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
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
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
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
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
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
            }
        }
    }).state('cars', {
        parent: 'nav',
        url: '/cars',
        views: {
            'content@': {
                templateUrl: 'app/views/cars.html',
                controller: 'CarsController',
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
            }
        }
    }).state('sellcar', {
        parent: 'nav',
        url: '/sellcar',
        views: {
            'content@': {
                templateUrl: 'app/views/sellcar.html',
                controller: 'SellCarController',
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
            }
        }
    }).state('MQ', {
        parent: 'nav',
        url: '/MQ',
        views: {
            'content@': {
                templateUrl: 'app/views/MQ.html',
                controller: 'MQController',
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
            }
        }
    }).state('about', {
        parent: 'nav',
        url: '/about',
        views: {
            'content@': {
                templateUrl: 'app/views/about.html',
                controller: 'AboutController',
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
            }
        }
    }).state('home', {
        parent: 'nav',
        url: '/',
        views: {
            'content@': {
                templateUrl: 'app/views/home.html',
                controller: 'HomeController'
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
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
            },
            'layout@footer' : {
                templateUrl:"app/views/footer.html"
            }
        }
    });
});
