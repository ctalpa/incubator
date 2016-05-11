(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('JobHistoryDetailController', JobHistoryDetailController);

    JobHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'JobHistory', 'Customer', 'Employee'];

    function JobHistoryDetailController($scope, $rootScope, $stateParams, entity, JobHistory, Customer, Employee) {
        var vm = this;
        vm.jobHistory = entity;
        
        var unsubscribe = $rootScope.$on('rxmenApp:jobHistoryUpdate', function(event, result) {
            vm.jobHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
