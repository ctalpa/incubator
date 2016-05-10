(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('DayOffDetailController', DayOffDetailController);

    DayOffDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DayOff', 'RestWorkDays'];

    function DayOffDetailController($scope, $rootScope, $stateParams, entity, DayOff, RestWorkDays) {
        var vm = this;
        vm.dayOff = entity;
        
        var unsubscribe = $rootScope.$on('rxmenApp:dayOffUpdate', function(event, result) {
            vm.dayOff = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
