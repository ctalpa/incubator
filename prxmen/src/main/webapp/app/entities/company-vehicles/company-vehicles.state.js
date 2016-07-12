(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-vehicles', {
            parent: 'entity',
            url: '/company-vehicles',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.companyVehicles.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-vehicles/company-vehicles.html',
                    controller: 'CompanyVehiclesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyVehicles');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-vehicles-detail', {
            parent: 'entity',
            url: '/company-vehicles/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.companyVehicles.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-vehicles/company-vehicles-detail.html',
                    controller: 'CompanyVehiclesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyVehicles');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CompanyVehicles', function($stateParams, CompanyVehicles) {
                    return CompanyVehicles.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('company-vehicles.new', {
            parent: 'company-vehicles',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-vehicles/company-vehicles-dialog.html',
                    controller: 'CompanyVehiclesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                companyVehiclesId: null,
                                numberPlate: null,
                                vendorVehicle: null,
                                modelVehicles: null,
                                insuranceExpirationDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-vehicles', null, { reload: true });
                }, function() {
                    $state.go('company-vehicles');
                });
            }]
        })
        .state('company-vehicles.edit', {
            parent: 'company-vehicles',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-vehicles/company-vehicles-dialog.html',
                    controller: 'CompanyVehiclesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyVehicles', function(CompanyVehicles) {
                            return CompanyVehicles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-vehicles', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-vehicles.delete', {
            parent: 'company-vehicles',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-vehicles/company-vehicles-delete-dialog.html',
                    controller: 'CompanyVehiclesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyVehicles', function(CompanyVehicles) {
                            return CompanyVehicles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-vehicles', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
