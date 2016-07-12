(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .controller('JobHistoryController', JobHistoryController);

    JobHistoryController.$inject = ['$scope', '$state', 'JobHistory', 'JobHistorySearch'];

    function JobHistoryController ($scope, $state, JobHistory, JobHistorySearch) {
        var vm = this;
        
        vm.jobHistories = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            JobHistory.query(function(result) {
                vm.jobHistories = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            JobHistorySearch.query({query: vm.searchQuery}, function(result) {
                vm.jobHistories = result;
            });
        }    }
})();
