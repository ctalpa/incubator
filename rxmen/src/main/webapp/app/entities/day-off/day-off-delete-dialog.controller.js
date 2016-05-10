(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('DayOffDeleteController',DayOffDeleteController);

    DayOffDeleteController.$inject = ['$uibModalInstance', 'entity', 'DayOff'];

    function DayOffDeleteController($uibModalInstance, entity, DayOff) {
        var vm = this;
        vm.dayOff = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            DayOff.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
