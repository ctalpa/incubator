(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('CompanyVehiclesController', CompanyVehiclesController);

    CompanyVehiclesController.$inject = ['$scope', '$state', 'CompanyVehicles', 'CompanyVehiclesSearch'];

    function CompanyVehiclesController ($scope, $state, CompanyVehicles, CompanyVehiclesSearch) {
        var vm = this;
        
        vm.companyVehicles = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CompanyVehicles.query(function(result) {
                vm.companyVehicles = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompanyVehiclesSearch.query({query: vm.searchQuery}, function(result) {
                vm.companyVehicles = result;
            });
        }    }
})();
