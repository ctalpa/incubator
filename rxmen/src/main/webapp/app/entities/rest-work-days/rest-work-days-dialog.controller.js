(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('RestWorkDaysDialogController', RestWorkDaysDialogController);

    RestWorkDaysDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RestWorkDays', 'Employee', 'DayOff'];

    function RestWorkDaysDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RestWorkDays, Employee, DayOff) {
        var vm = this;
        vm.restWorkDays = entity;
        vm.employees = Employee.query();
        vm.dayoffs = DayOff.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('rxmenApp:restWorkDaysUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.restWorkDays.id !== null) {
                RestWorkDays.update(vm.restWorkDays, onSaveSuccess, onSaveError);
            } else {
                RestWorkDays.save(vm.restWorkDays, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
