(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Task', 'Job'];

    function TaskDetailController($scope, $rootScope, $stateParams, entity, Task, Job) {
        var vm = this;
        vm.task = entity;
        
        var unsubscribe = $rootScope.$on('rxmenApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
