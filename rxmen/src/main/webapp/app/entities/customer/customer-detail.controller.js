(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('CustomerDetailController', CustomerDetailController);

    CustomerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Customer', 'Location', 'JobHistory'];

    function CustomerDetailController($scope, $rootScope, $stateParams, entity, Customer, Location, JobHistory) {
        var vm = this;
        vm.customer = entity;
        
        var unsubscribe = $rootScope.$on('rxmenApp:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();