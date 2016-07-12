(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('ContactDetailController', ContactDetailController);

    ContactDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Contact', 'Customer', 'Vendor'];

    function ContactDetailController($scope, $rootScope, $stateParams, entity, Contact, Customer, Vendor) {
        var vm = this;

        vm.contact = entity;

        var unsubscribe = $rootScope.$on('prxmenApp:contactUpdate', function(event, result) {
            vm.contact = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
