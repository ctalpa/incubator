(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('LocationController', LocationController);

    LocationController.$inject = ['$scope', '$state', 'Location', 'LocationSearch'];

    function LocationController ($scope, $state, Location, LocationSearch) {
        var vm = this;
        vm.locations = [];
        vm.loadAll = function() {
            Location.query(function(result) {
                vm.locations = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LocationSearch.query({query: vm.searchQuery}, function(result) {
                vm.locations = result;
            });
        };
        vm.loadAll();
        
    }
})();
