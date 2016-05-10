(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('RestWorkDaysController', RestWorkDaysController);

    RestWorkDaysController.$inject = ['$scope', '$state', 'RestWorkDays', 'RestWorkDaysSearch'];

    function RestWorkDaysController ($scope, $state, RestWorkDays, RestWorkDaysSearch) {
        var vm = this;
        vm.restWorkDays = [];
        vm.loadAll = function() {
            RestWorkDays.query(function(result) {
                vm.restWorkDays = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            RestWorkDaysSearch.query({query: vm.searchQuery}, function(result) {
                vm.restWorkDays = result;
            });
        };
        vm.loadAll();
        
    }
})();
