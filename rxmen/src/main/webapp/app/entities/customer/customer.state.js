(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer', {
            parent: 'entity',
            url: '/customer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rxmenApp.customer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer/customers.html',
                    controller: 'CustomerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-detail', {
            parent: 'entity',
            url: '/customer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rxmenApp.customer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer/customer-detail.html',
                    controller: 'CustomerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Customer', function($stateParams, Customer) {
                    return Customer.get({id : $stateParams.id});
                }]
            }
        })
        .state('customer.new', {
            parent: 'customer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-dialog.html',
                    controller: 'CustomerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                custumerId: null,
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer', null, { reload: true });
                }, function() {
                    $state.go('customer');
                });
            }]
        })
        .state('customer.edit', {
            parent: 'customer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-dialog.html',
                    controller: 'CustomerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer.delete', {
            parent: 'customer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-delete-dialog.html',
                    controller: 'CustomerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
