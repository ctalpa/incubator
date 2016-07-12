(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('CompanyVehiclesDialogController', CompanyVehiclesDialogController);

    CompanyVehiclesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyVehicles'];

    function CompanyVehiclesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyVehicles) {
        var vm = this;

        vm.companyVehicles = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyVehicles.id !== null) {
                CompanyVehicles.update(vm.companyVehicles, onSaveSuccess, onSaveError);
            } else {
                CompanyVehicles.save(vm.companyVehicles, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prxmenApp:companyVehiclesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insuranceExpirationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
