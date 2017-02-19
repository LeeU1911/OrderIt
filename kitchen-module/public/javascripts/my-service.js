myApp.factory('MyService', function ($resource) {
    return $resource('/register', {
    }, {
        login: {
            url: '/login',
            method: 'GET'
        },
        register: {
            url: '/register',
            method: 'POST'
        }
    })
});