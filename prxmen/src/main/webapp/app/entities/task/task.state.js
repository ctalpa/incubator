(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('task', {
            parent: 'entity',
            url: '/task',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.task.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task/tasks.html',
                    controller: 'TaskController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('task');
                    $translatePartialLoader.addPart('taskStatus');
                    $translatePartialLoader.addPart('taskPriority');
                    $translatePartialLoader.addPart('taskType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('task-detail', {
            parent: 'entity',
            url: '/task/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'prxmenApp.task.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task/task-detail.html',
                    controller: 'TaskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('task');
                    $translatePartialLoader.addPart('taskStatus');
                    $translatePartialLoader.addPart('taskPriority');
                    $translatePartialLoader.addPart('taskType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Task', function($stateParams, Task) {
                    return Task.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('task.new', {
            parent: 'task',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task/task-dialog.html',
                    controller: 'TaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                taskId: null,
                                title: null,
                                description: null,
                                taskDate: null,
                                taskStatus: null,
                                taskPriority: null,
                                taskType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('task', null, { reload: true });
                }, function() {
                    $state.go('task');
                });
            }]
        })
        .state('task.edit', {
            parent: 'task',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task/task-dialog.html',
                    controller: 'TaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Task', function(Task) {
                            return Task.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task.delete', {
            parent: 'task',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task/task-delete-dialog.html',
                    controller: 'TaskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Task', function(Task) {
                            return Task.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
