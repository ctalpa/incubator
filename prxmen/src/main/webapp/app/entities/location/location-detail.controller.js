(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('LocationDetailController', LocationDetailController);

    LocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Location', 'Customer', 'Vendor'];

    function LocationDetailController($scope, $rootScope, $stateParams, entity, Location, Customer, Vendor) {
        var vm = this;

        vm.location = entity;

        var unsubscribe = $rootScope.$on('prxmenApp:locationUpdate', function(event, result) {
            vm.location = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
