(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('RestWorkDaysDetailController', RestWorkDaysDetailController);

    RestWorkDaysDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RestWorkDays', 'Employee', 'DayOff'];

    function RestWorkDaysDetailController($scope, $rootScope, $stateParams, entity, RestWorkDays, Employee, DayOff) {
        var vm = this;
        vm.restWorkDays = entity;
        
        var unsubscribe = $rootScope.$on('rxmenApp:restWorkDaysUpdate', function(event, result) {
            vm.restWorkDays = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
