(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('CustomerDetailController', CustomerDetailController);

    CustomerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Customer', 'Contact', 'Location', 'JobHistory'];

    function CustomerDetailController($scope, $rootScope, $stateParams, entity, Customer, Contact, Location, JobHistory) {
        var vm = this;

        vm.customer = entity;

        var unsubscribe = $rootScope.$on('prxmenApp:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();