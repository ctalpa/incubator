(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .factory('JobSearch', JobSearch);

    JobSearch.$inject = ['$resource'];

    function JobSearch($resource) {
        var resourceUrl =  'api/_search/jobs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
