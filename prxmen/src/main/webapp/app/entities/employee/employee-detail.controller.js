(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Employee', 'Job', 'JobHistory', 'RestWorkDays'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, entity, Employee, Job, JobHistory, RestWorkDays) {
        var vm = this;

        vm.employee = entity;

        var unsubscribe = $rootScope.$on('prxmenApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
