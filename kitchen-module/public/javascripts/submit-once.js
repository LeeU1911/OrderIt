angular.module("myApp").directive('submitOnce', function ($timeout) {
    return {
        restrict: 'A',
        require: '^form',
        link: function (scope, element, attrs, form) {
            var replacementText = attrs.submitOnce;
            element.bind('click', function () {
                if (form.$valid) {
                    $timeout(function () {
                        if (replacementText) {
                            element.val(replacementText);
                        }
                        element.attr('disabled', true);
                    }, 0);
                }
            });
        }
    };
});