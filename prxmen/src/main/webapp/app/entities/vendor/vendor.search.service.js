(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .factory('VendorSearch', VendorSearch);

    VendorSearch.$inject = ['$resource'];

    function VendorSearch($resource) {
        var resourceUrl =  'api/_search/vendors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
