(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('EmployeeController', EmployeeController);

    EmployeeController.$inject = ['$scope', '$state', 'Employee', 'EmployeeSearch'];

    function EmployeeController ($scope, $state, Employee, EmployeeSearch) {
        var vm = this;
        vm.employees = [];
        vm.loadAll = function() {
            Employee.query(function(result) {
                vm.employees = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EmployeeSearch.query({query: vm.searchQuery}, function(result) {
                vm.employees = result;
            });
        };
        vm.loadAll();
        
    }
})();
