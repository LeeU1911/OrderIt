myApp.controller("myController",['$scope', 'MyService', '$location', function($scope, MyService, $location){
    $scope.userWithEmailAndPassword = {};
    $scope.loginUserWithEmailAndPassword = {};

    $scope.login = function(isValid){
        if(isValid && $scope.loginUserWithEmailAndPassword) {
            $scope.authenticating = true;
            MyService.login($scope.loginUserWithEmailAndPassword, function (res) {
                $scope.authenticating = false;
                if(res.success) {
                    $scope.loginFormVisible = false;
                    $location.path('/home');
                }
            }, function (err) {
                $scope.authenticating = false;
                console.log(err);
            });
        }
    };

    $scope.register = function (isValid) {
        if(isValid && $scope.userWithEmailAndPassword) {
            MyService.register($scope.userWithEmailAndPassword, function (res) {
                if(res.success) {
                    $scope.loginFormVisible = false;
                    $location.path('/home');
                }
            }, function (err) {
                console.log(err);
            });
        }
    };
}]);