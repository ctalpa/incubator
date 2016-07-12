(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .factory('CompanyVehiclesSearch', CompanyVehiclesSearch);

    CompanyVehiclesSearch.$inject = ['$resource'];

    function CompanyVehiclesSearch($resource) {
        var resourceUrl =  'api/_search/company-vehicles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
