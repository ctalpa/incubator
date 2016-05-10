(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .controller('JobHistoryController', JobHistoryController);

    JobHistoryController.$inject = ['$scope', '$state', 'JobHistory', 'JobHistorySearch'];

    function JobHistoryController ($scope, $state, JobHistory, JobHistorySearch) {
        var vm = this;
        vm.jobHistories = [];
        vm.loadAll = function() {
            JobHistory.query(function(result) {
                vm.jobHistories = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            JobHistorySearch.query({query: vm.searchQuery}, function(result) {
                vm.jobHistories = result;
            });
        };
        vm.loadAll();
        
    }
})();
