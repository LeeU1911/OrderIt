var myApp = angular.module("myApp",['ngResource', 'ngMessages', 'ui.router']).config(["$stateProvider", "$locationProvider",
    function ($stateProvider) {
        var homeState = {
            name: 'home',
            url: '/home',
            templateUrl: "/javascripts/angular-views/home.html"
        };

        $stateProvider.state(homeState);
    }]);