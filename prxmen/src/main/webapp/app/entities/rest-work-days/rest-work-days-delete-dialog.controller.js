(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('RestWorkDaysDeleteController',RestWorkDaysDeleteController);

    RestWorkDaysDeleteController.$inject = ['$uibModalInstance', 'entity', 'RestWorkDays'];

    function RestWorkDaysDeleteController($uibModalInstance, entity, RestWorkDays) {
        var vm = this;

        vm.restWorkDays = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RestWorkDays.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
