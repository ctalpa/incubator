(function() {
    'use strict';
    angular
        .module('rxmenApp')
        .factory('RestWorkDays', RestWorkDays);

    RestWorkDays.$inject = ['$resource'];

    function RestWorkDays ($resource) {
        var resourceUrl =  'api/rest-work-days/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
