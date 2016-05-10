(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('RestWorkDaysDeleteController',RestWorkDaysDeleteController);

    RestWorkDaysDeleteController.$inject = ['$uibModalInstance', 'entity', 'RestWorkDays'];

    function RestWorkDaysDeleteController($uibModalInstance, entity, RestWorkDays) {
        var vm = this;
        vm.restWorkDays = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            RestWorkDays.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
