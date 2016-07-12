(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('RestWorkDaysDetailController', RestWorkDaysDetailController);

    RestWorkDaysDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RestWorkDays', 'Employee', 'DayOff'];

    function RestWorkDaysDetailController($scope, $rootScope, $stateParams, entity, RestWorkDays, Employee, DayOff) {
        var vm = this;

        vm.restWorkDays = entity;

        var unsubscribe = $rootScope.$on('prxmenApp:restWorkDaysUpdate', function(event, result) {
            vm.restWorkDays = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
