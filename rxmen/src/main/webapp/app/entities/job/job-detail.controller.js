(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('JobDetailController', JobDetailController);

    JobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Job', 'Task', 'Employee'];

    function JobDetailController($scope, $rootScope, $stateParams, entity, Job, Task, Employee) {
        var vm = this;
        vm.job = entity;
        
        var unsubscribe = $rootScope.$on('rxmenApp:jobUpdate', function(event, result) {
            vm.job = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
