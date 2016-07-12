(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('CompanyVehiclesDeleteController',CompanyVehiclesDeleteController);

    CompanyVehiclesDeleteController.$inject = ['$uibModalInstance', 'entity', 'CompanyVehicles'];

    function CompanyVehiclesDeleteController($uibModalInstance, entity, CompanyVehicles) {
        var vm = this;

        vm.companyVehicles = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CompanyVehicles.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
