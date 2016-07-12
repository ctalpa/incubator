(function() {
    'use strict';
    angular
        .module('prxmenApp')
        .factory('DayOff', DayOff);

    DayOff.$inject = ['$resource', 'DateUtils'];

    function DayOff ($resource, DateUtils) {
        var resourceUrl =  'api/day-offs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
