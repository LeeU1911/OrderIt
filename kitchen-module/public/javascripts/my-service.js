myApp.factory('MyService', function ($resource) {
    return $resource('/register', {
    }, {
        login: {
            url: '/login',
            method: 'POST'
        },
        register: {
            url: '/register',
            method: 'POST'
        }
    })
});