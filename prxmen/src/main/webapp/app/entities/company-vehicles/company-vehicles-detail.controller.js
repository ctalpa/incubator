(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('CompanyVehiclesDetailController', CompanyVehiclesDetailController);

    CompanyVehiclesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CompanyVehicles'];

    function CompanyVehiclesDetailController($scope, $rootScope, $stateParams, entity, CompanyVehicles) {
        var vm = this;

        vm.companyVehicles = entity;

        var unsubscribe = $rootScope.$on('prxmenApp:companyVehiclesUpdate', function(event, result) {
            vm.companyVehicles = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
