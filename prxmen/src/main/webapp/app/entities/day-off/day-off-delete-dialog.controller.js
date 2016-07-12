(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('DayOffDeleteController',DayOffDeleteController);

    DayOffDeleteController.$inject = ['$uibModalInstance', 'entity', 'DayOff'];

    function DayOffDeleteController($uibModalInstance, entity, DayOff) {
        var vm = this;

        vm.dayOff = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DayOff.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
