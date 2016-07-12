(function() {
    'use strict';
    angular
        .module('prxmenApp')
        .factory('CompanyVehicles', CompanyVehicles);

    CompanyVehicles.$inject = ['$resource', 'DateUtils'];

    function CompanyVehicles ($resource, DateUtils) {
        var resourceUrl =  'api/company-vehicles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.insuranceExpirationDate = DateUtils.convertDateTimeFromServer(data.insuranceExpirationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
