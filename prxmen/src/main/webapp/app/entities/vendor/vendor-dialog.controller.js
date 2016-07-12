(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('VendorDialogController', VendorDialogController);

    VendorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vendor', 'Contact', 'Location'];

    function VendorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vendor, Contact, Location) {
        var vm = this;

        vm.vendor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.contacts = Contact.query();
        vm.locations = Location.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vendor.id !== null) {
                Vendor.update(vm.vendor, onSaveSuccess, onSaveError);
            } else {
                Vendor.save(vm.vendor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prxmenApp:vendorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
