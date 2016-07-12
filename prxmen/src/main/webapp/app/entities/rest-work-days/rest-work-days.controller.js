(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('RestWorkDaysController', RestWorkDaysController);

    RestWorkDaysController.$inject = ['$scope', '$state', 'RestWorkDays', 'RestWorkDaysSearch'];

    function RestWorkDaysController ($scope, $state, RestWorkDays, RestWorkDaysSearch) {
        var vm = this;
        
        vm.restWorkDays = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            RestWorkDays.query(function(result) {
                vm.restWorkDays = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            RestWorkDaysSearch.query({query: vm.searchQuery}, function(result) {
                vm.restWorkDays = result;
            });
        }    }
})();
