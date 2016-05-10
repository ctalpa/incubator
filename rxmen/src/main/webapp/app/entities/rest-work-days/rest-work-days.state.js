(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rest-work-days', {
            parent: 'entity',
            url: '/rest-work-days',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rxmenApp.restWorkDays.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rest-work-days/rest-work-days.html',
                    controller: 'RestWorkDaysController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('restWorkDays');
                    $translatePartialLoader.addPart('restWorkDaysStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rest-work-days-detail', {
            parent: 'entity',
            url: '/rest-work-days/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rxmenApp.restWorkDays.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rest-work-days/rest-work-days-detail.html',
                    controller: 'RestWorkDaysDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('restWorkDays');
                    $translatePartialLoader.addPart('restWorkDaysStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RestWorkDays', function($stateParams, RestWorkDays) {
                    return RestWorkDays.get({id : $stateParams.id});
                }]
            }
        })
        .state('rest-work-days.new', {
            parent: 'rest-work-days',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rest-work-days/rest-work-days-dialog.html',
                    controller: 'RestWorkDaysDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                restWorkDaysStatus: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rest-work-days', null, { reload: true });
                }, function() {
                    $state.go('rest-work-days');
                });
            }]
        })
        .state('rest-work-days.edit', {
            parent: 'rest-work-days',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rest-work-days/rest-work-days-dialog.html',
                    controller: 'RestWorkDaysDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RestWorkDays', function(RestWorkDays) {
                            return RestWorkDays.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('rest-work-days', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rest-work-days.delete', {
            parent: 'rest-work-days',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rest-work-days/rest-work-days-delete-dialog.html',
                    controller: 'RestWorkDaysDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RestWorkDays', function(RestWorkDays) {
                            return RestWorkDays.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('rest-work-days', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
