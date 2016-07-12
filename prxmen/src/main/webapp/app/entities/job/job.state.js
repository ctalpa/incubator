(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job', {
            parent: 'entity',
            url: '/job',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.job.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job/jobs.html',
                    controller: 'JobController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('job');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('job-detail', {
            parent: 'entity',
            url: '/job/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.job.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job/job-detail.html',
                    controller: 'JobDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('job');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Job', function($stateParams, Job) {
                    return Job.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('job.new', {
            parent: 'job',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job/job-dialog.html',
                    controller: 'JobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job', null, { reload: true });
                }, function() {
                    $state.go('job');
                });
            }]
        })
        .state('job.edit', {
            parent: 'job',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job/job-dialog.html',
                    controller: 'JobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Job', function(Job) {
                            return Job.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job.delete', {
            parent: 'job',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job/job-delete-dialog.html',
                    controller: 'JobDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Job', function(Job) {
                            return Job.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
