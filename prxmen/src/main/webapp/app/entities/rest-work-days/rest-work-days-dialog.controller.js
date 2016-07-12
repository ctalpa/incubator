(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('RestWorkDaysDialogController', RestWorkDaysDialogController);

    RestWorkDaysDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RestWorkDays', 'Employee', 'DayOff'];

    function RestWorkDaysDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RestWorkDays, Employee, DayOff) {
        var vm = this;

        vm.restWorkDays = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.dayoffs = DayOff.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.restWorkDays.id !== null) {
                RestWorkDays.update(vm.restWorkDays, onSaveSuccess, onSaveError);
            } else {
                RestWorkDays.save(vm.restWorkDays, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prxmenApp:restWorkDaysUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
