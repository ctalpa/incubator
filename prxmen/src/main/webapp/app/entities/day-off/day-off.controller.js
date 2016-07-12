(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('DayOffController', DayOffController);

    DayOffController.$inject = ['$scope', '$state', 'DayOff', 'DayOffSearch'];

    function DayOffController ($scope, $state, DayOff, DayOffSearch) {
        var vm = this;
        
        vm.dayOffs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            DayOff.query(function(result) {
                vm.dayOffs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DayOffSearch.query({query: vm.searchQuery}, function(result) {
                vm.dayOffs = result;
            });
        }    }
})();
