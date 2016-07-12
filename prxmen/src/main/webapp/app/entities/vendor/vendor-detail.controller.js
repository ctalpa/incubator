(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('VendorDetailController', VendorDetailController);

    VendorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Vendor', 'Contact', 'Location'];

    function VendorDetailController($scope, $rootScope, $stateParams, entity, Vendor, Contact, Location) {
        var vm = this;

        vm.vendor = entity;

        var unsubscribe = $rootScope.$on('prxmenApp:vendorUpdate', function(event, result) {
            vm.vendor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
