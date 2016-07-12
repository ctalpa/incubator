(function() {
    'use strict';
    angular
        .module('prxmenApp')
        .factory('Employee', Employee);

    Employee.$inject = ['$resource', 'DateUtils'];

    function Employee ($resource, DateUtils) {
        var resourceUrl =  'api/employees/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthDay = DateUtils.convertDateTimeFromServer(data.birthDay);
                        data.hireDate = DateUtils.convertDateTimeFromServer(data.hireDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
