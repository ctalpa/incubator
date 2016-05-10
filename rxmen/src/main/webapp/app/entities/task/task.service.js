(function() {
    'use strict';
    angular
        .module('rxmenApp')
        .factory('Task', Task);

    Task.$inject = ['$resource', 'DateUtils'];

    function Task ($resource, DateUtils) {
        var resourceUrl =  'api/tasks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.taskDate = DateUtils.convertDateTimeFromServer(data.taskDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
