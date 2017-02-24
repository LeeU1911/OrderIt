var myApp = angular.module("myApp",['ngResource', 'ngMessages', 'ui.router']).config(["$stateProvider", "$locationProvider",
    function ($stateProvider) {
        var homeState = {
            name: 'home',
            url: '/home',
            templateUrl: "/javascripts/angular-views/home.html"
        };

        var loginState = {
            name: 'login',
            url: '/login',
            templateUrl: "/javascripts/angular-views/login.html"
        };

        $stateProvider.state(loginState).state(homeState);
    }]);