(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('JobHistoryDialogController', JobHistoryDialogController);

    JobHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobHistory', 'Customer', 'Employee'];

    function JobHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobHistory, Customer, Employee) {
        var vm = this;
        vm.jobHistory = entity;
        vm.customers = Customer.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('rxmenApp:jobHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.jobHistory.id !== null) {
                JobHistory.update(vm.jobHistory, onSaveSuccess, onSaveError);
            } else {
                JobHistory.save(vm.jobHistory, onSaveSuccess, onSaveError);
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
