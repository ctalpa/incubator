(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('DayOffDialogController', DayOffDialogController);

    DayOffDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DayOff', 'RestWorkDays'];

    function DayOffDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DayOff, RestWorkDays) {
        var vm = this;

        vm.dayOff = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.restworkdays = RestWorkDays.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dayOff.id !== null) {
                DayOff.update(vm.dayOff, onSaveSuccess, onSaveError);
            } else {
                DayOff.save(vm.dayOff, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prxmenApp:dayOffUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
