(function() {
    'use strict';
    angular
        .module('prxmenApp')
        .factory('RestWorkDays', RestWorkDays);

    RestWorkDays.$inject = ['$resource'];

    function RestWorkDays ($resource) {
        var resourceUrl =  'api/rest-work-days/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
