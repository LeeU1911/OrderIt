myApp.controller("myController",['$scope', 'MyService', function($scope, MyService){
    $scope.userWithEmailAndPassword = {};
    $scope.loginUserWithEmailAndPassword = {};

    $scope.login = function(){

    };

    $scope.register = function (isValid) {
        if(isValid && $scope.userWithEmailAndPassword) {
            MyService.register($scope.userWithEmailAndPassword, function (res) {
                console.log(res);
            }, function (err) {
                console.log(err);
            });
        }
    };
}]);