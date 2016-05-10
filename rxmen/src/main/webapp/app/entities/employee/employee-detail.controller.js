(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Employee', 'RestWorkDays', 'Job', 'JobHistory'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, entity, Employee, RestWorkDays, Job, JobHistory) {
        var vm = this;
        vm.employee = entity;
        
        var unsubscribe = $rootScope.$on('rxmenApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
