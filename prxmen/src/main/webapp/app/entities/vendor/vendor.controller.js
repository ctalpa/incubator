(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('VendorController', VendorController);

    VendorController.$inject = ['$scope', '$state', 'Vendor', 'VendorSearch'];

    function VendorController ($scope, $state, Vendor, VendorSearch) {
        var vm = this;
        
        vm.vendors = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Vendor.query(function(result) {
                vm.vendors = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            VendorSearch.query({query: vm.searchQuery}, function(result) {
                vm.vendors = result;
            });
        }    }
})();
