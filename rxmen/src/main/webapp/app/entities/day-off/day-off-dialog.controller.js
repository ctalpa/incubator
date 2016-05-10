(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('DayOffDialogController', DayOffDialogController);

    DayOffDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DayOff', 'RestWorkDays'];

    function DayOffDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DayOff, RestWorkDays) {
        var vm = this;
        vm.dayOff = entity;
        vm.restworkdays = RestWorkDays.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('rxmenApp:dayOffUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.dayOff.id !== null) {
                DayOff.update(vm.dayOff, onSaveSuccess, onSaveError);
            } else {
                DayOff.save(vm.dayOff, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
