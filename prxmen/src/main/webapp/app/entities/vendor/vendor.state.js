(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vendor', {
            parent: 'entity',
            url: '/vendor',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.vendor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vendor/vendors.html',
                    controller: 'VendorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vendor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('vendor-detail', {
            parent: 'entity',
            url: '/vendor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.vendor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vendor/vendor-detail.html',
                    controller: 'VendorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vendor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Vendor', function($stateParams, Vendor) {
                    return Vendor.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('vendor.new', {
            parent: 'vendor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vendor/vendor-dialog.html',
                    controller: 'VendorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                vendorId: null,
                                businessName: null,
                                description: null,
                                piva: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vendor', null, { reload: true });
                }, function() {
                    $state.go('vendor');
                });
            }]
        })
        .state('vendor.edit', {
            parent: 'vendor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vendor/vendor-dialog.html',
                    controller: 'VendorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vendor', function(Vendor) {
                            return Vendor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vendor', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vendor.delete', {
            parent: 'vendor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vendor/vendor-delete-dialog.html',
                    controller: 'VendorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vendor', function(Vendor) {
                            return Vendor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vendor', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
