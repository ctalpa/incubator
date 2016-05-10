(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('DayOffController', DayOffController);

    DayOffController.$inject = ['$scope', '$state', 'DayOff', 'DayOffSearch'];

    function DayOffController ($scope, $state, DayOff, DayOffSearch) {
        var vm = this;
        vm.dayOffs = [];
        vm.loadAll = function() {
            DayOff.query(function(result) {
                vm.dayOffs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DayOffSearch.query({query: vm.searchQuery}, function(result) {
                vm.dayOffs = result;
            });
        };
        vm.loadAll();
        
    }
})();
