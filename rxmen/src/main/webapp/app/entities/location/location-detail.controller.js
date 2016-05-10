(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('LocationDetailController', LocationDetailController);

    LocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Location', 'Customer'];

    function LocationDetailController($scope, $rootScope, $stateParams, entity, Location, Customer) {
        var vm = this;
        vm.location = entity;
        
        var unsubscribe = $rootScope.$on('rxmenApp:locationUpdate', function(event, result) {
            vm.location = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
