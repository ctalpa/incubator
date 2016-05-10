(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('JobHistoryDeleteController',JobHistoryDeleteController);

    JobHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobHistory'];

    function JobHistoryDeleteController($uibModalInstance, entity, JobHistory) {
        var vm = this;
        vm.jobHistory = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            JobHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
