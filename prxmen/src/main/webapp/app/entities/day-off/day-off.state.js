(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('day-off', {
            parent: 'entity',
            url: '/day-off',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.dayOff.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/day-off/day-offs.html',
                    controller: 'DayOffController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dayOff');
                    $translatePartialLoader.addPart('dayOffType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('day-off-detail', {
            parent: 'entity',
            url: '/day-off/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.dayOff.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/day-off/day-off-detail.html',
                    controller: 'DayOffDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dayOff');
                    $translatePartialLoader.addPart('dayOffType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DayOff', function($stateParams, DayOff) {
                    return DayOff.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('day-off.new', {
            parent: 'day-off',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day-off/day-off-dialog.html',
                    controller: 'DayOffDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dayId: null,
                                dayOffType: null,
                                description: null,
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('day-off', null, { reload: true });
                }, function() {
                    $state.go('day-off');
                });
            }]
        })
        .state('day-off.edit', {
            parent: 'day-off',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day-off/day-off-dialog.html',
                    controller: 'DayOffDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DayOff', function(DayOff) {
                            return DayOff.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('day-off', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('day-off.delete', {
            parent: 'day-off',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day-off/day-off-delete-dialog.html',
                    controller: 'DayOffDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DayOff', function(DayOff) {
                            return DayOff.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('day-off', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
