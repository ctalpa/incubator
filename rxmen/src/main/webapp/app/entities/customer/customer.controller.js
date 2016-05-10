(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('CustomerController', CustomerController);

    CustomerController.$inject = ['$scope', '$state', 'Customer', 'CustomerSearch'];

    function CustomerController ($scope, $state, Customer, CustomerSearch) {
        var vm = this;
        vm.customers = [];
        vm.loadAll = function() {
            Customer.query(function(result) {
                vm.customers = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CustomerSearch.query({query: vm.searchQuery}, function(result) {
                vm.customers = result;
            });
        };
        vm.loadAll();
        
    }
})();
