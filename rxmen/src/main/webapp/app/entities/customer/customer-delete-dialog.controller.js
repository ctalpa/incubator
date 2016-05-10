(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('CustomerDeleteController',CustomerDeleteController);

    CustomerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Customer'];

    function CustomerDeleteController($uibModalInstance, entity, Customer) {
        var vm = this;
        vm.customer = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Customer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
